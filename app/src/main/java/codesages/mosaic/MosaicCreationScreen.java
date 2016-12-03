package codesages.mosaic;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.ImageManager;
import codesages.mosaic.helpers.ImagePath;
import codesages.mosaic.helpers.Keys;
import codesages.mosaic.helpers.Mosaic;

public class MosaicCreationScreen extends AppCompatActivity {

    int newThumbIds[] = {R.drawable.family, R.drawable.family, R.drawable.family};
    ImageView img;
    Bitmap bitmap;
    Drawable drawable;
    AddMosaic addTask;
    ImagePath imagePath;
    boolean isImageSet = false, isEdit = false, isUpdateMode, isImagePathChanged;
    Context ctx;
    int selectedMosaicIndex;
    static String TAG = "MosaicCreationScreen";
    Mosaic mosaic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic_creation_screen);
        ctx = this;

        getIntentData();

        BootstrapButton saveNewMosaicButton = (BootstrapButton) findViewById(R.id.saveNewMosaicButton);
        BootstrapButton pickImageButton = (BootstrapButton) findViewById(R.id.uploadMosaicImageButton);
        final BootstrapEditText mosaicNameEt = (BootstrapEditText) findViewById(R.id.mosaic_creation_mosaic_name_et);
        img = (ImageView) findViewById(R.id.mosaic_creation_imageview);

        if (isUpdateMode) {
            mosaic = getMosaic();
            saveNewMosaicButton.setBootstrapText(new BootstrapText.Builder(ctx)
                    .addFontAwesomeIcon(FontAwesome.FA_SAVE)
                    .addText(" Update Mosaic")
                    .build());
            mosaicNameEt.setText(mosaic.getName());
            if (mosaic.getImagePath() != null) {
                if (mosaic.getImagePath().isImageFromGoogleDrive()) {
                    Log.d(TAG, "getView: get Image from Google with " + mosaic.getImagePath().getStringUri());
                    Picasso.with(ctx)
                            .load(Uri.parse(mosaic.getImagePath().getStringUri()))
                            .error(android.R.drawable.ic_menu_report_image)
                            .into(img);

                } else {

                    Picasso.with(ctx)
                            .load(new File(mosaic.getImagePath().getPath()))
                            .error(android.R.drawable.ic_menu_report_image)
                            .into(img);
                }
            }
            //isImageSet = true;

        }
        saveNewMosaicButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mosaicNameEt.getText().toString().isEmpty()) {
                    addTask = new AddMosaic();
                    if (!isUpdateMode) {
                        if (isImageSet) {
                            Mosaic mosaic = new Mosaic(mosaicNameEt.getText().toString(), new Date(), imagePath);
                            addTask.execute(mosaic);
                        } else {
                            Mosaic mosaic = new Mosaic(mosaicNameEt.getText().toString(), new Date());
                            addTask.execute(mosaic);
                        }

                    } else {

                        mosaic.setName(mosaicNameEt.getText().toString());
                        mosaic.setImagePath(isImagePathChanged ? imagePath : mosaic.getImagePath());
                        addTask.execute(mosaic);
                    }

                } else {
                    new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Hold On...")
                            .setContentText("Enter a Name First")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mosaicNameEt.requestFocus();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

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
        isImagePathChanged = true;
        if (imagePath.isSuccess()) {
            drawable = ImageManager.getDrawable(this, imagePath);
            img.setImageDrawable(drawable);
            isImageSet = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private SweetAlertDialog CreatedDialog() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        return pDialog;

    }

    public void getIntentData() {
        isImagePathChanged = false;
        isEdit = getIntent().getBooleanExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_IS_EDIT, false);
        if (isEdit) {
            selectedMosaicIndex = getIntent().getIntExtra(Keys.INTENT_EXTRA_SELECTED_MOSAIC_INDEX, -1);
            if (selectedMosaicIndex != -1)
                isUpdateMode = true;
            else
                isUpdateMode = false;
        }

    }

    public Mosaic getMosaic() {
        return CacheManager.getMosaicByPosition(ctx, selectedMosaicIndex);
    }

    private class AddMosaic extends AsyncTask<Mosaic, Void, Void> {
        SweetAlertDialog dialog = CreatedDialog();

        @Override
        protected Void doInBackground(Mosaic... params) {
            if (!isUpdateMode)
                CacheManager.addToMosaicFromCache(MosaicCreationScreen.this, params[0]);
            else
                CacheManager.updateMosaicByPosition(MosaicCreationScreen.this, params[0], selectedMosaicIndex);
            return null;
        }

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (isUpdateMode) {
                Toast.makeText(getApplicationContext(), "Mosaic Have been Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "New Mosaic have been Created!", Toast.LENGTH_SHORT).show();
            }
            finish();
        }


    }
}
