package loyaltywallet.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hemantsingh on 03/06/17.
 */

public class AddMembershipResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("content")
    @Expose
    private Membership content;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Membership getContent() {
        return content;
    }

    public void setContent(Membership content) {
        this.content = content;
    }

}