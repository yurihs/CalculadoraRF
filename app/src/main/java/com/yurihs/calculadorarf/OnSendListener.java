package com.yurihs.calculadorarf;

interface OnSendListener {
    void onSendTransmitterEIRPToBudget(Double transmitter_eirp);

    void onSendFSLToBudget(Double fsl);
}
