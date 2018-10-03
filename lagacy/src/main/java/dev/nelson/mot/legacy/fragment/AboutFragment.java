package dev.nelson.mot.legacy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dev.nelson.mot.R;

public class AboutFragment extends Fragment {

    public static final String FRAGMENT_TAG = AboutFragment.class.getName();

    TextView mEasternEgg;
    private int counter = 0;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        mEasternEgg = view.findViewById(R.id.eastern_egg_19);
        mEasternEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter == 8){
                    Toast.makeText(view.getContext(), "Congratulation!!! You have found easter egg 19/25", Toast.LENGTH_SHORT).show();
                    counter = 3;
                }
            }
        });
        return view;
    }
}
