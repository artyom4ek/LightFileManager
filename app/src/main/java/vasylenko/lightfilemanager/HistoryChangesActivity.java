package vasylenko.lightfilemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import vasylenko.lightfilemanager.adapter.DatabaseAdapter;
import vasylenko.lightfilemanager.adapter.HistoryChangesArrayAdapter;
import vasylenko.lightfilemanager.model.HistoryChanges;

public class HistoryChangesActivity extends AppCompatActivity {
    private ListView historyChangesList;
    // private HistoryChangesArrayAdapter<HistoryChanges> historyChangesArrayAdapter;
    private ArrayAdapter<HistoryChanges> historyChangesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_changes);

        historyChangesList = (ListView)findViewById(R.id.list);
        DatabaseAdapter adapter = new DatabaseAdapter(this);
        adapter.open();

        List<HistoryChanges> historyChangesArray = adapter.getHistoryChanges();

        // historyChangesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyChangesArray);
        historyChangesArrayAdapter = new HistoryChangesArrayAdapter(this, R.layout.history_changes_list_item, historyChangesArray);
        historyChangesList.setAdapter(historyChangesArrayAdapter);
        adapter.close();
    }
}
