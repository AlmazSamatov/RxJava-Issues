package almaz.issues.View;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import almaz.issues.Adapters.CommentsAdapter;
import almaz.issues.Adapters.LabelsAdapter;
import almaz.issues.ObjectClasses.Comment;
import almaz.issues.ObjectClasses.Issue;
import almaz.issues.Presenter.IssuePresenter;
import almaz.issues.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Almaz on 3/19/2018.
 */

public class IssueActivity extends AppCompatActivity implements IssueView {

    private final String COMMENTS_INSTANCE_STATE = "commentsInstanceState";
    private IssuePresenter issuePresenter;
    private Parcelable listViewOnRestoreState;
    @BindView(R.id.titleInIssue)
    TextView title;
    @BindView(R.id.bodyInIssue)
    TextView body;
    @BindView(R.id.stateInIssue)
    TextView state;
    @BindView(R.id.created_atInIssue)
    TextView created_at;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.comments)
    ListView comments;
    @BindView(R.id.labels)
    ListView labels;
    @BindView(R.id.empty_comments)
    TextView emptyComments;
    @BindView(R.id.CommentsProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeRefreshLayoutComments)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.swipeComments)
    FrameLayout swipeComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_activity);
        initialize(savedInstanceState != null);
    }

    public void initialize(boolean isResumed) {
        issuePresenter = new IssuePresenter(this);
        ButterKnife.bind(this);
        String issueInJson = getIntent().getStringExtra("issue");
        issuePresenter.setOnRefreshComments(swipeRefreshLayout);
        issuePresenter.setAndShowIssue(issueInJson);
        issuePresenter.loadAndSetAvatar(avatar);
        issuePresenter.loadComments(isResumed);
    }

    @Override
    public void showIssue(Issue issue) {
        title.setText(issue.getTitle());
        title.setMovementMethod(new ScrollingMovementMethod());
        body.setText(issue.getBody());
        body.setMovementMethod(new ScrollingMovementMethod());
        state.setText(issue.getState());
        created_at.setText(issue.getCreated_at());
        login.setText(issue.getUser().getLogin());

        LabelsAdapter adapter = new LabelsAdapter(this, R.layout.label_list_item,
                issue.getLabels());
        TextView emptyText = findViewById(R.id.empty_labels);
        labels.setEmptyView(emptyText);
        labels.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        issuePresenter.saveComments();
        // save comments listView position
        outState.putParcelable(COMMENTS_INSTANCE_STATE, comments.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // restore comments listView position
        if(savedInstanceState != null)
            listViewOnRestoreState = savedInstanceState.getParcelable(COMMENTS_INSTANCE_STATE);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showComments(List<Comment> commentsList) {
        swipeComments.setVisibility(View.VISIBLE);
        CommentsAdapter adapter = new CommentsAdapter(getApplicationContext(),
                R.layout.comment_item, commentsList);
        comments.setEmptyView(emptyComments);
        comments.setAdapter(adapter);

        // set restored position of listView
        if(listViewOnRestoreState != null)
            comments.onRestoreInstanceState(listViewOnRestoreState);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void stopRefreshLayout() {
        if(swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        issuePresenter.deattachView();
        super.onDestroy();
    }
}