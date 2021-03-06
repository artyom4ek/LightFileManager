package vasylenko.lightfilemanager.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import vasylenko.lightfilemanager.MainActivity;

public class DeleteItemDialog extends DialogFragment {
    DeleteItemListener deleteItemListener;

    public interface DeleteItemListener extends Serializable {
        void onDeleteItem();
    }

    public static DeleteItemDialog getInstance(MainActivity.ItemsActionDialog itemsActionDialog) {
        DeleteItemDialog deleteItemDialog = new DeleteItemDialog();

        Bundle args = new Bundle();
        args.putSerializable("deleteItemDialog", itemsActionDialog);
        deleteItemDialog.setArguments(args);

        return deleteItemDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        deleteItemListener = (DeleteItemListener) getArguments().getSerializable("deleteItemDialog");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete items:")
                .setMessage("Do you really want delete items?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItemListener.onDeleteItem();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();

        return builder.create();
    }

}
