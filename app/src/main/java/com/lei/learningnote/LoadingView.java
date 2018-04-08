package com.lei.learningnote;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by jianglei on 2018/4/8.
 */

public class LoadingView extends View{

    private static final int NORMAL = 0;
    private static final int START = 1;
    private static final int PRE = 2;
    private static final int EXPANDLE = 3;
    private static final int LOAD = 4;
    private static final int END = 5;
    private float heightValue;
    private float mWidth;
    private float mHeight;
    private int status;
    private float currentLength;
    //背景画笔
    private Paint mBgPaint;
    private Paint mTextPaint;
    private int mTextColor;
    private float mTextSize;
    private float mAngle;
    private int mPreAnimSpeed;
    private ValueAnimator mAngleAnimator;
    private ValueAnimator mLoadAnimator;
    private long mPreAnimDuration = 2000;
    private Animation scaleAnimator;
    private Animation expandAnimator;
    private float mTranslationX;
    private int mLoadAngle;
    private int mProgress;
    private MovePoint[] mFourMovePoints = new MovePoint[4];
    private ValueAnimator mMovePointAnim;
    private int mMovePointSpeed;


    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode  = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        float widthValue = (getResources().getDisplayMetrics().widthPixels)*4/5;

        if(widthMode != MeasureSpec.EXACTLY){
            if(widthMode == MeasureSpec.AT_MOST){
                if(width > widthValue){
                    width = (int) widthValue;
                }
            }else{
                width = (int) widthValue;
            }
        }

        if(heightMode != MeasureSpec.EXACTLY){
            if(heightMode == MeasureSpec.AT_MOST){
                if(height > dip2px(heightValue)){
                    height = dip2px(heightValue);
                }
            }else{
                height = dip2px(heightValue);
            }
        }

        mWidth = width;
        mHeight = height;
        currentLength = mWidth;
        Log.e("tag","width = "+width+" height = "+height);

        mFourMovePoints[0] = new MovePoint((float) ((mWidth - mHeight / 2f) * 0.88), 0, dip2px(4));
        mFourMovePoints[1] = new MovePoint((float) ((mWidth - mHeight / 2f) * 0.83), 0, dip2px(3));
        mFourMovePoints[2] = new MovePoint((float) ((mWidth - mHeight / 2f) * 0.78), 0, dip2px(2));
        mFourMovePoints[3] = new MovePoint((float) ((mWidth - mHeight / 2f) * 0.70), 0, dip2px(5));

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(mMovePointSpeed);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = 0; i < mFourMovePoints.length; i++) {
                    mFourMovePoints[i].moveX = mFourMovePoints[i].startX - mFourMovePoints[0].startX * animation.getAnimatedFraction();
                    if (mFourMovePoints[i].moveX < mHeight / 2f) {
                        mFourMovePoints[i].isDraw = false;
                    }
                    mFourMovePoints[i].moveY = getMoveY(mFourMovePoints[i].moveX);
                    Log.d("TAG", "fourMovePoint[0].moveX:" + mFourMovePoints[0].moveX + ",fourMovePoint[0].moveY:" + mFourMovePoints[0].moveY);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (int i = 0; i < mFourMovePoints.length; i++) {
                    mFourMovePoints[i].isDraw = true;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                for (int i = 0; i < mFourMovePoints.length; i++) {
                    mFourMovePoints[i].isDraw = true;
                }

            }
        });
        mMovePointAnim = animator;

        super.onMeasure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(status == NORMAL || status == START){
            float left = (mWidth - currentLength)/2f;
            float right = (mWidth + currentLength)/2f;
            float r = mHeight/2f;
            canvas.drawRoundRect(new RectF(left,0,right,mHeight),r,r,mBgPaint);

            if (status == NORMAL){
                Paint.FontMetrics fm = mTextPaint.getFontMetrics();
                float x = mWidth/2;
                float y = mHeight / 2 + (fm.descent - fm.ascent) / 2 - fm.descent;
                canvas.drawText("下载",x,y,mTextPaint);
            }
        }

        if(status == PRE){
            //圆
            canvas.drawCircle(mWidth/2,mHeight/2,mHeight/2,mBgPaint);

            mTextPaint.setStyle(Paint.Style.FILL);
            //画中圆
            float cX = mWidth / 2f;
            float cY = mHeight / 2f;
            float radius = mHeight / 2 / 3f;
            canvas.drawCircle(cX, cY, radius, mTextPaint);

            canvas.save();
            canvas.rotate(mAngle,mWidth/2,mHeight/2);

            //画上方小圆
            float rr = radius/2;
            float cYY = cY-(radius+ rr/3);
            canvas.drawCircle(cX, cYY, rr, mTextPaint);

            float cXX = (float) (cX - Math.sqrt(3) / 2f * (radius + rr/3));
            cYY = (mHeight/2)+(radius+rr/3)/2;
            canvas.drawCircle(cXX,cYY,rr,mTextPaint);

            cXX = (float) (cX + Math.sqrt(3) / 2f * (radius + rr/3));
            cYY = (mHeight/2)+(radius+rr/3)/2;
            canvas.drawCircle(cXX,cYY,rr,mTextPaint);

            canvas.restore();

        }

        if (status == EXPANDLE) {

            float left = (mWidth - currentLength) / 2f;
            float right = (mWidth + currentLength) / 2f;
            float r = mHeight / 2f;
            canvas.drawRoundRect(new RectF(left, 0, right, mHeight), r, r, mBgPaint);
            canvas.save();
            mTextPaint.setStyle(Paint.Style.FILL);
            canvas.translate(mTranslationX, 0);



            //大圆的圆心 半径
            float cX = mWidth / 2f;
            float cY = mHeight / 2f;

            float radius = mHeight / 2 / 3f;
            canvas.drawCircle(cX, cY, radius, mTextPaint);

            float rr = radius/2;
            float cYY = cY-(radius+ rr/3);
            canvas.drawCircle(cX, cYY, rr, mTextPaint);

            float cXX = (float) (cX - Math.sqrt(3) / 2f * (radius + rr/3));
            cYY = (mHeight/2)+(radius+rr/3)/2;
            canvas.drawCircle(cXX,cYY,rr,mTextPaint);

            cXX = (float) (cX + Math.sqrt(3) / 2f * (radius + rr/3));
            cYY = (mHeight/2)+(radius+rr/3)/2;
            canvas.drawCircle(cXX,cYY,rr,mTextPaint);

            canvas.restore();
        }

        if(status == LOAD){


            float left = (mWidth - currentLength) / 2f;
            float right = (mWidth + currentLength) / 2f;
            float r = mHeight / 2f;
            mBgPaint.setColor(Color.GREEN);
            canvas.drawRoundRect(new RectF(left, 0, right, mHeight), r, r, mBgPaint);
            if (mProgress != 100) {
                for (int i = 0; i < mFourMovePoints.length; i++) {
                    if (mFourMovePoints[i].isDraw)
                        canvas.drawCircle(mFourMovePoints[i].moveX, mFourMovePoints[i].moveY, mFourMovePoints[i].radius, mTextPaint);
                }
            }


            float progressRight = 35 * mWidth / 100f;
            mBgPaint.setColor(Color.YELLOW);
            canvas.save();
            canvas.clipRect(0, 0, progressRight, mHeight);
            canvas.drawRoundRect(new RectF(left, 0, right, mHeight), r, r, mBgPaint);
            canvas.restore();

            //------------------------------------------

            mBgPaint.setColor(Color.BLUE);
            float cX = mWidth-(mHeight/2);
            float cY = mHeight / 2f;

            canvas.drawCircle(mWidth-(mHeight/2),mHeight/2,mHeight/2,mBgPaint);

            float radius = mHeight / 2 / 3f;
            mTextPaint.setStyle(Paint.Style.FILL);
            //画中圆
            canvas.drawCircle(cX, cY, radius, mTextPaint);

            canvas.save();
            canvas.rotate(mLoadAngle,cX,mHeight/2);

            cY = mHeight/2;
            //画上方小圆
            float rr = radius/2;
            float cYY = cY-(radius+ rr/3);
            canvas.drawCircle(cX, cYY, rr, mTextPaint);

            float cXX = (float) (cX - Math.sqrt(3) / 2f * (radius + rr/3));
            cYY = (mHeight/2)+(radius+rr/3)/2;
            canvas.drawCircle(cXX,cYY,rr,mTextPaint);

            cXX = (float) (cX + Math.sqrt(3) / 2f * (radius + rr/3));
            cYY = (mHeight/2)+(radius+rr/3)/2;
            canvas.drawCircle(cXX,cYY,rr,mTextPaint);

            canvas.restore();

        }

    }

    private void init() {
        initArgs();
        initPaint();
        initAnimator();
    }

    /**
     * 初始化动画
     */
    private void initAnimator() {

        scaleAnimator = new Animation(){
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                currentLength = mWidth * (1 - interpolatedTime);
                if (currentLength < mHeight) {
                    currentLength = mHeight;
                    clearAnimation();
                    mAngleAnimator.start();
                }
                invalidate();
            }
        };
        scaleAnimator.setDuration(2000);
        scaleAnimator.setInterpolator(new LinearInterpolator());
        scaleAnimator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                status = START;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAngle += mPreAnimSpeed;
                invalidate();
            }
        });
        animator.setDuration(mPreAnimDuration);
        animator.setInterpolator(new LinearInterpolator());
        mAngleAnimator = animator;
        mAngleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                status = PRE;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startAnimation(expandAnimator);
                status = EXPANDLE;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        expandAnimator = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                currentLength = mHeight+(mWidth-mHeight)*interpolatedTime;
                mTranslationX = (mWidth-mHeight)/2*interpolatedTime;
                invalidate();
            }
        };

        expandAnimator.setDuration(2000);
        expandAnimator.setInterpolator(new LinearInterpolator());
        expandAnimator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLoadAnimator.start();
                mMovePointAnim.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mLoadAnimator = new ValueAnimator().ofFloat(0,1);
        mLoadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLoadAngle += mPreAnimSpeed;
                invalidate();
            }
        });
        mLoadAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                status = LOAD;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mLoadAnimator.setDuration(Integer.MAX_VALUE);
        mLoadAnimator.setInterpolator(new LinearInterpolator());

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.BLUE);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
    }

    /**
     * 初始化参数
     */
    private void initArgs() {
        if(heightValue == 0){
            heightValue = 50f;
        }

        if (mTextSize == 0){
            mTextSize = 40;
        }

        if(mTextColor == 0){
            mTextColor = Color.WHITE;
        }

        if (mPreAnimSpeed == 0){
            mPreAnimSpeed = 10;
        }

        if (mMovePointSpeed == 0){
            mMovePointSpeed = 3000;
        }
    }

    /**
     * 将dp转为px
     * @param dip
     * @return
     */
    private int dip2px(float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) dip, getResources().getDisplayMetrics());
    }


    private float getMoveY(float moveX) {
        return (float) (mHeight / 2 + (mHeight / 2 - mFourMovePoints[3].radius) * Math.sin(4 * Math.PI * moveX / (mWidth - mHeight) + mHeight / 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_UP){
            Log.e("tag","动画start");
            startAnimation(scaleAnimator);
        }
        return true;
    }

    public static class MovePoint

    {
        float startX;
        float moveX;
        float moveY;
        float radius;
        boolean isDraw;

        public MovePoint(float startX, float moveY, float radius) {
            this.startX = startX;
            this.moveY = moveY;
            this.radius = radius;
        }
    }
}
