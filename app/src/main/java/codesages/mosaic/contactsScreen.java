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

import cn.pedant.SweetAlert.SweetAlertDialog;
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
                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(mosaic.getName())
                        .setContentText("No contacts were added under this Mosaic, Press the \"+\" button below to Add some")
                        .show();
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
                // Toast.makeText(ctx, "Item Clicked: " + ((TextView) view.findViewById(R.id.mosaic_contact_list_row_name)).getText().toString(), Toast.LENGTH_SHORT).show();
                openMosaicContactDetails(mosaicIndex, position);
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

    private void openMosaicContactDetails(int mosaicIndex, int contactIndex) {
        Intent intent = new Intent(ctx, contactDetailScreen.class);
        intent.putExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, mosaicIndex);
        intent.putExtra(Keys.INTENT_EXTRA_SELECTED_CONTACT_INDEX, contactIndex);
        startActivity(intent);


    }

    private void getMosaic() {
        mosaicIndex = getIntent().getIntExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, 0);
        Log.d(TAG, "getMosaic: " + mosaicIndex);
        mosaic = CacheManager.getMosaicByPosition(this, mosaicIndex);
        for (int i = 0; i < mosaic.getContacts().size(); i++) {
            Log.d(TAG, "getMosaic: " + mosaic.getContacts().get(i).getName());
            for (int j = 0; j < mosaic.getContacts().get(i).getContactNumbers().getEmails().size(); j++) {
                Log.d(TAG, "getMosaic: Emails: " + mosaic.getContacts().get(i).getContactNumbers().getEmails().get(j));
            }
            for (int j = 0; j < mosaic.getContacts().get(i).getContactNumbers().getNumbers().size(); j++) {
                Log.d(TAG, "getMosaic: Phone: " + mosaic.getContacts().get(i).getContactNumbers().getNumbers().get(j));
            }

        }
    }

}
