package codesages.mosaic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class editMosaic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mosaic);

        Button saveChangesButton = (Button) findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),contactsScreen.class);
                startActivity(intent);
                finish();
            }
        });

        /*Button uploadNewMosaicImageButton = (Button) findViewById(R.id.uploadNewMosaicImageButton);
        uploadNewMosaicImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),contactsScreen.class);
                startActivity(intent);
                finish();
            }
        });*/
    }
}
