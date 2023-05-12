package com.example.bagvana.Activity.Dialog;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bagvana.R;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FullscreenDialog extends DialogFragment {

    private Callback callback;
    private Chip chipRating, chipLowToHigh, chipHighToLow,
            chipColorBlack, chipColorWhite, chipColorBlue;
    private ArrayList<String> selectedChipData;

    private SeekBar seekBar;
    private TextView txtCount, txtApply, txtReset;
    private int progressValue = 0;
    private String sortFilter = "";
    private Toolbar toolbar;
    SharedPreferences preferences;

    public static FullscreenDialog newInstance() {
        return new FullscreenDialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container, false);

        toolbar = view.findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);

        chipRating = view.findViewById(R.id.chipRating);
        chipLowToHigh = view.findViewById(R.id.chipLowToHigh);
        chipHighToLow = view.findViewById(R.id.chipHighToLow);
        chipColorBlack = view.findViewById(R.id.chipColorBlack);
        chipColorWhite = view.findViewById(R.id.chipColorWhite);
        chipColorBlue = view.findViewById(R.id.chipColorBlue);
        txtApply = view.findViewById(R.id.txtApply);
        txtReset = view.findViewById(R.id.txtReset);
        txtCount = view.findViewById(R.id.txt_count);
        seekBar = view.findViewById(R.id.seekBar);

        selectedChipData = new ArrayList<>();

        preferences = getContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("colorFilter", "");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> myList = gson.fromJson(json, type);
        sortFilter = preferences.getString("sortFilter", "");
        progressValue = preferences.getInt("progressValue", 0);

        if (myList != null) {
            if (!myList.isEmpty()) {
                for (String str : myList) {
                    if (str.equals(chipColorBlack.getText().toString())) {
                        selectedChipData.add(str);
                        chipColorBlack.setChipStrokeWidth(0);
                        chipColorBlack.setChecked(true);
                    } else if (str.equals(chipColorWhite.getText().toString())) {
                        selectedChipData.add(str);
                        chipColorWhite.setChipStrokeWidth(0);
                        chipColorWhite.setChecked(true);
                    } else if (str.equals(chipColorBlue.getText().toString())) {
                        selectedChipData.add(str);
                        chipColorBlue.setChipStrokeWidth(0);
                        chipColorBlue.setChecked(true);
                    }
                }
            }
        }

        switch (sortFilter) {
            case "Rating":
                chipRating.setChipStrokeWidth(0);
                chipRating.setChecked(true);
                break;
            case "UpPrice":
                chipLowToHigh.setChipStrokeWidth(0);
                chipLowToHigh.setChecked(true);
                break;
            case "DownPrice":
                chipHighToLow.setChipStrokeWidth(0);
                chipHighToLow.setChecked(true);
                break;
        }

        chipRating.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sortFilter = "Rating";
                chipRating.setChipStrokeWidth(0);
            } else {
                sortFilter = "";
                chipRating.setChipStrokeWidth(3);
            }
        });
        chipLowToHigh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sortFilter = "UpPrice";
                chipLowToHigh.setChipStrokeWidth(0);
            } else {
                sortFilter = "";
                chipLowToHigh.setChipStrokeWidth(3);
            }
        });
        chipHighToLow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sortFilter = "DownPrice";
                chipHighToLow.setChipStrokeWidth(0);
            } else {
                sortFilter = "";
                chipHighToLow.setChipStrokeWidth(3);
            }
        });
        chipColorBlack.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedChipData.add(buttonView.getText().toString());
                chipColorBlack.setChipStrokeWidth(0);
            } else {
                selectedChipData.remove(buttonView.getText().toString());
                chipColorBlack.setChipStrokeWidth(3);
            }
        });
        chipColorWhite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedChipData.add(buttonView.getText().toString());
                chipColorWhite.setChipStrokeWidth(0);
            } else {
                selectedChipData.remove(buttonView.getText().toString());
                chipColorWhite.setChipStrokeWidth(3);
            }
        });
        chipColorBlue.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedChipData.add(buttonView.getText().toString());
                chipColorBlue.setChipStrokeWidth(0);
            } else {
                selectedChipData.remove(buttonView.getText().toString());
                chipColorBlue.setChipStrokeWidth(3);
            }
        });

        seekBar.setProgress(progressValue);
        txtCount.setText(String.valueOf(progressValue));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                txtCount.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        txtApply.setOnClickListener(v -> {

            callback.onActionClick(sortFilter, progressValue, selectedChipData);
            dismiss();
        });

        txtReset.setOnClickListener(v -> {
            chipRating.setChecked(false);
            chipLowToHigh.setChecked(false);
            chipHighToLow.setChecked(false);
            chipColorBlack.setChecked(false);
            chipColorWhite.setChecked(false);
            chipColorBlue.setChecked(false);

            seekBar.setProgress(0);
            progressValue = 0;

        });
        return view;
    }

    private void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(view -> dismiss());
    }

    public interface Callback {

        void onActionClick(String sortFilter, int progressValue, ArrayList<String> colorFilter);

    }

}