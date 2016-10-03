package com.example2.diablove.login_register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Diablove on 11/30/2015.
 */
public class ServerRequest {

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid;
    String msg;

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://ec2-54-201-231-40.us-west-2.compute.amazonaws.com/";//http://diabloves.comli.com/";
    public String sortarr[];
    public ArrayList<Integer> sortedarr;
    public ServerRequest(){}

    public ServerRequest(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback){
        progressDialog.show();
        new fetchUserDataAsyncClass(user, callback).execute();
    }

    public void requestItemListInBackGround(User user, GetUserCallback callback){

        try
        {
            new requestItemListAsyncClass(user, callback).execute().get();
        }
        catch (Exception e)
        {

        }
    }

    public void filterRequest(User user,String category, String distance, String search, GetUserCallback callback){

        try
        {
            new filterRequestAsyncClass(user,category,distance,search, callback).execute().get();
        }
        catch (Exception e)
        {

        }
    }

    public void uploadUserImage(User user, Bitmap bitmap, GetUserCallback callback){
        new uploadImageAsyncTask( bitmap, user.username, callback).execute();
    }

    public void uploadItemWithImage(Item item, Bitmap bitmap, GetUserCallback callback){

        try {
            new uploadItemWithImageAsyncTask(bitmap, item, callback).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void offerRequest(String offerer, String seller, String price,String itemName) throws ExecutionException, InterruptedException {
        new offerRequestAsyncTask(offerer, seller ,price, itemName).execute().get();
    }

    public void showOffersRequests(User user, GetUserCallback callback){
        try
        {
            new showOffersAsyncClass(user,callback).execute().get();
        }
        catch (Exception e)
        {

        }
    }

    public void acceptOfferRequest(Offer offer, GetUserCallback callback){
        try
        {
            new acceptOfferAsynClass(offer,callback).execute().get();
        }
        catch (Exception e)
        {

        }
    }

    public void showMessagesRequests(User user, GetUserCallback callback){
        try
        {
            new showMessagesAsyncClass(user,callback).execute().get();
        }
        catch (Exception e)
        {

        }
    }

    public void sendMessageRequest(User user, String username, String description, GetUserCallback callback){

        new sendMessageRequestAsyncClass(user, username,description, callback).execute();
    }


    public class showMessagesAsyncClass extends AsyncTask<Void, Void, JSONArray > {

        User user;
        JSONArray jsonArrayTemp = null;
        GetUserCallback userCallback;

        public showMessagesAsyncClass(User user,GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "ShowMessages.php");

            try
            {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                jsonArrayTemp = new JSONArray(result);
                return jsonArrayTemp;

            }catch (Exception e) {
                Log.e("itemNum", "catch");
                e.printStackTrace();
            }
            return jsonArrayTemp;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            userCallback.json(jsonArray);
        }



    }

    public class showOffersAsyncClass extends AsyncTask<Void, Void, JSONArray > {

        User user;
        JSONArray jsonArrayTemp = null;
        GetUserCallback userCallback;

        public showOffersAsyncClass(User user,GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "ShowOffers.php");

            try
            {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                jsonArrayTemp = new JSONArray(result);
                return jsonArrayTemp;

            }catch (Exception e) {
                Log.e("itemNum", "catch");
                e.printStackTrace();
            }
            return jsonArrayTemp;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            userCallback.json(jsonArray);
        }



    }

    public class offerRequestAsyncTask extends AsyncTask<Void, Void, Void >{

        String offerer, seller, price, itemName;

        GetUserCallback userCallback;

        public offerRequestAsyncTask(String offerer, String seller, String price, String itemName){
            this.offerer = offerer;
            this.seller = seller;
            this.price = price;
            this.itemName = itemName;
        }

        @Override
        protected Void doInBackground(Void... params) {


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("sellBy", seller));
            dataToSend.add(new BasicNameValuePair("offerBy", offerer));
            dataToSend.add(new BasicNameValuePair("price", price));
            dataToSend.add(new BasicNameValuePair("itemName", itemName));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "Offer.php");
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }

    public class uploadItemWithImageAsyncTask extends AsyncTask <Void, Void, Void>{

        Bitmap image;
        Item item;
        GetUserCallback callback;
        boolean successful = false;

        public uploadItemWithImageAsyncTask(Bitmap image, Item item, GetUserCallback callback) {
            this.image = image;
            this.item = item;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("username", item.user_name));
            dataToSend.add(new BasicNameValuePair("category", item.category));
            dataToSend.add(new BasicNameValuePair("item_name", item.item_name));
            dataToSend.add(new BasicNameValuePair("item_info", item.item_info));
            dataToSend.add(new BasicNameValuePair("item_pic", encodedImage));
            dataToSend.add(new BasicNameValuePair("latitude", item.item_loc_latitude));
            dataToSend.add(new BasicNameValuePair("longitude", item.item_loc_longitude));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post= new HttpPost(SERVER_ADDRESS + "Sell.php");

            try {

                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
                successful = true;

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            callback.flagged(successful);
            //Toast.makeText(null,"You uploaded image" , Toast.LENGTH_LONG ).show();

        }
    }

    public class uploadImageAsyncTask extends AsyncTask <Void, Void, Void>{

        Bitmap image;
        String name;
        GetUserCallback userCallback;
        boolean successful = false;

        public uploadImageAsyncTask(Bitmap image, String name, GetUserCallback userCallback) {
            this.image = image;
            this.name = name;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user_pic", encodedImage));
            dataToSend.add(new BasicNameValuePair("username", name));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post= new HttpPost(SERVER_ADDRESS + "SavePicture.php");

            try {

                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
                successful = true;

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            userCallback.flagged(true);
            //Toast.makeText(null,"You uploaded image" , Toast.LENGTH_LONG ).show();

        }
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void >{

        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback){
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("age", user.age + ""));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));
            dataToSend.add(new BasicNameValuePair("email", user.email));
            dataToSend.add(new BasicNameValuePair("latitude", user.latitude));
            dataToSend.add(new BasicNameValuePair("longitude", user.longitude));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);

            super.onPostExecute(aVoid);
        }
    }




    public class fetchUserDataAsyncClass extends AsyncTask<Void, Void, User > {

        User user;
        GetUserCallback userCallback;


        public fetchUserDataAsyncClass(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));
            dataToSend.add(new BasicNameValuePair("latitude", user.latitude));
            dataToSend.add(new BasicNameValuePair("longitude", user.longitude));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");
            User returnedUser = null;

            try{

                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();

                String result = EntityUtils.toString(entity);
                //Log.e(result," ");
                //Log.e(result.length()+"",""+result.length());
                JSONObject jObject = new JSONObject(result);

                Log.e("img", jObject.toString());

                if(jObject.length() == 0){
                    returnedUser = null;
                }else{
                    String name = jObject.getString("name");
                    int age = jObject.getInt("age");
                    String email = jObject.getString("email");
                    String latitude = jObject.getString("latitude");
                    String longitude = jObject.getString("longitude");
                    String balance = jObject.getString("balance");
                    Log.d("Balance:", balance);

                    returnedUser = new User(name,age,user.username, user.password, email, latitude, longitude,balance);
                }

            }catch (Exception e){
                e.printStackTrace();

            }

            return returnedUser;
        }


        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }

    }

    public class requestItemListAsyncClass extends AsyncTask<Void, Void, JSONArray > {

        User user;
        GetUserCallback userCallback;
        JSONArray jsonArrayTemp = null;


        public requestItemListAsyncClass(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }




        @Override
        protected JSONArray doInBackground(Void... params) {



            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("latitude", user.latitude));
            dataToSend.add(new BasicNameValuePair("longitude", user.longitude));
            Log.e("" + user.latitude, "" + user.longitude);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchAllItemData.php");



            try
            {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                jsonArrayTemp = new JSONArray(result);
                return jsonArrayTemp;

            }catch (Exception e) {
                Log.e("itemNum", "catch");
                e.printStackTrace();
            }
            return jsonArrayTemp;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            userCallback.json(jsonArray);
        }



    }

    public class filterRequestAsyncClass extends AsyncTask<Void, Void, JSONArray > {

        User user;
        String category,distance,searchName;
        GetUserCallback userCallback;
        JSONArray jsonArrayTemp = null;


        public filterRequestAsyncClass(User user, String category, String distance, String searchName, GetUserCallback userCallback) {
            this.user = user;
            this.category = category;
            this.distance = distance;
            this.searchName = searchName;
            this.userCallback = userCallback;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {



            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("filter_category", category));
            dataToSend.add(new BasicNameValuePair("filter_distance", distance));
            dataToSend.add(new BasicNameValuePair("search_name", searchName));
            dataToSend.add(new BasicNameValuePair("latitude", user.latitude));
            dataToSend.add(new BasicNameValuePair("longitude", user.longitude));

            Log.e("cat", category);
            Log.e("dist ",distance);
            Log.e("sn", searchName);
            Log.e("arsize", user.latitude);
            Log.e("arsize",user.longitude);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "Filter.php");



            try
            {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                jsonArrayTemp = new JSONArray(result);
                return jsonArrayTemp;

            }catch (Exception e) {
                Log.e("itemNum", "catch");
                e.printStackTrace();
            }
            return jsonArrayTemp;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            Log.e("arsize", jsonArray.length() + "");
            userCallback.json(jsonArray);
        }



    }

    public class acceptOfferAsynClass extends AsyncTask<Void, Void, Void > {

        Offer offer;
        GetUserCallback userCallback;
        public acceptOfferAsynClass(Offer offer, GetUserCallback userCallback) {
            this.offer = offer;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {



            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("itemName", offer.itemName));
            dataToSend.add(new BasicNameValuePair("sellBy", offer.sellBy));
            dataToSend.add(new BasicNameValuePair("offerBy", offer.offerBy));
            dataToSend.add(new BasicNameValuePair("price", offer.price));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT * 10);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "Balance.php");



            try
            {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e) {
                Log.e("itemNum", "catch");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            userCallback.done(null);

            super.onPostExecute(aVoid);
        }



    }

    public class sendMessageRequestAsyncClass extends AsyncTask <Void, Void, Void>{

        String otheruser;
        String description;
        User user;
        GetUserCallback callback;

        public sendMessageRequestAsyncClass(User user, String otheruser ,String description , GetUserCallback callback) {
            this.user = user;
            this.callback = callback;
            this.otheruser = otheruser;
            this.description = description;
        }

        @Override
        protected Void doInBackground(Void... params) {


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("sender", user.username));
            dataToSend.add(new BasicNameValuePair("receiver", otheruser));
            dataToSend.add(new BasicNameValuePair("message", description));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post= new HttpPost(SERVER_ADDRESS + "Message.php");

            try {

                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

        }
    }


}
