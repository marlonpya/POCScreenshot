package com.example.pocscreenshot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnScreenShot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ScrollView lScrollview = findViewById(R.id.scrollView);
                final Bitmap lBitmap =
                        getBitmapFromView(lScrollview,
                                lScrollview.getChildAt(0).getHeight(),
                                lScrollview.getChildAt(0).getWidth());
                takeScreen(lBitmap);
            }
        });
    }

    private void takeScreen(Bitmap pBitmap) {
        try {
            final Date ldNow = new Date();
            DateFormat.format("yyyy-MM-dd_hh:mm:ss", ldNow);
            final String lsPath = Environment.getExternalStorageDirectory().toString() + "/" + ldNow + ".jpeg";
            final File lFile = new File(lsPath);
            pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(lFile));
            final Uri lUri = FileProvider.getUriForFile(this,
                    "com.example.pocscreenshot.provider", //(use your app signature + ".provider" )
                    lFile);

            share(lUri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View pView,
                                     int piHeight,
                                     int width) {
        final Bitmap lBitmap = Bitmap.createBitmap(width, piHeight, Bitmap.Config.ARGB_8888);
        final Canvas lCanvas = new Canvas(lBitmap);
        final Drawable lDrawable = pView.getBackground();
        if (lDrawable != null)
            lDrawable.draw(lCanvas);
        else
            lCanvas.drawColor(Color.WHITE);
        pView.draw(lCanvas);
        return lBitmap;
    }

    private void share(Uri pUri) {
        Intent lShareIntent = new Intent();
        lShareIntent.setAction(Intent.ACTION_SEND);
        lShareIntent.putExtra(Intent.EXTRA_STREAM, pUri);
        lShareIntent.setType("image/*");
        startActivity(Intent.createChooser(lShareIntent, "Share"));
    }
}
