package fusionsoftware.loop.feedback.model;

import java.util.ArrayList;

public class Result {

    private String question;
    private String isActive;
    private String bad;
    private String happy;
    private String good;
    private String awosome;
    private int answerSize;
    private int loginId;

    public int getAnswerSize() {
        return answerSize;
    }
    ArrayList<AnswerData> answerDataList;

    public ArrayList<AnswerData> getAnswerDataList() {
        return answerDataList;
    }

    public void setAnswerDataList(ArrayList<AnswerData> answerDataList) {
        this.answerDataList = answerDataList;
    }
    public void setAnswerSize(int answerSize) {
        this.answerSize = answerSize;
    }

    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    private String feedbackStatus;


    public String getBad() {
        return bad;
    }

    public void setBad(String bad) {
        this.bad = bad;
    }

    public String getHappy() {
        return happy;
    }

    public void setHappy(String happy) {
        this.happy = happy;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }


    public String getAwosome() {
        return awosome;
    }

    public void setAwosome(String awosome) {
        this.awosome = awosome;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    private int questionId;


    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    private int answerId;

    public int getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(int questionStatus) {
        this.questionStatus = questionStatus;
    }

    private int questionStatus;

    private String AdminName;
    private String AdminEmail;
    private String AdminPassword;
    private String AdminProfile;
    private String AdminPhone;
    private int AdminId;
    private int feedbackId;
    private int userId;
    private String userName;
    private String fatherName;
    private String userPhone;
    private String name;
    private String phone;
    private String remark;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
    }

    public String getAdminEmail() {
        return AdminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        AdminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return AdminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        AdminPassword = adminPassword;
    }

    public String getAdminProfile() {
        return AdminProfile;
    }

    public void setAdminProfile(String adminProfile) {
        AdminProfile = adminProfile;
    }

    public String getAdminPhone() {
        return AdminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        AdminPhone = adminPhone;
    }

    public int getAdminId() {
        return AdminId;
    }

    public void setAdminId(int adminId) {
        AdminId = adminId;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
}
