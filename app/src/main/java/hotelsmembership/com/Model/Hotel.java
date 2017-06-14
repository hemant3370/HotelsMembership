
package hotelsmembership.com.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Hotels")
public class Hotel implements Parcelable {
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

    public String getHotelLogoURL() {
        return "http://hotelsmembership.com" + hotelLogoURL;
    }

    public void setHotelLogoURL(String hotelLogoURL) {
        this.hotelLogoURL = hotelLogoURL;
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
    }

    public Hotel() {
    }

    protected Hotel(Parcel in) {
        this.hotelId = in.readString();
        this.hotelName = in.readString();
        this.hotelAddress = in.readString();
        this.hotelLogoURL = in.readString();
    }

    public static final Parcelable.Creator<Hotel> CREATOR = new Parcelable.Creator<Hotel>() {
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
