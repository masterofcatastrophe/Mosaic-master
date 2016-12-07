package codesages.mosaic.lists;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import codesages.mosaic.MosaicCreationScreen;
import codesages.mosaic.R;
import codesages.mosaic.contactsScreen;
import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.ImageManager;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.Mosaic;

/**
 * Created by DELL on 11/11/2016.
 */

public class MosaicAdapter extends BaseAdapter implements SectionIndexer {

    HashMap<String, Integer> mapIndex;
    String[] sections;
    final String TAG = "MosaicAdapter";
    private Activity mContext;
    int[] colors = {Color.MAGENTA, Color.BLUE, Color.LTGRAY, Color.RED, Color.DKGRAY, Color.GRAY};
    private ArrayList<Mosaic> mosaics;


    public MosaicAdapter(
            ArrayList<Mosaic> mosaics, Activity context) {
        mContext = context;
        this.mosaics = mosaics;
    }

    private void PrepareSections() {

        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < mosaics.size(); i++) {
            Mosaic mosaic = mosaics.get(i);
            String ch = mosaic.getName().substring(0, 1);
            ch = ch.toUpperCase(Locale.US);

            mapIndex.put(ch, i);
        }

        Set<String> sectionLetters = mapIndex.keySet();

        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

        Collections.sort(sectionList);

        sections = new String[sectionList.size()];

        sectionList.toArray(sections);

    }

    @Override
    public int getCount() {

        return mosaics.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mosaic_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.mosaic_list_row_img);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.mosaic_list_row_txt);
            viewHolder.btn = (ImageButton) convertView.findViewById(R.id.mosaic_list_row_imgbtn);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt.setText(mosaics.get(position).getName());
        if (mosaics.get(position).getImagePath() != null) {
            if (mosaics.get(position).getImagePath().isImageFromGoogleDrive()) {
                Log.d(TAG, "getView: get Image from Google with " + mosaics.get(position).getImagePath().getStringUri());
                Picasso.with(mContext)
                        .load(Uri.parse(mosaics.get(position).getImagePath().getStringUri()))
                        //.placeholder(R.drawable.progress_animation)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(viewHolder.img);

            } else {

                Picasso.with(mContext)
                        .load(new File(mosaics.get(position).getImagePath().getPath()))
                        //.placeholder(R.drawable.progress_animation)
                        .error(android.R.drawable.ic_menu_report_image)
                        .resize(0, 500)
                        .into(viewHolder.img);
            }
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.family)
                    //.placeholder(R.drawable.progress_animation)
                    .error(R.drawable.family)
                    .resize(0, 500)
                    .into(viewHolder.img);

        }
        viewHolder.btn.setFocusable(false);
        viewHolder.btn.setFocusableInTouchMode(false);
        viewHolder.btn.setClickable(true);

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which == 0) {
                            Toast.makeText(mContext, "Edit", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, MosaicCreationScreen.class);
                            intent.putExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, position);
                            intent.putExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_IS_EDIT, true);
                            mContext.startActivity(intent);
                        } else {
                            createAlertSweetAlert(position);
                        }

                    }
                });
                builder.show();
            }
        });

        return convertView;
    }


    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {
        String sch = sections[section];
        for (int i = 0; i < mosaics.size(); i++) {
            String ch = mosaics.get(i).getName().substring(0, 1);
            if (sch.equals(ch)) {
                return i;
            }
        }
        return (int) (getCount() * ((float) section / (float) getSections().length));
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public class ViewHolder {
        public ImageView img;
        public TextView txt;
        public ImageButton btn;
    }

    public void createAlertSweetAlert(final int position) {
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Hold The Phone!")
                .setContentText("Do You Really Want to Delete This Mosaic?")
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
                        if (CacheManager.deleteMosaicByPosition(mContext, position)) {
                            createSucessSweetAlert();
                            mosaics.remove(position);
                            notifyDataSetChanged();
                        } else
                            Toast.makeText(mContext, "Oops, Something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                }).show();

    }

    public void createSucessSweetAlert() {
        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(":)")
                .setContentText("Deleted Successfully.")
                .show();


    }
}

