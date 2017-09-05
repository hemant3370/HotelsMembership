
package hotelsmembership.com.Model.Vouchers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Voucher implements Parcelable {

    @SerializedName("voucherNumber")
    @Expose
    private String voucherNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("voucherCategory")
    @Expose
    private VoucherCategory voucherCategory;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    boolean isSelected;

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getStatus() {
        switch (status){
            case "R":
                return  "Redeemed";
            case "U":
                return  "Unused";
            default:
                return status;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VoucherCategory getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(VoucherCategory voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getImageURL() {
        return "http://hotelsmembership.com" + imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.voucherNumber);
        dest.writeString(this.status);
        dest.writeParcelable(this.voucherCategory, flags);
        dest.writeString(this.imageURL);
    }

    public Voucher() {
    }

    protected Voucher(Parcel in) {
        this.voucherNumber = in.readString();
        this.status = in.readString();
        this.voucherCategory = in.readParcelable(VoucherCategory.class.getClassLoader());
        this.imageURL = in.readString();
    }

    public static final Parcelable.Creator<Voucher> CREATOR = new Parcelable.Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel source) {
            return new Voucher(source);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };
}
