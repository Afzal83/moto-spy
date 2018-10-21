package com.geon.lbs.network.apputil;


import com.geon.lbs.network.remote.RetroService;
import com.geon.lbs.network.remote.RetrofitClient;

/**
 * Created by Babu on 1/5/2018.
 */

public class ApiUtils {

    private static final String BASE_URL = "http://www.geon-bd.com/vts/index.php/";
    public static RetroService getRetrofitService() {
        return RetrofitClient.getClient(BASE_URL).create(RetroService.class);
    }
}
