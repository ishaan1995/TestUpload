package com.ishaan.production.fileupload;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.zip.Deflater;

import bolts.Task;

public class MainActivity extends AppCompatActivity {
    int FILE_CODE;
    String compFile;
    String desc;
    EditText descr;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton upload = (ImageButton) findViewById(R.id.upload);
        descr=(EditText)findViewById(R.id.desc);
        desc=descr.getText().toString();
        if(desc.isEmpty()){
            Log.d("ishaan","here");
            desc="hello";

        }
        Log.d("ishaan","Desc is: "+desc);


        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
                // This works if you defined the intent filter
                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                // Configure initial directory by specifying a String.
                // You could specify a String like "/storage/emulated/0/", but that can
                // dangerous. Always use Android's API calls to get paths to the SD-card or
                // internal memory.
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, FILE_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above

                ClipData clip = data.getClipData();

                if (clip != null) {
                    for (int i = 0; i < clip.getItemCount(); i++) {
                        Uri uri = clip.getItemAt(i).getUri();
                        // Do something with the URI
                        Log.d("ishaan","URI: "+uri);
                        Log.d("ishaan","URI2: "+uri.getEncodedPath());
                        Log.d("ishaan","URI3: "+uri.getPath());

                    }
                }
                // For Ice Cream Sandwich


            } else {
                Uri uri = data.getData();

                spinner.setVisibility(View.VISIBLE);

                //String extension = uri.toString().substring(uri.toString().lastIndexOf("."));
                //Log.d("path","Extension is: "+extension);
                // Do something with the URI

                String abc= uri.getLastPathSegment().toString();
                String[] ext=abc.split("\\.");
                String extension=ext[ext.length-1];
                Log.d("extension", "ext is: " + ext[ext.length - 1]);
                if(extension.equals("pdf")){
                    Toast.makeText(getApplicationContext(),"acceptable",Toast.LENGTH_SHORT).show();
                    FileInputStream fileInputStream=null;
                    File f= new File(uri.getPath());
                    



                    final ParseFile file = new ParseFile(f);
                    
                   file.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            // Handle success or failure here ..
                            if (e == null) {

                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                                Log.d("ishaan","uploadede url: "+file.getUrl());

                            } else {
                                Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("ishaan-parse", "" + e.getMessage());
                                e.printStackTrace();
                                Log.d("ishaan-parse",""+e.getCode());
                            }
                            spinner.setVisibility(View.GONE);
                        }
                    }, new ProgressCallback() {
                        public void done(Integer percentDone) {
                            // Update your progress spinner here. percentDone will be between 0 and 100.
                            //spinner.incrementProgressBy(percentDone);
                            Log.d("perc", "Done:" + percentDone);
                        }
                    });


                    ParseObject files = new ParseObject("Files");
                    files.put("file_link", file);
                    //files.put("compr_data",compFile);

                    files.put("file_name",abc);
                    files.put("file_desc",desc);
                    files.saveInBackground();

                }
                else{

                    Toast.makeText(getApplicationContext(),"Error!!Choose a valid pdf file",Toast.LENGTH_SHORT).show();
                    File f= new File(uri.getPath());
                    final ParseFile file = new ParseFile(f);

                    file.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            // Handle success or failure here ..
                            if (e == null) {

                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                                Log.d("ishaan","uploadede url: "+file.getUrl());

                            } else {
                                Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("ishaan-parse", "" + e.getMessage());
                                e.printStackTrace();
                                Log.d("ishaan-parse",""+e.getCode());
                            }
                            spinner.setVisibility(View.GONE);
                        }
                    }, new ProgressCallback() {
                        public void done(Integer percentDone) {
                            // Update your progress spinner here. percentDone will be between 0 and 100.
                            //spinner.incrementProgressBy(percentDone);
                            Log.d("perc", "Done:" + percentDone);
                        }
                    });


                    ParseObject files = new ParseObject("Files");
                    files.put("file_link", file);
                    //files.put("compr_data",compFile);

                    files.put("file_name",abc);
                    files.put("file_desc",desc);
                    files.saveInBackground();

                }


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public String base64(byte[] array)
    {
        return android.util.Base64.encodeToString(array, Base64.DEFAULT);
    }

}
