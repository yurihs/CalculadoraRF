package com.yurihs.calculadorarf;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TxRateShannonFragment extends CalculationFragment implements View.OnClickListener {

    private View rootView;

    public TxRateShannonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Remember to clean up in onDestroyView()
        rootView = inflater.inflate(R.layout.fragment_tx_rate_shannon, container, false);

        setResult(0);

        rootView.findViewById(R.id.calculate_button).setOnClickListener(this);

        return rootView;
    }

    private void setResult(double tx_rate) {
        Resources res = getResources();
        String tx_rate_unit = "bps";

        if (tx_rate >= 1000000000) {
            tx_rate /= 1000000000;
            tx_rate_unit = "Gbps";
        } else if (tx_rate >= 1000000) {
            tx_rate /= 1000000;
            tx_rate_unit = "Mbps";
        } else if (tx_rate >= 1000) {
            tx_rate /= 1000;
            tx_rate_unit = "kbps";
        }

        String fresnelString = res.getString(R.string.result_tx_rate_shannon, tx_rate, tx_rate_unit);
        ((TextView) rootView.findViewById(R.id.result_tx_rate_shannon)).setText(fresnelString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.calculate_button) {
            calculate();
        }
    }

    private void calculate() {
        TextInputLayout bandwidth_layout = rootView.findViewById(R.id.bandwidth_value);
        Double bandwidth_value = getFrequencyFromTextInputLayout(bandwidth_layout);
        int bandwidth_unit = getCheckedIdFromToggleGroup(rootView, R.id.bandwidth_unit);

        TextInputLayout snr_layout = rootView.findViewById(R.id.snr_value);
        Double snr_value = getPositiveDecibelFromTextInputLayout(snr_layout);
        int snr_unit = getCheckedIdFromToggleGroup(rootView, R.id.snr_unit);

        if (bandwidth_value == null || snr_value == null) {
            return;
        }

        if (bandwidth_unit == R.id.frequency_unit_kilohertz) {
            bandwidth_value *= 1000;
        }

        if (snr_unit == R.id.snr_unit_decibel) {
            snr_value = Math.pow(10, snr_value / 10);
        }

        double tx_rate = bandwidth_value * log2(1 + snr_value);
        setResult(tx_rate);
    }

    private static double log2(double f) {
        return Math.log(f) / Math.log(2.0);
    }
}
