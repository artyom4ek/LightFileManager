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

public class FragmentOne extends FragmentBasic implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private Set<String> selectedItemList = new HashSet<String>();
    private String startDir;
    private ItemArrayAdapter itemArrayAdapterOne;
    private FileWorker fileWorkerOne;
    private File currentDir;

    public FragmentOne() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        startDir = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("fragmentOneCurrentDir", "/sdcard/");

        currentDir = new File(startDir);
        fileWorkerOne = new FileWorker(getActivity(), this);
        try {
            itemArrayAdapterOne = new ItemArrayAdapter(getActivity(), R.layout.fragment_list_item,
                    fileWorkerOne.fillItemsList(currentDir)
            );
        } catch (Exception e) {
            Toast.makeText(getActivity(), "List fill  ERROR!", Toast.LENGTH_SHORT).show();
        }
        setListAdapter(itemArrayAdapterOne);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one_list, container, false);
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
    public Set<String> getSelectedPath(){
        return  selectedItemList;
    }

    @Override
    public  int getSelectedPathCount(){
        return getSelectedPath().size();
    }

    private void getSelectedItems(int i){
        if(getListView().isItemChecked(i)) {
            getListView().setItemChecked(i, false);
            selectedItemList.remove(itemArrayAdapterOne.getItem(i).getItemPath());
        }else {
            getListView().setItemChecked(i, true);
            selectedItemList.add(itemArrayAdapterOne.getItem(i).getItemPath());
        }
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        itemArrayAdapterOne.switchSelection(i);
        getSelectedItems(i);

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putInt("tabSelectedIndex", 0)
                .apply();
        StyleWorker.setStylePaneMode(getActivity());

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItemList.clear();
        // FragmentOne.
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putInt("tabSelectedIndex", 0)
                .apply();

        // Update FragmentOne list.
        try {
            fileWorkerOne.selectedListItem(i, itemArrayAdapterOne);
            itemArrayAdapterOne = new ItemArrayAdapter(getActivity(), R.layout.fragment_list_item,
                    fileWorkerOne.fillItemsList(
                            new File(fileWorkerOne.getCurrentDir())
                    )
            );

            setListAdapter(itemArrayAdapterOne);

            // Save current dir of FragmentOne.
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putString("fragmentOneCurrentDir", fileWorkerOne.getCurrentDir())
                    .apply();
            StyleWorker.setStylePaneMode(getActivity());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "File opening ERROR!", Toast.LENGTH_SHORT).show();
        }

    }

}
