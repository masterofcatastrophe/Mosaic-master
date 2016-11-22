package codesages.mosaic.lists;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import codesages.mosaic.R;
import codesages.mosaic.helpers.Contact;
import codesages.mosaic.helpers.ContactNumbers;
import codesages.mosaic.helpers.MosaicContact;

/**
 * Created by DELL on 11/11/2016.
 */

public class MosaicContactAdapter extends BaseAdapter implements SectionIndexer {

    HashMap<String, Integer> mapIndex;
    String[] sections;
    final String TAG = "ContactAdapter";
    private Activity mContext;
    int[] colors = {Color.MAGENTA, Color.BLUE, Color.LTGRAY, Color.RED, Color.DKGRAY, Color.GRAY};
    private ArrayList<MosaicContact> conatcts;

    public MosaicContactAdapter(Activity context,
                                ArrayList<MosaicContact> conatcts) {
        mContext = context;
        this.conatcts = conatcts;
        // indexing
        //PrepareSections();

    }


    private void PrepareSections() {

        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < conatcts.size(); i++) {
            MosaicContact contact = conatcts.get(i);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mosaic_contact_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.mosaic_contact_list_row_img);
            viewHolder.name = (TextView) convertView.findViewById(R.id.mosaic_contact_list_row_name);
            viewHolder.contact = (TextView) convertView.findViewById(R.id.mosaic_contact_list_row_contact);
            viewHolder.period = (TextView) convertView.findViewById(R.id.mosaic_contact_list_row_period);
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

        viewHolder.name.setText(conatcts.get(position).getName());
        String strContact = "";
        ContactNumbers contactNumbers = conatcts.get(position).getContactNumbers();
        for (int i = 0; i < contactNumbers.getNumbersEmail().size(); i++) {
            strContact += contactNumbers.getNumbersEmail().get(i) + ", ";
        }
        viewHolder.contact.setText(strContact);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM yyyy hh:mm");
        if (conatcts.get(position).getLastCall() == null) {
            //to convert Date to String, use format method of SimpleDateFormat class.
            //String strDate = dateFormat.format(new Date());
            viewHolder.period.setText(String.format("Last Contacted on: %s", "Unknown"));
        } else {
            String strDate = dateFormat.format(conatcts.get(position).getLastCall());
            viewHolder.period.setText(String.format("Last Contacted on: %s", strDate));
        }
        String[] initials = conatcts.get(position).getName().split(" ");
        String init = "M";
        if (initials.length > 0) {
            init = initials[0].substring(0, 1);
            if (initials.length > 1) {

                init += initials[1].substring(0, 1);
            }
        }

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        //int color = generator.getRandomColor();
        int color = generator.getColor(conatcts.get(position).getName());
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(init.toUpperCase(), color);


        viewHolder.img.setImageDrawable(drawable);
        if (position % 2 == 0) {
            //viewHolder.img.setBackgroundResource(R.drawable.crack);
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            //viewHolder.img.setBackgroundResource(R.drawable.purple_flower_fresh);
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
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
        public TextView name;
        public TextView contact;
        public TextView period;


    }
}

