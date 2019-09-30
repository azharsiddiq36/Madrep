package com.azhar.madrep.Rest;

public class CombineApi {
    public static final String BASE_URL = "http://produk.digtive.id/madrep/api/";
    public static final String img_url = "http://produk.digtive.id/madrep/";
//    public static final String BASE_URL = "http://192.168.43.201/ci/madrep/api/";
//    public static final String img_url = "http://192.168.43.201/ci/madrep/";
    public static MadrepInterface getApiService(){
        return ApiClient.getApiClient(BASE_URL).create(MadrepInterface.class);
    }
}
