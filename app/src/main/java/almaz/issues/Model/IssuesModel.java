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

import almaz.issues.ObjectClasses.Issue;

/**
 * Created by Almaz on 3/19/2018.
 */

public class IssuesModel {

    private List<Issue> issues;
    private SharedPreferences sharedPreferences;
    private final String NAME_OF_SHARED_PREF = "Issues";

    public IssuesModel(Context context){
        issues = new LinkedList<>();
        sharedPreferences = context.getSharedPreferences(NAME_OF_SHARED_PREF, Context.MODE_PRIVATE);
    }

    public boolean isDataInMemory(){
        return sharedPreferences.getBoolean("IsDataSaved", false);
    }

    public void saveData(){
        // serialize issues and save it as string in Shared Preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String issuesInJson = gson.toJson(issues);
        editor.putBoolean("IsDataSaved", true);
        editor.putString("Issues", issuesInJson);
        editor.apply();
    }

    private List<Issue> loadIssuesFromMemory(){
        List<Issue> issueList = new LinkedList<>();
        String issuesInJson = sharedPreferences.getString("Issues", "");
        // check whether we have our saved list of issues
        if(!issuesInJson.equals("")){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Issue>>(){}.getType();
            issueList = gson.fromJson(issuesInJson, listType);
        }
        return issueList;
    }

    public void loadIssues(LoadIssuesCallback loadIssuesCallback){
        LoadIssuesTask loadIssuesTask = new LoadIssuesTask(loadIssuesCallback);
        loadIssuesTask.execute();
    }

    public interface LoadIssuesCallback {
        void onLoad(List<Issue> issues);
    }

    @SuppressLint("StaticFieldLeak")
    class LoadIssuesTask extends AsyncTask<Void, Void, List<Issue>> {

        private LoadIssuesCallback callback;

        LoadIssuesTask(LoadIssuesCallback callback){
            this.callback = callback;
        }

        @Override
        protected List<Issue> doInBackground(Void... voids) {
            return loadIssuesFromMemory();
        }

        @Override
        protected void onPostExecute(List<Issue> issues) {
            if(callback != null){
                callback.onLoad(issues);
            }
        }
    }

    public List<Issue> getIssues(){
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
}
