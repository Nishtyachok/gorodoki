package ru.nishty.aai_referee.ui.secretary.players_list.region_list;

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

public class AddRegionDialogFragment extends DialogFragment {

    private OnRegionAddedListener listener;

    public interface OnRegionAddedListener {
        void onRegionAdded(String region);
    }

    public AddRegionDialogFragment(OnRegionAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_region, null);

        final EditText etRegionName = view.findViewById(R.id.etRegionName);
        Button btnAddRegion = view.findViewById(R.id.btnAddRegion);

        btnAddRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String region = etRegionName.getText().toString().trim();
                listener.onRegionAdded(region);
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}

