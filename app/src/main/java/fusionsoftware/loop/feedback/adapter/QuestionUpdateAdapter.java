package fusionsoftware.loop.feedback.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.feedback.R;
import fusionsoftware.loop.feedback.activity.GetAllReviewActivity;
import fusionsoftware.loop.feedback.activity.QuestionActivity;
import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.myalert.SweetAlertDialog;
import fusionsoftware.loop.feedback.swipeable_view.ItemTouchHelperAdapter;
import fusionsoftware.loop.feedback.swipeable_view.ItemTouchHelperViewHolder;
import fusionsoftware.loop.feedback.swipeable_view.OnStartDragListener;
import fusionsoftware.loop.feedback.utility.Contants;
import fusionsoftware.loop.feedback.utility.Utility;

public class QuestionUpdateAdapter extends RecyclerView.Adapter<QuestionUpdateAdapter.EditClass> implements ItemTouchHelperAdapter {
    private Context context;
    private List<fusionsoftware.loop.feedback.model.Result> resultList;
    private QuestionActivity questionActivity;
    private OnStartDragListener mDragStartListener;
    ProgressDialog pd;

    public QuestionUpdateAdapter(Context context, List<fusionsoftware.loop.feedback.model.Result> resultList1, QuestionActivity questionActivity, OnStartDragListener mDragStartListener) {
        this.context = context;
        this.resultList = resultList1;
        this.questionActivity = questionActivity;
        this.mDragStartListener = mDragStartListener;
    }


    @NonNull
    @Override
    public EditClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemQuesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ques_view, parent, false);
        return new EditClass(itemQuesView);
    }

    @Override
    public void onBindViewHolder(EditClass holder, final int position) {
        int p = position + 1;
        holder.textView.setText("Q." + p + "-" + resultList.get(position).getQuestion());
        String activeStatus = resultList.get(position).getIsActive();
        if (activeStatus.equalsIgnoreCase("yes")) {
            holder.tv_status.setText("\u2713"+" Active");
            holder.tv_status.setTextColor(Color.GREEN);
        }
        if (activeStatus.equalsIgnoreCase("no")) {
            holder.tv_status.setText("X "+"Deactive");
            holder.tv_status.setTextColor(Color.RED);


        }
        holder.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionActivity.updateData(resultList.get(position).getQuestionId(), resultList.get(position).getQuestion(), true);
            }
        });

        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Your imaginary file has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                deleteQuestion(position);
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .setCancelText("No Sorry")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
//        DbHelper dbHelper = new DbHelper(context);
//        dbHelper.updateStatusIdData(resultList.get(position).getQuestionId(), position);
    }

    private void deleteQuestion(final int position) {
        if (Utility.isOnline(context)) {
            pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.show();
            pd.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pd.setContentView(new ProgressBar(context));
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.deleteQuestion,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            resultList.remove(position);
                            questionActivity.setAdapter();
//                            notifyDataSetChanged();
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
                    params.put("questionId", String.valueOf(resultList.get(position).getQuestionId()));
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

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public boolean onItemMove(final int fromPosition, final int toPosition) {
        Collections.swap(resultList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition, 2, resultList);
        notifyItemChanged(toPosition);
//        Toast.makeText(context, "from"+fromPosition+"to"+toPosition, Toast.LENGTH_SHORT).show();
        if (Utility.isOnline(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.SERVICE_BASE_URL + Contants.updateQuestionStatus,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("questionId", String.valueOf(resultList.get(fromPosition).getQuestionId()));
                    params.put("questionStatus", String.valueOf(fromPosition));
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
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        resultList.remove(position);
        notifyItemRemoved(position);
    }

    public class EditClass extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        TextView textView;
        Button btn_Edit, btn_Delete;
        TextView tv_status;
        CardView linearlayout;

        public EditClass(final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            btn_Edit = itemView.findViewById(R.id.btn_Edit);
            tv_status = itemView.findViewById(R.id.tv_status);
            btn_Delete = itemView.findViewById(R.id.btn_Delete);
            linearlayout = itemView.findViewById(R.id.linearlayout);


        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#0ccfaf"));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        }
    }
}
