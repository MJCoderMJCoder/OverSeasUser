package com.ltt.overseasuser.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.net.URISyntaxException;

/**
 * Created by yunwen on 2018/5/10.
 */

public class FileUtils {

    //转换本地资源成绝对路径
    public static String getPath(Context context,Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
