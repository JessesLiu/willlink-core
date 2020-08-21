package com.willlink.wallet.jni;

import com.sun.jna.Library;

public interface GoFunction extends Library {

    String GetPublicAccount();

    String SignForData(GoString.ByValue data, GoString.ByValue privateKey);

    int VerifySignByPubKey(GoString.ByValue data, GoString.ByValue sign, GoString.ByValue publicKey);

    String SerialTx(int txVersion, int txNounce, GoString.ByValue recvAddr, long price, long amount, long timeStamp, GoString.ByValue payload);

    String SerialArbitrate(GoString.ByValue txHash, GoString.ByValue transactionNumber);

    String Kecak256(GoString.ByValue hexDataAndSignData);
}
