package com.example.bagvana.Activity.Chatbot;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;

import de.hdodenhof.circleimageview.CircleImageView;

public class BubbleView extends androidx.appcompat.widget.AppCompatImageView {
    private float mLastTouchX;
    private float mLastTouchY;
    private int mWidth;
    private int mHeight;
    private boolean mDragging;

    public BubbleView(Context context) {
        super(context);
        init();
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getRawX();
                float y = event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        mWidth = v.getWidth();
                        mHeight = v.getHeight();
                        mLastTouchX = x;
                        mLastTouchY = y;
                        mDragging = false;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float deltaX = x - mLastTouchX;
                        float deltaY = y - mLastTouchY;
                        if (!mDragging && (Math.abs(deltaX) > 5 || Math.abs(deltaY) > 5)) {
                            mDragging = true;
                        }
                        if (mDragging) {
                            float newX = v.getX() + deltaX;
                            float newY = v.getY() + deltaY;

                            if (newX < 0) newX = 0;
                            if (newY < 0) newY = 0;

                            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
                            int screenWidth = displayMetrics.widthPixels;
                            int screenHeight = displayMetrics.heightPixels;

                            if (newX + mWidth > screenWidth) {
                                newX = screenWidth - mWidth;
                            }
                            if (newY + mHeight > screenHeight) {
                                newY = screenHeight - mHeight;
                            }

                            v.setX(newX);
                            v.setY(newY);
                            mLastTouchX = x;
                            mLastTouchY = y;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (!mDragging) {
                            // Handle click event
                            Toast.makeText(getContext(), "Chat!", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getContext(), ChatActivity.class);
                            // myIntent.putExtras(myBundle);
                            getContext().startActivity(myIntent);
                        }
                        break;
                }

                return true;
            }
        });
    }
}
