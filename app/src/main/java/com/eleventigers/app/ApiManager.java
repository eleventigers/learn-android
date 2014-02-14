/**
 * Created by eleventigers on 13/02/14.
 */

package com.eleventigers.app;

import com.eleventigers.app.models.ScenesCollection;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.Observer;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

public class ApiManager {

    private static final String API_URL = "foobar";
    private static final String API_KEY = "foobar";

    interface Seene {
        @GET("/scenes/popular")
        ScenesCollection popularScenes(
                @Query("count") Integer count,
                @Query("page") Integer page
        );
    }

    private static final RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("User-Agent", "Android-Sample-App");
            request.addHeader("api-key", API_KEY);
        }
    };

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setRequestInterceptor(requestInterceptor)
            .build();

    private static final Seene seene = restAdapter.create(Seene.class);

    public static Observable<ScenesCollection> getPopularScenes(final Integer count, final Integer page) {
        return Observable.create(new Observable.OnSubscribeFunc<ScenesCollection>() {
            @Override
            public rx.Subscription onSubscribe(Observer<? super ScenesCollection> observer) {
                try {
                    observer.onNext(seene.popularScenes(count, page));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}
