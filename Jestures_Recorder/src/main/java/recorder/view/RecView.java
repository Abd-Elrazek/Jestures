package recorder.view;

import java.util.Queue;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javafx.scene.image.Image;
import jestures.core.codification.FrameLenght;
import jestures.core.tracking.Tracking;

/**
 *
 *
 */
public interface RecView {
    /**
     * Update view on frame event.
     *
     * @param frame
     *            the frame
     * @param derivative
     *            the {@link Vector2D} derivative
     * @param path
     *            the {@link Vector2D} gesture path
     */
    void notifyOnFrameChange(int frame, Vector2D derivative, Vector2D path);

    /**
     * Update view on feature vector event.
     *
     * @param featureVector
     */
    void notifyOnFeatureVectorEvent(Queue<Vector2D> featureVector);

    /**
     * Set the frame Length.
     *
     * @param length
     *            the {@link FrameLenght}
     */
    void setFrameLength(FrameLenght length);

    /**
     * Start the {@link Tracking}.
     */
    void startSensor();

    /**
     * Stop the {@link Tracking}.
     */
    void stopSensor();

    /**
     * Get the tracker.
     *
     * @return the {@link Tracking} tracker
     */
    Recording getTracker();

    /**
     * Get the {@link FrameLenght} for tracking.
     *
     * @return the {@link FrameLenght}
     */
    FrameLenght getFrameLength();

    void setOnStartRecording(boolean isRecording);

    void createUserInListView(final String nickname, final Image image);

}