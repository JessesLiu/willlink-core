package com.willlink.wallet.general;

import com.alibaba.fastjson.JSONObject;
import com.willlink.wallet.jni.GoFunctionManager;
import com.willlink.wallet.jni.GoString;
import com.willlink.wallet.model.*;
import com.willlink.wallet.utils.HttpUtils;
import com.willlink.wallet.utils.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class WillGitTransaction {

    private int txVersion = 0x00010001;
    private long timeStamp = System.currentTimeMillis()/1000;
    private String requestHost;
    private String headerKey;

    /**
     * 构建订单上链交易对象
     * @param txNounce 唯一标识
     * @param recvAddress 接收地址 格式: hexString
     * @param privateKey 发起人私钥 格式: hexString
     * @param orderData 订单上链数据 格式: hexString
     * @return
     */
    public WilllinkTransaction buildOrderDataToChain(long txNounce, String recvAddress, String privateKey, String orderData){
        if (StringUtils.isBlank(recvAddress)){
            throw new RuntimeException("recvAddress is null");
        }
        if (StringUtils.isBlank(privateKey)){
            throw new RuntimeException("privateKey is null");
        }
        if (StringUtils.isBlank(orderData)){
            throw new RuntimeException("orderData is null");
        }
        WilllinkTransaction willlinkTransaction = new WilllinkTransaction();
        willlinkTransaction.setTxVersion(txVersion);
        willlinkTransaction.setTxNounce(txNounce);
        willlinkTransaction.setRecvAddr(recvAddress);
        willlinkTransaction.setPrice(BigInteger.ZERO);
        willlinkTransaction.setAmount(BigInteger.ZERO);
        willlinkTransaction.setTimeStamp(timeStamp);
        willlinkTransaction.setPayload(orderData);

        String hexData = this.serialTx(
                willlinkTransaction.getTxVersion(),
                Long.valueOf(willlinkTransaction.getTxNounce()).intValue(),
                willlinkTransaction.getRecvAddr(),
                willlinkTransaction.getPrice().longValue(),
                willlinkTransaction.getAmount().longValue(),
                willlinkTransaction.getTimeStamp(),
                willlinkTransaction.getPayload());

        String signData = this.signData(hexData, privateKey);

        String txHash = this.kecak256(hexData + signData);

        willlinkTransaction.setSignData(signData);
        willlinkTransaction.setTxHash(txHash);
        return willlinkTransaction;
    }

    /**
     * 构建交易对象
     * @param txNounce 唯一标识
     * @param recvAddress 接收地址 格式: hexString
     * @param privateKey 发起人私钥 格式: hexString
     * @param contractAddress 交易合约地址 格式: hexString
     * @param msgToken 交易对象 格式: hexString
     * @return
     */
    public WilllinkTransaction buildTransaction(long txNounce, String recvAddress, String privateKey, String contractAddress, String msgToken){
        if (StringUtils.isBlank(recvAddress)){
            throw new RuntimeException("recvAddress is null");
        }
        if (StringUtils.isBlank(privateKey)){
            throw new RuntimeException("privateKey is null");
        }
        if (StringUtils.isBlank(msgToken)){
            throw new RuntimeException("msgData is null");
        }
        WilllinkTransaction willlinkTransaction = new WilllinkTransaction();
        willlinkTransaction.setTxVersion(txVersion);
        willlinkTransaction.setTxNounce(txNounce);
        willlinkTransaction.setRecvAddr(recvAddress);
        willlinkTransaction.setPrice(BigInteger.valueOf(0xFFFFFFFFFFFFFFFFL));
        willlinkTransaction.setAmount(BigInteger.ZERO);
        willlinkTransaction.setTimeStamp(timeStamp);
        willlinkTransaction.setPayload(msgToken);
        if (StringUtils.isNotBlank(contractAddress)){
            willlinkTransaction.setContractAddress(contractAddress);
        }

        String hexData = this.serialTx(
                willlinkTransaction.getTxVersion(),
                Long.valueOf(willlinkTransaction.getTxNounce()).intValue(),
                willlinkTransaction.getRecvAddr(),
                willlinkTransaction.getPrice().longValue(),
                willlinkTransaction.getAmount().longValue(),
                willlinkTransaction.getTimeStamp(),
                willlinkTransaction.getPayload());

        String signData = this.signData(hexData, privateKey);

        String txHash = this.kecak256(hexData + signData);

        willlinkTransaction.setSignData(signData);
        willlinkTransaction.setTxHash(txHash);
        return willlinkTransaction;
    }


    /**
     * 订单上链
     * @param txNounce 唯一标识
     * @param recvAddress 接收地址 格式: hexString
     * @param privateKey 发起人私钥 格式: hexString
     * @param orderData 订单上链数据 格式: hexString
     * @return
     */
    public WilllinkResponse sendOrderDataToChain(long txNounce, String recvAddress, String privateKey, String orderData){
        WilllinkTransaction willlinkTransaction =
                buildOrderDataToChain(txNounce, recvAddress, privateKey, orderData);
        return transaction(willlinkTransaction);
    }

    /**
     * 发起交易
     * @param txNounce 唯一标识
     * @param recvAddress 接收地址 格式: hexString
     * @param privateKey 发起人私钥 格式: hexString
     * @param contractAddress 交易合约地址 格式: hexString
     * @param msgToken 交易对象 格式: hexString
     * @return
     */
    public WilllinkResponse sendTransaction(long txNounce, String recvAddress, String privateKey, String contractAddress, String msgToken){
        WilllinkTransaction willlinkTransaction =
                buildTransaction(txNounce, recvAddress, privateKey, contractAddress, msgToken);
        return transaction(willlinkTransaction);
    }


    /**
     * 发起挖矿请求
     * @return
     */
    public MiningContractResponse sendMiningContract(MiningContractRequest miningContractRequest){
        checkTransaction();

        final String apiPath = "/v2/mining/send";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);
        headers.put("Content-Type","application/json");

        String requestBody = JSONObject.toJSONString(miningContractRequest);

        MiningContractResponse miningContractResponse = new MiningContractResponse();
        miningContractResponse.setStatus(-1);
        try {
            HttpResponse response = HttpUtils.doPost(requestHost, apiPath, "POST", headers, null, requestBody);
            JSONObject res = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            if(res.getIntValue("code") == 1 && res.containsKey("res_data")){
                JSONObject resData = res.getJSONObject("res_data");
                if (resData != null){
                    miningContractResponse.setStatus(resData.getIntValue("status"));
                    miningContractResponse.setOrderFlowNum(resData.getString("orderFlowNum"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            miningContractResponse.setStatus(-2);
        }
        return miningContractResponse;
    }


    /**
     * 查询资产余额
     * @param tokenId Token标识Id
     * @param address 账户地址 格式: hexString
     * @param contractAddress 合约地址 格式: hexString
     * @return
     */
    public long getBalance(int tokenId, String address, String contractAddress){
        checkTransaction();

        if (StringUtils.isBlank(address)){
            throw new RuntimeException("address is null");
        }

        String apiPath = "/v2/query/balance";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);

        try {
            Map<String, String> querys = new HashMap<>();
            querys.put("address",address);
            querys.put("tokenId", tokenId + "");
            querys.put("contractAddress",  contractAddress);
            HttpResponse response = HttpUtils.doGet(requestHost, apiPath, "GET", headers, querys);
            JSONObject res = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            if(res.getIntValue("code") == 1 && res.containsKey("res_data")){
                return res.getLongValue("res_data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查询交易状态结果
     * @param txHash 交易Hash 格式: hexString
     * @return
     */
    public int getTransactionStatus(String txHash){
        checkTransaction();

        if (StringUtils.isBlank(txHash)){
            throw new RuntimeException("txHash is null");
        }

        String apiPath = "/v2/query/status/transaction/"+txHash;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);

        try {
            HttpResponse response = HttpUtils.doGet(requestHost, apiPath, "GET", headers, null);
            JSONObject res = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            if(res.getIntValue("code") == 1 && res.containsKey("res_data")){
                JSONObject resData = res.getJSONObject("res_data");
                return resData.getIntValue("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 查询挖矿合约交易结果
     * @param orderFlowNum 交易 格式: hexString
     * @param tokenName 资产币种名称
     *                  WID
     *                  CAC
     *                  BCT
     * @return
     */
    public ContractResponse getContractResult(String orderFlowNum, String tokenName){
        checkTransaction();

        if (StringUtils.isBlank(orderFlowNum)){
            throw new RuntimeException("orderFlowNum is null");
        }
        if (StringUtils.isBlank(tokenName)){
            throw new RuntimeException("tokenName is null");
        }

        ContractResponse contractResponse = new ContractResponse();

        String apiPath = "/v2/query/contract/result";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);

        try {
            Map<String, String> querys = new HashMap<>();
            querys.put("orderFlowNum", orderFlowNum);
            querys.put("tokenName", tokenName);
            HttpResponse response = HttpUtils.doGet(requestHost, apiPath, "GET", headers, querys);
            JSONObject res = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            if(res.getIntValue("code") == 1 && res.containsKey("res_data")){
                contractResponse = res.getJSONObject("res_data").toJavaObject(ContractResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contractResponse;
    }



    /**
     * 序列化转账交易的payload
     * @return
     */
    public String serializeMsgToken(TokenMsgModel tokenMsgModel){
        checkTransaction();

        if (tokenMsgModel == null){
            throw new RuntimeException("tokenMsgModel is null");
        }

        final String apiPath = "/v2/serialize/msgToken";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);
        headers.put("Content-Type","application/json");

        String requestBody = JSONObject.toJSONString(tokenMsgModel);

        try {
            HttpResponse response =
                    HttpUtils.doPost(requestHost, apiPath, "POST", headers, null, requestBody);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 反序列化转账交易的payload
     * @return
     */
    public TokenMsgModel unSerializeMsgToken(String hexString){
        checkTransaction();
        if (StringUtils.isBlank(hexString)){
            throw new RuntimeException("hexString is null");
        }

        final String apiPath = "/v2/unSerialize/msgToken";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);
        headers.put("Content-Type","application/json");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("hexString", hexString);

            HttpResponse response =
                    HttpUtils.doPost(requestHost, apiPath, "POST", headers, null, jsonObject.toJSONString());
            String result = EntityUtils.toString(response.getEntity());
            if (StringUtils.isNotBlank(result)){
                return JSONObject.parseObject(result).toJavaObject(TokenMsgModel.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 序列化订单上链的payload
     * @return
     */
    public String serializeOrderData(OrderDataModel orderDataModel){
        checkTransaction();

        if (orderDataModel == null){
            throw new RuntimeException("orderDataModel is null");
        }

        final String apiPath = "/v2/serialize/orderData";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);
        headers.put("Content-Type","application/json");

        String requestBody = JSONObject.toJSONString(orderDataModel);

        try {
            HttpResponse response =
                    HttpUtils.doPost(requestHost, apiPath, "POST", headers, null, requestBody);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 反序列化订单上链的payload
     * @return
     */
    public OrderDataModel unSerializeOrderData(String hexString){
        checkTransaction();
        if (StringUtils.isBlank(hexString)){
            throw new RuntimeException("hexString is null");
        }

        final String apiPath = "/v2/unSerialize/orderData";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);
        headers.put("Content-Type","application/json");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("hexString", hexString);

            HttpResponse response =
                    HttpUtils.doPost(requestHost, apiPath, "POST", headers, null, jsonObject.toJSONString());
            String result = EntityUtils.toString(response.getEntity());
            if (StringUtils.isNotBlank(result)){
                return JSONObject.parseObject(result).toJavaObject(OrderDataModel.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private WilllinkResponse transaction(WilllinkTransaction willlinkTransaction){
        checkTransaction();

        final String apiPath = "/v2/transaction/send";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-chain-apikey", headerKey);
        headers.put("Content-Type","application/json");

        String requestBody = JSONObject.toJSONString(willlinkTransaction);

        WilllinkResponse willlinkResponse = new WilllinkResponse();
        willlinkResponse.setTxHash(willlinkTransaction.getTxHash());
        willlinkResponse.setStatus(-1);
        try {
            HttpResponse response = HttpUtils.doPost(requestHost, apiPath, "POST", headers, null, requestBody);
            JSONObject res = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            if(res.getIntValue("code") == 1 && res.containsKey("res_data")){
                JSONObject resData = res.getJSONObject("res_data");
                willlinkResponse.setStatus(resData.getIntValue("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            willlinkResponse.setStatus(-2);
        }
        return willlinkResponse;
    }

    public String serialTx(int txVersion, int txNounce, String recvAddr, long price, long amount, long timeStamp, String payload) {
        return GoFunctionManager.newInstance().SerialTx(txVersion, txNounce, new GoString.ByValue(recvAddr), price, amount, timeStamp, new GoString.ByValue(payload));
    }

    public String signData(String hexData, String privateKey){
        return GoFunctionManager.newInstance().SignForData(new GoString.ByValue(hexData), new GoString.ByValue(privateKey));
    }

    public String kecak256(String hexDataAndSignData) {
        return GoFunctionManager.newInstance().Kecak256(new GoString.ByValue(hexDataAndSignData));
    }

    public int getTxVersion() {
        return txVersion;
    }

    public void setTxVersion(int txVersion) {
        this.txVersion = txVersion;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRequestHost() {
        return requestHost;
    }

    public void setRequestHost(String requestHost) {
        this.requestHost = requestHost;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public WillGitTransaction() {
    }

    public WillGitTransaction(String requestHost, String headerKey) {
        this.requestHost = requestHost;
        this.headerKey = headerKey;
    }

    private void checkTransaction(){
        if (StringUtils.isBlank(requestHost)){
            throw new RuntimeException("requestHost is null");
        }
        if (StringUtils.isBlank(headerKey)){
            throw new RuntimeException("headerKey is null");
        }
    }


}
