package dev.nelson.mot.payment;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Payment implements Parcelable{

    public static final Parcelable.Creator<Payment> CREATOR
            = new Parcelable.Creator<Payment>() {
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    private int mData;

    private Integer mId;
    private String mTitle;
    private Integer mCategoryId;
    private String mCategoryName;
    private Double mCost;
    private String mSummary;

    public Payment(int id) {
        this.mId = id;
        mTitle = "";
        mCategoryId = -1;
        mCategoryName = "";
        mCost = 0.0;
        mSummary = "";
    }

    public Payment(Payment copyObj){
        mId = copyObj.mId;
        mTitle = copyObj.mTitle;
        mCategoryId = copyObj.mCategoryId;
        mCategoryName = copyObj.mCategoryName;
        mCost = copyObj.mCost;
        mSummary = copyObj.mSummary;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public double getCost() {
        return mCost;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public void setCost(double mCost) {
        this.mCost = mCost;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!Payment.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Payment currentState = (Payment) obj;
        return (
                   mId == currentState.getId()
                && mTitle.equals(currentState.getTitle())
                && Objects.equals(mCategoryId, currentState.mCategoryId)
                && mCategoryName.equals(currentState.getCategoryName())
                && mCost == currentState.getCost()
                && mSummary.equals(currentState.getSummary())

        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mData);
    }

    /** recreate object from parcel */
    private Payment(Parcel in) {
        mData = in.readInt();
    }
}
