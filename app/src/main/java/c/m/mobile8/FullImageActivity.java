package c.m.mobile8;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
    private GestureDetectorCompat mGestureDetector;
    private boolean mbZoomed = false;
    private int dx = 0;
    private int dy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullimage);

        mFullImage = (ImageView) findViewById(R.id.full_image);

        mImageLocation = getIntent().getStringExtra(EXTRA_IMAGE_LOCATION);

        mFullImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }

                return false;
            }
        });

        mGestureDetector = new GestureDetectorCompat(this, mGestureListener);
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mbZoomed = !mbZoomed;
                setMatrix();
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
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

    private void setMatrix() {
        Matrix m = new Matrix();
        if(mbZoomed) {
            m.setScale(2.0f, 2.0f);
            Log.d("TEST", "x = " + mFullImage.getTranslationX());
            m.postTranslate(dx, dy);
        }

        mFullImage.setImageMatrix(m);
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
        int dw, dh;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            dw = metrics.widthPixels * 2;
            dh = metrics.heightPixels * 2;
        }else {
            dh = metrics.widthPixels * 2;
            dw = metrics.heightPixels * 2;
        }

        int sampleSize = 1;

        while( (dw < imgWidth) || (dh < imgHeight) ) {
            imgWidth /= 2;
            imgHeight /= 2;
            sampleSize *= 2;
        }

        return sampleSize;
    }

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //Log.d("TEST", "Scroll x=" + distanceX + ", y=" + distanceY);
            dx -= distanceX;
            dy -= distanceY;

            setMatrix();

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };
}
