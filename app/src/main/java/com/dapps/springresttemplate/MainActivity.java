package com.dapps.springresttemplate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.dapps.springresttemplate.adapters.ContactAdapter;
import com.dapps.springresttemplate.models.Contact;
import com.dapps.springresttemplate.models.ContactsResponse;
import com.google.gson.Gson;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import static com.dapps.springresttemplate.springhelper.Helper.getRestTemplate;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_horizontal, recycler_vertical, recycler_grid;
    private List<Contact> responseContactsList = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        recycler_horizontal = findViewById(R.id.recycler_horizontal);
        recycler_vertical = findViewById(R.id.recycler_vertical);
        recycler_grid = findViewById(R.id.recycler_grid);

        if (isOnline(context)) {
            /* calling API*/
            new GetContacts().execute();
        } else {
            Toast.makeText(context, "No internet connection!!!", Toast.LENGTH_SHORT).show();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_horizontal.setLayoutManager(linearLayoutManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_vertical.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
        recycler_grid.setLayoutManager(gridLayoutManager);

    }

    @SuppressLint("StaticFieldLeak")
    private class GetContacts extends AsyncTask<Void, Void, ContactsResponse> {
        ProgressDialog progressDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ContactsResponse doInBackground(Void... voids) {
            try {
                String CONTACTS_URL = "https://api.androidhive.info/contacts/";
                /* get method = getForEntity, getForObject
                 put method = putForEntity*/
                ResponseEntity<ContactsResponse> responseContactResponseEntity = getRestTemplate().getForEntity(CONTACTS_URL, ContactsResponse.class);
                return responseContactResponseEntity.getBody();
            } catch (HttpStatusCodeException e) {
                String response = e.getResponseBodyAsString();
                return new Gson().fromJson(response, ContactsResponse.class);
            }
        }


        @Override
        protected void onPostExecute(ContactsResponse contactsResponse) {
            super.onPostExecute(contactsResponse);
            Log.i("TAG", "result--" + contactsResponse.toString());
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (contactsResponse != null) {
                for (Contact contact : contactsResponse.getContacts()) {
                    String name = contact.getName();
                    String email = contact.getEmail();
                    responseContactsList.add(contact);
                    ContactAdapter contactAdapter = new ContactAdapter(context, responseContactsList);
                    recycler_vertical.setAdapter(contactAdapter);
                    recycler_horizontal.setAdapter(contactAdapter);
                    recycler_grid.setAdapter(contactAdapter);
                }
            }
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
