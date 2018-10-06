package fusionsoftware.loop.feedback.model;

/**
 * Created by lalit on 11/8/2017.
 */

public class ContentData {
    private Result[] result;

    public Result[] getResult ()
    {
        return result;
    }

    public void setResult (Result[] result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+"]";
    }
}
