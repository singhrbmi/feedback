package fusionsoftware.loop.feedback.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

import fusionsoftware.loop.feedback.model.Result;
import fusionsoftware.loop.feedback.utility.Contants;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = Contants.DATABASE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userData");
        db.execSQL("DROP TABLE IF EXISTS QuestionData");
        db.execSQL("DROP TABLE IF EXISTS AdminData");
        db.execSQL("DROP TABLE IF EXISTS feedbackData");
        db.execSQL("DROP TABLE IF EXISTS feedbackSummeryData");
        db.execSQL("DROP TABLE IF EXISTS feedbackSummeryCounterData");
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_user_TABLE = "CREATE TABLE QuestionData(questionId INTEGER,question TEXT,isActive TEXT,quesOrderStatus INTEGER)";
        String CREATE_UserData_TABLE = "CREATE TABLE userData(loginId INTEGER,Username TEXT,UserPhone TEXT,EmailId TEXT,Password TEXT)";
        String CREATE_AdminData_TABLE = "CREATE TABLE AdminData(AdminId INTEGER,AdminName TEXT,AdminPhone TEXT,AdminEmail TEXT,AdminPassword TEXT,AdminProfile TEXT)";
        String CREATE_feedbackData_TABLE = "CREATE TABLE feedbackData(questionId INTEGER,feedbackStatus TEXT)";
        String CREATE_feedbackSummeryData_TABLE = "CREATE TABLE feedbackSummeryData(feedbackId INTEGER,name TEXT,phone TEXT,questionId INTEGER,remark TEXT,date TEXT,answerId INTEGER,feedbackStatus TEXT,answerSize INTEGER,loginId INTEGER)";
        String CREATE_feedbackSummeryCounterData_TABLE = "CREATE TABLE feedbackSummeryCounterData(feedbackId INTEGER,questionId INTEGER,bad TEXT,happy TEXT,good TEXT,awosome TEXT)";

        db.execSQL(CREATE_feedbackSummeryData_TABLE);
        db.execSQL(CREATE_feedbackSummeryCounterData_TABLE);
        db.execSQL(CREATE_user_TABLE);
        db.execSQL(CREATE_UserData_TABLE);
        db.execSQL(CREATE_AdminData_TABLE);
        db.execSQL(CREATE_feedbackData_TABLE);
    }

    //    // --------------------------Admin Data---------------
    public boolean upsertAdminData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getAdminId() != 0) {
            data = getAdminDataByAdminId(ob.getAdminId());
            if (data == null) {
                done = insertAdminData(ob);
            } else {
                done = updateAdminData(ob);
            }
        }
        return done;
    }


    //    // for Admin data..........
    private void populateAdminData(Cursor cursor, Result ob) {
        ob.setAdminId(cursor.getInt(0));
        ob.setAdminName(cursor.getString(1));
        ob.setAdminPhone(cursor.getString(2));
        ob.setAdminEmail(cursor.getString(3));
        ob.setAdminPassword(cursor.getString(4));
        ob.setAdminProfile(cursor.getString(5));
    }

    // insert Admin data.............
    public boolean insertAdminData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("AdminId", ob.getAdminId());
        values.put("AdminName", ob.getAdminName());
        values.put("AdminPhone", ob.getAdminPhone());
        values.put("AdminEmail", ob.getAdminEmail());
        values.put("AdminPassword", ob.getAdminPassword());
        values.put("AdminProfile", ob.getAdminProfile());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("AdminData", null, values);
        db.close();
        return i > 0;
    }

    //    Admin data
    public Result getAdminData() {

        String query = "Select * FROM AdminData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateAdminData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //
//    //show  Admin list data
    public List<Result> getAllAdminData() {
        ArrayList list = new ArrayList<>();
        String query = "Select * FROM AdminData";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateAdminData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    //  get Admin data
    public Result getAdminDataByAdminId(int id) {

        String query = "Select * FROM AdminData WHERE AdminId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateAdminData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //    update  Admin data
    public boolean updateAdminData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("AdminId", ob.getAdminId());
        values.put("AdminName", ob.getAdminName());
        values.put("AdminPhone", ob.getAdminPhone());
        values.put("AdminEmail", ob.getAdminEmail());
        values.put("AdminPassword", ob.getAdminPassword());
        values.put("AdminProfile", ob.getAdminProfile());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("AdminData", values, "AdminId = " + ob.getAdminId() + " ", null);

        db.close();
        return i > 0;
    }

    // delete Admin Data
    public boolean deleteAdminData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("AdminData", null, null);
        db.close();
        return result;
    }


    //    // --------------------------user Data---------------
    public boolean upsertUserData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getUserId() != 0) {
            data = getUserDataByLoginId(ob.getUserId());
            if (data == null) {
                done = insertUserData(ob);
            } else {
                done = updateUserData(ob);
            }
        }
        return done;
    }


    //    // for user data..........
    private void populateUserData(Cursor cursor, Result ob) {
        ob.setUserId(cursor.getInt(0));
        ob.setUserName(cursor.getString(1));
        ob.setUserPhone(cursor.getString(2));
//        ob.setEmpPassword(cursor.getString(4));
    }

    // insert userData data.............
    public boolean insertUserData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("loginId", ob.getUserId());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("userData", null, values);
        db.close();
        return i > 0;
    }

    //    user data
    public Result getUserData() {

        String query = "Select * FROM userData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //
//    //show  user list data
    public List<Result> getAllUserData() {
        ArrayList list = new ArrayList<>();
        String query = "Select * FROM userData";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateUserData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    //  get user data
    public Result getUserDataByLoginId(int id) {

        String query = "Select * FROM userData WHERE loginId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //    update  data
    public boolean updateUserData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("loginId", ob.getUserId());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("userData", values, "loginId = " + ob.getUserId() + " ", null);

        db.close();
        return i > 0;
    }

    // delete user Data
    public boolean deleteUserData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("userData", null, null);
        db.close();
        return result;
    }


    //--------------------------Question---------------------------


    public boolean upsertQuestionDataData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getQuestionId() != 0) {
            data = getQuestionDataByLoginId(ob.getQuestionId());
            if (data == null) {
                done = insertQuestionData(ob);
            } else {
                done = updateQuestionData(ob);
            }
        }
        return done;
    }

    //insert Question data.............
    public boolean insertQuestionData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("questionId", ob.getQuestionId());
        values.put("question", ob.getQuestion());
        values.put("isActive", ob.getIsActive());
        values.put("quesOrderStatus", ob.getQuestionStatus());


        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("QuestionData", null, values);
        db.close();
        return i > 0;
    }

    // for userData data.............
    private void populateQuestionformation(Cursor cursor, Result ob) {
        ob.setQuestionId(cursor.getInt(0));
        ob.setQuestion(cursor.getString(1));
        ob.setIsActive(cursor.getString(2));
        ob.setQuestionStatus(cursor.getInt(3));
    }

    //userData data
    public Result getQuestionData() {

        String query = "Select * FROM QuestionData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateQuestionformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    // get all Question data
    public List<Result> getAllQuestionData() {
        ArrayList list = new ArrayList();

        String query = "Select * from QuestionData";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateQuestionformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //Question data
    public Result getQuestionDataByLoginId(int id) {

        String query = "Select * FROM QuestionData WHERE questionId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateQuestionformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //update Question data
    public boolean updateQuestionData(Result ob) {
        ContentValues values = new ContentValues();

        values.put("questionId", ob.getQuestionId());
        values.put("question", ob.getQuestion());
        values.put("isActive", ob.getIsActive());
        values.put("quesOrderStatus", ob.getQuestionStatus());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("QuestionData", values, "questionId = " + ob.getQuestionId() + " ", null);

        db.close();
        return i > 0;
    }

    //update user data
    public boolean updateStatusIdData(int id, int stausId) {
        ContentValues values = new ContentValues();

        values.put("questionId", id);
        values.put("quesOrderStatus", stausId);
        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("QuestionData", values, "questionId = " + id + " ", null);

        db.close();
        return i > 0;
    }

    // delete Address Data from
    public boolean deleteQuestionData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("QuestionData", null, null);
        db.close();
        return result;
    }


    // ---------------------feedback data------------------------------


    // insert Feedback data.............
    public boolean insertFeedbackData(int qid, String status) {
        ContentValues values = new ContentValues();
        values.put("questionId", qid);
        values.put("feedbackStatus", status);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("feedbackData", null, values);
        db.close();
        return i > 0;
    }

    //    //show  Feedback list data
    public List<Result> getAllFeedbackData() {
        ArrayList list = new ArrayList<>();
        String query = "Select * FROM feedbackData";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateFeedbackData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    private void populateFeedbackData(Cursor cursor, Result ob) {
        ob.setQuestionId(cursor.getInt(0));
        ob.setFeedbackStatus(cursor.getString(1));
    }

    // delete feedback Data from
    public boolean deleteFeedbackData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("feedbackData", null, null);
        db.close();
        return result;
    }


    //--------------------------feedback summery---------------------------


    public boolean upsertFeedbackSummeryDataData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getFeedbackId() != 0) {
            data = getFeedbackSummeryDataByFeedbackId(ob.getFeedbackId());
            if (data == null) {
                done = insertFeedbackSummeryData(ob);
            } else {
                done = updateFeedbackSummeryData(ob);
            }
        }
        return done;
    }

    //insert FeedbackSummery data.............
    public boolean insertFeedbackSummeryData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("feedbackId", ob.getFeedbackId());
        values.put("name", ob.getName());
        values.put("phone", ob.getPhone());
        values.put("questionId", ob.getQuestionId());
        values.put("remark", ob.getRemark());
        values.put("date", ob.getDate());
        values.put("answerId", ob.getAnswerId());
        values.put("feedbackStatus", ob.getFeedbackStatus());
        values.put("answerSize", ob.getAnswerSize());
        values.put("loginId", ob.getLoginId());


        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("feedbackSummeryData", null, values);
        db.close();
        return i > 0;
    }

    // for FeedbackSummery data.............
    private void populateFeedbackSummeryformation(Cursor cursor, Result ob) {
        ob.setFeedbackId(cursor.getInt(0));
        ob.setName(cursor.getString(1));
        ob.setPhone(cursor.getString(2));
        ob.setQuestionId(cursor.getInt(3));
        ob.setRemark(cursor.getString(4));
        ob.setDate(cursor.getString(5));
        ob.setAnswerId(cursor.getInt(6));
        ob.setFeedbackStatus(cursor.getString(7));
        ob.setAnswerSize(cursor.getInt(8));
        ob.setLoginId(cursor.getInt(9));
    }


    // get all FeedbackSummery data
    public List<Result> getAllFeedbackSummeryData() {
        ArrayList list = new ArrayList();

        String query = "Select * from feedbackSummeryData";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateFeedbackSummeryformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //FeedbackSummery data
    public Result getFeedbackSummeryDataByFeedbackId(int id) {

        String query = "Select * FROM feedbackSummeryData WHERE feedbackId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateFeedbackSummeryformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //FeedbackSummery data
    public Result getFeedbackSummeryDataByLoginId(int id) {

        String query = "Select * FROM feedbackSummeryData WHERE loginId = " + id +  " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateFeedbackSummeryformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //FeedbackSummery data
    public List<Result> getFeedbackSummeryDataByQuestionId(int id) {
        ArrayList list = new ArrayList();
        String query = "Select * FROM feedbackSummeryData WHERE questionId = " + id + " ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateFeedbackSummeryformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //FeedbackSummery data
    public List<Result> getFeedbackSummeryDataByDate(String date) {
        ArrayList list = new ArrayList();
        String query = "Select * FROM feedbackSummeryData WHERE date = '" + date + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateFeedbackSummeryformation(cursor, data);
            list.add(data);
            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return list;
    }

    //FeedbackSummery data
    public List<Result> getAllFeedbackSummeryDataByAnswerIdLoginId(int id) {
        ArrayList list = new ArrayList();
        String query = "Select * FROM feedbackSummeryData WHERE loginId = " + id+" ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateFeedbackSummeryformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //update FeedbackSummery data
    public boolean updateFeedbackSummeryData(Result ob) {
        ContentValues values = new ContentValues();

        values.put("feedbackId", ob.getFeedbackId());
        values.put("name", ob.getName());
        values.put("phone", ob.getPhone());
        values.put("questionId", ob.getQuestionId());
        values.put("remark", ob.getRemark());
        values.put("date", ob.getDate());
        values.put("answerId", ob.getAnswerId());
        values.put("feedbackStatus", ob.getFeedbackStatus());
        values.put("answerSize", ob.getAnswerSize());
        values.put("loginId", ob.getLoginId());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("feedbackSummeryData", values, "feedbackId = " + ob.getFeedbackId() + " ", null);

        db.close();
        return i > 0;
    }


    // delete FeedbackSummery Data from
    public boolean deleteFeedbackSummeryData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("feedbackSummeryData", null, null);
        db.close();
        return result;
    }

//.......................FeedbackSummeryCounter........................

    //insert FeedbackSummeryCounter data.............
    public boolean insertFeedbackSummeryCounterData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("feedbackId", ob.getFeedbackId());
        values.put("questionId", ob.getQuestionId());
        values.put("bad", ob.getBad());
        values.put("happy", ob.getHappy());
        values.put("good", ob.getGood());
        values.put("awosome", ob.getAwosome());


        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("feedbackSummeryCounterData", null, values);
        db.close();
        return i > 0;
    }

    // for FeedbackSummeryCounter data.............
    private void populateFeedbackSummeryCounterformation(Cursor cursor, Result ob) {
        ob.setFeedbackId(cursor.getInt(0));
        ob.setQuestionId(cursor.getInt(1));
        ob.setBad(cursor.getString(2));
        ob.setHappy(cursor.getString(3));
        ob.setGood(cursor.getString(4));
        ob.setAwosome(cursor.getString(5));
    }

    //FeedbackSummeryCounter data
    public List<Result> getFeedbackSummeryCounterDataByQuestionId(int id) {
        ArrayList list = new ArrayList();
        String query = "Select * FROM feedbackSummeryCounterData WHERE questionId = " + id + " ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateFeedbackSummeryCounterformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    // delete FeedbackSummeryCounter Data from
    public boolean deleteFeedbackSummeryCounterData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("feedbackSummeryCounterData", null, null);
        db.close();
        return result;
    }
}