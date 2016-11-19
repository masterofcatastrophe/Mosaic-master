package codesages.mosaic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
/*    private void setList() {
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

        ArrayList<Card> _cards = initCard(mosaicArrayList);
        updateAdapter(_cards);

    }*/


    private ArrayList<Card> initCard(ArrayList<Mosaic> mosaics) {

        //Init an array of Cards
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < mosaics.size(); i++) {
            final Mosaic mosaic = mosaics.get(i);
            Card card = new Card(getApplicationContext());
            String title = "";

            //skip if all empty

            card.setTitle(mosaic.getName());
            //Create a CardHeader
            //CardHeader header = new CardHeader(getApplicationContext());
            CardThumbnail thump = new CardThumbnail(getApplicationContext());
            thump.setCustomSource(new CardThumbnail.CustomSource() {
                @Override
                public String getTag() {
                    return mosaic.getName() + "-Tag";
                }

                @Override
                public Bitmap getBitmap() {
                    return mosaic.getBitmap();
                }
            });
            //Set the header title
            //header.setTitle(mosaic.getName());


            //Add a popup menu. This method set OverFlow button to visible
           /* header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    Toast.makeText(getApplicationContext(), "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            card.addCardHeader(header);
*/
            card.addCardThumbnail(thump);

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

    private void fillThumbIds() {
        mThumbIds = new ArrayList();
        // somehow get older thumbnail ids if necessary (i.e. from storage)
        // and add to ArrayList like this:
        // mThumbIds.add(R.drawable.family);
        // mThumbIds.add(R.drawable.project);

        // assuming we transmit resource id's: use an int array with the Intent
        int[] newThumbIds = getIntent().getIntArrayExtra(THUMB_IDS);
        if (newThumbIds != null) {
            // loop through the array to add new thumb ids
            for (int i = 0; i < newThumbIds.length; i++) {
                mThumbIds.add(newThumbIds[i]);
            }
        }
    }

    // Note: mthumbIds no longer as array but as ArrayList above!
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        //@Override
        public int getCount() {
            return mThumbIds.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ImageView imageView = new ImageView(mContext);
            if (mosaicArrayList.get(position).getBitmap() != null) {
                mosaicArrayList.get(position).getBitmap();
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + position);
                    Intent intent = new Intent(getApplicationContext(), contactsScreen.class);
                    intent.putExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, position);
                    startActivity(intent);
                }
            });
            return imageView;
        }
    }
}
