package com.example.contacts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
    private EditText editText;
    private Button okButton;

    public interface MyDialogListener {
        void onOkButtonClicked(String inputText);
    }

    private MyDialogListener listener;

    public void setListener(MyDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.my_dialog_layout, null);

        editText = view.findViewById(R.id.editText);
        okButton = view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOkButtonClicked(editText.getText().toString());
                }
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}

