package com.eleventigers.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.eleventigers.app.models.ScenesCollection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import rx.Observable;
import rx.Observer;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by eleventigers on 14/02/14.
 */
public class ImageFetcher {

    private static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.
                    decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    private static InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    public static Observable<Bitmap> getImage(final String url) {
        return Observable.create(new Observable.OnSubscribeFunc<Bitmap>() {
            @Override
            public rx.Subscription onSubscribe(Observer<? super Bitmap> observer) {
                try {
                    observer.onNext(downloadImage(url));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}
