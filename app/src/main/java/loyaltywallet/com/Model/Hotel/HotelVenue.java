package loyaltywallet.com.Model.Hotel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 02/07/17.
 */

public class HotelVenue implements Parcelable {

    @SerializedName("venueId")
    @Expose
    private String venueId;
    @SerializedName("venueName")
    @Expose
    private String venueName;
    @SerializedName("venueCategory")
    @Expose
    private String venueCategory;
    @SerializedName("venueImageUrl")
    @Expose
    private String venueImageUrl;
    @SerializedName("venueDescription")
    @Expose
    private String venueDescription;
    @SerializedName("venuePhone")
    @Expose
    private String venuePhone;
    @SerializedName("openHours")
    @Expose
    private String openHours;


    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueCategory() {
        return venueCategory;
    }

    public void setVenueCategory(String venueCategory) {
        this.venueCategory = venueCategory;
    }

    public String getVenueImageUrl() {
        return "http://hotelsmembership.com" +  venueImageUrl;
    }

    public void setVenueImageUrl(String venueImageUrl) {
        this.venueImageUrl = venueImageUrl;
    }

    public String getVenueDescription() {
        return venueDescription;
    }

    public void setVenueDescription(String venueDescription) {
        this.venueDescription = venueDescription;
    }

    public String getVenuePhone() {
        return venuePhone;
    }

    public void setVenuePhone(String venuePhone) {
        this.venuePhone = venuePhone;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.venueId);
        dest.writeString(this.venueName);
        dest.writeString(this.venueCategory);
        dest.writeString(this.venueImageUrl);
        dest.writeString(this.venueDescription);
        dest.writeString(this.venuePhone);
        dest.writeString(this.openHours);
    }

    public HotelVenue() {
    }

    protected HotelVenue(Parcel in) {
        this.venueId = in.readString();
        this.venueName = in.readString();
        this.venueCategory = in.readString();
        this.venueImageUrl = in.readString();
        this.venueDescription = in.readString();
        this.venuePhone = in.readString();
        this.openHours = in.readString();
    }

    public static final Parcelable.Creator<HotelVenue> CREATOR = new Parcelable.Creator<HotelVenue>() {
        @Override
        public HotelVenue createFromParcel(Parcel source) {
            return new HotelVenue(source);
        }

        @Override
        public HotelVenue[] newArray(int size) {
            return new HotelVenue[size];
        }
    };
}
