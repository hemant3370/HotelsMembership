
package hotelsmembership.com.Model.Vouchers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoucherCategory implements Parcelable {

    @SerializedName("categoryCode")
    @Expose
    private String categoryCode;
    @SerializedName("categoryType")
    @Expose
    private String categoryType;
    @SerializedName("categoryTitle")
    @Expose
    private String categoryTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("termsAndConditions")
    @Expose
    private List<TermsAndCondition> termsAndConditions = null;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public List<TermsAndCondition> getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(List<TermsAndCondition> termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryCode);
        dest.writeString(this.categoryType);
        dest.writeString(this.categoryTitle);
        dest.writeString(this.description);
        dest.writeTypedList(this.termsAndConditions);
    }

    public VoucherCategory() {
    }

    protected VoucherCategory(Parcel in) {
        this.categoryCode = in.readString();
        this.categoryType = in.readString();
        this.categoryTitle = in.readString();
        this.description = in.readString();
        this.termsAndConditions = in.createTypedArrayList(TermsAndCondition.CREATOR);
    }

    public static final Creator<VoucherCategory> CREATOR = new Creator<VoucherCategory>() {
        @Override
        public VoucherCategory createFromParcel(Parcel source) {
            return new VoucherCategory(source);
        }

        @Override
        public VoucherCategory[] newArray(int size) {
            return new VoucherCategory[size];
        }
    };
}
