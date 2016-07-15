package me.naylinaung.myanmar_attractions_w6.data.agents.retrofit;

import me.naylinaung.myanmar_attractions_w6.data.responses.UserInfoResponse;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by NayLinAung on 7/14/2016.
 */
public interface UserApi {

    @FormUrlEncoded
    @POST(MyanmarAttractionsConstants.API_LOGIN)
    Call<UserInfoResponse> postLogin(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST(MyanmarAttractionsConstants.API_REGISTER)
    Call<UserInfoResponse> postRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("date_of_birth") String dateOfBirth,
            @Field("country_of_origin") String countryOfOrigin);
}
