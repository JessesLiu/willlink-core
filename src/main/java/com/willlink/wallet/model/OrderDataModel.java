package com.willlink.wallet.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDataModel implements Serializable {

    private String orderNo;

    private String merchantNo;

    private BigDecimal totalAmount;

    private String userId;

    private String userName;

    private String customerName;

    private String customerCode;

    private String createdTime;

    private Integer status;

    private Integer orderType;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public OrderDataModel() {

    }

    public OrderDataModel(String orderNo, String merchantNo, BigDecimal totalAmount, String userId, String userName, String customerName, String customerCode, String createdTime, Integer status, Integer orderType) {
        this.orderNo = orderNo;
        this.merchantNo = merchantNo;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.userName = userName;
        this.customerName = customerName;
        this.customerCode = customerCode;
        this.createdTime = createdTime;
        this.status = status;
        this.orderType = orderType;
    }
}
