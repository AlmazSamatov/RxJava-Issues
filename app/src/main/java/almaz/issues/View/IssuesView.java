package almaz.issues.View;

import android.content.Context;

import java.util.List;

import almaz.issues.ObjectClasses.Issue;

/**
 * Created by Almaz on 3/20/2018.
 */

public interface IssuesView {

    public void showIssues(List<Issue> issues);

    public void showError(String message);

    public void showProgressBar();

    public void hideProgressBar();

    public void stopRefreshLayout();

    public Context getContext();
}
