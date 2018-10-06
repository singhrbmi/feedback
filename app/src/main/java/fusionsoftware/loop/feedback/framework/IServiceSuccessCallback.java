package fusionsoftware.loop.feedback.framework;


public interface IServiceSuccessCallback {
     void onDone(String callerUrl, String result, String error);
}