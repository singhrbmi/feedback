package fusionsoftware.loop.feedback.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.activity.GetAllFeedbackActivity;
import fusionsoftware.loop.feedback.activity.QuestionActivity;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.AnswerData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.swipeable_view.ItemTouchHelperAdapter;
import fusionsoftware.loop.feedback.swipeable_view.ItemTouchHelperViewHolder;
import fusionsoftware.loop.feedback.swipeable_view.OnStartDragListener;
import fusionsoftware.loop.feedback.utility.FontManager;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    private Context context;
    private List<Result> resultList, resultListFeedback;
    ArrayList<AnswerData> answerDataList;

    public FeedbackAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
        resultListFeedback = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemQuesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(itemQuesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DbHelper database = new DbHelper(context);
        Collections.sort(resultList, new Comparator<Result>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(Result result, Result t1) {
                return Integer.compare(result.getQuestionId(), t1.getQuestionId());
            }
        });
        database.deleteFeedbackSummeryCounterData();
        Result result1 = database.getQuestionDataByLoginId(resultList.get(position).getQuestionId());
        if (result1 != null) {
            if (result1.getQuestion() != null) {
                holder.tv_question.setText(result1.getQuestion());
            }
        }
        answerDataList = new ArrayList<AnswerData>();
        List<Result> resultListSummery = database.getFeedbackSummeryDataByQuestionId(resultList.get(position).getQuestionId());
//        for (int i = 0; i < resultListSummery.size(); i++) {
//            List<Result> resultList = database.getFeedbackSummeryData(resultListSummery.get(position).getQuestionId(), resultListSummery.get(position).getAnswerId());
        for (Result result : resultListSummery) {
            AnswerData answerData = new AnswerData();
            answerData.setAnswerId(result.getAnswerId());
            answerData.setAnswer(result.getAnswerSize());
            answerData.setQuestionId(result.getQuestionId());
            answerData.setQusetionName(result.getQuestion());
            answerDataList.add(answerData);
//            }
        }
        shortAnswerList(answerDataList, holder);
    }

    //short answer
    private void shortAnswerList(ArrayList<AnswerData> answerDataList, ViewHolder holder) {
        ArrayList mData = new ArrayList();
        Collections.sort(answerDataList, new Comparator<AnswerData>() {
            @Override
            public int compare(AnswerData m1, AnswerData m2) {
                return m1.getAnswerId() - m2.getAnswerId();
            }
        });
//        if (answerDataList != null) {
//            for (AnswerData data : answerDataList) {
//                mData.add(data);
//            }
        SubFeedbackAdapter subFeedbackAdapter = new SubFeedbackAdapter(context, answerDataList);
        holder.recyclerView.setAdapter(subFeedbackAdapter);
        subFeedbackAdapter.notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_question;
        //                ;, good, bad, average, happy;
        RecyclerView recyclerView;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.tv_question);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        }

    }
}
