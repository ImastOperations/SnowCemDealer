/**/
package in.imast.snowcemdealer.Connection;


import com.google.gson.JsonObject;
import in.imast.snowcemdealer.model.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by Vishwnath 01_March_2021
 */

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login-user")
    Call<JsonObject> loginvalidation(@FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("login-user-verify")
    Call<LoginResponse> retailerLogin(@FieldMap Map<String, String> queryParams);

    @GET("v1/customers/{id}")
    Call<JsonObject> getCustomer(@Header("Authorization") String authorization, @Path("id") String id);

    @FormUrlEncoded
    @POST("v1/coupon-entries")
    Call<JsonObject> schemeredeemed1(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("v1/user-logout")
    Call<JsonObject> logout(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("v1/get-app-version")
    Call<JsonObject> checkVersion(@FieldMap Map<String, String> queryParams);

    @GET("v1/customer-list")
    Call<JsonObject> getCustomerScan(@Header("Authorization") String authorization);


    @FormUrlEncoded
    @POST("v1/customer-coupon-store")
    Call<JsonObject> CoupnCodeStore(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);


}
