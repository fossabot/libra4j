package com.chaingrok.libra4j.misc;

import java.io.IOException;

import com.chaingrok.lib.ChaingrokError;
import com.chaingrok.lib.ChaingrokLog;
import com.chaingrok.lib.Utils;
import com.chaingrok.libra4j.types.AccountAddress;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Minter {
	
	public static final int HTTP_OK = 200;
	
	private final Builder builder = new HttpUrl.Builder()
		    .scheme("http")
		    .host("faucet.testnet.libra.org");
	private final OkHttpClient okHttpClient = new OkHttpClient();
	
	private int httpCode = -1;
	
	public Minter mint(AccountAddress accountAddress,long microLibras) {
		httpCode = -1;
		HttpUrl url = builder
					.addQueryParameter("address",Utils.byteArrayToHexString(accountAddress.getBytes()))
					.addQueryParameter("amount",microLibras + "")
					.build();
		//System.out.println("mint url: " + url.toString());
		RequestBody formBody = new FormBody.Builder()
			      .build();
        Request request = new Request.Builder()
            .url(url)
            .post(formBody)
            .build();
        Response response = null;
        try {
			response = okHttpClient.newCall(request).execute();
		} catch (IOException e) {
			new ChaingrokError(ChaingrokLog.Type.HTTP_ERROR,e);
		}
        if (response != null) {
        	httpCode = response.code();
        	if (httpCode != HTTP_OK) {
        		new ChaingrokError(ChaingrokLog.Type.HTTP_ERROR,"http response code: " + httpCode);
        	}
        }
        return this;
	}
	
	public int getHttpCode() {
		return httpCode;
	}

}
