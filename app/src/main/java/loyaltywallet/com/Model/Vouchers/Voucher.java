
package loyaltywallet.com.Model.Vouchers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
    public boolean isSelected;

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
    private static boolean checkDuplicate(String type, List<Voucher> list){
        for (Voucher voucher : list){
            if(voucher.getVoucherCategory().getCategoryCode().equals(type)){
                return true;
            }
        }
        return  false;
    }
    public static List<Voucher> sortedVouchers(List<Voucher> toSortList){
        List<Voucher> sorted = new ArrayList<>();

        for (Voucher v :
                toSortList) {
            if (!v.getStatus().equals("Redeemed") && !checkDuplicate(v.getVoucherCategory().getCategoryCode(), sorted)) {
                sorted.add(v);
            }
        }
        for (Voucher v :
                toSortList) {
            if (v.getStatus().equals("Redeemed")) {
                sorted.add(v);
            }
        }
        return sorted;
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
