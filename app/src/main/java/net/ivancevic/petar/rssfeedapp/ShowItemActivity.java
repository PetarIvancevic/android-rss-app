package net.ivancevic.petar.rssfeedapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ShowItemActivity extends ActionBarActivity {
    private feedItem objFeedItem;


    private void printTheData() {
        ActionBar acBar = getSupportActionBar();
        acBar.setTitle(objFeedItem.getTitle());
        TextView mainView = (TextView) findViewById(R.id.textViewSecondActivity);
        mainView.setBackgroundResource(R.drawable.show_item_gradient);
        mainView.setTextSize(17);
        mainView.setPadding(10, 15, 0, 5);
        StringBuilder viewMsg = new StringBuilder();
        viewMsg.append(objFeedItem.getDescription());
        viewMsg.append("\n\n");
        viewMsg.append(objFeedItem.getCreator());
        viewMsg.append("\n");
        viewMsg.append("Broj Komentara: ");
        viewMsg.append(objFeedItem.getNumComments());
        viewMsg.append("\n");
        viewMsg.append(objFeedItem.getpubDate());
        mainView.setText(viewMsg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        Bundle bundle = getIntent().getExtras();
        this.objFeedItem = bundle.getParcelable("feedItem");
        this.printTheData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_item, menu);
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
}
