package codesages.mosaic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.Mosaic;
import codesages.mosaic.helpers.MosaicContact;
import codesages.mosaic.lists.ContactAdapter;
import codesages.mosaic.lists.MosaicContactAdapter;

public class contactsScreen extends AppCompatActivity {
    static String TAG = "ConatctScreen";
    Mosaic mosaic;
    int mosaicIndex;
    Context ctx;

    @Override
    protected void onResume() {
        super.onResume();
        getMosaic();
        if (mosaic != null) {
            if (mosaic.getContacts().size() > 0)
                setlist();
            else
                Toast.makeText(this, "No Contacts available in " + mosaic.getName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = this;

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
                Intent intent = new Intent(getApplicationContext(), ContactPickActivity.class);
                intent.putExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, mosaicIndex);
                startActivity(intent);
            }
        });


    }

    private void setViews() {
        TextView mosaicNameTv = (TextView) findViewById(R.id.contact_screen_mosaic_name_tv);
        mosaicNameTv.setText(mosaic.getName());

    }

    private void setlist() {
        ListView list = (ListView) findViewById(R.id.mosaic_contact_listview);
        MosaicContactAdapter adapter = new MosaicContactAdapter((Activity) ctx, mosaic.getContacts());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent,
                                    View view, int position, long id) {
                Toast.makeText(ctx, "Item Clicked: " + ((TextView) view.findViewById(R.id.mosaic_contact_list_row_name)).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ctx, "Item Long Clicked: " + ((TextView) view.findViewById(R.id.mosaic_contact_list_row_name)).getText().toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void getMosaic() {
        mosaicIndex = getIntent().getIntExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, 0);
        Log.d(TAG, "getMosaic: " + mosaicIndex);
        mosaic = CacheManager.getMosaicByPosition(this, mosaicIndex);
    }

}
