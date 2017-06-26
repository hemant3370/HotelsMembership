package hotelsmembership.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 26/06/17.
 */

public class RoomReservationPayload {
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("checkInDate")
    @Expose
    private String checkInDate;
    @SerializedName("checkOutDate")
    @Expose
    private String checkOutDate;
    @SerializedName("occupancy")
    @Expose
    private String occupancy;
    @SerializedName("discountDetail")
    @Expose
    private String discountDetail;
    @SerializedName("voucherDetail")
    @Expose
    private String voucherDetail;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(String occupancy) {
        this.occupancy = occupancy;
    }

    public String getDiscountDetail() {
        return discountDetail;
    }

    public void setDiscountDetail(String discountDetail) {
        this.discountDetail = discountDetail;
    }

    public String getVoucherDetail() {
        return voucherDetail;
    }

    public void setVoucherDetail(String voucherDetail) {
        this.voucherDetail = voucherDetail;
    }
}
