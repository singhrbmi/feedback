package fusionsoftware.loop.feedback.framework;

/**
 * Created by lk on 7/25/2017.
 */
public interface IAsyncWorkCompletedCallback {
    public void onDone(String workName, boolean isComplete);
}
