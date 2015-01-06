package com.superball.hellowifi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by TIAN on 2015/1/6.
 */
public class OUIHelper extends SQLiteOpenHelper {

    private static final int mVersion = 1;

    private static final String mName = "oui.db";

    private static OUIHelper mOUIHelper = null;

    private OUIHelper(Context context, String path) {

        super(context, path, null, mVersion);
    }

    public static void createInstance(Context context) {

        if (mOUIHelper == null) {

            ///storage/sdcard0/Android/data/com.superball.hellowifi/cache/oui.db
            String pathName = context.getExternalCacheDir() + File.separator + mName;

            copyFileFromAssets(context, pathName, mName);

            mOUIHelper = new OUIHelper(context, pathName);
        }
    }

    public static String getORG(String OUI) {

        String org = "";

        if (mOUIHelper != null) {

            String columnORG = "org";

            ///
            String strQuery = String.format(
                    "SELECT %s FROM tbl_oui WHERE oui ='%s'",
                    columnORG,
                    OUI
            );

            ///
            SQLiteDatabase db = mOUIHelper.getReadableDatabase();

            // //
            Cursor cursor = db.rawQuery(strQuery, null);

            if (cursor.moveToFirst()) {

                org = cursor.getString(cursor.getColumnIndex(columnORG));
            }

            // //
            db.close();
        }

        return org;
    }

    private static void copyFileFromAssets(Context context, String destPathName, String fileName) {

        File destFile = new File(destPathName);

        if (!destFile.exists()) {

            try {

                ///
                InputStream inputStream = context.getAssets().open(fileName);

                FileOutputStream outputStream = new FileOutputStream(destPathName);

                ///
                byte[] i_buffer = new byte[1024];

                for (; ; ) {

                    int length = inputStream.read(i_buffer);

                    if (length > 0) {

                        outputStream.write(i_buffer, 0, length);

                    } else {

                        break;
                    }
                }

                ///
                inputStream.close();

                outputStream.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
