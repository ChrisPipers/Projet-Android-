package fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static java.util.Arrays.asList;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.openclassroom.arrived.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatPersoFragment extends Fragment {

    private View mView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFUser;
    private FirebaseDatabase mFDatabase;
    private DatabaseReference mFRefDatabaseMeteo;
    private DatabaseReference mFRefDatabaseMode;
    private static final String TAG = "StatPersoFragment";
    private PieChart pieChartMode;
    private PieChart pieChartMeteo;
    private float[] valMode = {4f, 6f, 1f, 15f, 6f, 1f, 15f};
    private float[] valMeteo = {4f, 6f, 1f, 15f};
    private List<String> mListMode = asList("pieds", "voiture", "velo", "metro", "tram", "bus", "train");
    private List<String> mListMeteo = asList("sunny", "cloudy", "rain", "snow");
    private int mCptAllActivity;
    private RadioButton mRadioMode;
    private RadioButton mRadioMeteo;
    private RadioGroup mradiogroupe;
    private ArrayList<Integer> mColors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        mView = inflater.inflate(R.layout.fragment_personnal_stat, container, false);
        init();
        initFirebaseCo();
        initRepartition();
        onClicRadioBut();
        addDataSetMode();
        addDataSetMeteo();
        pieChartMode.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);
                for (int i = 0; i < valMode.length; i++) {
                    if (valMode[i] == Float.parseFloat(sales)) {
                        pos1 = i;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return mView;
    }

    private void init() {
        pieChartMode = (PieChart) mView.findViewById(R.id.pie_mode);
        pieChartMode.setHoleRadius(20f);
        pieChartMode.setTransparentCircleAlpha(0);
        pieChartMeteo = (PieChart) mView.findViewById(R.id.pie_type);
        pieChartMeteo.setHoleRadius(20f);
        pieChartMeteo.setTransparentCircleAlpha(0);
        pieChartMeteo.setVisibility(View.INVISIBLE);
        mRadioMode = (RadioButton) mView.findViewById(R.id.radio_mode);
        mRadioMeteo = (RadioButton) mView.findViewById(R.id.radio_meteo);
        mradiogroupe = (RadioGroup) mView.findViewById(R.id.radio_groupe_p);
        initListColor();
    }

    private void initFirebaseCo() {
        mAuth = FirebaseAuth.getInstance();
        mFUser = mAuth.getCurrentUser();
        final String UID = mFUser.getUid();
        mFDatabase = FirebaseDatabase.getInstance();
        mFRefDatabaseMode = mFDatabase.getReference("utilisateurs").child(mFUser.getUid()).child("mMapMode");
        mFRefDatabaseMeteo = mFDatabase.getReference("utilisateurs").child(mFUser.getUid()).child("mMapMeteo");
    }

    private void initRepartition() {
        mFRefDatabaseMode.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                Map<String, Integer> map = (Map<String, Integer>) dataSnapshot.getValue();
                if (map != null) {
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }


    private void onClicRadioBut() {
        mradiogroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mRadioMeteo.isChecked()) {
                    pieChartMode.setVisibility(View.INVISIBLE);
                    pieChartMeteo.setVisibility(View.VISIBLE);

                } else {
                    pieChartMeteo.setVisibility(View.INVISIBLE);
                    pieChartMode.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void addDataSetMode() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < valMode.length; i++) {
            yEntrys.add(new PieEntry(valMode[i], mListMode.get(i), i));
        }
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Moyen utiliser");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        pieDataSet.setColors(mColors);

        Legend l = pieChartMode.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setXEntrySpace(8);
        l.setYEntrySpace(7);


        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < valMeteo.length; i++) {
            xVals.add(mListMode.get(i));
        }


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChartMode.setData(pieData);
        pieChartMode.invalidate();

    }

    private void initListColor() {
        mColors = new ArrayList<>();
        mColors.add(Color.GRAY);
        mColors.add(Color.BLUE);
        mColors.add(Color.RED);
        mColors.add(Color.GREEN);
        mColors.add(Color.CYAN);
        mColors.add(Color.YELLOW);
        mColors.add(Color.MAGENTA);
    }

    private void addDataSetMeteo() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < valMeteo.length; i++) {
            yEntrys.add(new PieEntry(valMeteo[i], mListMeteo.get(i), i));
        }


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Meteo lors de déplacement");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);


        pieDataSet.setColors(mColors);

        Legend l = pieChartMeteo.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setXEntrySpace(8);
        l.setYEntrySpace(7);


        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < valMeteo.length; i++) {
            xVals.add(mListMeteo.get(i));
        }


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChartMeteo.setData(pieData);
        pieChartMeteo.invalidate();
    }
}

/*
        return mView;
    }




    private void setupPieChart() {

        List<PieEntry> pieEntries = new ArrayList<>();

        //for(int i = 0; i < )

        PieDataSet dataSet = new PieDataSet(pieEntries, "Transport utilisé");

    }


}
*/