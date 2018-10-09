package fusionsoftware.loop.feedback.utility;

import android.os.Environment;

/**
 * Created by lalit on 7/25/2017.
 */
public class Contants {
    public static final Boolean IS_DEBUG_LOG = true;

    public static final String LOG_TAG = "shayari";
    public static final String APP_NAME = "MyApplication"; // Do NOT change this value; meant for user preference

    public static final String DEFAULT_APPLICATION_NAME = "shayari";

    public static final String APP_DIRECTORY = "/E" + DEFAULT_APPLICATION_NAME + "Directory/";
    public static final String DATABASE_NAME = "database.db";// Environment.getExternalStorageDirectory() +  APP_DIRECTORY + "evergreen.db";

    public static String SERVICE_BASE_URL = "http://lalitsingh2.esy.es/feedbackApis/";

    public static String outputBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();


    public static final int LIST_PAGE_SIZE = 50;
    public static String InternetMessage = " You are Online.";
    public static final String BAD_NETWORK_MESSAGE = "We are trying hard to get latest content but the network seems to be slow. " +
            "You may continue to use app and get latest with in the app itself. ";


    public static final String OFFLINE_MESSAGE = "Oops! You are Offline. Please check your Internet Connection.";

    public static final String getAllQuestion = "getAllQuestion.php";
    public static final String Admin = "admin.php";
    public static final String uploadFeedback = "uploadFeedback.php";
    public static final String getAllFeedback = "getAllFeedback.php?page=";
    public static final String getAllFeedbackold = "getAllFeedbackold.php";
    public static final String getAllFeedbackGroup = "getAllFeedbackGroup.php";
    public static final String getAllFeedbackDetails = "getAllFeedbackDetails.php";
    public static final String updateQuestionStatus = "updateQuestionStatus.php";
    public static final String updateQuestionIsActive = "updateQuestionIsActive.php";
    public static final String deleteQuestion = "deleteQuestion.php";
    public static final String updateQuestion = "updateQuestion.php";
    public static final String addNewQuestion = "addNewQuestion.php";
    public static final String User = "user.php";
}
