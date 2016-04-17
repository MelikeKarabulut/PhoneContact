
/**
 * Created by Melike on 17.04.2016.
 */package com.example.melike.phonecontact;

        import android.content.Context;
        import android.widget.Toast;

        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.google.gson.reflect.TypeToken;

        import java.io.BufferedReader;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.lang.reflect.Type;
        import java.util.ArrayList;

/**
 * Created by siyah-pc on 17.4.2016.
 */
public class someJson {

    Gson gson = new Gson();


    public void writeToFile(Context context, ArrayList<ContactItem> list) {


        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("PhoneContact.json", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING));
            outputStreamWriter.write(gson.toJson(list));
            outputStreamWriter.close();
            Toast.makeText(context,"Backup Successful",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(context,"Backup Error",Toast.LENGTH_SHORT).show();
        }

    }


    public ArrayList<ContactItem> readFromFile(Context context) {

        ArrayList<ContactItem> list = new ArrayList<ContactItem>();
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("PhoneContact.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Type listType = new TypeToken<ArrayList<ContactItem>>(){}.getType();
                list = new GsonBuilder().create().fromJson(ret, listType);
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(context,"cant found backup",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context,"Recovery Error",Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context,"Recovery  Successful",Toast.LENGTH_SHORT).show();
        return list;
    }}
