package net.ivancevic.petar.rssfeedapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static TextView mainTextView;
    private static String msg = null;
    private static String url = "http://upitsplit.hr/feed";
    public static List<feedItem> feedData;
    
    private static final String EXTRA_MESSAGE = "net.ivancevic.petar.MainActivity";

//Napravi text view sa padding i ostalim stvarima
    private TextView createMyTextView(feedItem temp) {
        TextView tv = new TextView(this);
        tv.setBackgroundResource(R.drawable.textview_listing_gradient);
        tv.setTextSize(17);
        tv.setPadding(10, 17, 5, 13);
        tv.setText(temp.getTitle() + "\n" + temp.getCreator());
        return tv;
    }


    private void removeViews() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.appendingLayout);
        linearLayout.removeAllViews();
        this.feedData.clear();
    }

//dohvati sve feedIteme i stavi ih u viewove
    private void populateLayout() {

        if( feedData.isEmpty() ) {
            this.mainTextView.setText("Feed is either empty or there was an error? :/");
            return;
        } else {
            this.mainTextView.setVisibility(View.GONE);
        }
        LinearLayout relLayout = (LinearLayout) findViewById(R.id.appendingLayout);
        for( final feedItem temp : feedData ) {
            final TextView tempView = this.createMyTextView(temp);
            tempView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ShowItemActivity.class);
                    feedItem sending = new feedItem(temp);
                    intent.putExtra("feedItem", sending);
                    startActivity(intent);
                }
            });
            relLayout.addView(tempView);
        }
    }


    //make the request for the site
    private void makeSingletonRequest() {
        StringRequest stringRequest = new StringRequest(this.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                InputStream is = new ByteArrayInputStream(response.getBytes());
                try {
                    MyRSSXMLParser.parse(is);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                        populateLayout();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "There was an error in the connection!", Toast.LENGTH_LONG);
            }
        });

        RequestQueue singleton = MySingletonPattern.getInstance(this.getApplicationContext()).getRequestQueue();
        MySingletonPattern.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedData = new ArrayList();
        mainTextView = (TextView) findViewById(R.id.textView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void downloadTheFeed(View v) {
        if( this.feedData.isEmpty() == false ) {
            this.removeViews();
        }
        this.mainTextView.setText("Downloading feed. Please wait...");
        EditText et = (EditText) findViewById(R.id.editText);
        this.url = "http://"+et.getText().toString();
        this.makeSingletonRequest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
