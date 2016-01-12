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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.samplepaint.R;

/**
 * This is the main activity for the SamplePaint app. It uses the CustomView
 * that allows drawing on the view using the finger. There are two menu actions
 * supported
 * Clear: The clear action erases all the drawing on the screen
 * Color Picker: Opens the color picker and allow the user to select the color
 *               to draw on the screen.
 *
 *
 */
public class SamplePaintActivity extends AppCompatActivity {
    private static String TAG = SamplePaintActivity.class.getName();
    private CustomPaintView mCustomPaintView;
    private int mSelectedColor = 0;
    protected static final String KEY_SELECTED_COLOR = "selected_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_paint);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);

        //Set the initial color as Color.BLACK
        mSelectedColor = Color.parseColor("#000000");
        mCustomPaintView = (CustomPaintView)findViewById(R.id.custompaint_view);
        if (savedInstanceState != null) {
            mSelectedColor = (Integer) savedInstanceState.getSerializable(KEY_SELECTED_COLOR);
            if (mCustomPaintView != null) {
                Log.i(TAG,"Setting the selected color from saved state");
                mCustomPaintView.setSelectedColor(mSelectedColor);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample_paint, menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SELECTED_COLOR, mSelectedColor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch ( item.getItemId()){
            case R.id.action_clear:
                clearAll();
                return true;
            case R.id.action_pick_color:
                openColorPicker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void clearAll() {
        if (mCustomPaintView != null){
            mCustomPaintView.clearAll();
        }
    }

    private void openColorPicker(){
        //Get the color choices defined in resource file
        int[] colorArray = getColorChoices(this);
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(
                            R.string.color_picker_default_title,
                            colorArray,
                            mSelectedColor,
                            5,
                            ColorPickerDialog.SIZE_SMALL);

        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                Log.i(TAG,"SelectedColor " + color);
                //set the selected color in customPaintView
                mCustomPaintView.setSelectedColor(mSelectedColor);
            }
        });

        colorPickerDialog.show(getFragmentManager(),"colorpicker");

    }

    /**
     * Reads the color array from resource file. This array is then used
     * by ColorPickerDialog
     *
     * @param context
     * @return int[] array of colors
     */
    private int[] getColorChoices(Context context){
        int[] colorChoices = null;
        String[] colorArray = context.getResources().getStringArray(
                                    R.array.default_color_choice_values);
        if (colorArray != null && colorArray.length > 0){
            colorChoices = new int[colorArray.length];
            for(int i=0; i< colorArray.length; i++){
                colorChoices[i] = Color.parseColor(colorArray[i]);
            }
        }
        return colorChoices;
    }
}
