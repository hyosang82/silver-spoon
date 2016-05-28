package c.m.mobile8.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import c.m.mobile8.constants.MConstants;

/*
use Main..
	public void initDataBase(String DB_Name) {
        DBManager dbManager = DBManager.getInstance(getApplicationContext(),
                DB_Name);
        dbManager.CreateDataBase(DB_Name);
    }
 */

public class DataBaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = null;

	// private static String DB_NAME = BusConstants.DB_NAME;

	private static SQLiteDatabase myDataBase;

	private final Context myContext;

	private static final int DATABASE_VERSION = 3;


	public DataBaseHelper(Context context, String DB_Name) {
		super(context, DB_Name, null, DATABASE_VERSION);
		DB_PATH = MConstants.getDataBasePath(context);
		this.myContext = context;

	}

	public String getPath(String DB_Name) {
		return DB_PATH + DB_Name;
	}

	public void createDataBase(String DB_Name) throws IOException {

		boolean dbExist = checkDataBase(DB_Name);

		if (dbExist) {
			// do nothing - database already exist
		} else {
			this.getReadableDatabase();

			try {
				// if (DB_Name.equals(BusConstants.DB_NAME)) {
				// // Do nothing because Database Already in the data path
				// } else
				if (DB_Name.equals(MConstants.MY_DB_NAME)) {
					copyDataBase(DB_Name);
					// TODO Backup And Replace RecentFavorite.db

				} else {

				}

			} catch (IOException e) {
				e.printStackTrace();
				throw new Error("Error copying database");

			}
		}

	}
	private boolean checkDataBase(String DB_Name) {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_Name;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	public static void copyDirectoryOneLocationToAnotherLocation(
			File sourceLocation, File targetLocation) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < sourceLocation.listFiles().length; i++) {

				copyDirectoryOneLocationToAnotherLocation(new File(
						sourceLocation, children[i]), new File(targetLocation,
						children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);

			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}

	}

	private void copyDataBase(String DB_Name) throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_Name);
		String outFileName = DB_PATH + DB_Name;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public SQLiteDatabase openReadOnlyDataBase(String DB_Name)
			throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_Name;

		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		return myDataBase;
	}

	public SQLiteDatabase openReadWriteDataBase(String DB_Name)
			throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_Name;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		return myDataBase;
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}