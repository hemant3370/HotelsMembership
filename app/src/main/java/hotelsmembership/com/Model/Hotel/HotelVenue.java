package hotelsmembership.com.Model.Hotel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 02/07/17.
 */

public class HotelVenue {

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

}
