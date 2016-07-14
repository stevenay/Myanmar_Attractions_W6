package me.naylinaung.myanmar_attractions_w6.events;

import java.util.List;

import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class DataEvent {

    public static class AttractionDataLoadedEvent {
        private String extraMessage;
        private List<AttractionVO> attractionList;

        public AttractionDataLoadedEvent(String extraMessage, List<AttractionVO> attractionList) {
            this.extraMessage = extraMessage;
            this.attractionList = attractionList;
        }

        public String getExtraMessage() { return extraMessage; }

        public List<AttractionVO> getAttractionList() { return attractionList; }
    }
}
