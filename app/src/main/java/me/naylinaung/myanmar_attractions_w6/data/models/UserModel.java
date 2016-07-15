package me.naylinaung.myanmar_attractions_w6.data.models;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.naylinaung.myanmar_attractions_w6.data.agents.UserDataAgent;
import me.naylinaung.myanmar_attractions_w6.data.agents.retrofit.RetrofitDataAgent;
import me.naylinaung.myanmar_attractions_w6.data.vos.UserVO;
import me.naylinaung.myanmar_attractions_w6.events.DataEvent;

/**
 * Created by NayLinAung on 7/15/2016.
 */
public class UserModel {

    public static final String BROADCAST_DATA_LOADED = "BROADCAST_DATA_LOADED";

    private static UserModel objInstance;

    private UserVO user;
    private UserDataAgent dataAgent;

    private UserModel() {
        initDataAgent();
    }

    public static UserModel getInstance() {
        if (objInstance == null) {
            objInstance = new UserModel();
        }
        return objInstance;
    }

    private void initDataAgent() {
        dataAgent = RetrofitDataAgent.getInstance();
    }

    //region Login Part
    public void notifyUserLoginPosted(UserVO userVO) {
        //Notify that the data is ready - using LocalBroadcast
        this.user = userVO;
        UserVO.saveUser(this.user);
    }

    public void notifyErrorInUserLogin(String message) {
        broadcastLoginErrorWithEventBus(message);
    }

    public void postLogin(String email, String password) {
        dataAgent.postLogin(email, password);
    }

    private void broadcastLoginErrorWithEventBus(String message) {
        EventBus.getDefault().post(new DataEvent.LoginErrorEvent(message));
    }
    //endregion

    //region Register Part
    public void notifyUserRegistered(UserVO userVO) {
        //Notify that the data is ready - using LocalBroadcast
        this.user = userVO;
        UserVO.saveUser(this.user);
    }

    public void notifyErrorInUserRegister(String message) {
        broadcastRegisterErrorWithEventBus(message);
    }

    public void postRegister(String name, String email, String password, String dateOfBirth, String countryOfOrigin) {
        dataAgent.postRegister(name, email, password, dateOfBirth, countryOfOrigin);
    }

    private void broadcastRegisterErrorWithEventBus(String message) {
        EventBus.getDefault().post(new DataEvent.RegisterErrorEvent(message));
    }
    //endregion




}
