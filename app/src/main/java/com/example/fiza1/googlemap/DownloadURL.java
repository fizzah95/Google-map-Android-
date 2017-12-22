package com.example.fiza1.googlemap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Fiza1 on 15-Dec-17.
 */

public class DownloadURL {

    public String readURL(String url)throws IOException{
        String data="";
        InputStream inputStream=null;
        HttpURLConnection urlConnection=null;
        try {
            URL url1= new URL(url);
            urlConnection=(HttpURLConnection)url1.openConnection();
            urlConnection.connect();

            inputStream=urlConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer=new StringBuffer();

            String line="";
            while((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }

            data=stringBuffer.toString();
            bufferedReader.close();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
