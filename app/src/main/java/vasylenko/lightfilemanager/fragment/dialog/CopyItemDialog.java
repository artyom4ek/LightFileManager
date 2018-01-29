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

public class CopyItemDialog extends DialogFragment{
    CopyItemListener copyItemListener;

    public interface CopyItemListener extends Serializable {
        void onCopyItem();
    }

    public static CopyItemDialog getInstance(MainActivity.ItemsActionDialog itemsActionDialog) {
        CopyItemDialog copyItemDialog = new CopyItemDialog();

        Bundle args = new Bundle();
        args.putSerializable("copyItemDialog", itemsActionDialog);
        copyItemDialog.setArguments(args);

        return copyItemDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        copyItemListener = (CopyItemListener) getArguments().getSerializable("copyItemDialog");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Copy items:")
                .setMessage("Do you really want copy items?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        copyItemListener.onCopyItem();
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
