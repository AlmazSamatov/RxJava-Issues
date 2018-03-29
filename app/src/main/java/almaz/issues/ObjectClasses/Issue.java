package almaz.issues.ObjectClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Almaz on 3/19/2018.
 */

public class Issue {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("labels")
    @Expose
    private List<Label> labels = null;
    @SerializedName("comments_url")
    @Expose
    private String comments_url;
    @SerializedName("number")
    @Expose
    private int number;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return "state: ".concat(state);
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    public String getCreated_at() {
        String time = created_at.substring(11, 16);
        String date = created_at.substring(0, 10);
        return "created at: ".concat(time).concat(" ").concat(date);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
