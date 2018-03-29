package almaz.issues.View;

import android.content.Context;

import java.util.List;

import almaz.issues.ObjectClasses.Comment;
import almaz.issues.ObjectClasses.Issue;

/**
 * Created by Almaz on 3/23/2018.
 */

public interface IssueView {

    public void showIssue(Issue issue);

    public void showError(String message);

    public void showComments(List<Comment> comments);

    public void showProgressBar();

    public void hideProgressBar();

    public void stopRefreshLayout();

    public Context getContext();
}
