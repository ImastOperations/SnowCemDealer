package in.imast.snowcemdealer.Connection;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import in.imast.snowcemdealer.model.LoginResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static String BASE_API_URL = "https://snowcem.imast.in/api/";
    public static String BASE_IMAGE_URL = "https://snowcem.imast.in/api/v1/";
    public static String WEB_BASE_URL = "https://snowcem.imast.in/api/v1/";

    static Context context;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static void configClient(final String header) {
        httpClient.connectTimeout(300, TimeUnit.MINUTES);
        httpClient.readTimeout(3000, TimeUnit.MINUTES);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("AccessToken", header)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });

    }

    public static void configClient1() {
        httpClient.connectTimeout(30, TimeUnit.MINUTES);
        httpClient.readTimeout(30, TimeUnit.MINUTES);

    }

    public static void addLoggingIfNeeded() {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(logging);
    }



    static Retrofit retrofit = null;

    public static BaseApiService getBaseApiServiceInstance1() {

        if (retrofit == null) {
            addLoggingIfNeeded();
            configClient1();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit.create(BaseApiService.class);
    }

    public static BaseApiService getBaseApiServiceInstance124() {
        if (retrofit == null) {
            addLoggingIfNeeded();
            configClient1();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit.create(BaseApiService.class);
    }


    public static void loginvalidation(Map<String, String> queryParams, final APIResultLitener<JsonObject> resultLitener, Context mContext) {
        context = mContext;
        Call<JsonObject> apiResponseCall = getBaseApiServiceInstance1().loginvalidation(queryParams);

        apiResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }

    public static void checkVersion(Map<String, String> queryParams, final APIResultLitener<JsonObject> resultLitener, Context mContext) {
        context = mContext;
        Call<JsonObject> apiResponseCall = getBaseApiServiceInstance1().checkVersion(queryParams);

        apiResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }


    public static void retailerLogin(Map<String, String> queryParams, final APIResultLitener<LoginResponse> resultLitener, Context mContext) {
        context = mContext;
        Call<LoginResponse> apiResponseCall = getBaseApiServiceInstance1().retailerLogin(queryParams);

        apiResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }

    public static void getCustomer(String header, String id, final APIResultLitener<JsonObject> resultLitener) {
        Call<JsonObject> apiResponseCall = getBaseApiServiceInstance1().getCustomer(header, id);

        apiResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }


    public static void getCustomerScan(String header, final APIResultLitener<JsonObject> resultLitener, FragmentActivity activity) {
        Call<JsonObject> apiResponseCall = getBaseApiServiceInstance1().getCustomerScan(header);
        apiResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }


    public static void schemeredeemed1
            (String header, Map<String, String> queryParams, final APIResultLitener<JsonObject> resultLitener, Context mContext) {
        context = mContext;
        Call<JsonObject> apiResponseCall = getBaseApiServiceInstance1().schemeredeemed1
                (header, queryParams);

        apiResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                Log.v("akram", "reposense " + response);
                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.v("akram", "reposense failure " + t.getMessage());
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }
    public static void logout
            (String header, Map<String, String> queryParams, final APIResultLitener<JsonObject> resultLitener, Context mContext) {
        context = mContext;
        Call<JsonObject> apiResponseCall = getBaseApiServiceInstance1().logout
                (header, queryParams);

        apiResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                Log.v("akram", "reposense " + response);
                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.v("akram", "reposense failure " + t.getMessage());
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }


    public static void couponCodeStore
            (String header, Map<String, String> queryParams, final APIResultLitener<JsonObject> resultLitener, Context mContext) {
        context = mContext;
        Call<JsonObject> apiResponseCall = getBaseApiServiceInstance1().CoupnCodeStore(header, queryParams);

        apiResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                Log.v("akram", "reposense " + response);
                resultLitener.onAPIResult(response, null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.v("akram", "reposense failure " + t.getMessage());
                resultLitener.onAPIResult(null, t.getMessage());
            }
        });
    }






}
