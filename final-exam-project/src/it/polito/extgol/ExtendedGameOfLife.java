package it.polito.extgol;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Facade coordinating the core operations of the Extended Game of Life simulation.
 *
 * This class provides high-level methods to:
 *   - Evolve a single generation or advance multiple steps.
 *   - Visualize the board state and retrieve alive cells by coordinate.
 *   - Persist and reload entire game instances.
 */
public class ExtendedGameOfLife {

    private static GameRepository gameRepository = new GameRepository();

    /**
     * Computes and returns the next generation based on the current one.
     *
     * The method follows these steps:
     *   1. Validates that the current generation has an associated Board and Game.
     *   2. Applies til-based interactions to moidify lifePoints of currently alive cells.
     *   3. Computes the next alive/dead state for each cell based on Game of Life rules.
     *   3. Creates a new Generation object representing the next simulation step.
     *   4. Adjusts lifePoints depending on evoulution outcome:
     *        - +1 for survival
     *        - -1 for death
     *        - 0 for respawn (if cell was dead and will live)
     *        - unchanged if cell stays dead
     *   5. Block survival of cells with negative lifePointt. (Just an extra ressurance)
     *   6. Applies all calculated changes simultaneously to ensure consistency.
     *   7. Caputures a snapshot of all cells' state in the presistent map for future retrieval.
     *
     * @param current The current generation snapshot used for evolving to the next state.
     * @return A new Generation object reflecting the evolved board state.
     * @throws IllegalStateException If Generation is not properly initialized.
     */
    public Generation evolve(Generation current) {
        Objects.requireNonNull(current, "Current generation cannot be null");
        Board board = current.getBoard();
        Game game = current.getGame();
        
        // Ensure current generation is properly initialized
        if (board == null || game == null) {
            throw new IllegalStateException(
                "Generation must have associated Board and Game!");
        }

        // step 0 : interaction between cells
        // create a list of cells in order of coordinates
        List<Cell> aliveCells = current.getAliveCells()
                                       .stream()
                                       .sorted(Comparator.comparing(Cell::getX).thenComparing(Cell::getY))
                                       .collect(Collectors.toList());
        // create a set to store all interactions between cells in order to avoid multiple interactions between two cells
        Set<String> interactions = new HashSet<>();                   
        for (Cell cell : aliveCells) {
            List<Cell> neighbors = cell.getNeighbors()
                                       .stream()
                                       .map(Tile::getCell)
                                       .collect(Collectors.toList());

            for (Cell neighbor : neighbors) {
                String key = cell.hashCode() > neighbor.hashCode() ? cell.hashCode()+ ":" + neighbor.hashCode() : neighbor.hashCode() + ":" + cell.hashCode();
                if (!interactions.contains(key)) { // if the interaction has not happened yet, make the cells interact and store the interaction
                    interactions.add(key);
                    cell.interact(neighbor);
                }
            }
        }

        // done by r2 :
        // Prepare the next generation
        Generation nextGen = Generation.createNextGeneration(current);

        // Buffers for calculated state
        Map<Cell, Boolean> nextAliveness = new HashMap<>();
        Map<Cell, Integer> updatedLifePoints = new HashMap<>();

        // Tile interactions (pre-GOL), only affects alive cells
        for (Tile tile : board.getTiles()) {
            Cell c = tile.getCell();
            if (c == null) {
                throw new IllegalStateException("Missing cell on tile " + tile);
            }

            int lp = c.getLifePoints();
            if (c.isAlive()) {
                lp += tile.getLifePointModifier(); // Apply tile enrgy modifier
            }
            updatedLifePoints.put(c, lp); // Update life points state   
        }

        // Apply GOL rules and adjust lifePoints accordingly
        for (Tile tile : board.getTiles()) {
            Cell c = tile.getCell();
            boolean wasAlive = c.isAlive();
            int lp = updatedLifePoints.get(c);
            int aliveNeighbors = c.countAliveNeighbors();

            boolean willLive = c.evolve(aliveNeighbors); // GOL rules

            // Apply GOL rules to energy
            if (!wasAlive && willLive) {
                lp = 0; // Respawn reset LP
            } else if (wasAlive && !willLive) {
                lp -= 1; // Death reduces LP
            } else if (wasAlive && willLive) {
                lp += 1; // Survival increases LP
            }

            // GOL and LP interaction rule: even if GOL says live, LP < 0 -> dies
            if (willLive && lp < 0) {
                willLive = false;
            }

            updatedLifePoints.put(c, lp);
            nextAliveness.put(c, willLive);
        }

        // Apply all changes simultaneously
        for (Tile tile : board.getTiles()) {
            Cell c = tile.getCell();
            boolean alive = nextAliveness.get(c);
            int finalLp = updatedLifePoints.get(c);

            c.setAlive(alive);
            c.setLifePoints(finalLp);
            c.addGeneration(nextGen);  // Track cell in new generation
        }

        // Save new state snapshot
        nextGen.snapCells();

        return nextGen;
    }

    /**
     * Advances the simulation by evolving the game state through a given number of steps.
     *
     * Starting from the game's initial generation, this method repeatedly computes the next
     * generation and appends it to the game's history.
     *
     * @param game  The Game instance whose generations will be advanced.
     * @param steps The number of evolution steps (generations) to perform.
     * @return The same Game instance, updated with the new generation.
     */
    public Game run(Game game, int steps) {
        Generation current = game.getStart();
        for (int i = 0; i < steps; i++) {
            Generation next = evolve(current);
            current = next;
        }
        return game;
    }

    /**
     * Advances the simulation by evolving the game state through a given number of steps.
     *
     * Starting from the game's initial generation, this method repeatedly computes the next
     * generation and appends it to the game's history. 
     * 
     * It applies any events at their scheduled generations.
     *
     * At each step:
     *   1. If an event is scheduled for the current step (according to eventMap), the
     *      corresponding event is applied to all tiles before evolution.
     *   2. The board then evolves to the next generation, which is added to the game.
     *
     * @param game      The Game instance to run and update.
     * @param steps     The total number of generations to simulate.
     * @param eventMap  A map from generation index (0-based) to the EventType to trigger;
     *                  if a step is not present in the map, no event is applied that step.
     * @return          The same Game instance, now containing the extended generation history.
     */
    public Game run(Game game, int steps, Map<Integer, EventType> eventMap) {
        // TODO: implement for R3
        Generation current = game.getStart();
        for (int i = 0; i < steps; i++) {
            if (eventMap.keySet().contains(current.getStep())){ //there is an event for that generation
                EventType event = eventMap.get(current.getStep());
                current.setEvent(event); //set the corresponding event for that generation
                game.getEventMapInternal().put(current.getStep(),event);
                
                Board board = current.getBoard();
                for (Tile t: board.getTiles()){
                    if (t.hasCell()) { // or t.hasCell()
                        Cell cell = t.getCell();
                        game.unrollEvent(event, cell);  //call unrollEvent for each cell
                    }
                }

            }
            else current.setEvent(null);
            Generation next = evolve(current);
            current = next;
        }
        return game;
       
    }

    /**
     * Builds and returns a map associating each coordinate with its alive Cell 
     * instance for the specified generation.
     *
     * Iterates over all alive cells present in the given generation and constructs 
     * a coordinate-based map, facilitating cell access.
     *
     * @param generation The generation whose alive cells are mapped.
     * @return A Map from Coord (coordinates) to Cell instances representing all alive cells.
    */
    public Map<Coord, Cell> getAliveCells(Generation generation) {
        Map<Coord, Cell> alive = new HashMap<>();
        for (Cell c : generation.getAliveCells()) {
            alive.put(c.getCoordinates(), c);
        }
        return alive;
    }

    /**
     * Generates a visual string representation of the specified generation's board state.
     *
     * It produces a multi-line textual snapshot showing cells and their status.
     * "C" -> alive cell
     * "0" -> dead cell
     *
     * @param generation The Generation instance to visualize.
     * @return A multi-line String-based representiion of the board's current state.
    */
    public String visualize(Generation generation) {
        return generation.getBoard().visualize(generation);
    }

    /**
     * Persists the complete state of the provided Game instance, including its Board, Tiles,
     * Cells, and all associated Generations.
     *
     * If the Game is new, it will be created and persisted.
     * Otherwise, its state will be updated (merged) in the database. Ensures transactional 
     * safety and consistency through commit and rollback handling.
     *
     * @param game The Game instance to persist or update.
     */
    public void saveGame(Game game) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (game.getId() == null) {
                em.persist(game);
            } else {
                em.merge(game);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Loads and returns a persisted map of game events keyed by generation step.
     *
     * Delegates retrieval to the corresponding repository class, which in turn implements 
     * the provided generic repository class for persistence. This method reconstructs 
     * the event timeline for inspection or replay.
     *
     * @return A Map<Integer, EventType> mapping generation steps to associated events.
     */
    public Map<Integer, EventType> loadEvents() {
        return null;
    }

    public static GameRepository getGameRepository(){
        return gameRepository;
    }

    public static void addPersistentGame(Game game){ 
        gameRepository.create(game);
    }

    public static void updateGameRepository(Game game){ 
        gameRepository.update(game); 
    }

    


}
