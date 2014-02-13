package com.eleventigers.app;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import rx.Observer;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by eleventigers on 13/02/14.
 */
public class ApiManager {

    private static final String API_URL = "https://api.github.com";

    public static class Contributor {
        String login;
        int contributions;
    }

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        );
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    private static final GitHub github = restAdapter.create(GitHub.class);

    public static GitHub getService() {
        return github;
    }

    public static Observable<List<Contributor>> getContributors(final String owner, final String repo) {
        return Observable.create(new Observable.OnSubscribeFunc<List<Contributor>>() {
            @Override
            public rx.Subscription onSubscribe(Observer<? super List<Contributor>> observer) {
                try {
                    observer.onNext(github.contributors(owner, repo));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}
