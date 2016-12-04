package codesages.mosaic;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Cache;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.Mosaic;
import codesages.mosaic.lists.MosaicAdapter;
import codesages.mosaic.receiver.IncomingCallsReceiver;
import codesages.mosaic.receiver.OutgoingCallsReceiver;
import codesages.mosaic.receiver.OutgoingSMSObserver;
import codesages.mosaic.receiver.ReceiversHelper;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;

public class mosaicsListScreen extends AppCompatActivity {

    static final Uri SMS_STATUS_URI = Uri.parse("content://sms");
    static final String TAG = "MosiacListScreen";
    private ArrayList<Mosaic> mosaicArrayList = new ArrayList<>();
    Context ctx;
    static boolean isAlertShown = false;

    @Override
    protected void onResume() {
        super.onResume();
        setMosaicList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaics_list_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setMosaicList();

        ctx = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createMosaicButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MosaicCreationScreen.class);
                startActivity(intent);
            }
        });

        checkCallsPermissions();

        //if (!CacheManager.getOugoingSMSObserverFlag(ctx)) {
        startougoingSMSObserver();
        //}
    }

    private void setMosaicList() {
        mosaicArrayList = CacheManager.getMosaicFromCache(getApplicationContext());
        setList();

        if (!(mosaicArrayList.size() > 0) && !isAlertShown) {
            isAlertShown = true;
            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Mosaic")
                    .setContentText("No Mosaics yet, Press the \"+\" button below to Add some")
                    .show();
        }

    }

    private void setList() {


        ListView list = (ListView) findViewById(R.id.mosaic_listview);

        MosaicAdapter adapter = new MosaicAdapter(mosaicArrayList, (Activity) this);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent,
                                    View view, int position, long id) {
                Log.d(TAG, "onClick: " + position);
                Intent intent = new Intent(getApplicationContext(), contactsScreen.class);
                intent.putExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, position);
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String mname = ((TextView) view.findViewById(R.id.mosaic_list_row_txt)).getText().toString();
                Toast.makeText(mosaicsListScreen.this, "LongClick " + mname, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == 101) {
            //101 outgoing calls
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted 101");
                    ReceiversHelper.enableOutgoingReceiver(ctx, true);
                    checkCallsPermissions();
                } else {
                    showPermissionSweetAlert(101);
                }
            }

        } else if (requestCode == 103) {
            //103 READ SMS
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted 103/4");
                    ReceiversHelper.enableInComingSMSReceiver(ctx, true);
                    checkCallsPermissions();
                } else {
                    showPermissionSweetAlert(103);
                }
            }
        }

    }

    private void checkCallsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.PROCESS_OUTGOING_CALLS,
                                Manifest.permission.READ_PHONE_STATE}
                        , 101);
            }
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.READ_SMS,
                                Manifest.permission.RECEIVE_SMS},
                        103);
            }

        }
    }

    public void showPermissionSweetAlert(final int flag) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Hold On!")
                .setContentText("We will not be able to monitor the Calls/SMS!")
                .setConfirmText("Allow it")
                .setCancelText("I Don't Care!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (flag == 101)
                            ReceiversHelper.enableOutgoingReceiver(ctx, false);
                        else if (flag == 102)
                            ReceiversHelper.enableInComingReceiver(ctx, false);
                        else if (flag == 103)
                            ReceiversHelper.enableInComingSMSReceiver(ctx, false);
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        checkCallsPermissions();
                    }
                })
                .show();
    }

    public void startougoingSMSObserver() {
        Log.d(TAG, "startougoingSMSObserver: ");
        OutgoingSMSObserver smsSentObserver = new OutgoingSMSObserver(ctx, new Handler());
        ctx.getContentResolver().registerContentObserver(SMS_STATUS_URI, true, smsSentObserver);
        CacheManager.setOugoingSMSObserverFlag(ctx, true);

    }


}

