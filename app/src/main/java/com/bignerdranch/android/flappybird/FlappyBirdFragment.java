package com.bignerdranch.android.flappybird;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class FlappyBirdFragment extends Fragment{

    private View mFloorView;
    private View mGrassView;
    private View mSceneView;
    private View mTopPipe;
    private View mBottomPipe;
    private AnimationDrawable birdAnimation;
    private boolean mPipeFlag;

    public static FlappyBirdFragment newInstance() { return new FlappyBirdFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View view = inflater.inflate(R.layout.flappybird_fragment, container, false);

        final ImageView mBirdScene = (ImageView) view.findViewById(R.id.flappy_bird);
        this.birdAnimation = (AnimationDrawable) mBirdScene.getBackground();
        birdAnimation.start();

        mPipeFlag = false;
        
        mSceneView = view;
        mFloorView = view.findViewById(R.id.floor);
        mGrassView = view.findViewById(R.id.grass);
        mTopPipe = view.findViewById(R.id.top_pipe);
        mBottomPipe = view.findViewById(R.id.bottom_pipe);


        mSceneView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator birdDownAnimator = ObjectAnimator.ofFloat(mBirdScene, "y", mBirdScene.getHeight(), -3).setDuration(30);
                ObjectAnimator birdUpAnimator = ObjectAnimator.ofFloat(mBirdScene, "y", v.getBottom(), v.getTop()).setDuration(30);
                birdUpAnimator.setInterpolator(new AccelerateInterpolator());
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        animatorSet.play(birdDownAnimator);
                        animatorSet.start();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:

                        animatorSet.play(birdDownAnimator);
                        animatorSet.start();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;

                }
                return true;
            }
        });

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPipeFlag == false) {
                    startPipeAnimation();
                }
            }
        });

        return view;
    }

    private void startPipeAnimation() {
        float topPipeXStart = mTopPipe.getX();
        float bottomPipeXStart = mBottomPipe.getX();

        ObjectAnimator bottomWidthAnimator = ObjectAnimator.ofFloat(mTopPipe, "x", topPipeXStart,-250).setDuration(3000);
        ObjectAnimator topWidthAnimator = ObjectAnimator.ofFloat(mBottomPipe, "x", bottomPipeXStart,-250).setDuration(3000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(bottomWidthAnimator).with(topWidthAnimator);
        mPipeFlag = true;
        animatorSet.start();

    }
}
