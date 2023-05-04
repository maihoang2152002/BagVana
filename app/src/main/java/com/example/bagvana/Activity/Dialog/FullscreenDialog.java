package com.example.bagvana.Activity.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bagvana.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class FullscreenDialog extends DialogFragment  {

    private Callback callback;
    private Chip chipRating, chipLowToHigh, chipHighToLow,
            chipColorBlack, chipColorWhite, chipColorBlue;
    private Button btnApply, btnPlus, btnMinus;
    private ArrayList<String> selectedChipData;

    private ProgressBar progress;
    private TextView txtCount;
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
        btnApply = view.findViewById(R.id.btnApply);
        txtCount = view.findViewById(R.id.txt_count);
        progress = view.findViewById(R.id.progress);
        btnPlus = view.findViewById(R.id.btn_plus);
        btnMinus = view.findViewById(R.id.btn_minus);

        selectedChipData = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                }
            }
        };

        chipRating.setOnCheckedChangeListener(checkedChangeListener);
        chipLowToHigh.setOnCheckedChangeListener(checkedChangeListener);
        chipHighToLow.setOnCheckedChangeListener(checkedChangeListener);
        chipColorBlack.setOnCheckedChangeListener(checkedChangeListener);
        chipColorWhite.setOnCheckedChangeListener(checkedChangeListener);
        chipColorBlue.setOnCheckedChangeListener(checkedChangeListener);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressValue < 100) {
                    progressValue += 10;
                    progress.setProgress(progressValue);
                    txtCount.setText(String.valueOf(progressValue));
                }
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressValue > 0) {
                    progressValue -= 10;
                    progress.setProgress(progressValue);
                    txtCount.setText(String.valueOf(progressValue));
                }
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressValue != 0) {
                    selectedChipData.add(String.valueOf(progressValue));
                }
                callback.onActionClick(selectedChipData.toString());
                dismiss();
            }
        });
        return view;
    }
    private void setSupportActionBar(Toolbar toolbar_order) {
        toolbar_order.setNavigationIcon(R.drawable.ic_close);
        toolbar_order.setNavigationOnClickListener(new View.OnClickListener() {
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