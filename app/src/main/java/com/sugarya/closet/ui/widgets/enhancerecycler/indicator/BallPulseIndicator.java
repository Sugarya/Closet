package com.sugarya.closet.ui.widgets.enhancerecycler.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2015/10/16.
 */
public class BallPulseIndicator extends BaseIndicatorController {

    public static final float SCALE = 1.0f;
    public static final int UNIT_ANIMATOR_DURATION = 750;

    //scale x ,y
    private float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE};
    public static final float CIRCLE_SPACING = 4;


    @Override
    public void draw(Canvas canvas, Paint paint) {
        float radius = (Math.min(getWidth(), getHeight()) - CIRCLE_SPACING * 2) / 6;
        float x = getWidth() / 2 - (radius * 2 + CIRCLE_SPACING);
        float y = getHeight() / 2;
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + CIRCLE_SPACING * i;
            canvas.translate(translateX, y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        int[] delays = new int[]{120, 240, 360};
        for (int i = 0; i < 3; i++) {
            final int index = i;

            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.3f, 1);

            scaleAnim.setDuration(UNIT_ANIMATOR_DURATION);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);

            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();
            animators.add(scaleAnim);
        }
        return animators;
    }

}
