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

public class MoveItemDialog extends DialogFragment {
    MoveItemListener moveItemListener;

    public interface MoveItemListener extends Serializable {
        void onMoveItem();
    }

    public static MoveItemDialog getInstance(MainActivity.ItemsActionDialog itemsActionDialog) {
        MoveItemDialog moveItemDialog = new MoveItemDialog();

        Bundle args = new Bundle();
        args.putSerializable("moveItemDialog", itemsActionDialog);
        moveItemDialog.setArguments(args);

        return moveItemDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moveItemListener = (MoveItemListener) getArguments().getSerializable("moveItemDialog");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Move items:")
                .setMessage("Do you really want move items?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        moveItemListener.onMoveItem();
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
