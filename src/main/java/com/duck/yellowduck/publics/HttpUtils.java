package com.duck.yellowduck.publics;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpUtils
{
    public static String sendGet(String url, String param)
            throws IOException
    {
        param = URLEncoder.encode(param, "utf-8");
        String result = "";
        BufferedReader in = null;
        try
        {
            String urlNameString = url + "/" + param;
            URL realUrl = new URL(urlNameString);

            URLConnection connection = realUrl.openConnection();

            connection.setRequestProperty("accept", "*/*");

            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
            return result;
        }
        catch (Exception e)
        {
            System.out.println("发送 GET 请求出现异常" + e);
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGet(String url, Map<String, String> params, Integer type)
    {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            String content = "";
            if (type.intValue() == 1)
            {
                content = mpPost(params);
                content = content.substring(1, content.length());
                url = url + "?" + content;
            }
            else if (type.intValue() == 2)
            {
                content = mpGet(params);

                url = url + content;
            }
            URL realUrl = new URL(url);

            URLConnection connection = realUrl.openConnection();

            connection.setRequestProperty("accept", "*/*");

            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            System.out.println("发送 GET 请求出现异常" + e);
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String post(String url, Map<String, String> params)
    {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        DataOutputStream out = null;
        try
        {
            URL postUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection)postUrl.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(300000);

            connection.setRequestMethod("POST");

            connection.setUseCaches(false);

            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());

            String content = mpPost(params);
            content = content.substring(1, content.length());

            out.writeBytes(content);

            out.flush();
            out.close();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String sendPost(String url, String params)
    {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection)realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            out.write(params);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
            return result;
        }
        catch (Exception e)
        {
            System.out.println("发送 POST 请求出现异常" + e);
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
         return result;
    }

    private static String mpPost(Map<String, String> maps)
            throws UnsupportedEncodingException
    {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> entries = maps.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, String> entry = (Map.Entry)iterator.next();
            sb.append("&" + (String)entry.getKey() + "=" + URLEncoder.encode((String)entry.getValue(), "utf-8"));
        }
        return sb.toString();
    }

    private static String mpGet(Map<String, String> maps)
            throws Exception
    {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> entries = maps.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, String> entry = (Map.Entry)iterator.next();
            sb.append("/" + URLEncoder.encode((String)entry.getValue(), "utf-8"));
        }
        return sb.toString();
    }
}
