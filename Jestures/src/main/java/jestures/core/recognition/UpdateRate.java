/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package jestures.core.recognition;

/**
 * The {@link UpdateRate} class.
 *
 */
public enum UpdateRate {
    /**
     * Gestures duration in frame. 30 FPS base
     */
    FPS_30(30), FPS_10(10), FPS_5(5), FPS_2(2);

    private int frameNumber;

    /**
     * The @link{JestureFrameLenght.java} constructor.
     */
    UpdateRate(final int rate) {
        this.frameNumber = rate;
    }

    /**
     * Get the @link{frameNumber}.
     *
     * @return the @link{frameNumber}
     */
    public int getFrameNumber() {
        return this.frameNumber;
    }
}
