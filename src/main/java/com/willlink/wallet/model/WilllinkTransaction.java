package com.willlink.wallet.model;


import java.math.BigInteger;

public class WilllinkTransaction implements java.io.Serializable {

    private int txVersion;

    private long txNounce;

    private String recvAddr;

    private BigInteger price;

    private BigInteger amount;

    private long timeStamp;

    private String contractAddress;

    private String payload;

    private String signData;

    private String txHash;

    public int getTxVersion() {
        return txVersion;
    }

    public void setTxVersion(int txVersion) {
        this.txVersion = txVersion;
    }

    public long getTxNounce() {
        return txNounce;
    }

    public void setTxNounce(long txNounce) {
        this.txNounce = txNounce;
    }

    public String getRecvAddr() {
        return recvAddr;
    }

    public void setRecvAddr(String recvAddr) {
        this.recvAddr = recvAddr;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
