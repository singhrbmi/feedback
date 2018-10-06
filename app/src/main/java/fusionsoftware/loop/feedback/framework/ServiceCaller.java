package fusionsoftware.loop.feedback.framework;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import fusionsoftware.loop.feedback.database.DbHelper;
import fusionsoftware.loop.feedback.model.ContentData;
import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.utility.Contants;


/**
 * Created by lalit on 7/25/2017.
 */
public class ServiceCaller {
    Context context;

    public ServiceCaller(Context context) {
        this.context = context;
    }

    //hindi  data..........................................................................................................

    public void callgetQeshayari(final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.getAllQuestion;
        JSONObject obj = new JSONObject();
        try {
            // obj.put("PhoneNumber", phone);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone(result, true);
//                    parseAndhindiQuesData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callhindiLoveshayari done", false);
                }
            }
        });
    }
    public void callgetQuestionData(final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.getAllQuestion;
        JSONObject obj = new JSONObject();
        try {
            // obj.put("PhoneNumber", phone);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
//                    workCompletedCallback.onDone(result, true);
                    parseAndhindiQuesData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callhindiLoveshayari done", false);
                }
            }
        });
    }

    //parse and save parseAndhindiLoveData
    public void parseAndhindiQuesData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                if (result != null) {
                    ContentData data = new Gson().fromJson(result, ContentData.class);
                    if (data != null) {
                        if (data.getResult() != null) {
                            DbHelper database = new DbHelper(context);
                            database.deleteQuestionData();
                            for (Result result1 : data.getResult()) {
                                if (result1 != null) {
                                    database.upsertQuestionDataData(result1);
                                }
                            }
                            flag = true;
                        }
                    }
                }
                return flag;

            }


            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callhindiLoveshayari done", true);
                } else {
                    workCompletedCallback.onDone("callhindiLoveshayari done", false);
                }
            }
        }.execute();
    }
}