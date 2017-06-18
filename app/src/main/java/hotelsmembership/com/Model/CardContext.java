package hotelsmembership.com.Model;

import java.util.List;

import hotelsmembership.com.Model.Vouchers.Voucher;

/**
 * Created by hemantsingh on 18/06/17.
 */
public class CardContext {
    private Membership membership;
    private String cardNumber;
    private List<Voucher> vouchers;

    public CardContext(Membership membership, String cardNumber, List<Voucher> vouchers) {
        this.membership = membership;
        this.cardNumber = cardNumber;
        this.vouchers = vouchers;
    }

}
