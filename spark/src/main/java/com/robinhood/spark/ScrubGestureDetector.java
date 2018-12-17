/**
 * Copyright (C) 2016 Robinhood Markets, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.robinhood.spark;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 * Exposes simple methods for detecting scrub events.
 */
class ScrubGestureDetector implements View.OnTouchListener {
    static final long LONG_PRESS_TIMEOUT_MS = 20;

    private final ScrubListener scrubListener;

    private final Handler handler;

    private boolean enabled;

    ScrubGestureDetector(
            @NonNull ScrubListener scrubListener,
            @NonNull Handler handler,
            float touchSlop) {
        this.scrubListener = scrubListener;
        this.handler = handler;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!enabled) return false;

        final float x = event.getX();
        final float y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // store the time to compute whether future events are 'long presses'
                scrubListener.onScrubbed(x,y);
                return true;
            case MotionEvent.ACTION_MOVE:
                scrubListener.onScrubbed(x, y);

                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                scrubListener.onScrubEnded();
                return true;
            default:
                return false;
        }
    }

    interface ScrubListener {
        void onScrubbed(float x, float y);
        void onScrubEnded();
    }
}

