package com.willlink.wallet.model;

public class ContractResponse implements java.io.Serializable {

    private long amount;

    private int status;

    private String orderFlowNum;

    private String txHash;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderFlowNum() {
        return orderFlowNum;
    }

    public void setOrderFlowNum(String orderFlowNum) {
        this.orderFlowNum = orderFlowNum;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
