
package hotelsmembership.com.Model.Hotel;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@TypeConverters(ListConverter.class)
@Entity(tableName = "Hotels")
public class Hotel implements Parcelable  {
    @PrimaryKey
    @SerializedName("hotelId")
    @Expose
    private String hotelId;
    @SerializedName("hotelName")
    @Expose
    private String hotelName;
    @SerializedName("hotelAddress")
    @Expose
    private String hotelAddress;
    @SerializedName("hotelLogoURL")
    @Expose
    private String hotelLogoURL;
    @SerializedName("emailIds")
    @Expose
    private List<String> emailIds = null;
    @Embedded
    @SerializedName("phoneNumbers")
    @Expose
    private PhoneNumbers phoneNumbers;
    @Embedded
    @SerializedName("cardsImageURLs")
    @Expose
    private CardsImageURLs cardsImageURLs;
    @SerializedName("membershipTermsAndConditions")
    @Expose
    private List<String> membershipTermsAndConditions;

;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public void setHotelLogoURL(String hotelLogoURL) {
        this.hotelLogoURL = hotelLogoURL;
    }

    public List<String> getEmailIds() {
        return emailIds;
    }

    public void setEmailIds(List<String> emailIds) {
        this.emailIds = emailIds;
    }

    public PhoneNumbers getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(PhoneNumbers phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public CardsImageURLs getCardsImageURLs() {
        return cardsImageURLs;
    }

    public void setCardsImageURLs(CardsImageURLs cardsImageURLs) {
        this.cardsImageURLs = cardsImageURLs;
    }


    public List<String> getMembershipTermsAndConditions() {
        return membershipTermsAndConditions;
    }

    public void setMembershipTermsAndConditions(List<String> membershipTermsAndConditions) {
        this.membershipTermsAndConditions = membershipTermsAndConditions;
    }


    public String getHotelLogoURL() {
        return "http://hotelsmembership.com" + hotelLogoURL.replace("http://hotelsmembership.com","");
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hotelId);
        dest.writeString(this.hotelName);
        dest.writeString(this.hotelAddress);
        dest.writeString(this.hotelLogoURL);
        dest.writeStringList(this.emailIds);
        dest.writeParcelable(this.phoneNumbers, flags);
        dest.writeParcelable(this.cardsImageURLs, flags);
        dest.writeStringList(this.membershipTermsAndConditions);

    }

    public Hotel() {
    }

    protected Hotel(Parcel in) {
        this.hotelId = in.readString();
        this.hotelName = in.readString();
        this.hotelAddress = in.readString();
        this.hotelLogoURL = in.readString();
        this.emailIds = in.createStringArrayList();
        this.phoneNumbers = in.readParcelable(PhoneNumbers.class.getClassLoader());
        this.cardsImageURLs = in.readParcelable(CardsImageURLs.class.getClassLoader());
        this.membershipTermsAndConditions = in.createStringArrayList();

    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel source) {
            return new Hotel(source);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };
}
