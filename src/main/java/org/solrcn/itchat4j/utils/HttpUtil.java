package org.solrcn.itchat4j.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Created by longkeyy on 16/11/10.
 */
public class HttpUtil {

    private static String USER_AGENT = "iPhone; iOS 10.0.0; Scale/2.00";
//    private static String WEBSOCKET_HEADER = "permessage-deflate;server_max_window_bits=11; client_max_window_bits=11;client_no_context_takeover;server_no_context_takeover";


    public static int connectTimeout = 15000;
    public static int readTimeout = 15000;
    public static int writeTimeout = 15000;

    /**
     * @return
     */
    public static OkHttpClient newClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .proxy(Proxy.NO_PROXY)
                .build();
    }


    /**
     *
     * @param proxyserver
     * @return
     */
    public static OkHttpClient newClient(String proxyserver) {
        OkHttpClient client = newClient();
        String[] split = proxyserver.split(":");
        if (split.length != 2) {
            return client;
        } else {
            String ip = split[0];
            try {
                int port = Integer.parseInt(split[1]);
                InetSocketAddress socketAddress = InetSocketAddress.createUnresolved(ip, port);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
                return client.newBuilder().proxy(proxy).build();
            } catch (Exception e) {
                e.printStackTrace();
                return client;
            }
        }
    }

    /**
     * @return
     */
    public static Headers getHeaders() {
        return new Headers.Builder()
                .set("User-Agent", USER_AGENT)
                .set("X-Forwarded-For", RadmonUtil.getIp())
                .set("Accept","*/*")
                .set("Accept-Language","zh-Hans-CN;q=1, en-CN;q=0.9, zh-Hant-CN;q=0.8")
                .build();
    }

    /**
     * @param url
     * @return
     */
    public static Request newRequest(final String url) {
        return new Request.Builder()
                .headers(getHeaders())
                .url(url)
                .build();
    }

    /**
     * @param request
     * @param data
     * @return
     */
    public static Request addRequestPostData(final Request request, final RequestBody data) {
        return request.newBuilder().post(data).build();
    }


    /**
     * @param request
     * @return
     * @throws IOException
     */
    public static String getBodyString(final OkHttpClient client, final Request request) throws IOException {
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    /**
     * @param request
     * @return
     * @throws IOException
     */
    public static JSONObject getBodyJsonObject(final OkHttpClient client, final Request request) throws IOException {
        String bodyString = getBodyString(client, request);
        return JSONObject.parseObject(bodyString);
    }

}
