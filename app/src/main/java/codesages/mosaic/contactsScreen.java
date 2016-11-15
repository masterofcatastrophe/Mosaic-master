package codesages.mosaic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.Mosaic;

public class contactsScreen extends AppCompatActivity {
    static String TAG = "ConatctScreen";
    Mosaic mosaic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getMosaic();
        if (mosaic != null) {
            setViews();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addContactsID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Choose the contact from your phonebook", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(getApplicationContext(), ContactPickActivity.class));
            }
        });

        //This is temporary for prototype purposes only
        ImageButton AshleyButton = (ImageButton) findViewById(R.id.AshleyButton);
        AshleyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), contactDetailScreen.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton goBacktoMosaicScreenButton = (ImageButton) findViewById(R.id.goBacktoMosaicScreenButton);
        goBacktoMosaicScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mosaicsListScreen.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton editMosaicButton = (ImageButton) findViewById(R.id.editMosaicButton);
        editMosaicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), editMosaic.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setViews() {
        TextView mosaicNameTv = (TextView) findViewById(R.id.contact_screen_mosaic_name_tv);
        mosaicNameTv.setText(mosaic.getName());
    }

    private void getMosaic() {
        int index = getIntent().getIntExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, 0);
        Log.d(TAG, "getMosaic: " + index);
        mosaic = CacheManager.getMosaicByPosition(this, index);
    }

}
