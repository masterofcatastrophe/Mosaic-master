package codesages.mosaic.lists;

import android.app.Activity;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import codesages.mosaic.R;
import codesages.mosaic.helpers.Contact;
import codesages.mosaic.helpers.ImageManager;
import codesages.mosaic.helpers.Mosaic;

/**
 * Created by DELL on 11/11/2016.
 */

public class ContactAdapter extends BaseAdapter implements SectionIndexer {

    HashMap<String, Integer> mapIndex;
    String[] sections;
    final String TAG = "ContactAdapter";
    private Activity mContext;
    int[] colors = {Color.MAGENTA, Color.BLUE, Color.LTGRAY, Color.RED, Color.DKGRAY, Color.GRAY};
    private ArrayList<Contact> conatcts;

    public ContactAdapter(Activity context,
                          ArrayList<Contact> conatcts) {
        mContext = context;
        this.conatcts = conatcts;
        // indexing
        PrepareSections();

    }


    private void PrepareSections() {

        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < conatcts.size(); i++) {
            Contact contact = conatcts.get(i);
            String ch = contact.getName().substring(0, 1);
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
        return conatcts.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.contact_list_row_img);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.contact_list_row_txt);
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

        viewHolder.txt.setText(conatcts.get(position).getName());
        String[] initials = conatcts.get(position).getName().split(" ");
        String init = "M";
        if (initials.length > 0) {
            init = initials[0].substring(0, 1);
            if (initials.length > 1) {

                init += initials[1].substring(0, 1);
            }
        }

        TextDrawable drawable = TextDrawable.builder()
                .buildRect(init.toUpperCase(), colors[new Random().nextInt(6)]);
        viewHolder.img.setImageDrawable(drawable);

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {
        String sch = sections[section];
        for (int i = 0; i < conatcts.size(); i++) {
            String ch = conatcts.get(i).getName().substring(0, 1);
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

    }
}

