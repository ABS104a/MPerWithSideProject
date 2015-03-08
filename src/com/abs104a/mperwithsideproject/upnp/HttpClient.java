package com.abs104a.mperwithsideproject.upnp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * Http�ڑ������āAbyte����擾����֐�
 * ��ɃC���^�[�l�b�g����Twitter�̃A�C�R�����擾���邽�߂Ɏg�p����
 */
public class HttpClient {
    public static byte[] getByteArrayFromURL(String strUrl) {
        byte[] byteArray = new byte[1024];
        byte[] result = null;
        HttpURLConnection con = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        int size = 0;
        try {
            URL url = new URL(strUrl);
            con = (HttpURLConnection) url.openConnection();//�ڑ�
            con.setRequestMethod("GET");
            con.connect();
            Map<String, List<String>> header = con.getHeaderFields();
            try{
            	if(header.containsKey("Location")){
            		url = new URL(header.get("Location").get(0));
            		if (con != null)
                        con.disconnect();
            		con = (HttpURLConnection) url.openConnection();//�ڑ�
                    con.setRequestMethod("GET");
                    con.connect();
            	}
            }catch(Exception e){
            	e.printStackTrace();
            }
            in = con.getInputStream();

            out = new ByteArrayOutputStream();
            while ((size = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, size);
            }
            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.disconnect();
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap getImage(String url) {
        byte[] byteArray = getByteArrayFromURL(url);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}