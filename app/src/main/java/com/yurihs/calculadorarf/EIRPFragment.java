package com.yurihs.calculadorarf;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static java.lang.Double.valueOf;


public class EIRPFragment extends CalculationFragment implements View.OnClickListener {

    private View rootView;
    private Double currentResult;

    public EIRPFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Remember to clean up in onDestroyView()
        rootView = inflater.inflate(R.layout.fragment_eirp, container, false);

        setResult(0);

        rootView.findViewById(R.id.calculate_button).setOnClickListener(this);
        rootView.findViewById(R.id.send_to_budget_button).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    private void setResult(double i) {
        Resources res = getResources();
        String eirpString = res.getString(R.string.result_eirp, i);
        ((TextView) rootView.findViewById(R.id.result_eirp)).setText(eirpString);
        currentResult = valueOf(i);
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

    private void sendToBudget() {
        OnSendListener listener = (OnSendListener) getActivity();
        if (listener == null) {
            return;
        }

        listener.onSendTransmitterEIRPToBudget(currentResult);
    }

    private void calculate() {
        TextInputLayout transmitter_power_layout = rootView.findViewById(R.id.transmitter_power_value);
        Double transmitter_power_value = getPowerFromTextInputLayout(transmitter_power_layout);
        int transmitter_power_unit = getCheckedIdFromToggleGroup(rootView, R.id.transmitter_power_unit);

        TextInputLayout antenna_gain_layout = rootView.findViewById(R.id.antenna_gain_value);
        Double antenna_gain_value = getGainFromTextInputLayout(antenna_gain_layout, true);

        TextInputLayout cc_loss_layout = rootView.findViewById(R.id.cable_and_connector_loss_value);
        Double cc_loss_value = getPositiveDecibelFromTextInputLayout(cc_loss_layout, true);

        if (transmitter_power_value == null || antenna_gain_value == null || cc_loss_value == null) {
            return;
        }

        if (transmitter_power_unit == R.id.power_unit_milliwatt) {
            transmitter_power_value = 10 * Math.log10(transmitter_power_value);
        }

        double eirp = transmitter_power_value + antenna_gain_value - cc_loss_value;
        setResult(eirp);

        rootView.findViewById(R.id.send_to_budget_button).setEnabled(true);
    }
}
