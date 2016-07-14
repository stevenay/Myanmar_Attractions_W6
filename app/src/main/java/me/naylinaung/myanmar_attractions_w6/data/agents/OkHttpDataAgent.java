package me.naylinaung.myanmar_attractions_w6.data.agents;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.data.responses.AttractionListResponse;
import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;
import me.naylinaung.myanmar_attractions_w6.events.DataEvent;
import me.naylinaung.myanmar_attractions_w6.utils.CommonInstances;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class OkHttpDataAgent implements AttractionDataAgent {

    private static OkHttpDataAgent objInstance;

    private OkHttpClient mHttpClient;

    private OkHttpDataAgent() {
        // Step 1. Initialize HttpClient
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpDataAgent getObjInstance() {
        if (objInstance == null) {
            objInstance = new OkHttpDataAgent();
        }
        return objInstance;
    }

    @Override
    public void loadAttractions() {
        new AsyncTask<Void, Void, List<AttractionVO>>() {

            @Override
            protected List<AttractionVO> doInBackground(Void... voids) {
                // Step 2. Build Request Body
                RequestBody formBody = new FormBody.Builder()
                        .add(MyanmarAttractionsConstants.PARAM_ACCESS_TOKEN, MyanmarAttractionsConstants.ACCESS_TOKEN)
                        .build();

                // Step 3. Build Request
                Request request = new Request.Builder()
                        .url(MyanmarAttractionsConstants.ATTRACTION_BASE_URL + MyanmarAttractionsConstants.API_GET_ATTRACTION_LIST)
                        .post(formBody)
                        .build();

                try {
                    // Step 4. Get Response
                    Response response = mHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseString = response.body().string();
                        AttractionListResponse attractionListResponse = CommonInstances.getGsonInstance().fromJson(responseString, AttractionListResponse.class);
                        List<AttractionVO> voList = attractionListResponse.getAttractionList();
                        return voList;
                    } else {
                        // TODO notifyErrorInLoading
                    }
                } catch (IOException e) {
                    Log.e(MyanmarAttractionsApp.TAG, e.getMessage());
                    // TODO notifyErrorInLoading
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<AttractionVO> attractionVOs) {
                super.onPostExecute(attractionVOs);
                if (attractionVOs != null && attractionVOs.size() > 0) {
                    postLoadedAttractionsEvent(attractionVOs);
                }
            }
        }.execute();
    }

    @Override
    public void postLoadedAttractionsEvent(List<AttractionVO> attractionVOList) {
        EventBus.getDefault().post(new DataEvent.AttractionDataLoadedEvent("extra-in-broadcast", attractionVOList));
    }
}
