package de.fhg.iais.roberta.mode.action.brickpi;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum WakeupWord implements IMode {
    HELLO(),
    START(),
    LAOJIA();

    private final String[] values;

    private WakeupWord(String... values) {
        this.values = values;
    }

    public static WakeupWord get(String wakeupWord) {
        if ( wakeupWord == null || wakeupWord.isEmpty() ) {
            throw new DbcException("Invalid WakeupWord: " + wakeupWord);
        }
        String sUpper = wakeupWord.trim().toUpperCase(Locale.CHINESE);
        for ( WakeupWord l : WakeupWord.values() ) {
            if ( l.toString().equals(sUpper) ) {
                return l;
            }
            for ( String value : l.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return l;
                }
            }
        }
        throw new DbcException("Invalid WakeupWord: " + wakeupWord);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}