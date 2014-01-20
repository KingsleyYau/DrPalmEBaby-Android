package com.drcom.drpalm.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.drcom.drpalm.objs.NavigationItem;
import com.drcom.drpalm.objs.NavigationListItem;

/**
 * 导航页地区和学校数据库
 * @author menchx
 *
 */
public class NavigationDB {
	//table name
	private static final String NAVIGATION_TYPE_TABLE = "navigation_type_table";
	
	//table name
	private static final String SCHOOL_DISTRICT_TABLE = "school_district_table";
	
	//table bookmark
	private static final String SCHOOL_BOOKMARK_TABLE = "school_bookmark_table";
	
	//school table field
	private static final String ID = "_id";
	private static final String NAME = "name";
	private static final String SCHOOL_KEY = "school_key";
	private static final String POINT_ID = "point_id";//primary key 
	private static final String STATUS = "status"; //用于与后台数据库同步，做删除操作
	private static final String PARENT_ID = "parent_id";
	private static final String TYPE = "type";
	private static final String TITLEURL = "titleurl";
	private static final String BOOKMARK = "bookmark";

	
	//databasehelper
	NavigationDatabaseHelper mNavigationDBHelper;
	private static NavigationDB mNavigationDB = null;
	
	
	//查询条件
	private static final String POINT_ID_WHERE = POINT_ID + "=?";
	
	public static NavigationDB getInstance(Context context){
		if(mNavigationDB == null){
			mNavigationDB = new NavigationDB(context);
		}
		return mNavigationDB;
	}
	
	private NavigationDB(Context context){
		mNavigationDBHelper = NavigationDatabaseHelper.getInstance(context);
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		createTable(db);
		db.close();
	}
	
	public void startTransaction(){
		mNavigationDBHelper.getWritableDatabase().beginTransaction();
	}
	
	public void endTransaction(){
		mNavigationDBHelper.getWritableDatabase().setTransactionSuccessful();
		mNavigationDBHelper.getWritableDatabase().endTransaction();
	}
	
	private void createTable(SQLiteDatabase db){
		String CREATE_SCHOOL_DISTRICT_TABLE = "CREATE TABLE IF NOT EXISTS "+ SCHOOL_DISTRICT_TABLE + " ("
									+ ID + " INTEGER,"
									+ NAME + " TEXT,"
									+ SCHOOL_KEY + " TEXT,"
									+ POINT_ID + " INTEGER,"
									+ PARENT_ID + " INTEGER,"
									+ TYPE + " TEXT,"
									+ TITLEURL + " TEXT,"
									+ BOOKMARK + " BOOLEAN DEFAULT 0 NOT NULL,"
									+ STATUS + " INTEGER"
									+ ");";
		db.execSQL(CREATE_SCHOOL_DISTRICT_TABLE);
		
		String CREATE_SCHOOL_BOOKMARK_TABLE = "CREATE TABLE IF NOT EXISTS "+ SCHOOL_BOOKMARK_TABLE + " ("
									+ ID + " INTEGER,"
									+ POINT_ID + " INTEGER"
									+ ");";
		db.execSQL(CREATE_SCHOOL_BOOKMARK_TABLE);
		
		String CREATE_NAVIGATION_TYPE_TABLE = "CREATE TABLE IF NOT EXISTS "+ NAVIGATION_TYPE_TABLE + " ("
									+ ID + " INTEGER,"
									+ TYPE + " TEXT,"
									+ POINT_ID + " INTEGER"
									+ ");";
		db.execSQL(CREATE_NAVIGATION_TYPE_TABLE);
		
		mNavigationDBHelper.updateTable(db, SCHOOL_DISTRICT_TABLE);
		mNavigationDBHelper.updateTable(db, SCHOOL_BOOKMARK_TABLE);
		mNavigationDBHelper.updateTable(db, NAVIGATION_TYPE_TABLE);
	}
	
	public void clearChildID(String parentid){
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		db.delete(SCHOOL_DISTRICT_TABLE, PARENT_ID + "=?", new String[]{parentid});
	}
	
	public void clearChildBySearchKey(String searchkey){
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		
		db.delete(SCHOOL_DISTRICT_TABLE, NAME + " LIKE '%" + searchkey + "%'", null);
	}
	
	
	public boolean saveNavigationItem(NavigationItem item){
		boolean bFlags = false;
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, item.name);
		values.put(TYPE, item.type);
		values.put(POINT_ID, item.point_id);
		values.put(PARENT_ID, item.parent_id);
		values.put(SCHOOL_KEY, item.key);
		values.put(TITLEURL, item.titleurl);
		values.put(STATUS,item.status);
		if(schoolDistrictExist(item.point_id)){
			db.update(SCHOOL_DISTRICT_TABLE, values, POINT_ID_WHERE, new String[]{String.valueOf(item.point_id)});
		}else{
			bFlags = true;
			db.insert(SCHOOL_DISTRICT_TABLE, POINT_ID, values);
		}
		return bFlags;
	}
	
	private boolean schoolDistrictExist(int point_id){
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		Cursor cursor = db.query(SCHOOL_DISTRICT_TABLE, 
								null, 
								POINT_ID_WHERE, 
								new String[]{point_id+""}, 
								null, 
								null, 
								NAME + " ASC");
		int count = cursor.getCount();
		cursor.close();
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	
	public Cursor getBookmarkCursor(String limit){
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		String[] fields = new String[] { SCHOOL_DISTRICT_TABLE + "." + POINT_ID, NAME, SCHOOL_KEY, PARENT_ID, TYPE, TITLEURL, BOOKMARK, STATUS };
		String queryBuilder = SQLiteQueryBuilder.buildQueryString(false, 
																SCHOOL_DISTRICT_TABLE + ", " + SCHOOL_BOOKMARK_TABLE, 
																fields, 
																SCHOOL_DISTRICT_TABLE + "."+ POINT_ID + "=" + SCHOOL_BOOKMARK_TABLE + "." + POINT_ID, 
																null, 
																null, 
																NAME + " ASC", 
																limit);	
		return db.rawQuery(queryBuilder, null);
	}
	
	public Cursor getNavigationCursor(int parent_id, String limit){
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		String queryBuilder = SQLiteQueryBuilder.buildQueryString(false, 
																SCHOOL_DISTRICT_TABLE, 
																null, 
																PARENT_ID + "=" + parent_id, 
																null, 
																null, 
																NAME + " ASC", 
																limit);	
		return db.rawQuery(queryBuilder, null);
	}
	
	public void saveNavigationListItem(NavigationListItem item){
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TYPE, item.type);
		values.put(POINT_ID, item.point_id);
		if(!isNavigationListItemExists(item.point_id)){
			db.insert(NAVIGATION_TYPE_TABLE, POINT_ID, values);
		}else{
			db.update(NAVIGATION_TYPE_TABLE, values, POINT_ID_WHERE, new String[]{String.valueOf(item.point_id)});
		}
	}
	
	public void clearAllNavigationList(){
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		db.delete(NAVIGATION_TYPE_TABLE, null, null);
	}
	
	private boolean isNavigationListItemExists(int point_id){
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		Cursor cursor = db.query(NAVIGATION_TYPE_TABLE, 
								null, 
								POINT_ID_WHERE, 
								new String[]{point_id+""}, 
								null, 
								null, 
								null);
		int count = cursor.getCount();
		cursor.close();
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	
//	public void markAsBookmark(int point_id, boolean flags){
//		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(BOOKMARK, flags);
//		db.update(SCHOOL_DISTRICT_TABLE, values, POINT_ID + " =?", new String[]{String.valueOf(point_id)});
//	}
	
	public void markAsBookmark(int point_id){
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(POINT_ID, point_id);
		if(!pointExists(point_id)){
			db.insert(SCHOOL_BOOKMARK_TABLE, POINT_ID, values);
		}
	}
	
	public void deleteBookmarkFlag(int point_id){
		SQLiteDatabase db = mNavigationDBHelper.getWritableDatabase();
		if(pointExists(point_id)){
			db.delete(SCHOOL_BOOKMARK_TABLE, POINT_ID_WHERE, new String[]{String.valueOf(point_id)});
		}
	}
	
	private boolean pointExists(int point_id){
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		Cursor cursor = db.query(SCHOOL_BOOKMARK_TABLE, 
								null, 
								POINT_ID_WHERE, 
								new String[]{String.valueOf(point_id)}, 
								null, 
								null, 
								null);
		int count = cursor.getCount();
		cursor.close();
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getBookmarkByID(int point_id){
//		boolean bookmarkFlags = false;
//		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
//		String queryBuilder = SQLiteQueryBuilder.buildQueryString(false, 
//				SCHOOL_BOOKMARK_TABLE, 
//				new String[]{BOOKMARK}, 
//				POINT_ID + "=" + point_id, 
//				null, 
//				null, 
//				null, 
//				null);
//		Cursor result = db.rawQuery(queryBuilder, null);
//		if(null != result){
//			result.moveToFirst();
//			int bookmark_index = result.getColumnIndex(BOOKMARK);
//			bookmarkFlags = (result.getInt(bookmark_index) == 1?true:false);
//			result.close();
//		}
//		return bookmarkFlags;
		if(pointExists(point_id)){
			return true;
		}else{
			return false;
		}
	}
	
	public String getNameByID(int point_id){
		String name = "";
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		String queryBuilder = SQLiteQueryBuilder.buildQueryString(false, 
				SCHOOL_DISTRICT_TABLE, 
				new String[]{NAME}, 
				POINT_ID + "=" + point_id, 
				null, 
				null, 
				null, 
				null);
		Cursor result = db.rawQuery(queryBuilder, null);
		if(null != result){
			result.moveToFirst();
			int name_index = result.getColumnIndex(NAME);
			name = result.getString(name_index);
			result.close();
		}
		return name;
	}
	
	public NavigationItem getSchoolItem(String schoolkey){
		NavigationItem item = new NavigationItem();
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		String queryBuilder = SQLiteQueryBuilder.buildQueryString(false, 
				SCHOOL_DISTRICT_TABLE, 
				null, 
				SCHOOL_KEY + " = '" + schoolkey + "'", 
				null, 
				null, 
				null, 
				null);
		Cursor result = db.rawQuery(queryBuilder, null);
		if(null != result){
			if(result.moveToFirst()){
				item = retrieveNavigationItem(result);
			}
			result.close();
		}
		return item;
	}
	
	public Cursor searchItemsByKey(String key, String limit){
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		String queryString = SQLiteQueryBuilder.buildQueryString(false, 
																SCHOOL_DISTRICT_TABLE, 
																null, 
																NAME + " LIKE '%" + key + "%' AND " + STATUS + "=" + "1" + " AND " + TYPE + "= 'school'", 
																null, 
																null, 
																POINT_ID + " DESC", 
																limit);
		return db.rawQuery(queryString, null);
	}
	
	public List<NavigationItem> getBookmarkItems(String limit){
		List<NavigationItem> list = new ArrayList<NavigationItem>();
		Cursor cursor = getBookmarkCursor(limit);
		if(cursor != null){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				NavigationItem item = retrieveNavigationItem(cursor);
				list.add(item);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return list;
	}
	
	public List<NavigationListItem> getNavigationLists(){
		SQLiteDatabase db = mNavigationDBHelper.getReadableDatabase();
		List<NavigationListItem> list = new ArrayList<NavigationListItem>();
		String queryBuilder = SQLiteQueryBuilder.buildQueryString(false, 
				NAVIGATION_TYPE_TABLE, 
				null, 
				null, 
				null, 
				null, 
				POINT_ID + " ASC", 
				null);
		Cursor result = db.rawQuery(queryBuilder, null);
		if(result != null){
			result.moveToFirst();
			if(result.getCount()>0){
				while(!result.isAfterLast()){
					NavigationListItem item = new NavigationListItem();
					int type_index = result.getColumnIndex(TYPE);
					int point_id_index = result.getColumnIndex(POINT_ID);
					item.type = result.getString(type_index);
					item.point_id = result.getInt(point_id_index);
					list.add(item);
					result.moveToNext();
				}
			}
			result.close();
		}
		return list;
	}
	
	public List<NavigationItem> getNavigationItems(int parent_id, String limit){
		List<NavigationItem> list = new ArrayList<NavigationItem>();
		Cursor cursor = getNavigationCursor(parent_id, limit);
		int count = cursor.getCount();
		if(cursor != null){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				NavigationItem item = retrieveNavigationItem(cursor);
				list.add(item);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return list;
	}
	
	public List<NavigationItem> getSearchItems(String search_key, String limit){
		List<NavigationItem> list = new ArrayList<NavigationItem>();
		Cursor cursor = searchItemsByKey(search_key, limit);
		if(cursor != null){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				NavigationItem item = retrieveNavigationItem(cursor);
				list.add(item);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return list;
	}
	
	public NavigationItem retrieveNavigationItem(Cursor cursor){
		int parent_id_index = cursor.getColumnIndex(PARENT_ID);
		int point_id_index = cursor.getColumnIndex(POINT_ID);
		int name_index = cursor.getColumnIndex(NAME);
		int school_key_index = cursor.getColumnIndex(SCHOOL_KEY);
		int status_index = cursor.getColumnIndex(STATUS);
		int type_index = cursor.getColumnIndex(TYPE);
		int titlr_url_index = cursor.getColumnIndex(TITLEURL);
		int bookmark_index = cursor.getColumnIndex(BOOKMARK);
		
		NavigationItem item = new NavigationItem();
		item.status = cursor.getInt(status_index);
		item.parent_id = cursor.getInt(parent_id_index);
		item.point_id = cursor.getInt(point_id_index);
		item.key = cursor.getString(school_key_index);
		item.name = cursor.getString(name_index);
		item.type = cursor.getString(type_index);
		item.titleurl = cursor.getString(titlr_url_index);
		if(cursor.getInt(bookmark_index)==1){
			item.bookmark = true ;
		}else{
			item.bookmark = false ;
		}
		
		return item;
	}
}
