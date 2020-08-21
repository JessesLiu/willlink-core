package com.willlink.wallet.model;


public class MiningContractResponse implements java.io.Serializable {

    /**
     * 处理结果 1: 成功
     */
    private int status;

    private String orderFlowNum;

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
}
