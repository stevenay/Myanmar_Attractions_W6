package me.naylinaung.myanmar_attractions_w6.data.agents;

import java.util.List;

import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public interface AttractionDataAgent {
    void loadAttractions();
    void postLoadedAttractionsEvent(List<AttractionVO> attractionVOList);
}
