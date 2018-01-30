package vasylenko.lightfilemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vasylenko.lightfilemanager.R;
import vasylenko.lightfilemanager.model.HistoryChanges;

public class HistoryChangesArrayAdapter extends ArrayAdapter<HistoryChanges> {
    private Context context;
    private int layoutResourceId;
    private List<HistoryChanges> historyChangesArray;

    public HistoryChangesArrayAdapter(Context context, int layoutResourceId, List<HistoryChanges> historyChangesArray) {
        super(context, layoutResourceId, historyChangesArray);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.historyChangesArray = historyChangesArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HistoryChangesHolder historyChangesHolder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            historyChangesHolder = new HistoryChangesHolder();
            historyChangesHolder.historyChangesDate = (TextView)row.findViewById(R.id.history_changes_date);
            historyChangesHolder.historyChangesOperation = (TextView)row.findViewById(R.id.history_changes_operation);

            row.setTag(historyChangesHolder);
        } else {
            historyChangesHolder = (HistoryChangesHolder)row.getTag();
        }

        HistoryChanges historyChanges = this.historyChangesArray.get(position);
        historyChangesHolder.historyChangesDate.setText(historyChanges.getDate());
        historyChangesHolder.historyChangesOperation.setText(historyChanges.getOperation());

        return row;
    }

    static class HistoryChangesHolder {
        TextView historyChangesDate;
        TextView historyChangesOperation;
    }
}
