package codesages.mosaic;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codesages.mosaic.helpers.Contact;
import codesages.mosaic.helpers.ContactManager;
import codesages.mosaic.helpers.ContactNumbers;
import codesages.mosaic.lists.ContactAdapter;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

import static codesages.mosaic.helpers.ContactManager.ReadPhoneContacts;

public class ContactPickActivity extends AppCompatActivity {

    CardArrayRecyclerViewAdapter mCardArrayAdapter;
    public ArrayList<Contact> contacts;
    Context ctx;
    String TAG = "ContactPickActivity";
    int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_pick);
        ctx = this;
        setContacts();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                contacts = ContactManager.ReadPhoneContacts(getApplicationContext());
                setList();
            } else {
                contacts = new ArrayList<>();
                Toast.makeText(this, "Until you grant the permission, we canot display the your Contacts", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contacts = ContactManager.ReadPhoneContacts(getApplicationContext());
            setList();
        }
    }

    private void setList() {

        ListView list = (ListView) findViewById(R.id.contact_listview);

        ContactAdapter adapter = new ContactAdapter((Activity) ctx, contacts);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent,
                                    View view, int position, long id) {
                showRadioButtonDialog(contacts.get(position).getNumbers());
            }
        });
    }

    private void showRadioButtonDialog(final ContactNumbers nums) {
        // custom dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //builder.setView(R.layout.contact_dialog);
        builder.setTitle("Pick a Number");
        builder.setSingleChoiceItems(nums.getNumbers().toArray(new CharSequence[nums.getNumbers().size()]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex = which;
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ctx, " Added " + nums.getNumbers().get(selectedIndex), Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();


    }
    /*private void setList() {
        ArrayList<Card> cards = new ArrayList<Card>();


        mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getApplicationContext(), cards);

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) findViewById(R.id.carddemo_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }

        ArrayList<Card> _cards = initCard(contacts);
        updateAdapter(_cards);

    }*/

    private ArrayList<Card> initCard(ArrayList<Contact> contacts) {

        //Init an array of Cards
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            Card card = new Card(getApplicationContext());
            String title = "";

            //skip if all empty
            if (contact.getNumbers().getHome() == "" && contact.getNumbers().getPhone() == "" && contact.getNumbers().getWork() == "")
                continue;

            if (contact.getNumbers().getHome() != "")
                title += "Home " + contact.getNumbers().getHome();
            if (contact.getNumbers().getPhone() != "")
                title += "\nMobile " + contact.getNumbers().getPhone();
            if (contact.getNumbers().getWork() != "")
                title += "\nWork " + contact.getNumbers().getWork();

            card.setTitle(title);
            //Create a CardHeader
            CardHeader header = new CardHeader(getApplicationContext());

            //Set the header title
            header.setTitle(contact.getName());


            //Add a popup menu. This method set OverFlow button to visible
            header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    Toast.makeText(getApplicationContext(), "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            card.addCardHeader(header);


            //Add ClickListener
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getApplicationContext(), "Click Listener card=" + card.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            cards.add(card);
        }

        return cards;
    }

    private void updateAdapter(ArrayList<Card> cards) {
        if (cards != null) {
            mCardArrayAdapter.addAll(cards);
        }
    }
}
