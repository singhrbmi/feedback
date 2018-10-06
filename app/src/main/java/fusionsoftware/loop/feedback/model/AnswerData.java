package fusionsoftware.loop.feedback.model;

/**
 * Created by lalit on 10/4/2017.
 */

public class AnswerData {

    private String QusetionName;

    private int Answer;

    private int AnswerId;

    private int QuestionId;

    public String getQusetionName ()
    {
        return QusetionName;
    }

    public void setQusetionName (String QusetionName)
    {
        this.QusetionName = QusetionName;
    }

    public int getAnswer() {
        return Answer;
    }

    public void setAnswer(int answer) {
        Answer = answer;
    }

    public int getAnswerId ()
    {
        return AnswerId;
    }

    public void setAnswerId (int AnswerId)
    {
        this.AnswerId = AnswerId;
    }

    public int getQuestionId ()
    {
        return QuestionId;
    }

    public void setQuestionId (int QuestionId)
    {
        this.QuestionId = QuestionId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [QusetionName = "+QusetionName+", Answer = "+Answer+", AnswerId = "+AnswerId+", QuestionId = "+QuestionId+"]";
    }

}
