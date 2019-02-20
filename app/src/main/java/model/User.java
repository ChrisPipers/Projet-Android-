package model;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private String mUid;
    private String mMail;
    private String mName;
    private String mLName;
    private int mAllActivity;
    private int mAge;
    private ArrayList<String> mAdresse;
    private HashMap<String, String> mMapMode;
    private HashMap<String, String> mMapMeteo;

    public User() {
    }

    public User(String mUid, String mMail, String mName, String mLName,int mAllActivity, int mAge, ArrayList<String> mAdresse,
                HashMap<String, String> mMapMode, HashMap<String, String> mMapMeteo) {
        this.mUid = mUid;
        this.mMail = mMail;
        this.mName = mName;
        this.mLName = mLName;
        this.mAllActivity = mAllActivity;
        this.mAge = mAge;
        this.mAdresse = mAdresse;
        this.mMapMode = mMapMode;
        this.mMapMeteo = mMapMeteo;
    }

    public String getmUid() {
        return mUid;
    }

    public String getmMail() {
        return mMail;
    }

    public String getmName() {
        return mName;
    }

    public String getmLName() {
        return mLName;
    }

    public ArrayList<String> getmAdresse() {
        return mAdresse;
    }

    public HashMap<String, String> getmMapMode() {
        return mMapMode;
    }

    public HashMap<String, String> getmMapMeteo() {
        return mMapMeteo;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public void setmMail(String mMail) {
        this.mMail = mMail;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmLName(String mLName) {
        this.mLName = mLName;
    }

    public void setmAdresse(ArrayList<String> mAdresse) {
        this.mAdresse = mAdresse;
    }

    public void setmMapMode(HashMap<String, String> mMapMode) {
        this.mMapMode = mMapMode;
    }

    public void setmMapMeteo(HashMap<String, String> mMapMeteo) {
        this.mMapMeteo = mMapMeteo;
    }

    public int getmAllActivity() {
        return mAllActivity;
    }

    public void setmAllActivity(int mAllActivity) {
        this.mAllActivity = mAllActivity;
    }
    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

}
