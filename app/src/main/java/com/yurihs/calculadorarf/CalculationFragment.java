package com.yurihs.calculadorarf;


import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public abstract class CalculationFragment extends Fragment {

    protected static Integer getIntegerFromTextInputLayout(TextInputLayout textInputLayout) {
        EditText view = textInputLayout.getEditText();
        if (view == null) {
            return null;
        }
        try {
            return NumberFormat.getInstance().parse(view.getText().toString()).intValue();
        } catch (ParseException ep) {
            try {
                return Integer.valueOf(view.getText().toString());
            } catch (NumberFormatException en) {
                return null;
            }
        }
    }

    protected static Double getDoubleFromTextInputLayout(TextInputLayout textInputLayout) {
        EditText view = textInputLayout.getEditText();
        if (view == null) {
            return null;
        }
        try {
            return NumberFormat.getInstance().parse(view.getText().toString()).doubleValue();
        } catch (ParseException ep) {
            try {
                return Double.valueOf(view.getText().toString());
            } catch (NumberFormatException en) {
                return null;
            }
        }
    }

    protected static int getCheckedIdFromToggleGroup(View rootView, int toggleGroupId) {
        return ((SingleSelectToggleGroup) rootView.findViewById(toggleGroupId)).getCheckedId();
    }

    protected Double getDistanceFromTextInputLayout(TextInputLayout layout, boolean allowZero) {
        Resources res = getResources();
        Double value = getDoubleFromTextInputLayout(layout);

        String error = null;
        if (value == null) {
            error = res.getString(R.string.error_value_required);
        } else if (value == 0 && !allowZero) {
            error = res.getString(R.string.error_distance_zero);
        } else if (value < 0) {
            error = res.getString(R.string.error_distance_negative);
        }

        if (error == null) {
            layout.setErrorEnabled(false);
            return value;
        } else {
            layout.setError(error);
            layout.setErrorEnabled(true);
            return null;
        }
    }

    protected Double getDistanceFromTextInputLayout(TextInputLayout layout) {
        return getDistanceFromTextInputLayout(layout, false);
    }

    protected Double getFrequencyFromTextInputLayout(TextInputLayout layout) {
        Resources res = getResources();
        Double value = getDoubleFromTextInputLayout(layout);

        String error = null;
        if (value == null) {
            error = res.getString(R.string.error_value_required);
        } else if (value == 0) {
            error = res.getString(R.string.error_frequency_zero);
        } else if (value < 0)
            error = res.getString(R.string.error_frequency_negative);
        {
        }

        if (error == null) {
            layout.setErrorEnabled(false);
            return value;
        } else {
            layout.setError(error);
            layout.setErrorEnabled(true);
            return null;
        }
    }

    protected Double getPowerFromTextInputLayout(TextInputLayout layout) {
        Resources res = getResources();
        Double value = getDoubleFromTextInputLayout(layout);

        String error = null;
        if (value == null) {
            error = res.getString(R.string.error_value_required);
        } else if (value == 0) {
            error = res.getString(R.string.error_power_zero);
        } else if (value < 0) {
            error = res.getString(R.string.error_power_negative);
        }

        if (error == null) {
            layout.setErrorEnabled(false);
            return value;
        } else {
            layout.setError(error);
            layout.setErrorEnabled(true);
            return null;
        }
    }

    protected Double getGainFromTextInputLayout(TextInputLayout layout, boolean allowZero) {
        Resources res = getResources();
        Double value = getDoubleFromTextInputLayout(layout);

        String error = null;
        if (value == null) {
            error = res.getString(R.string.error_value_required);
        } else if (value == 0 && !allowZero) {
            error = res.getString(R.string.error_gain_zero);
        } else if (value < 0) {
            error = res.getString(R.string.error_gain_negative);
        }

        if (error == null) {
            layout.setErrorEnabled(false);
            return value;
        } else {
            layout.setError(error);
            layout.setErrorEnabled(true);
            return null;
        }
    }

    protected Double getGainFromTextInputLayout(TextInputLayout layout) {
        return getGainFromTextInputLayout(layout, false);
    }

    protected Double getPositiveDecibelFromTextInputLayout(TextInputLayout layout, boolean allowZero) {
        Resources res = getResources();
        Double value = getDoubleFromTextInputLayout(layout);

        String error = null;
        if (value == null) {
            error = res.getString(R.string.error_value_required);
        } else if (value == 0 && !allowZero) {
            error = res.getString(R.string.error_positive_decibel_zero);
        } else if (value < 0) {
            error = res.getString(R.string.error_positive_decibel_negative);
        }

        if (error == null) {
            layout.setErrorEnabled(false);
            return value;
        } else {
            layout.setError(error);
            layout.setErrorEnabled(true);
            return null;
        }
    }

    protected Double getPositiveDecibelFromTextInputLayout(TextInputLayout layout) {
        return getPositiveDecibelFromTextInputLayout(layout, false);
    }

    protected Integer getLevelsFromTextInputLayout(TextInputLayout layout) {
        Resources res = getResources();
        Integer value = getIntegerFromTextInputLayout(layout);

        String error = null;
        if (value == null) {
            error = res.getString(R.string.error_value_required);
        } else if (value == 0) {
            error = res.getString(R.string.error_levels_zero);
        } else if (value < 0) {
            error = res.getString(R.string.error_levels_negative);
        }

        if (error == null) {
            layout.setErrorEnabled(false);
            return value;
        } else {
            layout.setError(error);
            layout.setErrorEnabled(true);
            return null;
        }
    }
}
