package c.m.mobile8.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class ImageDecoder extends Thread {
    private static final String TAG = "ImageDecoder";

    private static final int MSG_SET_BITMAP = 0x01;

    private static ImageDecoder mInstance = new ImageDecoder();

    private Context mContext;
    private ArrayBlockingQueue<DecoderInfo> mQueue;
    private LruCache<String, Bitmap> mBitmapCache;
    private int mBaseWidth = 300;

    private ImageDecoder() {
        mQueue = new ArrayBlockingQueue<DecoderInfo>(100);
        mBitmapCache = new LruCache<>(100);

        this.start();
    }

    public static ImageDecoder getInstance() {
        return mInstance;
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();

        //이미지 디코딩 기준크기 지정
        float w = mContext.getResources().getDisplayMetrics().widthPixels;
        mBaseWidth = (int)(w * 0.6f);
    }

    public void decode(String location, ImageView targetView) {
        DecoderInfo info = new DecoderInfo();
        info.location = location;
        info.targetView = targetView;

        mQueue.offer(info);
    }

    private void calcDecodingSize(BitmapFactory.Options opt) {
        opt.outHeight = (int) ((float) opt.outHeight * ((float)mBaseWidth / (float)opt.outWidth));
        opt.outWidth = mBaseWidth;
    }

    private InputStream getInputStream(String location) throws FileNotFoundException {
        if(location.startsWith("content://")) {
            //use ContentResolver
            return mContext.getContentResolver().openInputStream(Uri.parse(location));
        }else if(location.startsWith("/")) {
            //use File
            return new FileInputStream(new File(location));
        }

        return null;
    }

    private int getSampleSize(int srcWidth) {
        int sampleSize = 1;

        while(srcWidth > mBaseWidth) {
            sampleSize *= 2;

            srcWidth = srcWidth / 2;
        }

        return sampleSize;
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                DecoderInfo info = mQueue.take();
                Bitmap bitmap = mBitmapCache.get(info.location);

                if(bitmap == null) {
                    InputStream is = getInputStream(info.location);

                    if(is == null) {
                        Log.d(TAG, "Unknown location = " + info.location);
                        continue;
                    }

                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;

                    BitmapFactory.decodeStream(is, null, opt);

                    Log.d(TAG, "Size = " + opt.outWidth + "x" + opt.outHeight);

                    opt.inJustDecodeBounds = false;
                    opt.inSampleSize = getSampleSize(opt.outWidth);

                    is = getInputStream(info.location);
                    bitmap = info.bitmap = BitmapFactory.decodeStream(is, null, opt);

                    mBitmapCache.put(info.location, bitmap);
                }

                Message.obtain(mHandler, MSG_SET_BITMAP, info).sendToTarget();
            }catch(InterruptedException e) {
                e.printStackTrace();
                this.interrupt();
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_SET_BITMAP) {
                DecoderInfo info = (DecoderInfo) msg.obj;
                info.targetView.setImageBitmap(info.bitmap);
                info.bitmap = null;
            }
        }
    };

    public static class DecoderInfo {
        public String location;
        public ImageView targetView;
        public Bitmap bitmap;
    }
}
