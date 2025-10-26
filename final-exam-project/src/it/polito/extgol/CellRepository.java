package it.polito.extgol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class CellRepository extends GenericExtGOLRepository<Cell, Long> {

    public CellRepository () {
        super(Cell.class);
    }
    
    public Cell load(Cell cell) {

        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null");
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cell managed = em.merge(cell); // retrieve game

            // load all lazy relationships
            managed.getBoard().getCellSet().isEmpty(); // load board
            managed.getGame().getEventMapInternal().isEmpty(); // load game
            managed.getTile().getId(); // load tile
            tx.commit();
            return managed; // return loaded
        } catch (RuntimeException e) {
            if (tx.isActive()){
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}