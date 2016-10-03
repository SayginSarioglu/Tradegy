
package com.example2.diablove.login_register;

import android.Manifest;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example2.diablove.login_register.tindercard.FlingCardListener;
import com.example2.diablove.login_register.tindercard.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,FlingCardListener.ActionDownInterface {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://ec2-54-201-231-40.us-west-2.compute.amazonaws.com/";
    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<Data> al;
    private SwipeFlingAdapterView flingContainer;
    boolean first = false;

    private LocationManager locationManager;
    private LocationListener locationListener;
    UserLocalStore userLocalStore;
    private List<ItemSlideMenu> listSliding;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    //private ImageView picture;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private Button /*offer,*/another;/*go*/;
    private int counter = 0;
    private boolean loged = true;
    String category;
    private Spinner spinner;
    private Item currItem;
    ArrayAdapter<CharSequence> adapterArr;
    EditText /*distance,*/ search, offerAmount;
    TextView sellerName,distanceRange,distanceItem,itemName;//budget;
    boolean showAnother = true;


    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }

    public static void removeBackground() {


        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //distance = (EditText) findViewById(R.id.distanceEt);
        //search = (EditText) findViewById(R.id.searchEt);
        //offerAmount = (EditText) findViewById(R.id.offeramount);
        sellerName = (TextView) findViewById(R.id.sellername);
        itemName = (TextView) findViewById(R.id.item_name);
        distanceRange = (TextView) findViewById(R.id.itemdistance);
        distanceItem = (TextView) findViewById(R.id.textView4);
        //budget = (TextView) findViewById(R.id.textView5);

        /*getSupportActionBar().setDisplayShowCustomEnabled(true);
        View cView = getLayoutInflater().inflate(R.layout.logo, null);
        getSupportActionBar().setCustomView(cView);

        Intent searchIntent = getIntent();
        Log.e("2222222","22222222");
        if (Intent.ACTION_SEARCH.equals(searchIntent.getAction())){
            Log.e("3333","333333");
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(MainActivity.this,query,Toast.LENGTH_SHORT).show();

        }*/
        Log.e("44444444444","44444444");
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        listViewSliding = (ListView) findViewById(R.id.iv_sliding_menu1);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout1);
        listSliding = new ArrayList<>();

        listSliding.add(new ItemSlideMenu(R.drawable.sell, "Sell"));
        listSliding.add(new ItemSlideMenu(R.drawable.profile, "Profile"));
        listSliding.add(new ItemSlideMenu(R.drawable.category, "Show Offers"));
        listSliding.add(new ItemSlideMenu(R.drawable.chat, "Chat"));
        listSliding.add(new ItemSlideMenu(R.drawable.contactus, "Contact Us"));
        listSliding.add(new ItemSlideMenu(R.drawable.logoicon, "Main Menu"));
        listSliding.add(new ItemSlideMenu(R.drawable.logout, "Log Out"));
        SlidingMenuAdapter adapter;
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Tradegy");

        listViewSliding.setItemChecked(0, true);
        drawerLayout.closeDrawer(listViewSliding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.menubar);
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTitle(listSliding.get(position).getTitle());
                Log.d("anasn", "asdas2222");

                listViewSliding.setItemChecked(position, true);
                replaceFragment(position);
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_opened, R.string.drawer_closed){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("anasn","asdas");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();

            }
        };


        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        al = new ArrayList<>();
        al.add(new Data("http://www.userlib.com/wp-content/uploads/2014/10/finger_swipe2.png", "START SWIPING DONT WAIT"));
        handleIntent(getIntent());

        myAppAdapter = new MyAppAdapter(al, MainActivity.this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                al.remove(0);
                //al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
                Log.e("itemSize", itemArrayList.size()+"");
                currItem = itemArrayList.get(counter);
                sellerName.setText(currItem.user_name);
                itemName.setText(currItem.item_name);

                Context context;
                context = getApplicationContext();
                Geocoder gcd = new Geocoder(context);
                Log.d("currItemLoc: ", currItem.item_name);
                try {
                    List<Address> addresses = gcd.getFromLocation(Double.parseDouble(currItem.item_loc_latitude), Double.parseDouble(currItem.item_loc_longitude), 1);
                    String completeAddress= "";
                    for(int i = 0; i < addresses.get(0).getMaxAddressLineIndex() ; i++ ){
                        if(i < addresses.get(0).getMaxAddressLineIndex()-1)
                            completeAddress += addresses.get(0).getAddressLine(i) + ", ";
                        else completeAddress += addresses.get(0).getAddressLine(i);
                    }
                    distanceRange.setText(completeAddress);
                    Log.i("LOCATION", completeAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                distanceItem.setText(currItem.distanceToSelf+"");
                Log.i("LOCATION", currItem.itemPath);
                //offerAmount.setText("");
                    /*try{

                    new downloadUserImageImageAsyncTask2(currItem.itemPath).execute().get();
                    }
                    catch (InterruptedException e){

                    }
                    catch (ExecutionException e){

                    }*/
                counter++;
                if(counter == itemArrayList.size()){
                    counter = 0;
                }
                al.add(new Data("http://ec2-54-201-231-40.us-west-2.compute.amazonaws.com/"+currItem.itemPath,"the Item"));

                myAppAdapter.notifyDataSetChanged();
                first = true;
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                //al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
                Log.e("itemSize", itemArrayList.size()+"");
                currItem = itemArrayList.get(counter);
                sellerName.setText(currItem.user_name);
                itemName.setText(currItem.item_name);


                Context context;
                context = getApplicationContext();
                Geocoder gcd = new Geocoder(context);
                Log.d("currItemLoc: ", currItem.item_name);
                try {
                    List<Address> addresses = gcd.getFromLocation(Double.parseDouble(currItem.item_loc_latitude), Double.parseDouble(currItem.item_loc_longitude), 1);
                    String completeAddress= "";
                    for(int i = 0; i < addresses.get(0).getMaxAddressLineIndex() ; i++ ){
                        if(i < addresses.get(0).getMaxAddressLineIndex()-1)
                            completeAddress += addresses.get(0).getAddressLine(i) + ", ";
                        else completeAddress += addresses.get(0).getAddressLine(i);
                    }
                    distanceRange.setText(completeAddress);
                    Log.i("LOCATION", completeAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                distanceItem.setText(currItem.distanceToSelf+"");
                Log.i("LOCATION", currItem.itemPath);
                //offerAmount.setText("");
                    /*try{

                    new downloadUserImageImageAsyncTask2(currItem.itemPath).execute().get();
                    }
                    catch (InterruptedException e){

                    }
                    catch (ExecutionException e){

                    }*/
                counter++;
                if(counter == itemArrayList.size()){
                    counter = 0;
                }
                al.add(new Data("http://ec2-54-201-231-40.us-west-2.compute.amazonaws.com/"+currItem.itemPath,"the Item"));
            if(first) {
                AlertDialog.Builder builder = new   AlertDialog.Builder(MainActivity.this);
                final EditText price = new EditText(context);
                price.setTextColor(Color.BLACK);
                String priceStr = price.getText().toString();
                builder.setTitle("Offer Price");
                builder.setView(price);
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        User currUser = userLocalStore.getLoggedInUser();
                        ServerRequest offerRequest = new ServerRequest();
                        Log.d("CurrUSer: ", currUser.username + sellerName.getText().toString() + price.getText().toString() + currItem.item_name);

                        try {
                            offerRequest.offerRequest(currUser.username, sellerName.getText().toString(), price.getText().toString(), currItem.item_name);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            }
                first = true;
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                myAppAdapter.notifyDataSetChanged();
            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if(loged) {
                    User user = userLocalStore.getLoggedInUser();
                    User registeredUser = new User(user.name, user.age, user.username, user.password, user.email, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), user.balance);
                    //Log.e(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));

                    updateUserDetails(registeredUser);
                    userLocalStore.storeUserData(registeredUser);
                    userLocalStore.setUserLoggedIn(true);

                    displayUserDetails();
                    loged = false;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET },10 );

                return;
            }else {
                //Add this line here
                configureButton();
            }
        }else{
            configureButton();
        }


        spinner = (Spinner) findViewById(R.id.spinner);
        adapterArr = ArrayAdapter.createFromResource(this, R.array.category_names, R.layout.spinner_item);
    /*
        adapterArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterArr);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/





        userLocalStore = new UserLocalStore(this);
        //picture = (ImageView) findViewById(R.id.photo);
        //offer = (Button) findViewById(R.id.offerButton);
        another = (Button) findViewById(R.id.dislikeButton);
        //go = (Button) findViewById(R.id.filterB);

                    /*picture.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
                    public void onSwipeTop() {
                    Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                    }
                    public void onSwipeRight() {
                    try {
                    Log.e("itemSize", itemArrayList.size()+"");
                    currItem = itemArrayList.get(counter);
                    sellerName.setText(currItem.user_name);


                    Context context;
                    context = getApplicationContext();
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());

                    try {
                    List<Address> addresses = gcd.getFromLocation(Double.parseDouble(currItem.item_loc_latitude), Double.parseDouble(currItem.item_loc_longitude), 1);
                        String completeAddress= "";
                        for(int i = 0; i < addresses.get(0).getMaxAddressLineIndex() ; i++ ){
                        if(i < addresses.get(0).getMaxAddressLineIndex()-1)
                        completeAddress += addresses.get(0).getAddressLine(i) + ", ";
                        else completeAddress += addresses.get(0).getAddressLine(i);
                        }
                        distanceRange.setText(completeAddress);
                        Log.i("LOCATION",completeAddress);
                        } catch (IOException e) {
                        e.printStackTrace();
                        }

                        distanceItem.setText(currItem.distanceToSelf+"");
                        offerAmount.setText("");
                        new downloadUserImageImageAsyncTask2(currItem.itemPath).execute().get();
                        counter++;
                        if(counter == itemArrayList.size()){
                        counter = 0;
                        }
                        }catch (Exception e){

                        }
                        showAnother = false;
                        }
                        public void onSwipeLeft() {
                        Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                        }
                        public void onSwipeBottom() {
                        Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                        }

                        });
                        */

        //offer.setOnClickListener(this);
        another.setOnClickListener(this);
        //go.setOnClickListener(this);

        User user = userLocalStore.getLoggedInUser();
        User registeredUser = new User(user.name, user.age, user.username, user.password, user.email, user.latitude,user.longitude,user.balance);
        //budget.setText(registeredUser.budget);


        ServerRequest serverRequest = new ServerRequest();
        serverRequest.requestItemListInBackGround(registeredUser, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

            }

            @Override
            public void json(JSONArray jsonArray) {
                int photocount = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jSon = jsonArray.getJSONObject(i);

                        String userName = jSon.getString("user_name");
                        String itemName = jSon.getString("item_name");
                        String category = jSon.getString("category");
                        String itemInfo = jSon.getString("item_info");
                        String latitude = jSon.getString("item_loc_latitude");
                        String longitude = jSon.getString("item_loc_longitude");
                        String itemPicPath = jSon.getString("item_pic_path");
                        String uploadDate = jSon.getString("upload_date");
                        String distanceJson = jSon.getString("distance");

                        User user = userLocalStore.getLoggedInUser();
                        currItem = new Item(userName, itemName, category, itemInfo, latitude, longitude);
                        currItem.distanceToSelf = distanceJson;
                        currItem.itemPath = itemPicPath;
                        itemArrayList.add(currItem);

                        if (itemArrayList.size() > 0) {

                            al.add(new Data(SERVER_ADDRESS + itemArrayList.get(photocount).itemPath, "doktor bu ne"));
                            photocount++;
                            myAppAdapter.notifyDataSetChanged();

                        }


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






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
/*

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.menu_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
*/
        Log.e("111111", "111111");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
/*
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.e("QUERY:    ", query);
        }*/
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        View view = null;

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }
        if (id == R.id.filter) {

            showDialogListView2(view);
            return true;
        }

        if (id == R.id.menu_search) {

            Log.e("sercannnnnnnnnnnn","sercannnnnnnnnnnn");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showDialogListView2(View view){

        LayoutInflater inflater=MainActivity.this.getLayoutInflater();
        view = inflater.inflate(R.layout.filterxml,null);
        final EditText et = (EditText) view.findViewById(R.id.location);
        final EditText et2 = (EditText) view.findViewById(R.id.search);
        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder builder=new
                AlertDialog.Builder(MainActivity.this);


        adapterArr = ArrayAdapter.createFromResource(this, R.array.category_names, R.layout.spinner_item);

        adapterArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sp = new Spinner(MainActivity.this);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp.setAdapter(adapterArr);
        layout.addView(view);
        layout.addView(sp);



        builder.setTitle("Filter Staffs");
        builder.setView(layout);

        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(), "Filters Applied", Toast.LENGTH_LONG).show();
                //itemArrayList.clear();
                //picture.setImageResource(android.R.color.transparent);
                counter = 0;
                User user = userLocalStore.getLoggedInUser();
                User registeredUser = new User(user.name, user.age, user.username, user.password, user.email, user.latitude, user.longitude, user.balance);
                itemArrayList.clear();
                while(al.size()>0){
                    al.remove(0);
                }
                al.add(new Data("http://www.userlib.com/wp-content/uploads/2014/10/finger_swipe2.png", "START SWIPING DONT WAIT"));
                Log.d("MEGAMIX: ", category+" " +et.getText().toString());
                ServerRequest serverRequest = new ServerRequest();
                if( et.getText().toString() == "")
                    et.setText("100000");
                serverRequest.filterRequest(registeredUser, category, et.getText().toString(),et2.getText().toString(), new GetUserCallback() {
                    @Override
                    public void done(User returnedUser) {

                    }

                    @Override
                    public void json(JSONArray jsonArray) {
                        int photocount = 0;
                        Log.d("JSONSIZE: ", jsonArray.length() + "");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jSon = jsonArray.getJSONObject(i);

                                String userName = jSon.getString("user_name");
                                String itemName = jSon.getString("item_name");
                                String category = jSon.getString("category");
                                String itemInfo = jSon.getString("item_info");
                                String latitude = jSon.getString("item_loc_latitude");
                                String longitude = jSon.getString("item_loc_longitude");
                                String itemPicPath = jSon.getString("item_pic_path");
                                String uploadDate = jSon.getString("upload_date");
                                String distanceJson = jSon.getString("distance");

                                User user = userLocalStore.getLoggedInUser();
                                currItem = new Item(userName, itemName, category, itemInfo, latitude, longitude);
                                currItem.distanceToSelf = distanceJson;
                                currItem.itemPath = itemPicPath;
                                itemArrayList.add(currItem);
                                Log.d("itemArraListy: ", itemArrayList.size() + "");
                                if (itemArrayList.size() > 0) {

                                    al.add(new Data(SERVER_ADDRESS + itemArrayList.get(photocount).itemPath, "doktor bu ne"));
                                    photocount++;
                                    myAppAdapter.notifyDataSetChanged();

                                }
                            } catch (Exception e) {
                                Log.d("itemArraListy: ", itemArrayList.size() + "");

                                e.printStackTrace();
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


            }
        });


        // builder.setView(listView);

        AlertDialog dialog=builder.create();

        dialog.show();



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
                startActivity(new Intent(this, OfferList.class));
                break;
            case 3:
                startActivity(new Intent(this, ChatActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, Contact.class));
                break;
            case 5:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 6:
                userLocalStore.clearUserData();
                startActivity(new Intent(this, Login.class));
                break;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate()) {

        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    public void showDialogListView(View view){

        AlertDialog.Builder builder=new
                AlertDialog.Builder(MainActivity.this);


        final EditText price = new EditText(this);
        String priceStr = price.getText().toString();
        builder.setTitle("Offer Price");
        builder.setView(price);
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                User currUser = userLocalStore.getLoggedInUser();
                ServerRequest offerRequest = new ServerRequest();
                Log.d("CurrUSer: ", currUser.username + sellerName.getText().toString()+ price.getText().toString() + currItem.item_name);

                try {
                    offerRequest.offerRequest(currUser.username, sellerName.getText().toString(), price.getText().toString(), currItem.item_name);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog dialog=builder.create();

        dialog.show();

    }

    private void updateUserDetails(User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                } else {
                    logUserIn(returnedUser);
                }
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



    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        //startActivity(new Intent(this, MainActivity.class));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000,0, locationListener);
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){

        User user = userLocalStore.getLoggedInUser();

    }

    @Override
    public void onClick(View v) {



        switch (v.getId()){
            /*case R.id.filterB:
                Toast.makeText(getApplicationContext(), "Filters Applied", Toast.LENGTH_LONG).show();
                itemArrayList.clear();
                //picture.setImageResource(android.R.color.transparent);
                sellerName.setText("");
                distanceRange.setText("");
                counter = 0;
                User user = userLocalStore.getLoggedInUser();
                User registeredUser = new User(user.name, user.age, user.username, user.password, user.email, user.latitude,user.longitude,user.budget);
                ServerRequest serverRequest = new ServerRequest();
                serverRequest.filterRequest(registeredUser, category, distance.getText().toString(), search.getText().toString(), new GetUserCallback() {
                    @Override
                    public void done(User returnedUser) {

                    }

                    @Override
                    public void json(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jSon =jsonArray.getJSONObject(i);
                                String userName = jSon.getString("user_name");
                                String itemName = jSon.getString("item_name");
                                String category = jSon.getString("category");
                                String distanceJson = jSon.getString("distance");
                                String itemInfo = jSon.getString("item_info");
                                String itemPicPath = jSon.getString("item_pic_path");
                                String uploadDate = jSon.getString("upload_date");
                                distanceItem.setText("Dist");
                                User user = userLocalStore.getLoggedInUser();
                                currItem = new Item(userName, itemName, category, itemInfo, user.latitude, user.longitude);
                                currItem.itemPath = itemPicPath;
                                currItem.distanceToSelf = distanceJson;
                                itemArrayList.add(currItem);

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

                break;
            */
            case R.id.dislikeButton:

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("username",currItem.user_name);
                startActivity(intent);

                showAnother = false;
                break;
            /*case R.id.offerButton:
                User currUser = userLocalStore.getLoggedInUser();
                ServerRequest Request = new ServerRequest(this);

                offerRequest.offerRequest(currUser.username, sellerName.getText().toString(), offerAmount.getText().toString(), distanceRange.getText().toString());
                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, PayPal_selection.class));
                break;*/
        }
    }


    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;


    }


    public class MyAppAdapter extends BaseAdapter {


        public List<Data> parkingList;
        public Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");

            Glide.with(MainActivity.this).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);

            return rowView;
        }
    }



    public class downloadUserImageImageAsyncTask2 extends AsyncTask<Void, Void, Bitmap> {

        String item_path;
        public downloadUserImageImageAsyncTask2( String name) {
            this.item_path = name;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url = SERVER_ADDRESS + item_path;

            try{
                URLConnection connection = new URL(url).openConnection() ;
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setReadTimeout(CONNECTION_TIMEOUT);
                return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null) {

            }
        }
    }


}
