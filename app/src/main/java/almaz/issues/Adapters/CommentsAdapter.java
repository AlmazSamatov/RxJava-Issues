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

import almaz.issues.ObjectClasses.Comment;
import almaz.issues.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Almaz on 3/20/2018.
 */

public class CommentsAdapter extends ArrayAdapter<Comment> {

    private List<Comment> comments;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.date)
    TextView date;


    public CommentsAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        comments = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Comment commentItem = comments.get(position);
        View view = null;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        } else {
            view = convertView;
        }

        ButterKnife.bind(this, view);

        name.setText(commentItem.getUser().getLogin());
        comment.setText(commentItem.getBody());
        date.setText(commentItem.getCreated_at());

        return view;
    }
}
