package com.yurihs.calculadorarf;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class BudgetFragment extends CalculationFragment implements View.OnClickListener {
    private static final String ARG_TRANSMITTER_EIRP = "transmitter_eirp";
    private static final String ARG_FSL = "transmitter_fsl";

    private View rootView;

    public BudgetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance(Double transmitter_eirp, Double fsl) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        if (transmitter_eirp != null) {
            args.putDouble(ARG_TRANSMITTER_EIRP, transmitter_eirp);
        }
        if (fsl != null) {
            args.putDouble(ARG_FSL, fsl);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Remember to clean up in onDestroyView()
        rootView = inflater.inflate(R.layout.fragment_budget, container, false);

        if (getArguments() != null) {
            TextInputLayout transmitter_eirp_layout = rootView.findViewById(R.id.transmitter_eirp_value);
            Double transmitter_eirp_value = getArguments().getDouble(ARG_TRANSMITTER_EIRP);
            EditText transmitter_eirp_edittext = transmitter_eirp_layout.getEditText();
            if (transmitter_eirp_edittext != null && transmitter_eirp_value != 0) {
                transmitter_eirp_edittext.setText(String.format(Locale.getDefault(), "%1$.2f", transmitter_eirp_value));
            }

            TextInputLayout fsl_layout = rootView.findViewById(R.id.fsl_value);
            Double fsl_value = getArguments().getDouble(ARG_FSL);
            EditText fsl_edittext = fsl_layout.getEditText();
            if (fsl_edittext != null && fsl_value != 0) {
                fsl_edittext.setText(String.format(Locale.getDefault(), "%1$.2f", fsl_value));
            }
        }

        rootView.findViewById(R.id.calculate_button).setOnClickListener(this);

        setResult(0);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
        if (getArguments() != null) {
            getArguments().clear();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.calculate_button) {
            calculate();
        }
    }

    private void calculate() {
        TextInputLayout transmitter_eirp_layout = rootView.findViewById(R.id.transmitter_eirp_value);
        Double transmitter_eirp_value = getPowerFromTextInputLayout(transmitter_eirp_layout);

        TextInputLayout fsl_layout = rootView.findViewById(R.id.fsl_value);
        Double fsl_value = getPositiveDecibelFromTextInputLayout(fsl_layout, true);

        TextInputLayout antenna_gain_layout = rootView.findViewById(R.id.antenna_gain_value);
        Double antenna_gain_value = getGainFromTextInputLayout(antenna_gain_layout, true);

        TextInputLayout cc_loss_layout = rootView.findViewById(R.id.cable_and_connector_loss_value);
        Double cc_loss_value = getPositiveDecibelFromTextInputLayout(cc_loss_layout, true);

        if (transmitter_eirp_value == null || fsl_value == null || antenna_gain_value == null || cc_loss_value == null) {
            return;
        }

        double budget = transmitter_eirp_value - fsl_value + antenna_gain_value - cc_loss_value;
        setResult(budget);
    }

    private void setResult(double i) {
        Resources res = getResources();
        String budgetString = res.getString(R.string.result_budget, i);
        ((TextView) rootView.findViewById(R.id.result_budget)).setText(budgetString);
    }
}
