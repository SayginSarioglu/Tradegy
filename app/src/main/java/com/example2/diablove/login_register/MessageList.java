package com.example2.diablove.login_register;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MessageList extends AppCompatActivity implements View.OnClickListener {


    UserLocalStore userLocalStore;
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<String> offers = new ArrayList<String>();
    ArrayList<Offer> offerArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        User registeredUser = new User(user.name, user.age, user.username, user.password, user.email, user.latitude, user.longitude, user.balance);
        listViewSliding = (ListView) findViewById(R.id.iv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        listSliding.add(new ItemSlideMenu(R.drawable.sell, "Sell"));
        listSliding.add(new ItemSlideMenu(R.drawable.profile, "Profile"));
        listSliding.add(new ItemSlideMenu(R.drawable.category, "Show Offers"));
        listSliding.add(new ItemSlideMenu(R.drawable.chat, "Chat"));
        listSliding.add(new ItemSlideMenu(R.drawable.contactus, "Contact Us"));
        listSliding.add(new ItemSlideMenu(R.drawable.help, "Help"));
        listSliding.add(new ItemSlideMenu(R.drawable.logout, "Log Out"));
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tragedy");
        listViewSliding.setItemChecked(0, true);
        drawerLayout.closeDrawer(listViewSliding);
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTitle(listSliding.get(position).getTitle());
                listViewSliding.setItemChecked(position, true);
                replaceFragment(position);
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.drawer_opened, R.string.drawer_closed){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();

            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.showMessagesRequests(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

            }

            @Override
            public void json(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jSon = jsonArray.getJSONObject(i);
                        String receiver = jSon.getString("receiver");
                        String sender = jSon.getString("sender");
                        String message = jSon.getString("message");
                        String date = jSon.getString("date");

                        //Offer tempOffer = new Offer(itemName,offerBy,sellBy,price);
                        String temp = "receiver: " + receiver+  "\nsender: " + sender + " " + "\nmessage: " + message + " "+ "\ndate: "+ date;
                        //offerArrayList.add(tempOffer);
                        offers.add(temp);

                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void flagged(boolean flag) {

            }

            @Override
            public void imgData(Bitmap image) {
            }
        });
        setTitle("Offer List");
        ListAdapter offerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, offers);
        ListView offerListView = (ListView) findViewById(R.id.listView);
        offerListView.setAdapter(offerAdapter);

        offerListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String anan = String.valueOf(parent.getItemAtPosition(position));

                        Toast.makeText(MessageList.this, anan, Toast.LENGTH_LONG).show();
                        Offer temporary = new Offer(offerArrayList.get(position).itemName,
                                offerArrayList.get(position).offerBy,
                                offerArrayList.get(position).sellBy,
                                offerArrayList.get(position).dateOffered,
                                offerArrayList.get(position).price);
                        ServerRequest serverRequest = new ServerRequest();
                        serverRequest.acceptOfferRequest(temporary, new GetUserCallback() {
                            @Override
                            public void done(User returnedUser) {

                            }

                            @Override
                            public void json(JSONArray jsonArray) {

                            }

                            @Override
                            public void flagged(boolean flag) {

                            }

                            @Override
                            public void imgData(Bitmap image) {

                            }
                        });
                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void replaceFragment(int position){

        switch (position){
            case 0:
                startActivity(new Intent(this, Sell.class));
                break;
            case 1:
                startActivity(new Intent(this, Profile.class));
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                startActivity(new Intent(this, Contact.class));
                break;
            case 5:
                break;
            case 6:
                userLocalStore.clearUserData();
                startActivity(new Intent(this, Login.class));
                break;
        }

    }

    @Override
    public void onClick(View v) {
        Bitmap image;
        User user = userLocalStore.getLoggedInUser();
        switch (v.getId()){


        }
    }
}
