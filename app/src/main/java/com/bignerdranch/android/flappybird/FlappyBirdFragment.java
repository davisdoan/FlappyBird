package com.bignerdranch.android.flappybird;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

public class FlappyBirdFragment extends Fragment{

    private View mFloorView;
    private View mGrassView;
    private View mSceneView;
    private View mTopPipe;
    private View mBottomPipe;
    private View mSkyView;
    private View mFlappyBird;
    private AnimationDrawable birdAnimation;
    private int startingSpot;
    private Rect birdHitBox;
    private Rect topPipeHitBox;
    private Rect bottomPipeHitBox;
    private boolean mPipeFlag;
    private boolean mFlyFlag;
    private boolean mCollsionFlag;
    private ObjectAnimator birdUpAnimator;
    private ObjectAnimator birdDownAnimator;

    public static FlappyBirdFragment newInstance() { return new FlappyBirdFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View view = inflater.inflate(R.layout.flappybird_fragment, container, false);

        mFlappyBird = view.findViewById(R.id.flappy_bird);
        this.birdAnimation = (AnimationDrawable) mFlappyBird.getBackground();
        birdAnimation.start();

        mPipeFlag = false;
        mCollsionFlag = false;
        
        mSceneView = view;
        mFloorView = view.findViewById(R.id.floor);
        mGrassView = view.findViewById(R.id.grass);
        mTopPipe = view.findViewById(R.id.top_pipe);
        mSkyView = view.findViewById(R.id.sky);
        mBottomPipe = view.findViewById(R.id.bottom_pipe);

        birdUpAnimator = ObjectAnimator.ofFloat(mFlappyBird, "translationY", -200);
        birdUpAnimator.setDuration(250);
        birdUpAnimator.setInterpolator(new DecelerateInterpolator());

        birdUpAnimator.addListener(flyUpListener);

        birdDownAnimator = ObjectAnimator.ofFloat(mFlappyBird, "translationY", 0);
        birdDownAnimator.setDuration(550);
        birdDownAnimator.setInterpolator(new AccelerateInterpolator());

        birdHitBox = new Rect(0,0,0,0);
        topPipeHitBox = new Rect(0,0,0,0);
        bottomPipeHitBox = new Rect(0,0,0,0);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPipeFlag == false) {
                    startPipeAnimation();
                }
                if (birdUpAnimator.isStarted()) {
                    birdUpAnimator.cancel();
                }

                if (startingSpot == 0) {
                    startingSpot = mFlappyBird.getTop();
                }

                mFlappyBird.layout(mFlappyBird.getLeft(), (int) mFlappyBird.getY(), mFlappyBird.getRight(), (int) mFlappyBird.getY() + mFlappyBird.getMeasuredHeight());
                mFlappyBird.setTranslationY(0);

                birdHitBox.set((int) mFlappyBird.getX(), (int) mFlappyBird.getY(), mFlappyBird.getWidth(), mFlappyBird.getHeight());
                topPipeHitBox.set((int) mTopPipe.getX(), (int) mBottomPipe.getY(), mBottomPipe.getWidth(), mBottomPipe.getHeight());
                bottomPipeHitBox.set((int) mBottomPipe.getX(), (int) mTopPipe.getY(), mBottomPipe.getWidth(), mBottomPipe.getHeight());

                mFlyFlag = false;
                birdUpAnimator.start();
            }
        });

        while(mCollsionFlag){
            CheckCollision(topPipeHitBox, bottomPipeHitBox, birdHitBox);
        }

        return view;
    }

    private Animator.AnimatorListener flyUpListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(!mFlyFlag){
                mFlappyBird.layout(mFlappyBird.getLeft(), (int) mFlappyBird.getY(), mFlappyBird.getRight(), (int) mFlappyBird.getY() + mFlappyBird.getMeasuredHeight());
                mFlappyBird.setTranslationY(0);

                birdDownAnimator.setFloatValues((mGrassView.getTop() - 100) - mFlappyBird.getTop());
                //Toast.makeText(getActivity(), "Bird Starting is: " + startingSpot + " and bird transY is: " + mFlappyBird.getTranslationY() + "bird top is " + mFlappyBird.getTop() + " floor location on window is " + mGrassView.getTop(), Toast.LENGTH_SHORT).show();
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

    public void CheckCollision(Rect topRect, Rect lowerRect, Rect mainRect){
        if(topRect.intersect(mainRect) || lowerRect.intersect(mainRect)){
            Toast.makeText(getActivity(), "COLLISION!!!!! ", Toast.LENGTH_SHORT).show();
            mCollsionFlag = true;
        }
    }
}
