/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.pantelwar.hellharv.tvapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    ImageView logoImageView;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, counterTextView;

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;

    String url = "http://ec2-52-37-127-67.us-west-2.compute.amazonaws.com:8080/template1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoImageView = (ImageView) findViewById(R.id.logoImageView);

        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        counterTextView = (TextView) findViewById(R.id.counterTextView);

        RequestTask requestTask = new RequestTask();
        try {
            String response = requestTask.execute(url).get();
            extractJSONResponse(response);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    protected class RequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            InputStream stream = null;

            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                stream = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);

                String result = reader.readLine();

                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    public void extractJSONResponse(String json) {

        try {

            JSONArray jsonArray = new JSONArray(json);

            JSONObject jsonObject = jsonArray.getJSONObject(0);

            String color1 = jsonObject.getString("color1");
            String color2 = jsonObject.getString("color2");
            String color3 = jsonObject.getString("color3");
            String color4 = jsonObject.getString("color4");


            String text1 = jsonObject.getString("text1");
            String text2 = jsonObject.getString("text2");
            String text3 = jsonObject.getString("text3");
            String text4 = jsonObject.getString("text4");
            String text5 = jsonObject.getString("text5");
            String text6 = jsonObject.getString("text6");

            String counter = jsonObject.getString("counter");

            String imageUrl = jsonObject.getString("imgUrl");

            linearLayout1.setBackgroundColor(Color.parseColor(color1));
            linearLayout2.setBackgroundColor(Color.parseColor(color2));
            linearLayout3.setBackgroundColor(Color.parseColor(color3));
            linearLayout4.setBackgroundColor(Color.parseColor(color4));

            textView1.setText(text1);
            textView2.setText(text2);
            textView3.setText(text3);
            textView4.setText(text4);
            textView5.setText(text5);
            textView6.setText(text6);

            counterTextView.setText(counter);

            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(logoImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}