package me.naylinaung.myanmar_attractions_w6.data.models;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.data.agents.AttractionDataAgent;
import me.naylinaung.myanmar_attractions_w6.data.agents.OkHttpDataAgent;
import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;
import me.naylinaung.myanmar_attractions_w6.events.DataEvent;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class AttractionModel {

    public static final String BROADCAST_DATA_LOADED = "BROADCAST_DATA_LOADED";

    private static final int INIT_DATA_AGENT_OFFLINE = 1;
    private static final int INIT_DATA_AGENT_HTTP_URL_CONNECTION = 2;
    private static final int INIT_DATA_AGENT_OK_HTTP = 3;
    private static final int INIT_DATA_AGENT_RETROFIT = 4;

    private static AttractionModel objInstance;

    private List<AttractionVO> mAttractionVOList;

    private AttractionDataAgent mDataAgent;

    private AttractionModel() {
        mAttractionVOList = new ArrayList<>();

        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);

        initDataAgent(INIT_DATA_AGENT_OK_HTTP);
        mDataAgent.loadAttractions();
    }

    public static AttractionModel getInstance()
    {
        if (objInstance == null) {
            objInstance = new AttractionModel();
        }

        return objInstance;
    }

    private void initDataAgent(int initType) {
        switch (initType) {
            case INIT_DATA_AGENT_OFFLINE:
                break;
            case INIT_DATA_AGENT_HTTP_URL_CONNECTION:
                break;
            case INIT_DATA_AGENT_OK_HTTP:
                mDataAgent = OkHttpDataAgent.getObjInstance();
                break;
            case INIT_DATA_AGENT_RETROFIT:
                break;
        }
    }

    public void onEventMainThread(DataEvent.AttractionDataLoadedEvent event) {
//        String extra = event.getExtraMessage();
//        Toast.makeText(MyanmarAttractionsApp.getContext(), "Extra : " + extra, Toast.LENGTH_SHORT).show();

        // Get the data from the Event
        this.mAttractionVOList = event.getAttractionList();

        // Keep the data in persistent layer.
        AttractionVO.saveAttractions(this.mAttractionVOList);
    }

    public List<AttractionVO> getAttractionList() {
        return mAttractionVOList;
    }

    public AttractionVO getAttractionByName(String attractionName) {
        for (AttractionVO attraction : mAttractionVOList) {
            if (attraction.getTitle().equals(attractionName))
                return attraction;
        }

        return null;
    }
}
