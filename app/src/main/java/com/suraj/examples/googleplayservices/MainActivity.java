package com.suraj.examples.googleplayservices;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.suraj.examples.googleplayservices.api.Etsy;
import com.suraj.examples.googleplayservices.google.GoogleServicesHelper;
import com.suraj.examples.googleplayservices.model.ActiveListings;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private static final String STATE_ACTIVE_LISTINGS = "StateActiveListings";
    private ListingAdapter adapter;

    private GoogleServicesHelper googleServicesHelper;
    private RecyclerView recyclerView;
    private View progressBar;
    private TextView errorView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActiveListings activeListings = adapter.getActiveListings();
        if (activeListings!=null) {
            outState.putParcelable(STATE_ACTIVE_LISTINGS, activeListings);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorView = (TextView) findViewById(R.id.error_view);

        //setup recyclerview
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ListingAdapter(this);

        recyclerView.setAdapter(adapter);

        googleServicesHelper = new GoogleServicesHelper(this, adapter);

        showLoading();

        if (savedInstanceState !=null) {
            if (savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)) {
                adapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS), null);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleServicesHelper.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleServicesHelper.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleServicesHelper.handleActivityResult(requestCode, resultCode, data);

        if (requestCode==ListingAdapter.REQUEST_CODE_PLUS_ONE) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    public void showList() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    public void showError() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }
}
