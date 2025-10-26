package it.polito.extgol;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

/**
 * Entity representing a cell in the Extended Game of Life.
 *
 * Serves as the base class for all cell types, embedding its board coordinates,
 * alive/dead state, energy budget (lifePoints), and interaction mood.
 * Each Cell is linked to a Board, Game, Tile, and a history of Generations.
 * Implements Evolvable to apply Conway’s rules plus energy checks each
 * generation,
 * and Interactable to model cell–cell energy exchanges.
 */
@Entity
public class Cell implements Evolvable, Interactable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * In-memory coordinates, persisted as two columns cell_x and cell_y.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "cell_x", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name = "cell_y", nullable = false))
    })
    private Coord cellCoord;

    /** Persisted alive/dead state */
    @Column(name = "is_alive", nullable = false)
    protected Boolean isAlive = false;

    /** Persisted lifepoints (default 0) */
    @Column(name = "lifepoints", nullable = false)
    protected Integer lifepoints = 0;

    /** Reference to the parent board (read-only). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false, updatable = false)
    protected Board board;

    /** Reference to the owning game (read-only). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false, updatable = false)
    protected Game game;

    /** Transient list tracking generations this cell belongs to. */
    @Transient
    protected List<Generation> generations = new ArrayList<>();

    /** Back-reference: Tile owns the foreign key mapping. */
    @OneToOne(mappedBy = "cell", fetch = FetchType.LAZY)
    protected Tile tile;

    /** type of cell: BASIC, HIGHLANDER, LONER, SOCIAL */
    @Enumerated(EnumType.STRING)
    @Column(name = "cellType", nullable = false)
    protected CellType cellType = CellType.BASIC;

    /** mood of the cell */
    @Enumerated(EnumType.STRING)
    @Column(name = "mood", nullable = false) 
    protected CellMood mood = CellMood.NAIVE;

    // death-inducing conditions counter
    @Transient
    private int consecutiveNearToDeath = 0;

    // to manage the blood mooon 
    @Column(name = "empowered_bite", nullable = false)
    protected Boolean empoweredBite=false; 

    // to manage mood changes
    @Transient
    private Boolean fromNaiveToVampire = false;

    @Transient
    private Boolean fromHealerToVampire = false;



    /** Default constructor for JPA compliance. */
    public Cell() {
    }

    /**
     * Constructs a new Cell at given coordinates, defaulting to dead.
     * 
     * @param coord the cell's coordinates
     */
    public Cell(Coord tileCoord) {
        this.cellCoord = tileCoord;
        this.isAlive = false;
    }

    /**
     * Constructs a new Cell with its tile, board, and game context.
     * 
     * @param coord the cell's coordinates
     * @param tile  the owning Tile
     * @param board the Board context
     * @param game  the owning Game
     */
    public Cell(Coord tileCoord, Tile t, Board b, Game g) {
        this.cellCoord = tileCoord;
        this.isAlive = false;
        this.tile = t;
        this.board = b;
        this.game = g;
    }

    public Long getId() {
        return id;
    }

    /**
     * Applies the classic Conway’s Game of Life rules to calculate the cell’s next
     * alive/dead state.
     *
     * Rules:
     * - Underpopulation: A live cell with fewer than 2 neighbors dies.
     * - Overpopulation: A live cell with more than 3 neighbors dies.
     * - Respawn: A dead cell with exactly 3 neighbors becomes alive.
     * - Survival: A live cell with 2 or 3 neighbors stays alive.
     *
     * @param aliveNeighbors the count of alive neighboring cells
     * @return true if the cell will live, false otherwise
     */
    @Override
    public Boolean evolve(int aliveNeighbors) {
        tile.interact(this); // update lifePoints 

        // Start by assuming the cell retains its current state
        Boolean willLive = this.isAlive;

        switch (cellType) {
            case BASIC: 
                willLive = evolveBasic(aliveNeighbors);
                break;

            case HIGHLANDER:
                willLive = evolveHighlander(aliveNeighbors);
                break;

            case LONER:
                willLive = evolveLoner(aliveNeighbors);
                break;
            
            case SOCIAL: 
                willLive = evolveSocial(aliveNeighbors);
                break; 

            default:
                willLive = evolveBasic(aliveNeighbors);
                break;
        }

        if (this.mood == CellMood.NAIVE && this.fromNaiveToVampire) {
            this.setMood(CellMood.VAMPIRE);
        }
        if (this.mood == CellMood.HEALER && this.fromHealerToVampire) {
            this.setMood(CellMood.VAMPIRE);
        }

        return willLive;
    }


    private Boolean evolveBasic (int aliveNeighbors) {
        Boolean willLive = this.isAlive;

        // overpopulation + underpopulation
        if (aliveNeighbors < 2 || aliveNeighbors > 3) { 
            willLive = false;
        } 
        // respawn
        else if (!this.isAlive && aliveNeighbors == 3 ){
            willLive = true;
        }

        return willLive;
    }
    
    private Boolean evolveHighlander (int aliveNeighbors) {
        Boolean willLive = this.isAlive;

        // check consecutive near to death conditions
        if (aliveNeighbors < 2 || aliveNeighbors > 3) {
            consecutiveNearToDeath++;
        }
        else consecutiveNearToDeath = 0;

        
        if (consecutiveNearToDeath >= 3) {
            willLive = false;
        }
        // respawn
        else if (!isAlive && aliveNeighbors == 3) {
            willLive = true;
        }
        
        return willLive;
    }
    
    private Boolean evolveLoner (int aliveNeighbors) {
        Boolean willLive = this.isAlive;

        if (aliveNeighbors < 1 || aliveNeighbors > 3) {
            willLive = false; 
        }
        else if (!this.isAlive && aliveNeighbors == 3) {
            willLive = true;
        }
        
        return willLive;
    }
    
    private Boolean evolveSocial (int aliveNeighbors) {
        Boolean willLive = this.isAlive;

        if (aliveNeighbors < 2 || aliveNeighbors > 8) {
            willLive = false;
        }
        else if (!isAlive && aliveNeighbors == 3) {
            willLive = true;
        }

        return willLive;
    }
    
    /**
     * Retrieves all tiles adjacent to this cell's tile.
     *
     * This method returns a copy of the underlying neighbor list to ensure
     * external code cannot modify the board topology.
     *
     * @return an immutable List of neighboring Tile instances
     */
    public List<Tile> getNeighbors() {
        return List.copyOf(tile.getNeighbors());
    }

    /**
     * Counts the number of live cells adjacent to this cell’s tile.
     *
     * Iterates over all neighboring tiles and increments the count for each
     * tile that hosts an alive Cell.
     *
     * @return the total number of alive neighboring cells
     */
    public int countAliveNeighbors() {
        int count = 0;
        for (Tile t : tile.getNeighbors()) {
            if (t.getCell() != null && t.getCell().isAlive())
                count++;
        }
        return count;
    }

    /**
     * Registers this cell in the specified generation’s back-reference list.
     *
     * Used internally by the ORM to maintain the relationship between
     * cells and the generations they belong to. Adds the given generation
     * to the cell’s internal history.
     *
     * @param gen the Generation instance to associate with this cell
     */
    void addGeneration(Generation gen) {
        generations.add(gen);
    }

    /**
     * Provides an unmodifiable history of all generations in which this cell has
     * appeared.
     *
     * Returns a copy of the internal list to prevent external modification
     * of the cell’s generation history.
     *
     * @return an immutable List of Generation instances tracking this cell’s
     *         lineage
     */
    public List<Generation> getGenerations() {
        return List.copyOf(generations);
    }

    /**
     * Returns the X coordinate of this cell on the board.
     *
     * @return the cell’s X position
     */
    public int getX() {
        return this.cellCoord.getX();
    }

    /**
     * Returns the Y coordinate of this cell on the board.
     *
     * @return the cell’s Y position
     */
    public int getY() {
        return this.cellCoord.getY();
    }

    /**
     * Retrieves the full coordinate object for this cell.
     *
     * @return a Coord instance representing this cell’s position
     */
    public Coord getCoordinates() {
        return this.cellCoord;
    }

    /**
     * Checks whether this cell is currently alive.
     *
     * @return true if the cell is alive; false if it is dead
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Updates the alive/dead state of this cell.
     *
     * @param isAlive true to mark the cell as alive; false to mark it as dead
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Returns a string representation of this cell’s position in the format "x,y".
     *
     * Overrides Object.toString() to provide a concise coordinate-based
     * representation.
     * 
     * @return a comma-separated string of the cell’s X and Y coordinates
     */
    @Override
    public String toString() {
        return getX() + "," + getY();
    }

    // EXTENDED BEHAVIORS

    /**
     * Retrieves the current energy level of this cell.
     *
     * @return the number of life points the cell currently has
     */
    public int getLifePoints() {
        return this.lifepoints;
    }

    /**
     * Updates the energy level of this cell.
     *
     * @param lifePoints the new number of life points to assign to the cell
     */
    public void setLifePoints(int lifePoints) {
        // TODO Auto-generated method stub
        this.lifepoints=lifePoints;
    }

    public void setEmpoweredBite(Boolean bite){
        empoweredBite = bite; 
    }

    public Boolean getEmpoweredBite(){
        return empoweredBite;
    }

    /**
     * Implements the interact() method of Interactable to
     * define the interaction between this cell and another cell.
     * Implementations will adjust life points, mood, or other state based on the
     * interaction rules.
     *
     * @param cell the Cell object to interact with
     */
    @Override
    public void interact(Cell otherCell) {
        if (!this.isAlive || !otherCell.isAlive) {
            return;
        }
        if (this.mood == otherCell.getMood()) return;

        switch (mood) {
            case HEALER:
                // HEALER + NAIVE
                if (otherCell.getMood() == CellMood.NAIVE) {
                    otherCell.setLifePoints(otherCell.getLifePoints() + 1);
                }

                // HEALER + VAMPIRE
                else if (otherCell.getMood() == CellMood.VAMPIRE) {
                    this.lifepoints -= 1;
                    otherCell.setLifePoints(otherCell.getLifePoints() + 1);
                    if (empoweredBite) {
                        this.fromHealerToVampire = true;
                    }
                }
            
                break;
            
            case VAMPIRE:
                if (otherCell.lifepoints < 0) break;
                // VAMPIRE + VAMPIRE -> nothing happens

                // VAMPIRE + NAIVE
                if (otherCell.getMood() == CellMood.NAIVE) {
                    this.lifepoints += 1;
                    otherCell.setLifePoints(otherCell.getLifePoints() - 1);
                    otherCell.fromNaiveToVampire = true;
                }

                // VAMPIRE + HEALER
                else if (otherCell.getMood() == CellMood.HEALER) {
                    this.lifepoints += 1;
                    otherCell.setLifePoints(otherCell.getLifePoints() - 1);
                    if (empoweredBite) {
                        otherCell.fromHealerToVampire = true;
                    }
                }

                break;

            case NAIVE:
                if (this.lifepoints < 0) break;
                // NAIVE + NAIVE -> nothing happens

                // NAIVE + HEALER   
                if (otherCell.getMood() == CellMood.HEALER) {
                    this.lifepoints += 1;
                }
                // NAIVE + VAMPIRE
                else if (otherCell.getMood() == CellMood.VAMPIRE) {
                    this.fromNaiveToVampire = true;
                    this.lifepoints -= 1;
                    otherCell.setLifePoints(otherCell.getLifePoints() + 1);
                }
        }

    }

    /**
     * Assigns a specific cell type to this cell, influencing its behavior.
     *
     * @param t the CellType to set (e.g., BASIC, HIGHLANDER, LONER, SOCIAL)
     */
    public void setType(CellType t) {
        this.cellType = t;
    }

    public CellType getType() {
        return this.cellType;
    }

    /**
     * Sets the current mood of this cell, impacting how it interacts with others.
     *
     * @param mood the CellMood to assign (NAIVE, HEALER, or VAMPIRE)
     */
    public void setMood(CellMood mood) {
        this.mood = mood;
    }

    /**
     * Retrieves the current mood of this cell.
     *
     * @return the CellMood representing the cell’s interaction style
     */
    public CellMood getMood() {
        return this.mood;
    }

    public Board getBoard() {
        return board;
    }

    public Game getGame() {
        return game;
    }

    public Tile getTile() {
        return tile;
    }

}
