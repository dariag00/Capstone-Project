package com.kloso.capstoneproject.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;


public class ViewAnimation {

    public static boolean rotateFab(View view, boolean rotate){
        view.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }

    public static void animateFabTranslationVertically(View view, float dimen){
        view.animate().translationY(dimen);
    }

    public static void animateFabTranslationHorizontally(View view, float dimen){
        view.animate().translationX(dimen);
    }

}
