//package com.pillcare.pillcare_server.network;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitClient {
//    private static final Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://http://158.179.162.60:8080/api/")  // 실제 서버 IP로 교체 필요
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//
//    public static final ApiServices apiService = retrofit.create(ApiServices.class);
//}
