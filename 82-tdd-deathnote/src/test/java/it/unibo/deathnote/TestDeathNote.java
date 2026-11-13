package it.unibo.deathnote;

import org.junit.jupiter.api.BeforeEach;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

class TestDeathNote {
    
    private DeathNote deathNote;

    @BeforeEach
    void init(){
        deathNote = new DeathNoteImpl();
    }

    @Test
    void TestIllegalRules(){
        
    }

    
}