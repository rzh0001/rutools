package org.zlwl.ethereum.api;

import java.util.List;


import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ERC721TokenTxDTO {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<ERC721TokenTxData> mERC721TokenTxData;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<ERC721TokenTxData> getResult() {
        return mERC721TokenTxData;
    }

    public void setResult(List<ERC721TokenTxData> ERC721TokenTxData) {
        mERC721TokenTxData = ERC721TokenTxData;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
