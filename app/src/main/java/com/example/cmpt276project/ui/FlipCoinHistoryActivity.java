package com.example.cmpt276project.ui;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.example.cmpt276project.model.Children;
import com.example.cmpt276project.model.FlipHistory;
import com.example.cmpt276project.model.FlipHistoryManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmpt276project.R;

import java.util.Objects;

// Activity to display the history of results from FlipCoinActivity
// Allows the user to choose between seeing all results or the results of the current child
public class FlipCoinHistoryActivity extends AppCompatActivity {

    private FlipHistoryManager manager;
    private Children children;
    private boolean toggleHistory = true;
    private String currentChildName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" on toolbar
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);

        manager = FlipHistoryManager.getInstance();
        children = Children.getInstance();

        populateListView();
        if(children.getNumChildren(this) != 0) {
            currentChildName = "ChildName: " + children.getChild(children.getCurrentChildIndex(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu:
        getMenuInflater().inflate(R.menu.menu_flip_coin_history, menu);
        return true;
    }

    // Switch History to current history
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.CurrentChildHsitory_Button_item) {
            populateListView();
            toggleHistory = false;
            return true;
        }

        if (item.getItemId() == R.id.AllChildHsitory_Button_item) {
            populateListView();
            toggleHistory = true;
            return true;
        }

        //return false;
        return super.onOptionsItemSelected(item);
    }

    public void populateListView(){
        ArrayAdapter<FlipHistory> adapter = new myListAdapter();
        ListView list = findViewById(R.id.history_ListView);
        list.setAdapter(adapter);
    }

    private class myListAdapter extends ArrayAdapter<FlipHistory> {
        public myListAdapter(){
            super(FlipCoinHistoryActivity.this, R.layout.history_item, manager.getMyHistory());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.history_item, parent, false);
            }

            return getCoinHistoryView(position, itemView);
        }
    }

    private View getCoinHistoryView(int position, View itemView) {
        //Find the History to work with
        FlipHistory currentHistory = manager.getMyHistory().get(position);

        ImageView profilePic = itemView.findViewById(R.id.Portrait);
        profilePic.setImageBitmap(children.decodeToBase64(currentHistory.getProfile()));

        //Fill the view
        ImageView imageView = itemView.findViewById(R.id.item_Win);
        imageView.setImageResource(currentHistory.getIconID());

        // ChildName:
        TextView ChildName = itemView.findViewById(R.id.item_ChildrenName);
        ChildName.setText(currentHistory.getChildName());

        // CurrentDate:
        TextView CurrentDate = itemView.findViewById(R.id.item_CurrentDate);
        CurrentDate.setText(currentHistory.getCurrentDateAndTime());

        // Result:
        TextView FlipResult = itemView.findViewById(R.id.item_ResultOfHeadOrTail);
        FlipResult.setText(currentHistory.getFlipResult());

        itemView.setVisibility(View.VISIBLE);

        if (!toggleHistory) {
            if (!manager.getMyHistory().get(position).getChildName().equals(currentChildName))
                itemView.setVisibility(View.GONE);
        }

        return itemView;
    }
}