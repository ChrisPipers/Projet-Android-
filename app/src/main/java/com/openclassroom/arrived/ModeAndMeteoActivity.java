package com.openclassroom.arrived;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ModeAndMeteoActivity extends AppCompatActivity {

    private static final String TAG = "ModeAndMeteoActivity";

    private TextView mTextMode = null;
    private TextView mTextMeteo = null;
    private GridView mGridVMode = null;
    private GridView mGridVMeteo = null;
    private Button button_home_comfirmation = null;

    private static final String KEY_INDEX = "index";
    private int mCurrentIndex;
    private int nbItems;

    private ImageViewAdapterMode myAdapterMode = null;
    private ImageViewAdapterMeteo myAdapterMeteo = null;

    private String itemModeSel = null;

    private final static String ITEMS_MODE = "ITEMS_MODE";
    private final static String ITEMS_METEO = "ITEMS_METEO";

    private ArrayList<String> mListItemsModeSelected = null;
    private ArrayList<String> mListItemsMeteoSelected = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { //null si on vient de démarrer l’application
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        }

        setContentView(R.layout.activity_mode_and_meteo);
        init();
        onClicMGridMode();
        onClicMGridMeteo();
        onClicButComfirm();
    }

    private void init() {
        mTextMode = (TextView) findViewById(R.id.text_v_mode);
        mTextMeteo = (TextView) findViewById(R.id.text_v_meteo);
        mGridVMode = (GridView) findViewById(R.id.grid_view_mode);
        mGridVMeteo = (GridView) findViewById(R.id.grid_view_meteo);
        button_home_comfirmation = findViewById(R.id.button_home_comfirmation);
        mListItemsModeSelected = new ArrayList<>();
        mListItemsMeteoSelected = new ArrayList<>();
    }

    private void onClicMGridMode() {
        nbItems = 0;
        mGridVMode.setAdapter(new ImageViewAdapterMode(this));
        mGridVMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                float currentOpacity = v.getAlpha();
                Log.i(TAG, "test voit valor opacity " + currentOpacity);
                if (currentOpacity == 0.30f) {
                    myAdapterMode.setNbItemSelet(myAdapterMode.getNbItemSelet() + 1);

                    Toast.makeText(ModeAndMeteoActivity.this, " Selectionner ", Toast.LENGTH_SHORT).show();

                    v.setAlpha(1.0f);
                    mListItemsModeSelected.add((String) v.getTag());
//TODO mettre un setter d alpha pour aller plus vite
                    Log.i(TAG, "vleur du string " + (String) v.getTag());

                } else {
                    myAdapterMode.setNbItemSelet(myAdapterMode.getNbItemSelet() - 1);

                    Toast.makeText(ModeAndMeteoActivity.this, " Deselectionner ", Toast.LENGTH_SHORT).show();
                    v.setAlpha(0.30f);
                    mListItemsModeSelected.remove((String) v.getTag());
                }
                Log.i(TAG, "nb item select " + myAdapterMode.getNbItemSelet());
            }
        });
        myAdapterMode = new ImageViewAdapterMode(ModeAndMeteoActivity.this);
    }

    private void onClicMGridMeteo() {
        mGridVMeteo.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        mGridVMeteo.setAdapter(new ImageViewAdapterMeteo(this));
        mGridVMeteo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                float currentOpacity = v.getAlpha();
                Log.i(TAG, "test voit valor opacity " + currentOpacity);
                Log.i(TAG, "nb item select " + mGridVMeteo.getCheckedItemPositions().size() + "  " + mGridVMeteo.getCheckedItemCount() + " " + mGridVMeteo.getCount());
                if (currentOpacity == 0.30f && myAdapterMeteo.getmNbItemSelet() == 0) {
                    Toast.makeText(ModeAndMeteoActivity.this, " selectionner ", Toast.LENGTH_SHORT).show();
                    v.setAlpha(1.0f);
                    mListItemsMeteoSelected.add((String) v.getTag());
                    v.setSelected(true);
                    myAdapterMeteo.setmNbItemSelet(1);
                } else if (currentOpacity == 1.00f) {
                    myAdapterMeteo.setmNbItemSelet(0);
                    mListItemsMeteoSelected.remove((String) v.getTag());
                    Toast.makeText(ModeAndMeteoActivity.this, " deselectionner ", Toast.LENGTH_SHORT).show();
                    v.setAlpha(0.30f);
                    v.setSelected(false);

                } else {
                    Toast.makeText(ModeAndMeteoActivity.this, " not possible ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myAdapterMeteo = new ImageViewAdapterMeteo(ModeAndMeteoActivity.this);

    }

    private void onClicButComfirm() {
        button_home_comfirmation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ModeAndMeteoActivity.this);
                builder1.setMessage("Write your message here.");
                if (myAdapterMode.getNbItemSelet() == 0 || myAdapterMeteo.getmNbItemSelet() == 0) {
                    builder1.setMessage("Certains champs ne sont pas selectionner");
                    builder1.setCancelable(true);
                } else {
                    ArrayList<String> stringArrayList = getIntent().getStringArrayListExtra("SELECTED_LETTER");
                    Log.i(TAG, "string des trucs select  " + itemModeSel);
                    // TODO ajouter les tag des elem select dans l alert val select pieds voiture etc
                    // TODO creer une classe pour lancer l alert
                    String choix  =" mode : ";
                    for (int x = 0 ; x < mListItemsModeSelected.size(); x++)
                    {
                        choix += mListItemsModeSelected.get(x) + " ";
                    }
                    choix += "\n";
                    choix += " meteo : ";
                    for (int x = 0 ; x < mListItemsMeteoSelected.size(); x++)
                    {
                        choix += mListItemsMeteoSelected.get(x) + " ";
                    }





                    builder1.setMessage("Comfirmez vous votre selection ? "+ choix);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Comfimer selection",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intentItem = new Intent(ModeAndMeteoActivity.this, LocalisationActivity.class);
                                    intentItem.putExtra(ITEMS_MODE, mListItemsModeSelected);
                                    intentItem.putExtra(ITEMS_METEO, mListItemsMeteoSelected);
                                    startActivity(intentItem);
                                }
                            });
                    builder1.setNegativeButton(
                            "Retour",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                }
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        // outState.putParcelableArrayList(KEY_INDEX, myAdapterMeteo);
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private Menu m = null;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch(item.getItemId())
        {
            case R.id.item1:
                //Dans le Menu "m", on active tous les items dans le groupe d'identifiant "R.id.group2"
                m.setGroupEnabled(R.id.group2, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
        */
        return true;
    }


}
