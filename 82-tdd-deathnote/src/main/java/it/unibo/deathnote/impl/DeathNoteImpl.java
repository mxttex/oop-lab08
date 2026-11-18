package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {

    private final Map<String, Death> deaths;
    private String lastNameWritten;

    /**
     * A new blank death note
     */
    public DeathNoteImpl() {
        this.deaths = new LinkedHashMap<String, Death>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Index number " + ruleNumber + "does not exist.");
        }

        return RULES.get(ruleNumber - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name);
        lastNameWritten = name;
        this.deaths.put(name, new Death());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(final String cause) {
        return updateDeath(cause, death -> death.writeCause(cause));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(final String details) {
        return updateDeath(details, death -> death.writeDetails(details));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(final String name) {
        return getDeath(name).cause;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(final String name) {
        return getDeath(name).details;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(final String name) {
        return deaths.containsKey(name);
    }

    private Death getDeath(final String name) {
        final Death death = deaths.get(name);
        if (death == null) {
            throw new IllegalArgumentException("The name has never been written");
        }

        return death;
    }

    private boolean updateDeath(final String update, final DeathTransformer operation) {
        if (lastNameWritten == null) {
            throw new IllegalStateException("No name written yet");
        }
        if (update == null) {
            throw new IllegalStateException("Nothing to update");
        }

        final var previous = deaths.get(lastNameWritten);
        final var updated = operation.call(previous);

        if (previous.equals(updated)) {
            return false;
        } else {
            deaths.put(lastNameWritten, updated);
            return true;
        }
    }

    @FunctionalInterface
    private interface DeathTransformer {
        Death call(Death input);
    }

    private static final class Death {
        private static String DEFAULT_MESSAGE = "heart attack";
        private static final byte VALID_CAUSE_TIMEOUT = 40;
        private static final short VALID_DETAILS_TIMEOUT = 6000 + VALID_CAUSE_TIMEOUT;
        private final String cause;
        private final String details;
        private final long timeOfDeath;

        private Death(final String cause, final String details, final long timeOfDeath) {
            this.cause = cause;
            this.details = details;
            this.timeOfDeath = timeOfDeath;
        }

        Death() {
            this(DEFAULT_MESSAGE, "", System.currentTimeMillis());
        }

        Death writeCause(final String deathCause) {
            final long now = System.currentTimeMillis();
            return System.currentTimeMillis() < timeOfDeath + VALID_CAUSE_TIMEOUT ? new Death(deathCause, this.details, now)
                    : this;
        }

        Death writeDetails(final String details) {
            return System.currentTimeMillis() < timeOfDeath + VALID_DETAILS_TIMEOUT ? new Death(this.cause, details, this.timeOfDeath)
                    : this;
        }

        @Override
        public boolean equals(final Object obj) {
            // se il riferimento coincide allora sono la stessa istanza (e sono peffo
            // uguali)
            if (this == obj) {
                return true;
            }
            // se il parametro passato non e' un oggetto Death allora non possono essere
            // uguali
            if (!(obj instanceof Death other)) {
                return false;
            }

            return Objects.equals(this.cause, other.cause)
                    && Objects.equals(this.details, other.details)
                    && Objects.equals(this.timeOfDeath, other.timeOfDeath);
        }
    }

}
