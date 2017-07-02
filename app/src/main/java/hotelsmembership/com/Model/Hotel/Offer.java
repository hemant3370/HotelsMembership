package hotelsmembership.com.Model.Hotel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 02/07/17.
 */
@Entity(tableName = "offers")
class Offer implements Parcelable {
    @PrimaryKey
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

    @Override
    public String toString() {
        return "Offer{" +
                "offerId='" + offerId + '\'' +
                ", description='" + description + '\'' +
                ", offerCategory='" + offerCategory + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validThrough='" + validThrough + '\'' +
                '}';
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.offerId);
        dest.writeString(this.description);
        dest.writeString(this.offerCategory);
        dest.writeString(this.validFrom);
        dest.writeString(this.validThrough);
    }

    public Offer() {
    }

    protected Offer(Parcel in) {
        this.offerId = in.readString();
        this.description = in.readString();
        this.offerCategory = in.readString();
        this.validFrom = in.readString();
        this.validThrough = in.readString();
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel source) {
            return new Offer(source);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
}
