package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinWriteValue;
import de.fhg.iais.roberta.syntax.action.mbed.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.expr.Image;
import de.fhg.iais.roberta.syntax.expr.PredefinedImage;
import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.functions.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerOrientationSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.MbedGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.MicrophoneSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinTouchSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface MbedAstVisitor<V> extends AstVisitor<V> {

    /**
     * visit a {@link DisplayTextAction}.
     *
     * @param displayTextAction phrase to be visited
     */
    V visitDisplayTextAction(DisplayTextAction<V> displayTextAction);

    /**
     * visit a {@link PredefinedImage}.
     *
     * @param predefinedImage phrase to be visited
     */
    V visitPredefinedImage(PredefinedImage<V> predefinedImage);

    /**
     * visit a {@link DisplayImageAction}.
     *
     * @param displayImageAction phrase to be visited
     */
    V visitDisplayImageAction(DisplayImageAction<V> displayImageAction);

    /**
     * visit a {@link PlayNoteAction}.
     *
     * @param playNoteAction phrase to be visited
     */
    V visitPlayNoteAction(PlayNoteAction<V> playNoteAction);

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction);

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction);

    /**
     * visit a {@link Image}.
     *
     * @param image phrase to be visited
     */
    V visitImage(Image<V> image);

    /**
     * visit a {@link GestureSensor}.
     *
     * @param gestureSensor phrase to be visited
     */
    V visitGestureSensor(GestureSensor<V> gestureSensor);

    /**
     * visit a {@link TemperatureSensor}.
     *
     * @param temperatureSensor phrase to be visited
     */
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    /**
     * visit a {@link LedColor}.
     *
     * @param ledColor phrase to be visited
     */
    V visitLedColor(LedColor<V> ledColor);

    /**
     * visit a {@link LedOnAction}.
     *
     * @param ledOnAction phrase to be visited
     */
    V visitLedOnAction(LedOnAction<V> ledOnAction);

    /**
     * visit a {@link AmbientLightSensor}.
     *
     * @param ambientLightSensor phrase to be visited
     */
    V visitAmbientLightSensor(AmbientLightSensor<V> ambientLightSensor);

    /**
     * visit a {@link RadioSendAction}.
     *
     * @param radioSendAction phrase to be visited
     */
    V visitRadioSendAction(RadioSendAction<V> radioSendAction);

    /**
     * visit a {@link RadioReceiveAction}.
     *
     * @param radioReceiveAction phrase to be visited
     */
    V visitRadioReceiveAction(RadioReceiveAction<V> radioReceiveAction);

    /**
     * visit a {@link MbedGetSampleSensor}.
     *
     * @param getSampleSensor phrase to be visited
     */
    V visitMbedGetSampleSensor(MbedGetSampleSensor<V> getSampleSensor);

    /**
     * visit a {@link RgbColor}.
     *
     * @param rgbColor phrase to be visited
     */
    V visitRgbColor(RgbColor<V> rgbColor);

    /**
     * visit a {@link PinTouchSensor}.
     *
     * @param pinTouchSensor phrase to be visited
     */
    V visitPinTouchSensor(PinTouchSensor<V> pinTouchSensor);

    /**
     * visit a {@link PinGetValueSensor}.
     *
     * @param pinValueSensor phrase to be visited
     */
    V visitPinGetValueSensor(PinGetValueSensor<V> pinValueSensor);

    /**
     * visit a {@link PinWriteValue}.
     *
     * @param pinWriteValueSensor phrase to be visited
     */
    V visitPinWriteValueSensor(PinWriteValue<V> pinWriteValueSensor);

    /**
     * visit a {@link DisplaySetBrightnessAction}.
     *
     * @param displaySetBrightnessAction phrase to be visited
     */
    V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<V> displaySetBrightnessAction);

    /**
     * visit a {@link DisplayGetBrightnessAction}.
     *
     * @param displayGetBrightnessAction phrase to be visited
     */
    V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<V> displayGetBrightnessAction);

    /**
     * visit a {@link DisplaySetPixelAction}.
     *
     * @param DisplaySetPixelAction phrase to be visited
     */
    V visitDisplaySetPixelAction(DisplaySetPixelAction<V> displaySetPixelAction);

    /**
     * visit a {@link DisplayGetPixelAction}.
     *
     * @param DisplayGetPixelAction phrase to be visited
     */
    V visitDisplayGetPixelAction(DisplayGetPixelAction<V> displayGetPixelAction);

    /**
     * visit a {@link AccelerometerSensor}.
     *
     * @param AccelerometerSensor phrase to be visited
     */
    V visitAccelerometerSensor(AccelerometerSensor<V> accelerometerSensor);

    /**
     * visit a {@link AccelerometerOrientationSensor}.
     *
     * @param AccelerometerOrientationSensor phrase to be visited
     */
    V visitAccelerometerOrientationSensor(AccelerometerOrientationSensor<V> accelerometerOrientationSensor);

    /**
     * visit a {@link MicrophoneSensor}.
     *
     * @param microphoneSensor phrase to be visited
     */
    V visitMicrophoneSensor(MicrophoneSensor<V> microphoneSensor);

    /**
     * visit a {@link RadioSetChannelAction}.
     *
     * @param radioSetChannelAction phrase to be visited
     */
    V visitRadioSetChannelAction(RadioSetChannelAction<V> radioSetChannelAction);
}
