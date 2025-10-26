package it.polito.extgol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class BoardRepository extends GenericExtGOLRepository<Board, Long> {
    public BoardRepository() {
        super(Board.class);
    }

    public static Board load(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Board managed = em.merge(board); // Retrieve managed board instance

            // Force-load all lazy relationships
            managed.getTiles().isEmpty();          // Load tiles
            managed.getCellSet().size(); // triggers loading from DB

            tx.commit();
            return managed;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
