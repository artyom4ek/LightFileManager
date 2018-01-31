package vasylenko.lightfilemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import vasylenko.lightfilemanager.adapter.DatabaseAdapter;
import vasylenko.lightfilemanager.adapter.HistoryChangesArrayAdapter;
import vasylenko.lightfilemanager.model.HistoryChanges;

public class HistoryChangesActivity extends AppCompatActivity implements View.OnClickListener{
    private Button clearHistoryChangesButton;
    private ListView historyChangesList;
    private DatabaseAdapter databaseAdapter;
    private ArrayAdapter<HistoryChanges> historyChangesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_changes);

        clearHistoryChangesButton = (Button)findViewById(R.id.clear_history_changes_button);
        clearHistoryChangesButton.setOnClickListener(this);

        historyChangesList = (ListView)findViewById(R.id.list);
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();

        List<HistoryChanges> historyChangesArray = databaseAdapter.getHistoryChanges();

        // historyChangesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyChangesArray);
        historyChangesArrayAdapter = new HistoryChangesArrayAdapter(this, R.layout.history_changes_list_item, historyChangesArray);
        historyChangesList.setAdapter(historyChangesArrayAdapter);
        databaseAdapter.close();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.clear_history_changes_button:
                databaseAdapter.open();
                databaseAdapter.clearHistoryChanges();
                databaseAdapter.close();
                break;
        }
    }

}
