package com.example2.diablove.login_register;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


import java.net.URL;

public class Profile extends AppCompatActivity implements View.OnClickListener  {


    TextView username,name,email,balance;
    UserLocalStore userLocalStore;
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public ImageView picture;
    private static final String baseUrlForImage = "http://192.168.0.2/php/images/";
    private Button selectImageButton;
    private Button uploadImageButton;

    private static final int SELECT_PICTURE = 1, RESULT_LOAD_IMAGE = 1;

    Bitmap bmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = (TextView) findViewById(R.id.username);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        balance = (TextView) findViewById(R.id.balance);
        userLocalStore = new UserLocalStore(this);
        selectImageButton = (Button) findViewById(R.id.uploadButton);
        uploadImageButton = (Button) findViewById(R.id.button);
        picture = (ImageView) findViewById(R.id.photo);

        selectImageButton.setOnClickListener(this);
        uploadImageButton.setOnClickListener(this);

        picture.buildDrawingCache();
        bmap = picture.getDrawingCache();

        setTitle("Profile");

        User user = userLocalStore.getLoggedInUser();
        User registeredUser = new User(user.name, user.age, user.username, user.password, user.email, user.latitude, user.longitude, user.balance);


        updateUserDetails(registeredUser);
        userLocalStore.storeUserData(registeredUser);
        userLocalStore.setUserLoggedIn(true);
        displayUserDetails();



        listViewSliding = (ListView) findViewById(R.id.iv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();

        listSliding.add(new ItemSlideMenu(R.drawable.sell, "Sell"));
        listSliding.add(new ItemSlideMenu(R.drawable.profile, "Profile"));
        listSliding.add(new ItemSlideMenu(R.drawable.category, "Show Offers"));
        listSliding.add(new ItemSlideMenu(R.drawable.chat, "Char"));
        listSliding.add(new ItemSlideMenu(R.drawable.contactus, "Contact Us"));
        listSliding.add(new ItemSlideMenu(R.drawable.logoicon, "Main Menu"));
        listSliding.add(new ItemSlideMenu(R.drawable.logout, "Log Out"));


        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Log.d("anasn", "asdas");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            picture.setImageURI(selectedImage);
        }
    }



    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image == null)return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

    }

    private void updateUserDetails(User user){


        ServerRequest serverRequest = new ServerRequest(this);

        serverRequest.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                } else {
                    logUserIn(returnedUser);
                    displayUserDetails();
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


    private void displayUserDetails(){

        User user = userLocalStore.getLoggedInUser();
        Log.d("USER", user.username + user.balance + user.longitude);
        username.setText("Welcome " + user.username + "!");
        name.setText(user.name);
        email.setText(user.email);
        balance.setText("Budget: "  +  user.balance);
        new downloadUserImageImageAsyncTask(user.username).execute();


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

            case R.id.uploadButton:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_PICTURE);
                break;
            case R.id.button:
                image = ((BitmapDrawable) picture.getDrawable()).getBitmap();


                ServerRequest serverRequest = new ServerRequest();
                serverRequest.uploadUserImage(user, image, new GetUserCallback() {
                    @Override
                    public void json(JSONArray jsonArray) {

                    }

                    @Override
                    public void done(User returnedUser) {

                    }

                    @Override
                    public void flagged(boolean flag) {
                        if (flag == true) {
                            Toast.makeText(Profile.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Profile.this);
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

    public static final String SERVER_ADDRESS = "http://ec2-54-201-231-40.us-west-2.compute.amazonaws.com/";//http://diabloves.comli.com/";

    public class downloadUserImageImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        String name;
        public downloadUserImageImageAsyncTask( String name) {
            this.name = name;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url = SERVER_ADDRESS + "pictures/" + name + ".JPG";
            try{
                URLConnection connection = new URL (url).openConnection() ;
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000*30);
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
            if(bitmap != null){
                picture.setImageBitmap(bitmap);
            }
        }
    }

}
