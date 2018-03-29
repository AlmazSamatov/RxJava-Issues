package almaz.issues.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import almaz.issues.ObjectClasses.Comment;
import almaz.issues.ObjectClasses.Issue;

/**
 * Created by Almaz on 3/19/2018.
 */

public class IssueModel {

    private Issue issue;
    private List<Comment> commentsList;
    private SharedPreferences sharedPreferences;
    private final String NAME_OF_SHARED_PREF = "Comments";

    public IssueModel(Context context){
        issue = new Issue();
        commentsList = new LinkedList<>();
        sharedPreferences = context.getSharedPreferences(NAME_OF_SHARED_PREF, Context.MODE_PRIVATE);
    }

    public boolean isCommentsInMemory(){
        return sharedPreferences.getBoolean("isInMemory", false);
    }

    public void saveComments(){
        // serialize comments and save it as string in Shared Preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String commentsInJson = gson.toJson(commentsList);
        editor.putBoolean("isInMemory", true);
        editor.putString("comments", commentsInJson);
        editor.apply();
    }

    private List<Comment> loadCommentsFromMemory(){
        String commentsInJson = sharedPreferences.getString("comments", "");
        // check whether we have our saved list of comments
        if(!commentsInJson.equals("")){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Comment>>(){}.getType();
            commentsList = gson.fromJson(commentsInJson, listType);
        }
        return commentsList;
    }

    public void loadComments(LoadCommentsCallback callback){
        LoadCommentsTask loadCommentsTask = new LoadCommentsTask(callback);
        loadCommentsTask.execute();
    }

    public interface LoadCommentsCallback{
        void onLoad(List<Comment> comments);
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadCommentsTask extends AsyncTask<Void, Void, List<Comment>>{
        LoadCommentsCallback callback;

        LoadCommentsTask(LoadCommentsCallback callback){
            this.callback = callback;
        }

        @Override
        protected List<Comment> doInBackground(Void... voids) {
            return loadCommentsFromMemory();
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            if(callback != null){
                callback.onLoad(comments);
            }
        }
    }


    public List<Comment> getComments() {
        return commentsList;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public void setComments(List<Comment> comments) {
        this.commentsList = comments;
    }
}
