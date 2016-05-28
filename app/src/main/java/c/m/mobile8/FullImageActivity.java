package c.m.mobile8;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Hyosang on 2016-05-29.
 */
public class FullImageActivity extends Activity {
    public static final String TAG = "FullImageActivity";

    public static final String EXTRA_IMAGE_LOCATION = "extra_image_location";

    private String mImageLocation;
    private ImageView mFullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullimage);

        mFullImage = (ImageView) findViewById(R.id.full_image);

        mImageLocation = getIntent().getStringExtra(EXTRA_IMAGE_LOCATION);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadImage();
    }

    private boolean loadImage() {
        Log.d(TAG, "Load image = " + mImageLocation);

        if(mImageLocation != null && mImageLocation.length() > 0) {
            InputStream is;
            try {
                is = getInputStream();

                if(is != null) {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;

                    BitmapFactory.decodeStream(is, null, opt);

                    opt.inSampleSize = getSampleSize(opt.outWidth, opt.outHeight);
                    opt.inJustDecodeBounds = false;

                    Bitmap bitmap = BitmapFactory.decodeStream(getInputStream(), null, opt);
                    if(bitmap != null) {
                        mFullImage.setImageBitmap(bitmap);

                        return true;
                    }
                }
            }catch(FileNotFoundException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        return false;
    }

    private InputStream getInputStream() throws FileNotFoundException {
        if(mImageLocation.startsWith("content://")) {
            //ContentResolver
            return getContentResolver().openInputStream(Uri.parse(mImageLocation));
        }else if(mImageLocation.startsWith("/")) {
            //local file
            return new FileInputStream(mImageLocation);
        }

        return null;
    }

    private int getSampleSize(int imgWidth, int imgHeight) {
        //디스플레이의 가로세로 두배 크기로 로드한다.
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dw = metrics.widthPixels * 2;
        int dh = metrics.heightPixels * 2;

        int sampleSize = 1;

        while( (dw < imgWidth) || (dh < imgHeight) ) {
            imgWidth /= 2;
            imgHeight /= 2;
            sampleSize *= 2;
        }

        return sampleSize;
    }
}
