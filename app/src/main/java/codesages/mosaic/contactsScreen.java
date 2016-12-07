package codesages.mosaic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
            else {
                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(mosaic.getName())
                        .setContentText("No contacts were added under this Mosaic, Press the \"+\" button below to Add some")
                        .show();
                setlist();
            }
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
            getSupportActionBar().setTitle(mosaic.getName().toUpperCase());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mosaic_contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mosaic_contact_email_all_option:
                break;
            case R.id.mosaic_contact_text_all_option:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    /*private void smsAction() {
        //mosaic.getContacts().s
        Log.d(TAG, "smsAction: " + number);
        Uri uri = Uri.parse("smsto:" + number);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "Hi, ");
        startActivity(it);
    }
    private void emailAction(String email) {
        Log.d(TAG, "emailAction: " + email);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Mosaic: been a long time");
        intent.putExtra(Intent.EXTRA_TEXT, "Sent via Mosaic.");
        startActivity(intent);
    }*/
}
