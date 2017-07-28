package hotelsmembership.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 26/06/17.
 */

public class TableReservationPayload {
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("paxCount")
    @Expose
    private Integer paxCount = 1;
    @SerializedName("reservationDate")
    @Expose
    private String reservationDate;
    @SerializedName("timeSlot")
    @Expose
    private String timeSlot;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getPaxCount() {
        return paxCount;
    }

    public void setPaxCount(Integer paxCount) {
        this.paxCount = paxCount;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
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
