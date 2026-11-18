package it.unibo.deathnote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;
import static it.unibo.deathnote.api.DeathNote.RULES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static java.lang.Thread.sleep;

import java.util.List;


class TestDeathNote {
    
    private DeathNote deathNote;
    private final static String NIKOLAS_PANTERINI = "Nikolas Panterini";
    private final static String DIEGO_VAGNINI = "Diego Vagnini";
    private final static String EMPTY_STRING = "";
    private final static String FUNNIEST_MC_DEATHMESSAGE = "experienced kinetic energy whilst trying to escape ender dragon";
    private final static String KARTING_INCIDENT = "karting incident";
    private final static String HEARTH_ATTACK = "heart attack";


    @BeforeEach
    void init(){
        this.deathNote = new DeathNoteImpl();
    }

    /**
     * Tests if negative or rule number 0 dont exists
     */
    @Test
    void testIllegalRules(){
        //RULES.sizes()+1 perche' vediamo se da errore
        for(final var index: List.of(-1, 0, RULES.size()+1)){
            assertThrows((IllegalArgumetsThrower) () -> deathNote.getRule(index));
        }
    }

    /**
     * Checks if the rules are NOT blank
     */
    @Test
    void testNotBlankRules(){
        //questo era op ma l'interfaccia richiede che il primo indice sia uno, quindi tocca scrivere for manualmente
        // for(String rule:RULES){
        //     assertNotNull(rule);
        //     assertFalse(rule.isBlank());
        // }

        for(int i = 1; i <= RULES.size(); i++){
            final String rule = deathNote.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    /**
     * Test if the name written dies
     */
    @Test
    void testDeath(){
        assertFalse(deathNote.isNameWritten(NIKOLAS_PANTERINI));
        deathNote.writeName(NIKOLAS_PANTERINI);
        assertTrue(deathNote.isNameWritten(NIKOLAS_PANTERINI));
        assertFalse(deathNote.isNameWritten(DIEGO_VAGNINI));
        assertFalse(deathNote.isNameWritten(EMPTY_STRING));
    }

    @Test
    void testDeathCause() throws InterruptedException{

        assertThrows((IllegalStateThrower) () -> deathNote.writeDeathCause(FUNNIEST_MC_DEATHMESSAGE));

        deathNote.writeName(DIEGO_VAGNINI);
        assertTrue(deathNote.isNameWritten(DIEGO_VAGNINI));
        assertEquals(HEARTH_ATTACK, deathNote.getDeathCause(DIEGO_VAGNINI));
        deathNote.writeName(NIKOLAS_PANTERINI);
        assertTrue(deathNote.writeDeathCause(KARTING_INCIDENT));
        assertEquals(KARTING_INCIDENT, deathNote.getDeathCause(NIKOLAS_PANTERINI));
        sleep(100);
        assertFalse(deathNote.writeDeathCause(FUNNIEST_MC_DEATHMESSAGE));
        assertEquals(KARTING_INCIDENT, deathNote.getDeathCause(NIKOLAS_PANTERINI));
    }

    @Test
    void testDeathDetails() throws InterruptedException{
        assertThrows((IllegalStateThrower) () -> deathNote.writeDetails("death details"));
        deathNote.writeName(NIKOLAS_PANTERINI);
        assertTrue(deathNote.isNameWritten(NIKOLAS_PANTERINI));
        assertEquals(EMPTY_STRING, deathNote.getDeathDetails(NIKOLAS_PANTERINI));
        assertTrue(deathNote.getDeathDetails(NIKOLAS_PANTERINI).isBlank());
        assertTrue(deathNote.writeDetails("ran for too long"));
        assertEquals("ran for too long", deathNote.getDeathDetails(NIKOLAS_PANTERINI));
        deathNote.writeName(DIEGO_VAGNINI);
        assertTrue(deathNote.isNameWritten(DIEGO_VAGNINI));
        sleep(6100);
        assertFalse(deathNote.writeDetails("ran for too long"));
        assertEquals("", deathNote.getDeathDetails(DIEGO_VAGNINI));
    }

    static void assertThrows(final RuntimeExceptionThrower exceptionThrower){
        try {
            exceptionThrower.run();
            fail("Exception was expected but not thrown");
        } catch (IllegalArgumentException | IllegalStateException e) {
            assertTrue(
                exceptionThrower instanceof IllegalArgumetsThrower && e instanceof IllegalArgumentException
                || exceptionThrower instanceof IllegalStateThrower && e instanceof IllegalStateException
            );
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().isBlank());
        }
    }

    @FunctionalInterface
    private interface RuntimeExceptionThrower {
        void run();
    }

    @FunctionalInterface 
    private interface IllegalArgumetsThrower extends RuntimeExceptionThrower {}

    @FunctionalInterface 
    private interface IllegalStateThrower extends RuntimeExceptionThrower {}


    
}