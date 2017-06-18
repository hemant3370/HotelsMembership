package hotelsmembership.com.Retrofit.Services;


import hotelsmembership.com.Model.AddCardPayload;
import hotelsmembership.com.Model.AddMembershipResponse;
import hotelsmembership.com.Model.BasicResponse;
import hotelsmembership.com.Model.HotelsResponse;
import hotelsmembership.com.Model.RedeemPayload;
import hotelsmembership.com.Model.VerifyOTPPayload;
import hotelsmembership.com.Model.Vouchers.VouchersResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by HemantSingh on 18/06/16.
 */
public interface ApiInterface {

//    //Hotels
    @GET("hotels")
    Observable<HotelsResponse> getHotels();
    //Add Card
    @POST("memberDetail/{hotelid}")
    Observable<AddMembershipResponse> addMembership(@Body AddCardPayload payload, @Path("hotelid") String hotelid);

    @POST("vouchers/{hotelid}")
    Observable<VouchersResponse> getVouchers(@Body AddCardPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("sendOTP/{hotelid}")
    Observable<BasicResponse> sendOTP(@Body RedeemPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("redeemVoucher/{hotelid}")
    Observable<BasicResponse> redeemVoucher(@Body VerifyOTPPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);


}
