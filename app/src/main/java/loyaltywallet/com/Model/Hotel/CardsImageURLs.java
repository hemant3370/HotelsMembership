package loyaltywallet.com.Model.Hotel;

/**
 * Created by hemantsingh on 17/06/17.
 */

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "cardImages")
public class CardsImageURLs implements Parcelable {

    @SerializedName("Gold")
    @Expose
    private String gold;
    @SerializedName("Silver")
    @Expose
    private String silver;

    public String getGold() {
        if (gold != null) {
            return "http://hotelsmembership.com" + gold.replace("http://hotelsmembership.com", "");
        }
        return "";
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getSilver() {
        if (silver != null) {
            return "http://hotelsmembership.com" + silver.replace("http://hotelsmembership.com", "");
        }
        return "";
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gold);
        dest.writeString(this.silver);
    }

    public CardsImageURLs() {
    }

    protected CardsImageURLs(Parcel in) {
        this.gold = in.readString();
        this.silver = in.readString();
    }

    public static final Parcelable.Creator<CardsImageURLs> CREATOR = new Parcelable.Creator<CardsImageURLs>() {
        @Override
        public CardsImageURLs createFromParcel(Parcel source) {
            return new CardsImageURLs(source);
        }

        @Override
        public CardsImageURLs[] newArray(int size) {
            return new CardsImageURLs[size];
        }
    };
}