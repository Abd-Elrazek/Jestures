package jestures.core.recognition.gesturedata;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.Codification;
import jestures.core.codification.DerivativeCodifier;

/**
 *
 * Represents all type of gesture data depending on the type of codification used.
 * <p>
 * {@link Codification} like {@link DerivativeCodifier} o Vectorial Space Codifier.
 *
 */
public interface UserData {
    /**
     * Set the user name.
     *
     * @param name
     *            the {@link String} user name
     */
    void setUserName(String name);

    /**
     * Get the user name.
     *
     * @return the String name
     */
    String getUserName();

    /**
     * Get the {@link RecognitionSettings}.
     *
     * @return the settings
     */
    RecognitionSettingsImpl getRecognitionSettings();

    /**
     * Set the settings.
     *
     * @param recognitionSettings
     *            the {@link RecognitionSettings}
     */
    void setRecognitionSettings(RecognitionSettingsImpl recognitionSettings);

    /**
     * Get all user gestures.
     *
     * @return the {@link List} of gestures
     */
    List<String> getAllUserGestures();

    /**
     * Get all template (feature vectors) for the selected gesture.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @return the {@link List} of feature vectors
     */
    List<List<Vector2D>> getGestureDataset(String gestureName);

    /**
     * Get a copy all feature vectors for all gestures.
     * <p>
     * Useful for template match
     *
     * @return the {@link Map} of all gestures data
     */
    Map<String, List<List<Vector2D>>> getAllGesturesData();

    /**
     * Delete all the gesture's dataset.
     *
     * @param gestureName
     *            the gesture name
     */
    void deleteGestureDataset(String gestureName);

    /**
     * Delete a single feature vector given a gesture and an index in the dataset.
     *
     * @param gestureName
     *            the String gesture name
     * @param index
     *            the index in the dataset
     */
    void deleteGestureFeatureVector(String gestureName, int index);

    /**
     * The Feature Vector to serialize.
     *
     * @param featureVector
     *            the {@link List} of feature vector
     * @param gestureName
     *            the {@link String} gesture name
     */
    void addGestureFeatureVector(String gestureName, List<Vector2D> featureVector);

    /**
     * The Feature Vector to serialize.
     *
     * @param gestureFeatureVectors
     *            the {@link List} of {@link List} of feature vector
     * @param gestureName
     *            the {@link String} gesture name
     */
    void addAllGestureFeatureVector(String gestureName, List<List<Vector2D>> gestureFeatureVectors);
}