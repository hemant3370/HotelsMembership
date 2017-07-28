package hotelsmembership.com.Model.Hotel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 02/07/17.
 */

public class Offer  {


    @SerializedName("offerId")
    @Expose
    private String offerId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("offerCategory")
    @Expose
    private String offerCategory;
    @SerializedName("validFrom")
    @Expose
    private String validFrom;
    @SerializedName("validThrough")
    @Expose
    private String validThrough;
    @SerializedName("offerImageUrl")
    @Expose
    private String offerImageUrl;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfferCategory() {
        return offerCategory;
    }

    public void setOfferCategory(String offerCategory) {
        this.offerCategory = offerCategory;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidThrough() {
        return validThrough;
    }

    public void setValidThrough(String validThrough) {
        this.validThrough = validThrough;
    }

    public String getOfferImageUrl() {
        return offerImageUrl;
    }

    public void setOfferImageUrl(String offerImageUrl) {
        this.offerImageUrl = offerImageUrl;
    }

}
