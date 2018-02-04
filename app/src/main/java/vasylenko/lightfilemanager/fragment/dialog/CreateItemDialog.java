package vasylenko.lightfilemanager.fragment.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;

import vasylenko.lightfilemanager.MainActivity;
import vasylenko.lightfilemanager.R;

public class CreateItemDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText itemEditText;
    private RadioGroup createItemRadioGroup;
    private CreateItemListener createItemListener;

    public CreateItemDialog() {
    }

    public interface CreateItemListener extends Serializable{
        void onCreateItem(int item, String itemName);
    }

    public static CreateItemDialog getInstance(MainActivity.ItemsActionDialog itemsActionDialog) {
        CreateItemDialog createItemDialog = new CreateItemDialog();

        Bundle args = new Bundle();
        args.putSerializable("createItemDialog", itemsActionDialog);
        createItemDialog.setArguments(args);

        return createItemDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_item, container);

        createItemListener = (CreateItemListener) getArguments().getSerializable("createItemDialog");

        itemEditText = (EditText) view.findViewById(R.id.item_edit_text);
        createItemRadioGroup = (RadioGroup) view.findViewById(R.id.create_item_radio_group);

        itemEditText.setOnEditorActionListener(this);
        itemEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Enter item name:");
        getDialog().getWindow().setBackgroundDrawableResource(R.color.colorBackgroundBasic);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        int radioButtonID = createItemRadioGroup.getCheckedRadioButtonId();
        View radioButton = createItemRadioGroup.findViewById(radioButtonID);
        int idx = createItemRadioGroup.indexOfChild(radioButton);

        /* CreateItemListener activity = (CreateItemListener) getActivity();
        activity.onCreateItem(idx, itemEditText.getText().toString() );*/
        createItemListener.onCreateItem(idx, itemEditText.getText().toString());
        this.dismiss();

        return true;
    }
}
