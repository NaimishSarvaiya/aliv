package com.iotsmartaliv.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * This class is used to set the dialog for displaying info after scanning.
 * Created by canopus on 2018-10-17.
 *
 * @author CanopusInfoSystems
 */
public class FormatSelectorDialogFragment extends DialogFragment {
    private ArrayList<Integer> mSelectedIndices;
    private FormatSelectorDialogListener mListener;

    public static FormatSelectorDialogFragment newInstance(FormatSelectorDialogListener listener, ArrayList<Integer> selectedIndices) {
        FormatSelectorDialogFragment fragment = new FormatSelectorDialogFragment();
        if (selectedIndices == null) {
            selectedIndices = new ArrayList<Integer>();
        }
        fragment.mSelectedIndices = new ArrayList<Integer>(selectedIndices);
        fragment.mListener = listener;
        return fragment;
    }

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mSelectedIndices == null || mListener == null) {
            dismiss();
            return null;
        }

        String[] formats = new String[ZXingScannerView.ALL_FORMATS.size()];
        boolean[] checkedIndices = new boolean[ZXingScannerView.ALL_FORMATS.size()];
        int i = 0;
        for (BarcodeFormat format : ZXingScannerView.ALL_FORMATS) {
            formats[i] = format.toString();
            checkedIndices[i] = mSelectedIndices.contains(i);
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Choose Format")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(formats, checkedIndices,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedIndices.add(which);
                                } else if (mSelectedIndices.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedIndices.remove(which);
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedIndices results somewhere
                        // or return them to the component that opened the dialog
                        if (mListener != null) {
                            mListener.onFormatsSaved(mSelectedIndices);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    public interface FormatSelectorDialogListener {
        void onFormatsSaved(ArrayList<Integer> selectedIndices);
    }
}
