package almaz.issues.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import almaz.issues.Adapters.IssuesAdapter;
import almaz.issues.ObjectClasses.Issue;
import almaz.issues.Presenter.IssuesPresenter;
import almaz.issues.R;
import almaz.issues.View.IssueActivity;
import almaz.issues.View.IssuesView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Almaz on 3/19/2018.
 */

public class MainActivity extends AppCompatActivity implements IssuesView {

    private final String LIST_INSTANCE_STATE = "listInstanceState";
    private Parcelable listViewOnRestoreState;
    private IssuesPresenter issuesPresenter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_issues)
    TextView emptyIssues;
    @BindView(R.id.IssuesProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeIssues)
    FrameLayout swipeIssues;
    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initialize(savedInstanceState != null);
    }

    public void initialize(boolean isResumed){
        issuesPresenter = new IssuesPresenter(this);
        ButterKnife.bind(this);
        issuesPresenter.setOnRefreshListener(swipeRefreshLayout);
        issuesPresenter.loadIssues();
    }

    public void showIssues(final List<Issue> issues) {

        swipeIssues.setVisibility(View.VISIBLE);

        IssuesAdapter adapter = new IssuesAdapter(this, R.layout.list_item, issues);
        listView.setEmptyView(emptyIssues);
        listView.setAdapter(adapter);

        // set restored position of listView
        if(listViewOnRestoreState != null)
            listView.onRestoreInstanceState(listViewOnRestoreState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getBaseContext(), IssueActivity.class);
                Bundle b = new Bundle();
                Gson gson = new Gson();
                String issueInJson = gson.toJson(issues.get(position));
                b.putString("issue", issueInJson);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public void showError(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
    public void onSaveInstanceState(Bundle outState) {
        issuesPresenter.saveData();
        // save issues listView position
        outState.putParcelable(LIST_INSTANCE_STATE, listView.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // restore issues listView position
        if(savedInstanceState != null)
            listViewOnRestoreState = savedInstanceState.getParcelable(LIST_INSTANCE_STATE);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        issuesPresenter.deattachView();
        super.onDestroy();
    }
}
