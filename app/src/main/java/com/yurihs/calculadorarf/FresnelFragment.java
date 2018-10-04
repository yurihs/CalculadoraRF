package com.yurihs.calculadorarf;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FresnelFragment extends CalculationFragment implements View.OnClickListener {

    private View rootView;

    public FresnelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Remember to clean up in onDestroyView()
        rootView = inflater.inflate(R.layout.fragment_fresnel, container, false);

        rootView.findViewById(R.id.calculate_button).setOnClickListener(this);

        setResult(0);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    private void setResult(double i) {
        Resources res = getResources();
        String fresnelString = res.getString(R.string.result_fresnel, i);
        ((TextView) rootView.findViewById(R.id.result_fresnel)).setText(fresnelString);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.calculate_button) {
            return;
        }

        TextInputLayout distance_a_b_layout = rootView.findViewById(R.id.distance_a_b_value);
        Double distance_a_b_value = getDistanceFromTextInputLayout(distance_a_b_layout);
        int distance_a_b_unit = getCheckedIdFromToggleGroup(rootView, R.id.distance_a_b_unit);

        TextInputLayout distance_a_o_layout = rootView.findViewById(R.id.distance_a_o_value);

        Double distance_a_o_value = getDistanceFromTextInputLayout(distance_a_o_layout, true);
        int distance_a_o_unit = getCheckedIdFromToggleGroup(rootView, R.id.distance_a_o_unit);

        TextInputLayout frequency_layout = rootView.findViewById(R.id.frequency_value);
        Double frequency_value = getFrequencyFromTextInputLayout(frequency_layout);
        int frequency_unit = getCheckedIdFromToggleGroup(rootView, R.id.frequency_unit);

        if (distance_a_b_value == null || distance_a_o_value == null || frequency_value == null) {
            return;
        }

        Resources res = getResources();

        if (distance_a_o_value > distance_a_b_value) {
            distance_a_o_layout.setError(res.getString(R.string.error_obstacle_distance_greater_than_total));
            distance_a_o_layout.setErrorEnabled(true);
            return;
        } else {
            distance_a_o_layout.setErrorEnabled(false);
        }

        if (distance_a_b_unit == R.id.distance_a_b_unit_meter) {
            distance_a_b_value /= 1000;
        }
        if (distance_a_o_unit == R.id.distance_a_o_unit_meter) {
            distance_a_o_value /= 1000;
        }
        if (frequency_unit == R.id.frequency_unit_gigahertz) {
            frequency_value *= 1000;
        }

        Double distance_b_o_value = distance_a_b_value - distance_a_o_value;

        double fresnel = 550 * Math.sqrt((distance_a_o_value * distance_b_o_value) / (distance_a_b_value * frequency_value));
        setResult(fresnel);
    }
}
