package com.example.bsproperty.net;

/**
 * Created by yezi on 2018/1/27.
 */

public class ApiManager {

    private static final String HTTP = "http://";
    private static final String IP = "192.168.55.102";
    private static final String PROT = ":8080";
    private static final String HOST = HTTP + IP + PROT;
    private static final String API = "/api";
    private static final String USER = "/user";
    private static final String QUESTION = "/question";
    private static final String REPLAY = "/replay";

    public static final String LOGIN = HOST+API+USER+"/login";

    public static final String ADD_QUESTION = HOST+API+QUESTION+"/add";

    public static final String GET_QUESTION_LIST = HOST+API+QUESTION+"/list";

    public static final String GET_QUESTION_IMG = HOST+API+QUESTION;

    public static final String POST_QUESTION_DEL = HOST+API+QUESTION+"/delete";

    public static final String GET_QUESTION_LIST_M = HOST+API+QUESTION+"/listM";

    public static final String POST_REPLAY_ADD = HOST+API+REPLAY+"/add";

    public static final String GET_REPLAY_LIST = HOST+API+REPLAY+"/list";

    public static final String POST_REPLAY_DEL = HOST+API+REPLAY+"/delete";

    public static final String POST_SCORE = HOST+API+QUESTION+"/score";
}
