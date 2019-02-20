package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.openclassroom.arrived.ModeAndMeteoActivity;
import com.openclassroom.arrived.R;

public class ChoiceFragment extends Fragment {

    private Button mButLocation;
    private Button mButConsultPers;
    //private Button mButConsultGlob;
    private View mView;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mView = inflater.inflate(R.layout.fragment_choix, container, false);
        init();
        initActionButton();
        initActionButton();
        return mView;
    }

    private void init() {
        mButLocation = mView.findViewById(R.id.but_enr_location);
        mButConsultPers = mView.findViewById(R.id.but_cons_pers);
        //mButConsultGlob = mView.findViewById(R.id.but_cons_glob);
    }

    private void initActionButton() {
        initLocalisationButton();
        initStatPersButton();
        initStatGlobButton();
    }

    private void initLocalisationButton() {
        this.mButLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModeAndMeteoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initStatPersButton() {

        this.mButConsultPers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatPersoFragment fm = new StatPersoFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new StatPersoFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void initStatGlobButton() {
        /*
        this.mButLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocalisationActivity.class);
                startActivity(intent);
            }
        });
        */
    }

}
