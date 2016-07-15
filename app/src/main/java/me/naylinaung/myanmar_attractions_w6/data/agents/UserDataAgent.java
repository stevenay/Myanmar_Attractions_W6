package me.naylinaung.myanmar_attractions_w6.data.agents;

/**
 * Created by NayLinAung on 7/15/2016.
 */
public interface UserDataAgent {
    void postLogin(String email, String password);
    void postRegister(String name, String email, String password, String date_of_birth, String country_of_origin);
}
