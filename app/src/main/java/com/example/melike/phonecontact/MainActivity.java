package com.example.melike.phonecontact;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // The ListView
    private ListView listview;
    Button backup;
    Button recovery;
    private ArrayList<ContactItem> contactsList;
    public ArrayList<ContactItem> list1;
    public ArrayList<ContactItem> list2;
    public ArrayList<ContactItem> list3;
    public ArrayList<ContactItem> list4;
    RadioButton all;
    RadioButton vod;
    RadioButton turk;
    RadioButton avea;
    CustomListAdapter adapter1;
    CustomListAdapter adapter2;
    CustomListAdapter adapter3;
    CustomListAdapter adapter4;
    CustomListAdapter recoveryAdapter;
    someJson sj = new someJson();

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 201;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 302;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 403;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the list view
        listview = (ListView) findViewById(R.id.listView);
        backup = (Button) findViewById(R.id.backupButton);
        recovery = (Button)findViewById(R.id.recoveryButton);
        all = (RadioButton)findViewById(R.id.all);
        vod = (RadioButton)findViewById(R.id.rdBtnVod);
        turk = (RadioButton)findViewById(R.id.rdBtnTurk);
        avea = (RadioButton)findViewById(R.id.rdBtnAvea);
        listview.setOnItemClickListener(this);

        showContacts();

        ///BACKUP CLICK ACTION

        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&& checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    if(all.isChecked())
                        sj.writeToFile(getApplicationContext(),contactsList);
                    else if(vod.isChecked())
                        sj.writeToFile(getApplicationContext(),list2);
                    else if(turk.isChecked())
                        sj.writeToFile(getApplicationContext(),list3);
                    else if(avea.isChecked())
                        sj.writeToFile(getApplicationContext(),list1);

                }

            }
        });

        // RECOVERY CLIK ACTION

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&& checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else
                {
                    // Read backup json and set new list
                    ArrayList<ContactItem> recoveryList = sj.readFromFile(getApplicationContext());
                    recoveryAdapter = new CustomListAdapter(MainActivity.this, R.layout.content_list,recoveryList);
                    listview.setAdapter(recoveryAdapter);
                }

            }
        });




    }

    public void doAbout(MenuItem item){
        Toast.makeText(this, "Melike Karabulut", Toast.LENGTH_SHORT).show();
    }



    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&& checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            /*
            List<String> contacts = getContactNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            lstNames.setAdapter(adapter);
            */
            ArrayList contacts = getContact();
            listview.setAdapter(new CustomListAdapter(this, R.layout.content_list, contacts));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                    Object o = listview.getItemAtPosition(position);
                    ContactItem contactData = (ContactItem) o;
                    Call(contactData.getNumber().toString());
                    //Toast.makeText(MainActivity.this, "Selected :" + " " + contactData.getName(), Toast.LENGTH_SHORT).show();
                }


            });


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS  || requestCode == PERMISSIONS_REQUEST_CALL_PHONE
                || requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE || requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            } else {
                Toast.makeText(this, "Access denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private ArrayList getContact() {


        contactsList = new ArrayList<ContactItem>();
        list1 = new ArrayList<ContactItem>();
        list2 = new ArrayList<ContactItem>();
        list3 = new ArrayList<ContactItem>();
        list4 = new ArrayList<ContactItem>();
        ContentResolver cr = getContentResolver();


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");


        while (cursor.moveToNext())
        {
            // Get the contacts name
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContactItem ci = new ContactItem();
            ci.setName(name);
            ci.setNumber(num);
            contactsList.add(ci);

            adapter4 = new CustomListAdapter(
                    MainActivity.this, R.layout.content_list,contactsList);

            if (num.substring(0,3).equals("050")||num.substring(0,3).equals("055")||num.substring(0,5).equals("+90 55")){
                String sub_phone = num.substring(2,5);
                // Log.d("Test", sub_phone);
                list1.add(ci);

                adapter1 = new CustomListAdapter(
                        MainActivity.this, R.layout.content_list,list1);


            }
            if (num.substring(0,3).equals("054")||num.substring(0,5).equals("+90 54")){
                String sub_phone = num.substring(2,5);
                list2.add(ci);
                adapter2 = new CustomListAdapter(
                        MainActivity.this, R.layout.content_list,list2);

            }
            if (num.substring(0,3).equals("053")||num.substring(0,5).equals("+90 53")){
                String sub_phone = num.substring(2,5);
                list3.add(ci);
                adapter3 = new CustomListAdapter(
                        MainActivity.this, R.layout.content_list,list3);

            }


        }


        cursor.close();

        return contactsList;
    }


    public void clickAvea(View v){


        listview.setAdapter(adapter1);
    }
    public void clickVodafone(View v){


        listview.setAdapter(adapter2);
    }
    public void clickTurkcell(View v){


        listview.setAdapter(adapter3);
    }
    public void clickAll(View v){


        listview.setAdapter(adapter4);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void Call(String Number){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+Number));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&& checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {

            startActivity(callIntent);
        }


    }

    public void fileSavePer(){


    }
}