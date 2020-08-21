package com.willlink.wallet.model;

import java.io.Serializable;


public class MiningContractRequest implements Serializable {

    private String orderFlowNum;

    private String txHash;

    private String recvAddress;

    private long orderAmount;

    private long timeStamp = System.currentTimeMillis()/1000;

    private float allocRate;

    private byte payload = 127;

    private int minerType = 0;

    private String tokenName;

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

    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress;
    }

    public long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getAllocRate() {
        return allocRate;
    }

    public void setAllocRate(float allocRate) {
        this.allocRate = allocRate;
    }

    public byte getPayload() {
        return payload;
    }

    public void setPayload(byte payload) {
        this.payload = payload;
    }

    public int getMinerType() {
        return minerType;
    }

    public void setMinerType(int minerType) {
        this.minerType = minerType;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }


    public MiningContractRequest() {

    }

    public MiningContractRequest(String orderFlowNum, String txHash, String recvAddress, long orderAmount, float allocRate, String tokenName) {
        this.orderFlowNum = orderFlowNum;
        this.txHash = txHash;
        this.recvAddress = recvAddress;
        this.orderAmount = orderAmount;
        this.allocRate = allocRate;
        this.tokenName = tokenName;
    }
}
