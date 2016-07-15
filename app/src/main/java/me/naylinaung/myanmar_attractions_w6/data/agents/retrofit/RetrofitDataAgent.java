package me.naylinaung.myanmar_attractions_w6.data.agents.retrofit;

import java.util.concurrent.TimeUnit;

import me.naylinaung.myanmar_attractions_w6.data.agents.UserDataAgent;
import me.naylinaung.myanmar_attractions_w6.data.models.UserModel;
import me.naylinaung.myanmar_attractions_w6.data.responses.UserInfoResponse;
import me.naylinaung.myanmar_attractions_w6.utils.CommonInstances;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class RetrofitDataAgent implements UserDataAgent {

    private static RetrofitDataAgent objInstance;

    private final UserApi theApi;

    private RetrofitDataAgent() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyanmarAttractionsConstants.ATTRACTION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(CommonInstances.getGsonInstance()))
                .client(okHttpClient)
                .build();

        // theApi already include all the implementation for AttractionApi
        theApi = retrofit.create(UserApi.class);
    }

    public static RetrofitDataAgent getInstance() {
        if (objInstance == null) {
            objInstance = new RetrofitDataAgent();
        }
        return objInstance;
    }

    @Override
    public void postLogin(String email, String password) {
        Call<UserInfoResponse> postLoginCall = theApi.postLogin(email, password);
        postLoginCall.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                UserInfoResponse userInfoResponse = response.body();
                if (userInfoResponse == null) {
                    UserModel.getInstance().notifyErrorInUserLogin(response.message());
                } else {
                    switch (userInfoResponse.getCode()) {
                        case 401:
                            UserModel.getInstance().notifyErrorInUserLogin(userInfoResponse.getMessage());
                            break;
                        case 200:
                            UserModel.getInstance().notifyUserLoginPosted(userInfoResponse.getUser());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                UserModel.getInstance().notifyErrorInUserLogin(t.getMessage());
            }
        });
    }

    @Override
    public void postRegister(String name, String email, String password, String date_of_birth, String country_of_origin) {
        Call<UserInfoResponse> postRegisterCall = theApi.postRegister(name, email, password, date_of_birth, country_of_origin);
        postRegisterCall.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                UserInfoResponse userInfoResponse = response.body();
                if (userInfoResponse == null) {
                    UserModel.getInstance().notifyErrorInUserRegister(response.message());
                } else {
                    switch (userInfoResponse.getCode()) {
                        case 401:
                            UserModel.getInstance().notifyErrorInUserRegister(userInfoResponse.getMessage());
                            break;
                        case 200:
                            UserModel.getInstance().notifyUserRegistered(userInfoResponse.getUser());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                UserModel.getInstance().notifyErrorInUserRegister(t.getMessage());
            }
        });
    }
}
