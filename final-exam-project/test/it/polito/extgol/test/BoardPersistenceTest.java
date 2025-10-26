package it.polito.extgol.test;

import org.junit.Test;
import static org.junit.Assert.*;
import it.polito.extgol.*;

public class BoardPersistenceTest {

    @Test
    public void testPersistenceSanity() {
        TestDatabaseUtil.clearDatabase(); 

        Game game = Game.createExtended("persist-test", 3, 3);
        
        ExtendedGameOfLife life = new ExtendedGameOfLife();
        life.saveGame(game);

        BoardRepository repo = new BoardRepository();
        Board reloaded = repo.findById((long) game.getBoard().getId()).get();
        Board loaded = BoardRepository.load(reloaded);

        assertNotNull(loaded);
        assertEquals(9, loaded.getTiles().size());
    }
}
