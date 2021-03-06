package com.example.matheus.wannaplay.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.matheus.wannaplay.Activities.LoginActivity;
import com.example.matheus.wannaplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.innovattic.rangeseekbar.RangeSeekBar;

public class PreferencesFragment extends Fragment {

    private TextView mDistanceTextView;
    private TextView mMinAgeTextView;
    private TextView mMaxAgeTextView;
    private CheckBox mVocalBtn, mGuitarBtn, mBassBtn, mDrumsBtn, mOthersBtn, mLogoutBtn;
    private Switch mNewMessagesSwitch;
    private SeekBar mDistanceSeekbar;
    private RangeSeekBar mAgeSeekbar;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Turns the fragment_preferences layout into a View
        View preferencesView = inflater.inflate(R.layout.fragment_preferences, container, false);

        //Loading Toggle Buttons
        mVocalBtn = preferencesView.findViewById(R.id.preferencesVocalCheckbox);
        mGuitarBtn = preferencesView.findViewById(R.id.preferencesGuitarCheckbox);
        mBassBtn = preferencesView.findViewById(R.id.preferencesBassCheckbox);
        mDrumsBtn = preferencesView.findViewById(R.id.preferencesDrumCheckbox);
        mOthersBtn = preferencesView.findViewById(R.id.preferencesOtherCheckbox);
        mLogoutBtn = preferencesView.findViewById(R.id.preferencesBtnLogout);
        mLogoutBtn.setChecked(true);

        //Loading Switches
        mNewMessagesSwitch = preferencesView.findViewById(R.id.preferencesSwitchMessages);

        /* --- Setting up the SeekBar --- */

        //Loading the component
        mDistanceSeekbar = preferencesView.findViewById(R.id.distance_seekbar);

        //Loading the tracking distance TextView label component
        mDistanceTextView = preferencesView.findViewById(R.id.txt_distance);

        //Setting up the maximum distance that can be covered by the filter
        mDistanceSeekbar.setMax(200);

        //Loading the component
        mAgeSeekbar = preferencesView.findViewById(R.id.age_seekbar);

        //Loading the tracking TextView labels for max and min age
        mMinAgeTextView = preferencesView.findViewById(R.id.txt_min_age);
        mMaxAgeTextView = preferencesView.findViewById(R.id.txt_max_age);

        //Distance SeekBar config
        mDistanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressChangedValue;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progressChangedValue = progress;
                mDistanceTextView.setText(String.valueOf(progressChangedValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mDistanceTextView.setText(String.valueOf(progressChangedValue));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDistanceTextView.setText(String.valueOf(progressChangedValue));
            }
        });

        //Age RangeBar config
        mAgeSeekbar.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {

            int minChangedValue;
            int maxChangedValue;

            @Override
            public void onValueChanged(int i, int i1) {
                minChangedValue = i;
                maxChangedValue = i1;
                mMinAgeTextView.setText(String.valueOf(minChangedValue));
                mMaxAgeTextView.setText(String.valueOf(maxChangedValue));
            }

            @Override
            public void onStoppedSeeking() {
                mMinAgeTextView.setText(String.valueOf(minChangedValue));
                mMaxAgeTextView.setText(String.valueOf(maxChangedValue));
            }

            @Override
            public void onStartedSeeking() {
                mMinAgeTextView.setText(String.valueOf(minChangedValue));
                mMaxAgeTextView.setText(String.valueOf(maxChangedValue));
            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogoutBtn.setChecked(false);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent (getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return preferencesView;

    }

    @Override
    public void onStart() {
        SharedPreferences preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getBoolean("vocal", true)) {
            mVocalBtn.setChecked(true);
        }

        if (preferences.getBoolean("guitar", true)) {
            mGuitarBtn.setChecked(true);
        }

        if (preferences.getBoolean("bass", true)) {
            mBassBtn.setChecked(true);
        }

        if (preferences.getBoolean("drums", true)) {
            mDrumsBtn.setChecked(true);
        }

        if (preferences.getBoolean("others", true)) {
            mOthersBtn.setChecked(true);
        }

        mDistanceTextView.setText(String.valueOf(preferences.getInt("distance", 100)));
        mDistanceSeekbar.setProgress(preferences.getInt("distance", 100));
        mMinAgeTextView.setText(String.valueOf(preferences.getInt("minAge", 18)));
        mMaxAgeTextView.setText(String.valueOf(preferences.getInt("maxAge", 45)));
        mAgeSeekbar.setMinThumbValue(preferences.getInt("minAge", 18));
        mAgeSeekbar.setMaxThumbValue(preferences.getInt("maxAge", 45));

        if (preferences.getBoolean("newMessages", false)) {
            mNewMessagesSwitch.setChecked(true);
        }

        super.onStart();
    }

    @Override
    public void onStop() {
        SharedPreferences preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("vocal", mVocalBtn.isChecked());
        editor.putBoolean("guitar", mGuitarBtn.isChecked());
        editor.putBoolean("bass", mBassBtn.isChecked());
        editor.putBoolean("drums", mDrumsBtn.isChecked());
        editor.putBoolean("others", mOthersBtn.isChecked());
        editor.putInt("distance", Integer.parseInt(mDistanceTextView.getText().toString()));
        editor.putInt("minAge", Integer.parseInt(mMinAgeTextView.getText().toString()));
        editor.putInt("maxAge", Integer.parseInt(mMaxAgeTextView.getText().toString()));
        editor.putBoolean("newMessages", mNewMessagesSwitch.isChecked());
        editor.apply();
        super.onStop();
    }
}
