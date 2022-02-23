package org.zlwl.ethereum.api;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ERC20TokenTxDTO {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<ERC20TokenTxData> mERC20TokenTxData;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<ERC20TokenTxData> getResult() {
        return mERC20TokenTxData;
    }

    public void setResult(List<ERC20TokenTxData> ERC20TokenTxData) {
        mERC20TokenTxData = ERC20TokenTxData;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
