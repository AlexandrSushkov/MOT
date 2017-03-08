package dev.nelson.mot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.nelson.mot.R;


public class AboutFragment extends Fragment {

    @BindView(R.id.eastern_egg_19)
    TextView mEasternEgg;
    private int counter = 0;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.eastern_egg_19)
    void onClickEasternEgg(){
        counter++;
        if(counter == 8){
            Toast.makeText(view.getContext(), "Congratulation!!! You have found eastern egg 19/25", Toast.LENGTH_SHORT).show();
            counter = 3;
        }
    }
}