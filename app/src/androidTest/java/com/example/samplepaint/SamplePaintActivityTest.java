package com.example.samplepaint;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.Instrumentation;
import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.View;

import com.android.colorpicker.ColorPickerDialog;
import com.example.samplepaint.CustomPaintView;
import com.example.samplepaint.R;
import com.example.samplepaint.SamplePaintActivity;

/**
 * This class includes unit test for SamplePaintActivity
 * Created by prerana on 1/12/2016.
 */
public class SamplePaintActivityTest extends ActivityInstrumentationTestCase2<SamplePaintActivity> {

    private Instrumentation mInstrumentation;
    private SamplePaintActivity mSamplePaintActivity;
    private CustomPaintView mCustomPaintView;
    private View mClearAction;
    private View mOpenColorPickerAction;

    public SamplePaintActivityTest() {
        super(SamplePaintActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        mSamplePaintActivity = getActivity();
        mClearAction = mSamplePaintActivity.findViewById(R.id.action_clear);
        mOpenColorPickerAction = mSamplePaintActivity.findViewById(R.id.action_pick_color);
    }

    /**
     * Verify that the activity loads the CustomPaintView
     */
    public void testLoadCustomView(){
        mCustomPaintView = (CustomPaintView)mSamplePaintActivity.findViewById(
                R.id.custompaint_view);
        assertNotNull(mCustomPaintView);

    }

    /**
     * Test that the activity include two menu actions - clear and openColorPicker
     */
    public void testActionBarMenuItemExist(){
        View mSamplePaintActivityDecorView = mSamplePaintActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(mSamplePaintActivityDecorView, mClearAction);
        ViewAsserts.assertOnScreen(mSamplePaintActivityDecorView, mOpenColorPickerAction);
    }

    /**
     * Test to ensure that the ColorPickerDialog is displayed when the colorPickerAction is
     * invoked.
     */
    public  void testOpenColorPickerAction(){
        // Click ActionBar menu item open color picker
        TouchUtils.clickView(this, mOpenColorPickerAction);

        getInstrumentation().waitForIdleSync();
        Fragment dialog = getActivity().getFragmentManager().
                                    findFragmentByTag(mSamplePaintActivity.FRAGMENT_TAG);
        assertTrue(dialog instanceof DialogFragment);
        assertTrue(((DialogFragment) dialog).getShowsDialog());
    }

    /**
     * Test the setting of color in ColorPickerDialog. When the activity opens for the first time,
     * the selected color in ColorPickerDialog is set to Color.BLACK as default color.
     * Verified that the ColorPickerDialog returns the default color.
     */
    public void testSetSelectedColorAsBlack(){

        // Click ActionBar menu item open color picker
        TouchUtils.clickView(this, mOpenColorPickerAction);

        getInstrumentation().waitForIdleSync();
        ColorPickerDialog dialog = (ColorPickerDialog)getActivity().
                getFragmentManager().findFragmentByTag(mSamplePaintActivity.FRAGMENT_TAG);
        //Verify that the dialog opens with selected color as black
        assertTrue(dialog.getSelectedColor() == Color.BLACK);
    }
}
