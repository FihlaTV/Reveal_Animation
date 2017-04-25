package com.spaceo.revealanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int DELAY = 100;
    private RelativeLayout relativeContainer;
    private ImageView img_green;
    private ImageView img_blue;
    private ImageView img_yellow;
    private Interpolator interpolator;
    private TextView textdemo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);

        relativeContainer=(RelativeLayout)findViewById(R.id.relativeContainer);
        img_green=(ImageView)findViewById(R.id.img_green);
        img_blue=(ImageView)findViewById(R.id.img_blue);

        img_yellow =(ImageView)findViewById(R.id.img_yellow);
        textdemo1 =(TextView)findViewById(R.id.textdemo1);


        img_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                revealColor(relativeContainer, R.color.green);
                textdemo1.setText("Reveal animation from center");


            }
        });
        img_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animateImageOutside();
                Animator anim = mainTostartAnimation(relativeContainer, R.color.blue, relativeContainer.getWidth() / 2, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateImageInside();
                    }
                });
                textdemo1.setText("Reveal animation from top center");


            }
        });

        img_yellow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (view.getId() == R.id.img_yellow) {
                        mainTostartAnimation(relativeContainer, R.color.yellow, (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                        textdemo1.setText("Reveal animation start on Touch points");

                    }
                }
                return false;
            }
        });

    }


    private void revealColor(ViewGroup viewRoot, @ColorRes int color) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        mainTostartAnimation(viewRoot, color, cx, cy);
    }

    private Animator mainTostartAnimation(ViewGroup viewRoot, @ColorRes int color, int x, int y) {

        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, color));
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;

    }

    private void animateImageInside() {
        for (int i = 0; i < relativeContainer.getChildCount(); i++) {
            View child = relativeContainer.getChildAt(i);
            child.animate()
                    .setStartDelay(100 + i * DELAY)
                    .setInterpolator(interpolator)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1);
        }
    }

    private void animateImageOutside() {
        for (int i = 0; i < relativeContainer.getChildCount(); i++) {
            View child = relativeContainer.getChildAt(i);
            child.animate()
                    .setStartDelay(i)
                    .setInterpolator(interpolator)
                    .alpha(0)
                    .scaleX(0f)
                    .scaleY(0f);
        }
    }


}
