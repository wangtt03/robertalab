package de.fhg.iais.roberta.mode.sensor.mbed;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum ValueType {
    ANALOG(), DIGITAL(), PULSEHIGH(), PULSELOW();

    private final String[] values;

    private ValueType(String... values) {
        this.values = values;

    }

    /**
     * get mode from {@link MotorTachoMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link MotorTachoMode}
     */
    public static ValueType get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( ValueType mo : ValueType.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.values ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid mode: " + s);
    }
}
