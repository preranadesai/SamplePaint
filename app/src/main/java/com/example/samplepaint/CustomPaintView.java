/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.samplepaint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * This CustomView extends the View class. It is listening on the touch event
 * and draws on canvas as user is moving the finger on the screen
 *
 * Created by prerana on 1/10/2016.
 */
public class CustomPaintView extends View {
    private static String TAG = "CustomPaintView";
    //drawing path
    private Path mDrawPath;
    //defines what to draw
    private Paint mCanvasPaint;
    //defines how to draw
    private Paint mDrawPaint;
    //initial color
    private int mColor = Color.BLACK;
    //canvas - holding pen, holds the drawings
    //and transfers them to the view
    private Canvas mCanvas;
    //canvas bitmap
    private Bitmap mCanvasBitmap;

    private static final String SUPER_ID = "super";
    private static final String BITMAP_ID = "bitmap";

    //Constructor
    public CustomPaintView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        setSaveEnabled(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    /**
     * Initialize the Paint and Path. These will be used to draw the
     * view on the canvas
     */
    public void init(){
        Log.d(TAG, "initializing CustomPaintView");

        mDrawPath = new Path();
        mDrawPaint = new Paint();
        mDrawPaint.setColor(mColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(5); //Should be defined in resource file
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
   }


    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mCanvasBitmap == null) {
            //create canvas and canvasbitmap
            mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        } else {
            //Creating the scaled image so that it can be used in case of orientation change
            Bitmap temporary = Bitmap.createScaledBitmap(mCanvasBitmap, w, h, true);
            mCanvasBitmap = temporary;
        }
        mCanvas = new Canvas( mCanvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                handleActionUp(x, y);
                break;
            case MotionEvent.ACTION_DOWN:
                handleActionDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(x, y);
                break;
            default:
                return false;

        }
        //redraw
        invalidate();
        return true;
    }

    /**
     * Handles the touch event MotionEvent.ACTION_UP indicating that the user completed the drawing
     * @param x
     * @param y
     */
    private void handleActionUp(float x, float y){
        mDrawPath.lineTo(x, y);
        mCanvas.drawPath(mDrawPath, mDrawPaint);
        mDrawPath.reset();
    }

    /**
     * Handles the touch event MotionEvent.ACTION_DOWN indicating the user started the drawing
     * @param x
     * @param y
     */
    private void handleActionDown(float x, float y){
        mDrawPath.moveTo(x, y);
    }

    /**
     * Handles the touch event MotionEvent.ACTION_MOVE indicating the user is moving the finger on
     * the view
     * @param x
     * @param y
     */
    private void handleActionMove(float x, float y){
        mDrawPath.lineTo(x, y);
    }

    /**
     * Clear the canvas. Invoked by the user action to clear the content of the canvas
     */
    public void clearAll(){
        Log.d(TAG, "Clear all called");
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    /**
     * Set the color in the Paint that the user selected using the color picker
     * @param color
     */
    public void setSelectedColor(int color){
        Log.d(TAG, "setting the selected color to" + color);
        invalidate();
        mDrawPaint.setColor(color);
    }

    /**
     * Get the selected color. Used by unit test to verify the selected color
     * @return int color
     */
    public int getSelectedColor(){
        return mDrawPaint.getColor();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle outState = new Bundle();
        outState.putParcelable(SUPER_ID, super.onSaveInstanceState());
        outState.putParcelable(BITMAP_ID, mCanvasBitmap);
        return outState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle stateBundle = (Bundle)state;
            if (stateBundle.containsKey(SUPER_ID)){
                super.onRestoreInstanceState(stateBundle.getParcelable(SUPER_ID));
            }

            if (stateBundle.containsKey(BITMAP_ID)){
                mCanvasBitmap = stateBundle.getParcelable(BITMAP_ID);
            }
        }
    }
}
