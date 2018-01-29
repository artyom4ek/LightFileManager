package vasylenko.lightfilemanager.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import vasylenko.lightfilemanager.R;
import vasylenko.lightfilemanager.adapter.ItemArrayAdapter;
import vasylenko.lightfilemanager.worker.FileWorker;
import vasylenko.lightfilemanager.worker.StyleWorker;

public class FragmentTwo extends FragmentBasic implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private Set<String> selectedItemList = new HashSet<String>();
    private String startDir;
    private ItemArrayAdapter itemArrayAdapterTwo;
    private FileWorker fileWorkerTwo;
    private File currentDir;

    public FragmentTwo() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        startDir = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("fragmentTwoCurrentDir", "/sdcard/");

        currentDir = new File(startDir);
        fileWorkerTwo = new FileWorker(getActivity(), this);
        try {
            itemArrayAdapterTwo = new ItemArrayAdapter(getActivity(), R.layout.fragment_list_item,
                    fileWorkerTwo.fillItemsList(currentDir)
            );
        } catch (Exception e) {
            Toast.makeText(getActivity(), "List fill  ERROR!", Toast.LENGTH_SHORT).show();
        }
        setListAdapter(itemArrayAdapterTwo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(new ColorDrawable(Color.WHITE));
        getListView().setDividerHeight(2);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setOnItemLongClickListener(this);
        getListView().setOnItemClickListener(this);
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public Set<String> getSelectedPath() { return selectedItemList; }

    @Override
    public  int getSelectedPathCount(){ return getSelectedPath().size(); }

    private void getSelectedItems(int i){
        if(getListView().isItemChecked(i)) {
            getListView().setItemChecked(i, false);
            selectedItemList.remove(itemArrayAdapterTwo.getItem(i).getItemPath());
        }else {
            getListView().setItemChecked(i, true);
            selectedItemList.add(itemArrayAdapterTwo.getItem(i).getItemPath());
        }
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItemList.clear();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putInt("tabSelectedIndex", 1)
                .apply();
        try {
            fileWorkerTwo.selectedListItem(i, itemArrayAdapterTwo);

            itemArrayAdapterTwo = new ItemArrayAdapter(getActivity(), R.layout.fragment_list_item,
                    fileWorkerTwo.fillItemsList(
                            new File(fileWorkerTwo.getCurrentDir())
                    )
            );
            setListAdapter(itemArrayAdapterTwo);

            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putString("fragmentTwoCurrentDir", fileWorkerTwo.getCurrentDir())
                    .apply();
            StyleWorker.setStylePaneMode(getActivity());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "File openning ERROR!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        itemArrayAdapterTwo.switchSelection(i);
        getSelectedItems(i);

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putInt("tabSelectedIndex", 1)
                .apply();
        StyleWorker.setStylePaneMode(getActivity());

        return false;
    }

}
