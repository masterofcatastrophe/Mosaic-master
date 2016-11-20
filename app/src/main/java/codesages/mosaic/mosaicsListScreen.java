package codesages.mosaic;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.Mosaic;
import codesages.mosaic.lists.MosaicAdapter;
import codesages.mosaic.receiver.OutgoingCallsReceiver;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;

public class mosaicsListScreen extends AppCompatActivity {

    public static final String THUMB_IDS = "";
    public static final String TAG = "MosiacListScreen";
    private GridView mosaicList;
    private ArrayList<Integer> mThumbIds;
    private ArrayList<Mosaic> mosaicArrayList = new ArrayList<>();
    CardArrayRecyclerViewAdapter mCardArrayAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        mosaicArrayList = CacheManager.getMosaicFromCache(getApplicationContext());
        if (!(mosaicArrayList.size() > 0)) {
            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Mosaic")
                    .setContentText("No Mosaics yet, Press the \"+\" button below to Add some")
                    .show();
        }


        setList();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaics_list_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createMosaicButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MosaicCreationScreen.class);
                startActivity(intent);
            }
        });

        checkOutgoingCallPermission();
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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                enableOutgoingReceiver(true);
            } else {
                showPermissionSweetAlert();
            }
        }
    }

    private void checkOutgoingCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 101);
        }
    }

    public void showPermissionSweetAlert() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Hold On!")
                .setContentText("We will not be able to monitor the calls!")
                .setConfirmText("Grant Permission")
                .setCancelText("I Don't Care!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        enableOutgoingReceiver(false);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        checkOutgoingCallPermission();
                    }
                })
                .show();
    }

    public void enableOutgoingReceiver(boolean enable) {
        int flag = (enable ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);

        ComponentName component = new ComponentName(mosaicsListScreen.this, OutgoingCallsReceiver.class);

        getPackageManager().setComponentEnabledSetting(
                component,
                flag,
                PackageManager.DONT_KILL_APP
        );
    }
}

