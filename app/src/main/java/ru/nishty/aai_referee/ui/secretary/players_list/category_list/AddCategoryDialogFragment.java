package ru.nishty.aai_referee.ui.secretary.players_list.category_list;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import ru.nishty.aai_referee.R;

public class AddCategoryDialogFragment extends DialogFragment {

    private OnCategoryAddedListener listener;

    public interface OnCategoryAddedListener {
        void onCategoryAdded(String category);
    }

    public AddCategoryDialogFragment(OnCategoryAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);

        final EditText etCategoryName = view.findViewById(R.id.etCategoryName);
        Button btnAddCategory = view.findViewById(R.id.btnAddCategory);

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = etCategoryName.getText().toString().trim();
                listener.onCategoryAdded(category);
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}

