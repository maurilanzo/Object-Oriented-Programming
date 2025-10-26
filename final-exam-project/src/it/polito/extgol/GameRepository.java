package it.polito.extgol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class GameRepository extends GenericExtGOLRepository<Game,Long>{

    protected GameRepository() {
        super(Game.class);
    }
    
    public static Game load(Game game) {

    if (game == null) {
        throw new IllegalArgumentException("Game cannot be null");
    }

    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
        tx.begin();
        Game managed = em.merge(game);      //retreive game
        managed.getEventMapInternal().isEmpty(); //load event map
        managed.getGenerations().isEmpty();       //load lazy generations
        managed.getBoard().getCellSet().isEmpty(); //load lazy board
        tx.commit();
        return managed;                             //return loaded game
    } catch (RuntimeException ex) {
        if (tx.isActive())
            tx.rollback();
        throw ex;
    } finally {
        em.close();
    }
    
    }

}
