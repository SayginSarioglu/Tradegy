package com.example2.diablove.login_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.net.URL;

public class Sell extends AppCompatActivity implements View.OnClickListener {

    String category;
    TextView itemname, iteminfo;
    EditText itemnameet, iteminfoet;
    UserLocalStore userLocalStore;
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public ImageView picture;
    Spinner spinner;
    private Button camImageButton;
    private Button galleryImageButton;
    private Button sellButton;

    private static final int SELECT_PICTURE = 1,SELECT_CAMERA = 18, RESULT_LOAD_IMAGE = 1;
    ArrayAdapter<CharSequence> adapterArr;

    Bitmap bmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        itemname = (TextView) findViewById(R.id.textView2);
        iteminfo = (TextView) findViewById(R.id.textView3);
        itemnameet = (EditText) findViewById(R.id.editText);
        iteminfoet = (EditText) findViewById(R.id.editText2);

        iteminfoet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        itemnameet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userLocalStore = new UserLocalStore(this);
        camImageButton = (Button) findViewById(R.id.uploadButtoCamera);
        galleryImageButton = (Button) findViewById(R.id.uploadPhotoGallery);
        sellButton = (Button) findViewById(R.id.sellbutton);

        picture = (ImageView) findViewById(R.id.photo);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapterArr = ArrayAdapter.createFromResource(this, R.array.category_names, R.layout.spinner_item );

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
        });

        sellButton.setOnClickListener(this);
        camImageButton.setOnClickListener(this);
        galleryImageButton.setOnClickListener(this);

        picture.buildDrawingCache();
        bmap = picture.getDrawingCache();



        User user = userLocalStore.getLoggedInUser();
        User registeredUser = new User(user.name, user.age, user.username, user.password, user.email, user.latitude, user.longitude, user.balance);


        updateUserDetails(registeredUser);
        userLocalStore.storeUserData(registeredUser);
        userLocalStore.setUserLoggedIn(true);

        ServerRequest serverRequest = new ServerRequest();



        listViewSliding = (ListView) findViewById(R.id.iv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();

        listSliding.add(new ItemSlideMenu(R.drawable.sell, "Sell"));
        listSliding.add(new ItemSlideMenu(R.drawable.profile, "Profile"));
        listSliding.add(new ItemSlideMenu(R.drawable.category, "Show Offers"));
        listSliding.add(new ItemSlideMenu(R.drawable.chat, "Chat"));
        listSliding.add(new ItemSlideMenu(R.drawable.contactus, "Contact Us"));
        listSliding.add(new ItemSlideMenu(R.drawable.logoicon, "Main Activity"));
        listSliding.add(new ItemSlideMenu(R.drawable.logout, "Log Out"));


        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sell");

        listViewSliding.setItemChecked(0, true);
        drawerLayout.closeDrawer(listViewSliding);
        replaceFragment(0);


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


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag: ", requestCode+" "+ resultCode+ data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            picture.setImageURI(selectedImage);
        }
        else if (requestCode == SELECT_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            picture.setImageBitmap(thumbnail);
        }

    }

    private void SetImage(Bitmap image) {
        this.picture.setImageBitmap(image);

        // upload
        String imageData = encodeTobase64(image);

    }

    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image == null)return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 0, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        //startActivity(new Intent(this, MainActivity.class));
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
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
        //TextView textView = (TextView) findViewById(R.id.textView);


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
    public void onClick(View v) {
        Bitmap image;
        User user = userLocalStore.getLoggedInUser();
        switch (v.getId()){

            case R.id.uploadPhotoGallery:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_PICTURE);
                break;
            case R.id.uploadButtoCamera:
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, SELECT_CAMERA);
                break;
            case R.id.sellbutton:

                image = ((BitmapDrawable) picture.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bitmapdata = baos.toByteArray();
                image = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);

                Item item = new Item(user.username, itemnameet.getText().toString(), category, iteminfoet.getText().toString(), user.latitude, user.longitude );
                ServerRequest serverRequest = new ServerRequest();
                serverRequest.uploadItemWithImage(item, image, new GetUserCallback() {
                    @Override
                    public void done(User returnedUser) {

                    }

                    @Override
                    public void json(JSONArray jsonArray) {

                    }

                    @Override
                    public void flagged(boolean flag) {
                        if (flag == true) {
                            Toast.makeText(Sell.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Sell.this, MainActivity.class));
                            finish();
                        } else {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Sell.this);
                            dialogBuilder.setMessage("Image Upload Failed. Try uploading again");
                            dialogBuilder.setPositiveButton("OK", null);
                            dialogBuilder.show();
                        }
                    }

                    @Override
                    public void imgData(Bitmap image) {
                    }
                });
                break;

        }
    }


}
