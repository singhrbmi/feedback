package fusionsoftware.loop.feedback.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.activity.GetAllReviewActivity;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.AnswerData;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.FontManager;
import fusionsoftware.loop.feedback.utility.Utility;

public class SubFeedbackAdapter extends RecyclerView.Adapter<SubFeedbackAdapter.ViewHolder> {
    private Context context;
    private List<AnswerData> questionIdList;
    private List<Result> resultList;
    ProgressDialog pd;

    public SubFeedbackAdapter(Context context, List<AnswerData> questionIdList) {
        this.context = context;
        this.questionIdList = questionIdList;
        this.resultList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemQuesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_feedback, parent, false);
        return new ViewHolder(itemQuesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DbHelper dbHelper = new DbHelper(context);

//        Result answerData = dbHelper.getFeedbackSummeryDataByAnswerId(questionIdList.get(position).getAnswerId());
//        if (answerData != null) {
//            holder.tv_emojiText.setText(answerData.getFeedbackStatus());
//        }
//        for (AnswerData answerData : questionIdList) {
//            this.answerData = answerData;
//        }
        int count = questionIdList.get(position).getAnswer();
        holder.count.setText("(" + count + ")");
        if (questionIdList.get(position).getAnswerId() == 1) {
            holder.tv_emojiIcon.setText(Html.fromHtml("&#xf1f8;"));
            holder.tv_emojiIcon.setTextColor(Color.parseColor("#E73C4E"));
            holder.tv_emojiText.setTextColor(Color.parseColor("#E73C4E"));
            holder.count.setTextColor(Color.parseColor("#E73C4E"));
            holder.tv_emojiText.setText("Bad");
        }
        if (questionIdList.get(position).getAnswerId() == 2) {
            holder.tv_emojiIcon.setText(Html.fromHtml("&#xf1f6;"));
            holder.tv_emojiIcon.setTextColor(Color.parseColor("#F9DE6D"));
            holder.tv_emojiText.setTextColor(Color.parseColor("#F9DE6D"));
            holder.count.setTextColor(Color.parseColor("#F9DE6D"));
            holder.tv_emojiText.setText("Happy");
        }
        if (questionIdList.get(position).getAnswerId() == 3) {
            holder.tv_emojiIcon.setText(Html.fromHtml("&#xf1f5;"));
            holder.tv_emojiIcon.setTextColor(Color.parseColor("#1fa5e4"));
            holder.tv_emojiText.setTextColor(Color.parseColor("#1fa5e4"));
            holder.count.setTextColor(Color.parseColor("#1fa5e4"));
            holder.tv_emojiText.setText("Good");
        }
        if (questionIdList.get(position).getAnswerId() == 4) {
            holder.tv_emojiIcon.setText(Html.fromHtml("&#xf1f2;"));
            holder.tv_emojiIcon.setTextColor(Color.parseColor("#00923F"));
            holder.tv_emojiText.setTextColor(Color.parseColor("#00923F"));
            holder.count.setTextColor(Color.parseColor("#00923F"));
            holder.tv_emojiText.setText("Awosome");
        }

        holder.layout_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllFeedbackData(position);

            }
        });
    }

    private void getAllFeedbackData(final int position) {
        if (Utility.isOnline(context)) {
            pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.show();
            pd.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pd.setContentView(new ProgressBar(context));
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.getAllFeedbackDetails,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            resultList.clear();
                            if (!response.equalsIgnoreCase("")) {
                                ContentData contentData = new Gson().fromJson(response, ContentData.class);
                                if (contentData != null) {
                                    for (Result result : contentData.getResult()) {
                                        if (result != null) {
                                            resultList.addAll(Arrays.asList(result));

                                        }
                                    }
                                    showDataPopup(resultList);
                                }

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("questionId", String.valueOf(questionIdList.get(position).getQuestionId()));
                    params.put("answerId", String.valueOf(questionIdList.get(position).getAnswerId()));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } else {

            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry...")
                    .setContentText("You are Offline. Please check your Internet Connection.Thank You ")
                    .show();
        }
    }

    private void showDataPopup(List<Result> resultList) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custome_feedback_dialog);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        FeedbackDetailsAdapter feedbackDetailsAdapter = new FeedbackDetailsAdapter(context, resultList);
        recyclerView.setAdapter(feedbackDetailsAdapter);
        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return questionIdList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_emojiIcon, tv_emojiText, count;
        LinearLayout layout_emoji;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_emojiIcon = itemView.findViewById(R.id.tv_emojiIcon);
            count = itemView.findViewById(R.id.count);
            tv_emojiText = itemView.findViewById(R.id.tv_emojiText);
            layout_emoji = itemView.findViewById(R.id.layout_emoji);
            Typeface typeface = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            tv_emojiIcon.setTypeface(typeface);
        }

    }
}
