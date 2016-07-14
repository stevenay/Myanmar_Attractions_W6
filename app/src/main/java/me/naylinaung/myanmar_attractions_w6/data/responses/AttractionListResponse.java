package me.naylinaung.myanmar_attractions_w6.data.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class AttractionListResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("attraction")
    private ArrayList<AttractionVO> attractionList;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<AttractionVO> getAttractionList() {
        return attractionList;
    }

}
