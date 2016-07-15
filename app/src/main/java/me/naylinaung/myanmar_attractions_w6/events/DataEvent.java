package me.naylinaung.myanmar_attractions_w6.events;

import java.util.List;

import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;
import me.naylinaung.myanmar_attractions_w6.data.vos.UserVO;

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

    public static class LoginErrorEvent {
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public LoginErrorEvent(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    public static class LoginUserLoadedEvent {
        private UserVO user;

        public UserVO getUser() {
            return user;
        }

        public LoginUserLoadedEvent(UserVO user) {
            this.user = user;
        }
    }

    public static class RegisterErrorEvent {
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public RegisterErrorEvent(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
