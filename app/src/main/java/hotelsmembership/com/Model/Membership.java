
package hotelsmembership.com.Model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import hotelsmembership.com.Model.Hotel.Hotel;

@Entity (tableName = "memberships")
public class Membership implements Parcelable {

    @PrimaryKey
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @Embedded
    private Hotel hotel;
    @SerializedName("memberName")
    @Expose
    private String memberName;
    @SerializedName("memberAddress")
    @Expose
    private String memberAddress;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("cardValidFromDate")
    @Expose
    private String cardValidFromDate;
    @SerializedName("cardExpiryDate")
    @Expose
    private String cardExpiryDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cardType")
    @Expose
    private String cardType;
    @SerializedName("authToken")
    @Expose
    private String authToken;
    @SerializedName("tokenExpirydate")
    @Expose
    private String tokenExpirydate;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardValidFromDate() {
        return cardValidFromDate;
    }

    public void setCardValidFromDate(String cardValidFromDate) {
        this.cardValidFromDate = cardValidFromDate;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getTokenExpirydate() {
        return tokenExpirydate;
    }

    public void setTokenExpirydate(String tokenExpirydate) {
        this.tokenExpirydate = tokenExpirydate;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardNumber);
        dest.writeParcelable(this.hotel, flags);
        dest.writeString(this.memberName);
        dest.writeString(this.memberAddress);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.cardValidFromDate);
        dest.writeString(this.cardExpiryDate);
        dest.writeString(this.status);
        dest.writeString(this.cardType);
        dest.writeString(this.authToken);
        dest.writeString(this.tokenExpirydate);
    }

    public Membership() {
    }

    protected Membership(Parcel in) {
        this.cardNumber = in.readString();
        this.hotel = in.readParcelable(Hotel.class.getClassLoader());
        this.memberName = in.readString();
        this.memberAddress = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.cardValidFromDate = in.readString();
        this.cardExpiryDate = in.readString();
        this.status = in.readString();
        this.cardType = in.readString();
        this.authToken = in.readString();
        this.tokenExpirydate = in.readString();
    }

    public static final Creator<Membership> CREATOR = new Creator<Membership>() {
        @Override
        public Membership createFromParcel(Parcel source) {
            return new Membership(source);
        }

        @Override
        public Membership[] newArray(int size) {
            return new Membership[size];
        }
    };
}
