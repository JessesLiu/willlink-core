package com.willlink.wallet.model;

import java.io.Serializable;

public class TokenMsgModel implements Serializable {

    private int msgType;

    private String tokenName;

    private int tokenId;

    private long amount;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public TokenMsgModel() {
    }

    public TokenMsgModel(int msgType, String tokenName, int tokenId, long amount) {
        this.msgType = msgType;
        this.tokenName = tokenName;
        this.tokenId = tokenId;
        this.amount = amount;
    }
}
