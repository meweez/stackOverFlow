import java.io.*;

import org.bson.Document;

public class Message implements Serializable {

    static final int SEARCH = 0, LOGIN = 1, LOGOUT = 2, REGISTER = 3,SUGNUP = 4;
    private int type;
    //public String messages;
    private Document info;

    // constructor
//    Message(int type, String message) {
//        this.type = type;
//        this.messages = message;
//    }
    Message(int type, Document info) {
        this.type = type;
        this.info = info;
    }

    // getters
    int getType() {
        return type;
    }
    Document getInfo() {
        return info;
    }
}

