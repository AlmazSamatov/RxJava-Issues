package almaz.issues.ObjectClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Almaz on 3/20/2018.
 */

public class Comment {

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("created_at")
    @Expose
    private String created_at;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated_at() {
        String time = created_at.substring(11, 16);
        String date = created_at.substring(0, 10);
        return "created at: ".concat(time).concat(" ").concat(date);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
