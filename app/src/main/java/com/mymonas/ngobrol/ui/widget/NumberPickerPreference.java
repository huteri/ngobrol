package com.mymonas.ngobrol.ui.widget;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.util.Clog;

/**
 * Created by Huteri on 10/25/2014.
 */
public class NumberPickerPreference extends DialogPreference {

    private static final int DEFAULT_VALUE = 5;
    private NumberPicker mNumberPicker;
    private int mCurrentValue;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_numberpicker);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);

    }

    @Override
    protected void onBindDialogView(View view) {
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(100);
        mNumberPicker.setValue(mCurrentValue);
        super.onBindDialogView(view);

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult) {
            mCurrentValue = mNumberPicker.getValue();
            persistInt(mNumberPicker.getValue());
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        Clog.d("");
        if(restorePersistedValue) {
            mCurrentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else  {
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }
    }
}
