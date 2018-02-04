package vasylenko.lightfilemanager;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

import vasylenko.lightfilemanager.adapter.DatabaseAdapter;
import vasylenko.lightfilemanager.fragment.FragmentBasic;
import vasylenko.lightfilemanager.fragment.FragmentOne;
import vasylenko.lightfilemanager.fragment.FragmentTwo;
import vasylenko.lightfilemanager.fragment.dialog.CopyItemDialog;
import vasylenko.lightfilemanager.fragment.dialog.CreateItemDialog;
import vasylenko.lightfilemanager.fragment.dialog.DeleteItemDialog;
import vasylenko.lightfilemanager.fragment.dialog.MoveItemDialog;
import vasylenko.lightfilemanager.worker.StyleWorker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String HEX_EDITOR = "vasylenko.lightfilemanager.HEX_EDITOR";

    private static FragmentBasic fragmentOne;
    private static FragmentBasic fragmentTwo;

    private TabLayout tabLayout;
    private static int tabSelectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button actionButton = (Button) findViewById(R.id.action_button);
        actionButton.setOnClickListener(this);

        Button hexButton = (Button) findViewById(R.id.hex_button);
        hexButton.setOnClickListener(this);

        Button moreButton = (Button)findViewById(R.id.more_button);
        moreButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            fragmentOne = (FragmentOne) getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragmentOne");
            fragmentTwo = (FragmentTwo) getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragmentTwo");
        } else {
            fragmentOne = new FragmentOne();
            fragmentTwo = new FragmentTwo();
        }

        getScreenOrientation();
        StyleWorker.setStylePaneMode(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragmentOne", fragmentOne);
        getSupportFragmentManager().putFragment(outState, "fragmentTwo", fragmentTwo);
    }

    private void getScreenOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setUpPortraitLayout();
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setUpLandscapeLayout();
    }

    private void saveGlobalState() {
        //Log.e("test", "DESTROY: "+tabLayout.getSelectedTabPosition());
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putInt("tabSelectedIndex", tabLayout.getSelectedTabPosition())
                .apply();
       /* globalStatePreference.edit()
                .putInt("tabSelectedIndex", tabLayout.getSelectedTabPosition())
                .apply();*/

    }

    // ------------------------------------------------------------------------------------------ //
    private void setUpPortraitLayout() {
        tabSelectedIndex = PreferenceManager.getDefaultSharedPreferences(this).getInt("tabSelectedIndex", 0);
        setupTabLayout(tabSelectedIndex);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment_one, fragmentOne);
        transaction.replace(R.id.container_fragment_two, fragmentTwo);
        transaction.commit();
    }

    private void setupTabLayout(int index) {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("A"));
        tabLayout.addTab(tabLayout.newTab().setText("B"));
        tabLayout.getTabAt(index).select();
        setCurrentTabFragment(index);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelectedIndex = tab.getPosition();
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                        .edit()
                        .putInt("tabSelectedIndex", tabSelectedIndex)
                        .apply();

                setCurrentTabFragment(tabSelectedIndex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(fragmentOne);
                break;
            case 1:
                replaceFragment(fragmentTwo);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FrameLayout containerFragmentOne = (FrameLayout) findViewById(R.id.container_fragment_one);
        FrameLayout containerFragmentTwo = (FrameLayout) findViewById(R.id.container_fragment_two);

        if (fragment instanceof FragmentOne) {
            containerFragmentTwo.setVisibility(View.GONE);
            containerFragmentOne.setVisibility(View.VISIBLE);
        } else if (fragment instanceof FragmentTwo) {
            containerFragmentOne.setVisibility(View.GONE);
            containerFragmentTwo.setVisibility(View.VISIBLE);
        }
    }

    // ------------------------------------------------------------------------------------------ //
    private void setUpLandscapeLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment_one, fragmentOne);
        transaction.replace(R.id.container_fragment_two, fragmentTwo);
        transaction.commit();
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onClick(View view) {
        // close existing dialog fragments
        FragmentManager manager = getFragmentManager();
        android.app.Fragment dialogFragment = manager.findFragmentByTag("dialog_fragment");
        if (dialogFragment != null) manager.beginTransaction().remove(dialogFragment).commit();

        switch (view.getId()) {
            case R.id.action_button:
                ItemsActionDialog itemsActionDialog = new ItemsActionDialog();
                itemsActionDialog.show(getSupportFragmentManager(), "dialog_fragment");
                break;

            case R.id.hex_button:
                openHexEditor();
                break;

            case R.id.more_button:
                ShowMoreDialog showMoreDialog = new ShowMoreDialog();
                showMoreDialog.show(getSupportFragmentManager(), "dialog_fragment");
                break;
        }
    }

    // ------------------------------------------------------------------------------------------ //
    private void openHexEditor(){
        tabSelectedIndex = PreferenceManager.getDefaultSharedPreferences(this).getInt("tabSelectedIndex", 0);
        if (tabSelectedIndex == 0) {
            if(fragmentOne.getSelectedPathCount() == 1) {
                Intent intent = new Intent(this, HexEditorActivity.class);
                intent.putExtra(HEX_EDITOR, fragmentOne.getSelectedPath().iterator().next());
                startActivity(intent);
            }else{
                Toast.makeText(this, "You must choose one file item!", Toast.LENGTH_SHORT).show();
            }
        } else if (tabSelectedIndex == 1) {
            if(fragmentTwo.getSelectedPathCount() == 1 ) {
                Intent intent = new Intent(this, HexEditorActivity.class);
                intent.putExtra(HEX_EDITOR, fragmentTwo.getSelectedPath().iterator().next());
                startActivity(intent);
            }else{
                Toast.makeText(this, "You must choose one file item!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ------------------------------------------------------------------------------------------ //
    public static class ItemsActionDialog extends DialogFragment implements View.OnClickListener,
            CreateItemDialog.CreateItemListener, DeleteItemDialog.DeleteItemListener,
            MoveItemDialog.MoveItemListener, CopyItemDialog.CopyItemListener {

        private DatabaseAdapter databaseAdapter;
        private Activity parentActivity;
        private Button createButton;
        private Button removeButton;
        private Button moveButton;
        private Button copyButton;

        public ItemsActionDialog() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_items_action, container);

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            createButton = (Button) view.findViewById(R.id.create_button);
            createButton.setOnClickListener(this);

            removeButton = (Button) view.findViewById(R.id.delete_button);
            removeButton.setOnClickListener(this);

            moveButton = (Button) view.findViewById(R.id.move_button);
            moveButton.setOnClickListener(this);

            copyButton = (Button) view.findViewById(R.id.copy_button);
            copyButton.setOnClickListener(this);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            parentActivity = (MainActivity) getActivity();
            databaseAdapter = new DatabaseAdapter(parentActivity);
        }

        @Override
        public void onClick(View view) {
            android.support.v4.app.FragmentManager manager = getFragmentManager();
            Fragment dialogFragment = manager.findFragmentByTag("dialog_fragment");
            if (dialogFragment != null) manager.beginTransaction().remove(dialogFragment).commit();

            switch (view.getId()){
                case R.id.create_button:
                    CreateItemDialog createItemDialog = CreateItemDialog.getInstance(ItemsActionDialog.this);
                    createItemDialog.show(getActivity().getFragmentManager(), "dialog_fragment");
                    break;
                case R.id.delete_button:
                    DeleteItemDialog deleteItemDialog = DeleteItemDialog.getInstance(ItemsActionDialog.this);
                    deleteItemDialog.show(getActivity().getFragmentManager(), "dialog_fragment");
                    break;
                case R.id.move_button:
                    MoveItemDialog moveItemDialog = MoveItemDialog.getInstance(ItemsActionDialog.this);
                    moveItemDialog.show(getActivity().getFragmentManager(), "dialog_fragment");
                    break;
                case R.id.copy_button:
                    CopyItemDialog copyItemDialog = CopyItemDialog.getInstance(ItemsActionDialog.this);
                    copyItemDialog.show(getActivity().getFragmentManager(), "dialog_fragment");
                    break;
            }
        }

        @Override
        public void onCreateItem(int item, String itemName) {
            tabSelectedIndex = PreferenceManager.getDefaultSharedPreferences(parentActivity).getInt("tabSelectedIndex", 0);
            try {
                if (tabSelectedIndex == 0) {
                    String fragmentOneCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                            .getString("fragmentOneCurrentDir", "/sdcard/");
                    if (fragmentOne.createItem(item, fragmentOneCurrentDir + "/", itemName)) {
                        // -------------------------------------------------------------------------
                        String currentState =  "'"+itemName + "' created in " + fragmentOneCurrentDir;
                        databaseAdapter.insertHistoryChanges(
                                currentState,
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, currentState, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(parentActivity, "Item not created!", Toast.LENGTH_SHORT).show();
                    }

                } else if (tabSelectedIndex == 1) {
                    String fragmentTwoCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                            .getString("fragmentTwoCurrentDir", "/sdcard/");
                    if (fragmentTwo.createItem(item, fragmentTwoCurrentDir + "/", itemName)) {
                        // -------------------------------------------------------------------------
                        String currentState =  "'"+itemName + "' created in " + fragmentTwoCurrentDir;
                        databaseAdapter.insertHistoryChanges(
                                currentState,
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, currentState, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(parentActivity, "Item not created!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (IOException io) {
                Toast.makeText(parentActivity, "Item create ERROR!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDeleteItem() {
            tabSelectedIndex = PreferenceManager.getDefaultSharedPreferences(parentActivity).getInt("tabSelectedIndex", 0);
            if (tabSelectedIndex == 0) {
                if(fragmentOne.getSelectedPathCount()>0) {
                    try {
                        fragmentOne.deleteItems();

                        StringBuilder itemNames = new StringBuilder();
                        for(String path : fragmentOne.getSelectedPath())
                            itemNames.append(path + ", ");

                        // -------------------------------------------------------------------------
                        String fragmentOneCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                                .getString("fragmentOneCurrentDir", "/sdcard/");
                        databaseAdapter.insertHistoryChanges(
                                "'" + itemNames + "' deleted from "+fragmentOneCurrentDir+"!",
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, "Item deleted SUCCESSFUL !", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(parentActivity, "Item delete ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(parentActivity, "You must choose 1 and more items!", Toast.LENGTH_SHORT).show();
                }

            } else if (tabSelectedIndex == 1) {
                if(fragmentTwo.getSelectedPathCount()>0) {
                    try {
                        fragmentTwo.deleteItems();

                        StringBuilder itemNames = new StringBuilder();
                        for(String path : fragmentTwo.getSelectedPath())
                            itemNames.append(path + ", ");

                        // -------------------------------------------------------------------------
                        String fragmentTwoCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                                .getString("fragmentTwoCurrentDir", "/sdcard/");
                        databaseAdapter.insertHistoryChanges(
                                "'" +itemNames+"' deleted from" +fragmentTwoCurrentDir+"!",
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, "Item deleted SUCCESSFUL !", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(parentActivity, "Item delete ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(parentActivity, "You must choose 1 and more items!", Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onMoveItem() {
            tabSelectedIndex = PreferenceManager.getDefaultSharedPreferences(parentActivity).getInt("tabSelectedIndex", 0);
            String fragmentOneCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                    .getString("fragmentOneCurrentDir", "/sdcard/");
            String fragmentTwoCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                    .getString("fragmentTwoCurrentDir", "/sdcard/");

            if (tabSelectedIndex == 0) {
                if(fragmentOne.getSelectedPathCount()>0){
                    try {
                        fragmentOne.moveItems(fragmentTwoCurrentDir);

                        StringBuilder itemNames = new StringBuilder();
                        for(String path : fragmentOne.getSelectedPath())
                            itemNames.append(path + ", ");

                        // -------------------------------------------------------------------------
                        databaseAdapter.insertHistoryChanges(
                                "'"+itemNames+"' moved to " + fragmentTwoCurrentDir,
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, "Item moved SUCCESSFUL !", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(parentActivity, "Item move ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(parentActivity, "You must choose 1 and more items!", Toast.LENGTH_SHORT).show();
                }

            } else if (tabSelectedIndex == 1) {
                if(fragmentTwo.getSelectedPathCount()>0) {
                    try {
                        fragmentTwo.moveItems(fragmentOneCurrentDir);

                        StringBuilder itemNames = new StringBuilder();
                        for(String path : fragmentTwo.getSelectedPath())
                            itemNames.append(path + ", ");

                        // -------------------------------------------------------------------------
                        databaseAdapter.insertHistoryChanges(
                                "'"+itemNames+"' moved to " + fragmentOneCurrentDir,
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, "Item moved SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(parentActivity, "Item move ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(parentActivity, "You must choose 1 and more items!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onCopyItem() {
            tabSelectedIndex = PreferenceManager.getDefaultSharedPreferences(parentActivity).getInt("tabSelectedIndex", 0);
            String fragmentOneCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                    .getString("fragmentOneCurrentDir", "/sdcard/");
            String fragmentTwoCurrentDir = PreferenceManager.getDefaultSharedPreferences(parentActivity)
                    .getString("fragmentTwoCurrentDir", "/sdcard/");

            if (tabSelectedIndex == 0) {
                if(fragmentOne.getSelectedPathCount()>0){
                    try {
                        fragmentOne.copyItems(fragmentTwoCurrentDir);

                        StringBuilder itemNames = new StringBuilder();
                        for(String path : fragmentOne.getSelectedPath())
                            itemNames.append(path + ", ");

                        // -------------------------------------------------------------------------
                        databaseAdapter.insertHistoryChanges(
                                "'"+itemNames+"' copied to " + fragmentTwoCurrentDir,
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, "Item copied SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(parentActivity, "Item copy ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(parentActivity, "You must choose 1 and more items!", Toast.LENGTH_SHORT).show();
                }

            } else if (tabSelectedIndex == 1) {
                if(fragmentTwo.getSelectedPathCount()>0) {
                    try {
                        fragmentTwo.copyItems(fragmentOneCurrentDir);

                        StringBuilder itemNames = new StringBuilder();
                        for(String path : fragmentTwo.getSelectedPath())
                            itemNames.append(path + ", ");

                        // -------------------------------------------------------------------------
                        databaseAdapter.insertHistoryChanges(
                                "'"+itemNames+"' moved to " + fragmentOneCurrentDir,
                                DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Toast.makeText(parentActivity, "Item copied SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(parentActivity, "Item copy ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(parentActivity, "You must choose 1 and more items!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------ //
    public static class ShowMoreDialog extends DialogFragment implements View.OnClickListener{
        private Activity parentActivity;
        private Button historyChangesButton;
        private Button settingsButton;
        private Button aboutButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_show_more, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            historyChangesButton = (Button) view.findViewById(R.id.history_changes_button);
            historyChangesButton.setOnClickListener(this);

            settingsButton = (Button) view.findViewById(R.id.settings_button);
            settingsButton.setOnClickListener(this);

            aboutButton = (Button) view.findViewById(R.id.about_button);
            aboutButton.setOnClickListener(this);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            parentActivity = (MainActivity) getActivity();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.history_changes_button:
                    openHistoryChanges();
                    break;
                case R.id.settings_button:
                    break;
                case R.id.about_button:
                    break;
            }
        }

        private void openHistoryChanges(){
            startActivity(new Intent(parentActivity , HistoryChangesActivity.class));
        }
    }
}
