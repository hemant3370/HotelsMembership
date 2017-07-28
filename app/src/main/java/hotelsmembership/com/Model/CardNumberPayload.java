package hotelsmembership.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 28/07/17.
 */

public class CardNumberPayload {
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;

    public CardNumberPayload(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
