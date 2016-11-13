package codesages.mosaic.lists;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import codesages.mosaic.R;
import codesages.mosaic.helpers.ImageManager;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mosaic_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.mosaic_list_row_img);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.mosaic_list_row_txt);
            viewHolder.pb = (ProgressBar) convertView.findViewById(R.id.mosaic_list_row_pb);
            /**
             * At very first time when the List View row Item control's
             * instance is created it will be store in the convertView as a
             * ViewHolder Class object for the reusability purpose
             **/
            convertView.setTag(viewHolder);
        } else {
            /**
             * Once the instance of the row item's control it will use from
             * already created controls which are stored in convertView as a
             * ViewHolder Instance
             * */
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt.setText(mosaics.get(position).getName());

        if (mosaics.get(position).getImagePath().isImageFromGoogleDrive()) {
            Picasso.with(mContext)
                    .load(new File(mosaics.get(position).getImagePath().getStringUri()))
                    //.placeholder(R.drawable.progress_animation)
                    .error(R.drawable.img_placeholder)
                    .into(viewHolder.img);

        } else {

            Picasso.with(mContext)
                    .load(new File(mosaics.get(position).getImagePath().getPath()))
                    //.placeholder(R.drawable.progress_animation)
                    .error(R.drawable.img_placeholder)
                    .into(viewHolder.img);
        }

        viewHolder.pb.setVisibility(View.GONE);
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
        public ProgressBar pb;

    }
}

