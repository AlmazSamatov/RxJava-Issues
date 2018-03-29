package almaz.issues.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import almaz.issues.ObjectClasses.Issue;
import almaz.issues.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Almaz on 3/20/2018.
 */

public class IssuesAdapter extends ArrayAdapter<Issue> {

    private List<Issue> issues;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.body)
    TextView body;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.created_at)
    TextView created_at;


    public IssuesAdapter(@NonNull Context context, int resource, @NonNull List<Issue> objects) {
        super(context, resource, objects);
        issues = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Issue issue = issues.get(position);
        View view = null;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        } else {
            view = convertView;
        }

        ButterKnife.bind(this, view);

        title.setText(issue.getTitle());
        body.setText(issue.getBody());
        state.setText(issue.getState());
        created_at.setText(issue.getCreated_at());

        return view;
    }
}
