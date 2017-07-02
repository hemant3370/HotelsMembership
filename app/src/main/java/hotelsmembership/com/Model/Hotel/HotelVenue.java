package hotelsmembership.com.Model.Hotel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 02/07/17.
 */
@Entity(tableName = "venues")
public class HotelVenue {
    @PrimaryKey
    @SerializedName("venueId")
    @Expose
    private String venueId;
    @SerializedName("venuelName")
    @Expose
    private String venuelName;
    @SerializedName("venueCategory")
    @Expose
    private String venueCategory;

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenuelName() {
        return venuelName;
    }

    public void setVenuelName(String venuelName) {
        this.venuelName = venuelName;
    }

    public String getVenueCategory() {
        return venueCategory;
    }

    public void setVenueCategory(String venueCategory) {
        this.venueCategory = venueCategory;
    }
}
