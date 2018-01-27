package loyaltywallet.com.Model.Hotel;

/**
 * Created by hemantsingh on 17/06/17.
 */

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "phoneNumbers")
public class PhoneNumbers implements Parcelable {

    @SerializedName("Table Resevation")
    @Expose
    private String tableResevation;
    @SerializedName("Room Resevation")
    @Expose
    private String roomResevation;

    public String getTableResevation() {
        return tableResevation;
    }

    public void setTableResevation(String tableResevation) {
        this.tableResevation = tableResevation;
    }

    public String getRoomResevation() {
        return roomResevation;
    }

    public void setRoomResevation(String roomResevation) {
        this.roomResevation = roomResevation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tableResevation);
        dest.writeString(this.roomResevation);
    }

    public PhoneNumbers() {
    }

    protected PhoneNumbers(Parcel in) {
        this.tableResevation = in.readString();
        this.roomResevation = in.readString();
    }

    public static final Parcelable.Creator<PhoneNumbers> CREATOR = new Parcelable.Creator<PhoneNumbers>() {
        @Override
        public PhoneNumbers createFromParcel(Parcel source) {
            return new PhoneNumbers(source);
        }

        @Override
        public PhoneNumbers[] newArray(int size) {
            return new PhoneNumbers[size];
        }
    };
}
