package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UPCBarcodeAPIInterface {
    @Headers({"Accept: application/json", "Authorization: Bearer C1DDC7B6471E6A8D4E70B9C84F806EB1"})
    @GET("/product/{barcode}?apikey=C1DDC7B6471E6A8D4E70B9C84F806EB1")
    Call<Product> getProductInfoWithBarcode(@Path("barcode") String barcode);
}