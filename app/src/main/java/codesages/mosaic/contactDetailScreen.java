package codesages.mosaic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.beardedhen.androidbootstrap.BootstrapButton;

import cn.pedant.SweetAlert.SweetAlertDialog;
import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.MosaicContact;

public class contactDetailScreen extends AppCompatActivity {
    static String TAG = "contactDetailScreen";
    Context ctx;
    int mosaicIndex;
    int contactIndex;
    MosaicContact contact;
    int dialogSelectedIndex;
    int newFrequency;

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: New" + newFrequency + " old: " + contact.getFrequencyInDays());
        if (contact.getFrequencyInDays() != newFrequency) {
            //Toast.makeText(ctx, "Freqency Changed", Toast.LENGTH_SHORT).show();
            CacheManager.updateMosaicContact(ctx, mosaicIndex, contactIndex, newFrequency);
        }
        /*else
            Toast.makeText(ctx, "Frequency is still the Same", Toast.LENGTH_SHORT).show();
            */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ctx = this;

        getContact();

        if (contact == null) {
            Toast.makeText(ctx, "Error in Opening The Contact", Toast.LENGTH_SHORT).show();
            finish();
        }
        getSupportActionBar().setTitle(contact.getName().toUpperCase());
        setViews();
    }

    private void setViews() {
        newFrequency = contact.getFrequencyInDays();
        Log.d(TAG, "setViews: Freq= " + newFrequency);
        ImageView initImage = (ImageView) findViewById(R.id.mosaic_contact_details_img);
        TextView nameTv = (TextView) findViewById(R.id.mosaic_contact_details_name);
        TextView lastContactedOn = (TextView) findViewById(R.id.mosaic_contact_details_contacted_on);
        final TextView freqTv = (TextView) findViewById(R.id.mosaic_contact_details_frequency_label);

        BootstrapButton phoneBtn = (BootstrapButton) findViewById(R.id.mosaic_contact_details_phone_btn);
        BootstrapButton emailBtn = (BootstrapButton) findViewById(R.id.mosaic_contact_details_email_btn);
        BootstrapButton textBtn = (BootstrapButton) findViewById(R.id.mosaic_contact_details_sms_btn);
        SeekBar seekBar = (SeekBar) findViewById(R.id.mosaic_contact_details_seek_bar);

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("EEE MMM yyyy hh:mm");
        String initials = getInitials();

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(contact.getName());
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(initials.toUpperCase(), color);
        initImage.setImageDrawable(drawable);

        nameTv.setText(contact.getName());
        lastContactedOn.setText(contact.getLastCall() == null ? "Unknown" : dateFormat.format(contact.getLastCall()));
        freqTv.setText("" + contact.getFrequencyInDays());
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(ActionType.CALL);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(ActionType.EMAIL);
            }
        });

        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(ActionType.SMS);
            }
        });


        seekBar.setProgress(contact.getFrequencyInDays());
        newFrequency = contact.getFrequencyInDays();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress != 0) {
                    newFrequency = progress;
                    freqTv.setText(progress + "");
                } else {
                    seekBar.setProgress(1);
                    newFrequency = 1;
                    freqTv.setText("" + 1);

                }
                Log.d(TAG, "onProgressChanged: Freq=" + newFrequency);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void createDialog(final ActionType actionType) {
        CharSequence[] items;
        if (actionType == ActionType.CALL || actionType == ActionType.SMS) {
            items = contact.getContactNumbers()
                    .getNumbers()
                    .toArray(new CharSequence[contact.getContactNumbers().getNumbers().size()]);
        } else {
            items = contact.getContactNumbers()
                    .getEmails()
                    .toArray(new CharSequence[contact.getContactNumbers().getEmails().size()]);
        }
        if (items.length == 0) {
            new SweetAlertDialog(ctx)
                    .setTitleText("Oops")
                    .setContentText("The Selected Contact doesn't have any " +
                            (actionType == ActionType.CALL || actionType == ActionType.SMS ? "Numbers" : "Emails"))
                    .show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogSelectedIndex = which;
                switch (actionType) {
                    case CALL:
                        dialog.dismiss();
                        callAction(contact.getContactNumbers().getNumbers().get(which));
                        break;
                    case EMAIL:
                        dialog.dismiss();
                        emailAction(contact.getContactNumbers().getEmails().get(which));
                        break;
                    case SMS:
                        dialog.dismiss();
                        smsAction(contact.getContactNumbers().getNumbers().get(which));
                        break;
                }
            }
        });
        builder.setTitle(actionType == ActionType.CALL || actionType == ActionType.SMS ? "Select a Number" : "Select an Email");
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contactdetails_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.contact_detail_edit_option)
            Toast.makeText(ctx, "Open Edit Activity", Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.contact_detail_delete_option) {
            createAlertSweetAlert();
        }


        return super.onOptionsItemSelected(item);
    }

    private void callAction(String number) {
        Log.d(TAG, "callAction: " + number);
        String uri = "tel:" + number;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void smsAction(String number) {
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
    }

    private void getContact() {
        mosaicIndex = getIntent().getIntExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, -1);
        contactIndex = getIntent().getIntExtra(Keys.INTENT_EXTRA_SELECTED_CONTACT_INDEX, -1);
        if (mosaicIndex != -1 && contactIndex != -1) {

            contact = CacheManager.getMosaicContact(ctx, mosaicIndex, contactIndex);
        }
    }

    public String getInitials() {
        String[] initials = contact.getName().split(" ");
        String init = "M";
        if (initials.length > 0) {
            init = initials[0].substring(0, 1);
            if (initials.length > 1) {

                init += initials[1].substring(0, 1);
            }
        }
        return init;
    }

    public void createAlertSweetAlert() {
        new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Hold The Phone!")
                .setContentText("Do You Really Want to Delete This Contact?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        if (CacheManager.deleteMosaicContact(ctx, mosaicIndex, contactIndex)) {
                            createSucessSweetAlert();

                        } else
                            Toast.makeText(ctx, "Oops, Something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                }).show();

    }

    public void createSucessSweetAlert() {
        new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(":)")
                .setContentText("Deleted Successfully.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .show();


    }

    public enum ActionType {CALL, EMAIL, SMS}

}
