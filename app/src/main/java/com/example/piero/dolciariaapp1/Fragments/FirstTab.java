package com.example.piero.dolciariaapp1.Fragments;



import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewFlipper;



import com.example.piero.dolciariaapp1.R;




/**
 * A simple {@link Fragment} subclass.
 */
public class FirstTab extends Fragment {




 ViewFlipper viewFlipper;
 public static int varA = 1;




    public FirstTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewFlipper = view.findViewById(R.id.viewFlipper);



        if (android.os.Build.VERSION.SDK_INT >= 11) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            slideShow();
        }






        switch (varA) {
            case 1:
                getFragmentManager().beginTransaction().add(R.id.adapter, new LoginRegisterBtn()).commit();
                break;
            case 2:
                getFragmentManager().beginTransaction().replace(R.id.adapter, new LoggedUser()).commit();
                break;
            case 3:
                getFragmentManager().beginTransaction().replace(R.id.adapter, new LoggedAdmin()).commit();
                break;
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_first_tab, container, false);

    }


    public void slideShow() {
        int images[] = {R.drawable.imm1, R.drawable.imm2,R.drawable.imm3,R.drawable.imm4,R.drawable.imm5,
        };

        for(int image : images) {
            flipImages(image);
        }

    }


    public void flipImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        viewFlipper.startFlipping();

        //animation
        viewFlipper.setInAnimation(getContext(), android.R.anim.fade_in);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.fade_out);

    }


}
