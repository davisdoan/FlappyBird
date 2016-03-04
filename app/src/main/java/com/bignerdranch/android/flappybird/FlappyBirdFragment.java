package com.bignerdranch.android.flappybird;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

public class FlappyBirdFragment extends Fragment{

    private View mFloorView;
    private View mGrassView;
    private View mGameView;
    private View mTopPipe;
    private View mBottomPipe;
    private View mSkyView;
    private View mFlappyBird;
    private AnimationDrawable birdFlapAnimation;
    private int startingSpot;
    private int screenHeight;
    private Rect birdHitBox;
    private Rect topPipeHitBox;
    private Rect bottomPipeHitBox;
    private boolean mPipeStartedFlag;
    private boolean mFlyFlag;
    private boolean mCollsionFlag;
    private ObjectAnimator birdUpAnimator;
    private ObjectAnimator birdDownAnimator;
    private AnimatorSet pipeAnimatorSet;

    public static FlappyBirdFragment newInstance() { return new FlappyBirdFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View view = inflater.inflate(R.layout.flappybird_fragment, container, false);

        mFlappyBird = view.findViewById(R.id.flappy_bird);
        this.birdFlapAnimation = (AnimationDrawable) mFlappyBird.getBackground();
        birdFlapAnimation.start();

        mPipeStartedFlag = false;
        mCollsionFlag = false;
        
        mGameView = view;
        mFloorView = view.findViewById(R.id.floor);
        mGrassView = view.findViewById(R.id.grass);
        mTopPipe = view.findViewById(R.id.top_pipe);
        mSkyView = view.findViewById(R.id.sky);
        mBottomPipe = view.findViewById(R.id.bottom_pipe);

        WindowManager windowManager = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenHeight = screenSize.y;

        birdHitBox = new Rect(0,0,0,0);
        topPipeHitBox = new Rect(0,0,0,0);
        bottomPipeHitBox = new Rect(0,0,0,0);

        birdUpAnimator = ObjectAnimator.ofFloat(mFlappyBird, "translationY", -200);
        birdUpAnimator.setDuration(250);
        birdUpAnimator.setInterpolator(new DecelerateInterpolator());
        birdUpAnimator.addListener(flyUpListener);

        birdDownAnimator = ObjectAnimator.ofFloat(mFlappyBird, "translationY", 0);
        birdDownAnimator.setDuration(550);
        birdDownAnimator.setInterpolator(new AccelerateInterpolator());

        mGameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPipeStartedFlag == false) {
                    startPipeAnimation();
                }
                if (birdUpAnimator.isStarted()) {
                    birdUpAnimator.cancel();
                }
                if (startingSpot == 0) {
                    startingSpot = mFlappyBird.getTop();
                }
                if (yIsOutOfBounds(mFlappyBird)) {
                    // end the game
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

                birdDownAnimator.setFloatValues(mGrassView.getTop()  - mFlappyBird.getTop());
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

            ObjectAnimator bottomPipehAnimator = ObjectAnimator.ofFloat(mTopPipe, "x", topPipeXStart, -250).setDuration(3000);
            ObjectAnimator topPipeAnimator = ObjectAnimator.ofFloat(mBottomPipe, "x", bottomPipeXStart, -250).setDuration(3000);
            bottomPipehAnimator.setInterpolator(new AccelerateInterpolator());
            topPipeAnimator.setInterpolator(new AccelerateInterpolator());

            pipeAnimatorSet = new AnimatorSet();
            pipeAnimatorSet.play(bottomPipehAnimator).with(topPipeAnimator);

            mPipeStartedFlag = true;

            pipeAnimatorSet.start();
    }

    public void CheckCollision(Rect topRect, Rect lowerRect, Rect mainRect){
        if(topRect.intersect(mainRect) || lowerRect.intersect(mainRect)){
            Toast.makeText(getActivity(), "COLLISION!!!!! ", Toast.LENGTH_SHORT).show();
            mCollsionFlag = true;
        }
    }

    private boolean yIsOutOfBounds(View view){
        float y = view.getY();
        if( y < 0 || (y < 1600 && y > 1345)){
            Toast.makeText(getActivity(), "out of bounds!!!!! top" + "y is: " + y + "grass top is " + mGrassView.getTop(), Toast.LENGTH_SHORT).show();
            return true;
        }
        if( y + view.getHeight() + 150 > screenHeight){
            Toast.makeText(getActivity(), "out of bounds!!!!! bottom" + "y is: " + y, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
