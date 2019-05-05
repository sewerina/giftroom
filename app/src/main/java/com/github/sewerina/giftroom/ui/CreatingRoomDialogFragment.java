package com.github.sewerina.giftroom.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.github.sewerina.giftroom.R;

public class CreatingRoomDialogFragment extends DialogFragment {
    private static final String TAG = "CreatingRoomDialog";
    private EditText mNameEt;

    public static void showDialog(FragmentManager fragmentManager) {
        CreatingRoomDialogFragment dialogFragment = new CreatingRoomDialogFragment();
        dialogFragment.show(fragmentManager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final FragmentActivity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogfragment_creatingroom, null);
        mNameEt = view.findViewById(R.id.et_roomName);

        Drawable doneIcon = activity.getDrawable(R.drawable.ic_done);

        builder
                .setView(view)
                .setPositiveButtonIcon(doneIcon)
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = mNameEt.getText().toString();
                        if (name.isEmpty()) {
                            Toast.makeText(activity, "Необходимо указать название!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (activity instanceof MainActivity) {
                                ((MainActivity) activity).createRoom(name);
                            }
                        }
                    }
                });

        return builder.create();
    }


}
