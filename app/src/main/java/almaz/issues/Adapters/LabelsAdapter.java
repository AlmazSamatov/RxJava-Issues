package almaz.issues.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import almaz.issues.ObjectClasses.Label;
import almaz.issues.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Almaz on 3/20/2018.
 */

public class LabelsAdapter extends ArrayAdapter<Label> {

    private List<Label> labels;

    @BindView(R.id.label)
    TextView text;

    public LabelsAdapter(@NonNull Context context, int resource, @NonNull List<Label> objects) {
        super(context, resource, objects);
        labels = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Label label = labels.get(position);
        View view = null;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.label_list_item,
                    parent, false);
        } else {
            view = convertView;
        }

        ButterKnife.bind(this, view);

        text.setText(label.getName());
        text.setBackgroundColor(Color.parseColor("#" + label.getColor()));

        return view;
    }
}
