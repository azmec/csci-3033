package org.csci.mealmanual.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ConfirmTransferDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Transfer")
                .setMessage("Do you want to keep the groceries in the list or clear it before transferring to the pantry?")
                .setPositiveButton("Keep", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User wants to keep the groceries, handle the action
                        dismiss();
                    }
                })
                .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User wants to clear the groceries, handle the action
                        GroceryViewModel groceryViewModel = new ViewModelProvider(requireActivity()).get(GroceryViewModel.class);
                        List<String> emptyList = new ArrayList<String>();
                        groceryViewModel.setData(emptyList);
                        dismiss(); // Close the dialog
                    }
                });

        return builder.create();
    }
}
