package com.bignerdranch.android.flappybird;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class FlappyBirdFragment extends Fragment{

    private View mFloorView;
    private View mGrassView;
    private View mSceneView;
    private View mTopPipe;
    private View mBottomPipe;
    private View mBirdScene;
    private AnimationDrawable birdAnimation;
    private int startingSpot;
    private boolean mPipeFlag;
    private boolean mFlyFlag;
    private ObjectAnimator birdUpAnimator;
    private ObjectAnimator birdDownAnimator;

    public static FlappyBirdFragment newInstance() { return new FlappyBirdFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View view = inflater.inflate(R.layout.flappybird_fragment, container, false);

        mBirdScene = (ImageView) view.findViewById(R.id.flappy_bird);
        this.birdAnimation = (AnimationDrawable) mBirdScene.getBackground();
        birdAnimation.start();

        mPipeFlag = false;
        
        mSceneView = view;
        mFloorView = view.findViewById(R.id.floor);
        mGrassView = view.findViewById(R.id.grass);
        mTopPipe = view.findViewById(R.id.top_pipe);
        mBottomPipe = view.findViewById(R.id.bottom_pipe);

        birdUpAnimator = ObjectAnimator.ofFloat(mBirdScene, "translationY", -200);
        birdUpAnimator.setDuration(250);
        birdUpAnimator.setInterpolator(new DecelerateInterpolator());
        birdUpAnimator.addListener(flyUpListener);

        birdDownAnimator = ObjectAnimator.ofFloat(mBirdScene, "translationY", 0);
        birdDownAnimator.setDuration(250);
        birdDownAnimator.setInterpolator(new AccelerateInterpolator());

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPipeFlag == false) {
                    startPipeAnimation();
                }
                if(birdUpAnimator.isStarted()){
                    birdUpAnimator.cancel();
                }

                if(startingSpot == 0) {
                    startingSpot = mBirdScene.getTop();
                }

                mBirdScene.layout(mBirdScene.getLeft(), (int) mBirdScene.getY(), mBirdScene.getRight(), (int) mBirdScene.getY() + mBirdScene.getMeasuredHeight());
                mBirdScene.setTranslationY(0);
                mFlyFlag = false;
                birdUpAnimator.start();
            }
        });
        return view;
    }

    private Animator.AnimatorListener flyUpListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(!mFlyFlag){
                mBirdScene.layout(mBirdScene.getLeft(),(int)mBirdScene.getY(), mBirdScene.getRight(), (int)mBirdScene.getY() + mBirdScene.getMeasuredHeight());
                mBirdScene.setTranslationY(0);
                birdDownAnimator.setFloatValues(startingSpot - mBirdScene.getTop());
                birdDownAnimator.start();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mFlyFlag = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private void startPipeAnimation() {
            float topPipeXStart = mTopPipe.getX();
            float bottomPipeXStart = mBottomPipe.getX();

            ObjectAnimator bottomWidthAnimator = ObjectAnimator.ofFloat(mTopPipe, "x", topPipeXStart, -250).setDuration(3000);
            ObjectAnimator topWidthAnimator = ObjectAnimator.ofFloat(mBottomPipe, "x", bottomPipeXStart, -250).setDuration(3000);

            bottomWidthAnimator.setInterpolator(new AccelerateInterpolator());
            topWidthAnimator.setInterpolator(new AccelerateInterpolator());

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(bottomWidthAnimator).with(topWidthAnimator);
            mPipeFlag = true;
            animatorSet.start();
    }


}
