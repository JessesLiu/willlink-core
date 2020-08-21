package com.willlink.wallet.model;


public class WilllinkResponse implements java.io.Serializable {

    /**
     * 处理结果 1: 成功
     */
    private int status;

    /**
     * 区块链处理后的txHash
     */
    private String txHash;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
