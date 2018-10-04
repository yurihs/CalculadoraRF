package com.yurihs.calculadorarf;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FSLFragment extends CalculationFragment implements View.OnClickListener {

    private View rootView;
    private Double currentResult;

    public FSLFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Remember to clean up in onDestroyView()
        rootView = inflater.inflate(R.layout.fragment_fsl, container, false);

        rootView.findViewById(R.id.calculate_button).setOnClickListener(this);
        rootView.findViewById(R.id.send_to_budget_button).setOnClickListener(this);

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
        String fslString = res.getString(R.string.result_fsl, i);
        ((TextView) rootView.findViewById(R.id.result_fsl)).setText(fslString);
        currentResult = i;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.calculate_button) {
            calculate();
        } else if (viewId == R.id.send_to_budget_button) {
            sendToBudget();
        }
    }

    private void calculate() {
        TextInputLayout distance_layout = rootView.findViewById(R.id.distance_value);
        Double distance_value = getDistanceFromTextInputLayout(distance_layout);
        int distance_unit = getCheckedIdFromToggleGroup(rootView, R.id.distance_unit);

        TextInputLayout frequency_layout = rootView.findViewById(R.id.frequency_value);
        Double frequency_value = getFrequencyFromTextInputLayout(frequency_layout);
        int frequency_unit = getCheckedIdFromToggleGroup(rootView, R.id.frequency_unit);

        if (distance_value == null || frequency_value == null) {
            return;
        }

        if (distance_unit == R.id.distance_unit_meter) {
            distance_value /= 1000;
        }

        if (frequency_unit == R.id.frequency_unit_gigahertz) {
            distance_value *= 1000;
        }

        double fsl = 32.5 + 20 * Math.log10(distance_value) + 20 * Math.log10(frequency_value);
        setResult(fsl);

        rootView.findViewById(R.id.send_to_budget_button).setEnabled(true);
    }

    private void sendToBudget() {
        OnSendListener listener = (OnSendListener) getActivity();
        if (listener == null) {
            return;
        }

        listener.onSendFSLToBudget(currentResult);
    }

}
