package com.bignerdranch.android.flappybird;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FlappyBirdFragment extends Fragment{

    private View mFloorView;
    private View mGrassView;
    private View mSceneView;
    private View mDrawingView;
    private View mTopPipe;
    private View mBottomPipe;
    private AnimationDrawable birdAnimation;
    private AnimationDrawable birdAnim2;
    private boolean mPipeFlag;

    public static FlappyBirdFragment newInstance() { return new FlappyBirdFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View view = inflater.inflate(R.layout.flappybird_fragment, container, false);

        ImageView mBirdScene = (ImageView) view.findViewById(R.id.flappy_bird);
        this.birdAnimation = (AnimationDrawable) mBirdScene.getBackground();
        birdAnimation.start();

        mPipeFlag = false;
        
        mSceneView = view;
        mFloorView = view.findViewById(R.id.floor);
        mGrassView = view.findViewById(R.id.grass);
        mTopPipe = view.findViewById(R.id.top_pipe);
        mBottomPipe = view.findViewById(R.id.bottom_pipe);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPipeFlag == false) {
                    startPipeAnimation();
                }
            }
        });

        ImageView mBirdTwo = (ImageView) view.findViewById(R.id.flappy_bird2);
        this.birdAnim2 = (AnimationDrawable) mBirdTwo.getBackground();
        birdAnim2.start();

        return view;
    }

    private void startPipeAnimation() {
        float topPipeXStart = mTopPipe.getX();
        float bottomPipeXStart = mBottomPipe.getX();

        ObjectAnimator bottomWidthAnimator = ObjectAnimator.ofFloat(mTopPipe, "x", topPipeXStart,0).setDuration(3000);
        ObjectAnimator topWidthAnimator = ObjectAnimator.ofFloat(mBottomPipe, "x", bottomPipeXStart,0).setDuration(3000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(bottomWidthAnimator).with(topWidthAnimator);
        mPipeFlag = true;
        animatorSet.start();

    }
}
