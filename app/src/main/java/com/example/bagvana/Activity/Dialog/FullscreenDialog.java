package com.example.bagvana.Activity.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bagvana.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class FullscreenDialog extends DialogFragment {

    private Callback callback;
    private Chip chipRating, chipLowToHigh, chipHighToLow,
            chipColorBlack, chipColorWhite, chipColorBlue;
    private ArrayList<String> selectedChipData;

    private SeekBar seekBar;
    private TextView txtCount, txtApply, txtReset;
    private int progressValue = 0;
    private Toolbar toolbar;

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

        chipRating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                    chipRating.setChipStrokeWidth(0);
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                    chipRating.setChipStrokeWidth(3);
                }
            }
        });
        chipLowToHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                    chipLowToHigh.setChipStrokeWidth(0);
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                    chipLowToHigh.setChipStrokeWidth(3);
                }
            }
        });
        chipHighToLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                    chipHighToLow.setChipStrokeWidth(0);
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                    chipHighToLow.setChipStrokeWidth(3);
                }
            }
        });
        chipColorBlack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                    chipColorBlack.setChipStrokeWidth(0);
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                    chipColorBlack.setChipStrokeWidth(3);
                }
            }
        });
        chipColorWhite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                    chipColorWhite.setChipStrokeWidth(0);
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                    chipColorWhite.setChipStrokeWidth(3);
                }
            }
        });
        chipColorBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                    chipColorBlue.setChipStrokeWidth(0);
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                    chipColorBlue.setChipStrokeWidth(3);
                }
            }
        });

        seekBar.setProgress(0);
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
        txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressValue != 0) {
                    selectedChipData.add(String.valueOf(progressValue));
                }
                callback.onActionClick(selectedChipData.toString());
                dismiss();
            }
        });

        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipRating.setChecked(false);
                chipLowToHigh.setChecked(false);
                chipHighToLow.setChecked(false);
                chipColorBlack.setChecked(false);
                chipColorWhite.setChecked(false);
                chipColorBlue.setChecked(false);

                seekBar.setProgress(0);
                progressValue = 0;

            }
        });
        return view;
    }

    private void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface Callback {

        void onActionClick(String content);

    }

}