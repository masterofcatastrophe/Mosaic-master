package codesages.mosaic;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.ImageManager;
import codesages.mosaic.helpers.ImagePath;
import codesages.mosaic.helpers.Mosaic;

public class MosaicCreationScreen extends AppCompatActivity {

    int newThumbIds[] = {R.drawable.family, R.drawable.family, R.drawable.family};
    ImageView img;
    Bitmap bitmap;
    Drawable drawable;
    AddMosaic addTask;
    ImagePath imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic_creation_screen);

        Button saveNewMosaicButton = (Button) findViewById(R.id.saveNewMosaicButton);
        Button pickImageButton = (Button) findViewById(R.id.uploadMosaicImageButton);
        final EditText mosaicNameEt = (EditText) findViewById(R.id.mosaic_creation_mosaic_name_et);
        img = (ImageView) findViewById(R.id.mosaic_creation_imageview);

        saveNewMosaicButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Intent intent = new Intent(getApplicationContext(), mosaicsListScreen.class);
                intent.putExtra(mosaicsListScreen.THUMB_IDS, newThumbIds);
                startActivity(intent);
                finish();
                */
                if (!mosaicNameEt.getText().toString().isEmpty()) {
                    Mosaic mosaic = new Mosaic(mosaicNameEt.getText().toString(), new Date(), imagePath);
                    addTask = new AddMosaic();
                    addTask.execute(mosaic);

                } else {
                    Toast.makeText(getApplicationContext(), "Enter a Name First!", Toast.LENGTH_SHORT).show();
                    mosaicNameEt.requestFocus();
                }
            }
        });
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    private void OpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            } else {
                Toast.makeText(this, "Cannot Access Photos!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imagePath = ImageManager.ImageProcessingEngine(resultCode, data, this);
        if (imagePath.isSuccess()) {
            drawable = ImageManager.getDrawable(this, imagePath);
            img.setImageDrawable(drawable);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ProgressDialog CreatedDialog() {
        ProgressDialog dialog = new ProgressDialog(MosaicCreationScreen.this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Adding. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;

    }

    private class AddMosaic extends AsyncTask<Mosaic, Void, Void> {
        ProgressDialog dialog = CreatedDialog();

        @Override
        protected Void doInBackground(Mosaic... params) {
            CacheManager.addToMosaicFromCache(MosaicCreationScreen.this, params[0]);
            return null;
        }

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
            Toast.makeText(getApplicationContext(), "Adding New Mosaic...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //dialog.dismiss();
            Toast.makeText(getApplicationContext(), "New Mosaic have been Created!", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
