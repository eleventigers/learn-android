package com.eleventigers.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eleventigers.app.adapters.ScenesListViewAdapter;
import com.eleventigers.app.models.Scene;
import com.eleventigers.app.models.ScenesCollection;

import butterknife.InjectView;
import butterknife.ButterKnife;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.util.functions.Action1;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        ApiManager.getPopularScenes(60, 1)
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Action1<ScenesCollection>() {
                              @Override
                              public void call(ScenesCollection collection) {
                                  Toast.makeText(
                                          MainActivity.this,
                                          "success",
                                          Toast.LENGTH_SHORT)
                                          .show();

                                 populateList(collection.getModels());

                              }
                          }, new Action1<Throwable>() {
                              @Override
                              public void call(Throwable error) {
                                  Toast.makeText(
                                          MainActivity.this,
                                          "error: " + error.getMessage(),
                                          Toast.LENGTH_SHORT)
                                          .show();
                              }
                          }
               );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateList(final List<Scene> scenes) {

        final ListView listview = (ListView) findViewById(R.id.listview);

        final ScenesListViewAdapter adapter = new ScenesListViewAdapter(
                this,
                android.R.layout.simple_list_item_1,
                scenes);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    final View view,
                    int position, long id
            ) {
                final Scene item = (Scene) parent.getItemAtPosition(position);
                view.animate()
                    .setDuration(500)
                    .scaleY(0f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            scenes.remove(item);
                            adapter.notifyDataSetChanged();
                            view.setScaleY(1);
                        }
                    });
            }

        });

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @InjectView(R.id.contrib_title) TextView contribTitle;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState
        ) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            contribTitle.setText("Testing");
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.reset(this);
        }
    }

}
