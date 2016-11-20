package codesages.mosaic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Contact;
import codesages.mosaic.helpers.ContactManager;
import codesages.mosaic.helpers.ContactNumbers;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.MosaicContact;
import codesages.mosaic.lists.ContactAdapter;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;

public class ContactPickActivity extends AppCompatActivity {

    CardArrayRecyclerViewAdapter mCardArrayAdapter;
    public ArrayList<Contact> contacts;
    Context ctx;
    String TAG = "ContactPickActivity";
    int selectedIndex = 0;
    int mosaicIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_pick);
        ctx = this;
        getMosaicIndex();
        setContacts();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                new getContacts().execute();

            } else {
                contacts = new ArrayList<>();
                showSweetAlertPermision();
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

            new getContacts().execute();

        }
    }

    private void setList() {

        ListView list = (ListView) findViewById(R.id.contact_listview);
        if (list != null) {
            ContactAdapter adapter = new ContactAdapter((Activity) ctx, contacts);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(android.widget.AdapterView<?> parent,
                                        View view, int position, long id) {
                    Log.d(TAG, "onItemClick: ");
                    showRadioButtonDialog(contacts.get(position));
                }
            });
        }
    }

    private void showRadioButtonDialog(final Contact contact) {
        // custom dialog
        Log.d(TAG, "showRadioButtonDialog: ");
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //builder.setView(R.layout.contact_dialog);
        builder.setTitle("Pick a Number");
        builder.setMultiChoiceItems(
                contact.getNumbers().getNumbersEmail().toArray(new CharSequence[contact.getNumbers().getNumbersEmail().size()]),
                new boolean[contact.getNumbers().getNumbersEmail().size()],
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedIndex = which;
                    }
                });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ListView lw = ((AlertDialog) dialog).getListView();
                SparseBooleanArray checkedItem = lw.getCheckedItemPositions();
                boolean added = AddContactToMosaic(checkedItem, contact);
                ShowSweetAlert(added, contact);
            }
        });
        builder.create().show();


    }

    private void ShowSweetAlert(boolean added, Contact contact) {
        if (added) {
            //Toast.makeText(ctx,
            //      " Added: " + contact.getNumbers().getNumbersEmail().get(selectedIndex), Toast.LENGTH_SHORT).show();
            new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success!")
                    .setContentText(" Added: " + contact.getNumbers().getNumbersEmail().get(selectedIndex))
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    })
                    .show();

        } else
            new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Fail!")
                    .setContentText(" Could not Add the contact! ")
                    .setConfirmText("Dismiss")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
    }

    private void showSweetAlertPermision() {
        new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Fail!")
                .setContentText("Until you grant the permission, we cannot display your Contacts")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .show();


    }

    private boolean AddContactToMosaic(SparseBooleanArray checkedItem, Contact contact) {

        ArrayList<String> emails = new ArrayList<String>();
        ArrayList<String> phonenums = new ArrayList<String>();
        if (!(checkedItem.size() > 0))
            return false;
        for (int i = 0; i < contact.getNumbers().getNumbersEmail().size(); i++) {
            if (checkedItem.get(i)) {
                if (contact.getNumbers().getNumbersEmail().get(i).contains("Email:")) {
                    String email = contact.getNumbers().getNumbersEmail().get(i);
                    String emailTrimmed = email.substring(email.indexOf("Email: ") + 7);
                    emails.add(emailTrimmed);

                } else {
                    phonenums.add(contact.getNumbers().getNumbersEmail().get(i));
                }
            }
        }

        MosaicContact mContact = new MosaicContact(contact.getName(), new ContactNumbers(phonenums, emails));
        return CacheManager.addMosaicContact(ctx, mosaicIndex, mContact);
    }

    public void getMosaicIndex() {
        mosaicIndex = getIntent().getIntExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, 0);
    }

    private class getContacts extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            setList();

        }

        @Override
        protected Void doInBackground(Void... params) {
            contacts = ContactManager.ReadPhoneContacts(getApplicationContext());

            return null;
        }
    }
}
