package me.naylinaung.myanmar_attractions_w6.data.responses;

import com.google.gson.annotations.SerializedName;

import me.naylinaung.myanmar_attractions_w6.data.vos.UserVO;

/**
 * Created by NayLinAung on 7/15/2016.
 */
public class UserInfoResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("login_user")
    private UserVO user;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public UserVO getUser() {
        return user;
    }
}
