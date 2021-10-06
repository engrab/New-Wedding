package com.example.weddingplanner.appBase.roomsDB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.util.Log;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.models.profile.ProfileRowModel;
import com.example.weddingplanner.appBase.roomsDB.category.CategoryDao;
import com.example.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.example.weddingplanner.appBase.roomsDB.cost.CostDao;
import com.example.weddingplanner.appBase.roomsDB.cost.CostRowModel;
import com.example.weddingplanner.appBase.roomsDB.dbVerson.DbVersionDao;
import com.example.weddingplanner.appBase.roomsDB.dbVerson.DbVersionRowModel;
import com.example.weddingplanner.appBase.roomsDB.guest.GuestDao;
import com.example.weddingplanner.appBase.roomsDB.guest.GuestRowModel;
import com.example.weddingplanner.appBase.roomsDB.payment.PaymentDao;
import com.example.weddingplanner.appBase.roomsDB.payment.PaymentRowModel;
import com.example.weddingplanner.appBase.roomsDB.profile.ProfileDao;
import com.example.weddingplanner.appBase.roomsDB.taskList.SubTaskDao;
import com.example.weddingplanner.appBase.roomsDB.taskList.SubTaskRowModel;
import com.example.weddingplanner.appBase.roomsDB.taskList.TaskDao;
import com.example.weddingplanner.appBase.roomsDB.taskList.TaskRowModel;
import com.example.weddingplanner.appBase.roomsDB.vendor.VendorDao;
import com.example.weddingplanner.appBase.roomsDB.vendor.VendorRowModel;
import com.example.weddingplanner.appBase.utils.Constants;

@Database(entities = {TaskRowModel.class, SubTaskRowModel.class, CategoryRowModel.class, CostRowModel.class, GuestRowModel.class, VendorRowModel.class, PaymentRowModel.class, ProfileRowModel.class, DbVersionRowModel.class}, exportSchema = false, version = 2)
public abstract class AppDataBase extends RoomDatabase {
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            Log.d("AppDatabase", "migration started");
            supportSQLiteDatabase.execSQL("delete from dbVersionList");
            supportSQLiteDatabase.execSQL("insert into dbVersionList(versionNumber) values(2)");
            supportSQLiteDatabase.execSQL(AppDataBase.updateCategoryIdQuery("taskList"));
            supportSQLiteDatabase.execSQL(AppDataBase.updateCategoryIdQuery("costList"));
            supportSQLiteDatabase.execSQL(AppDataBase.updateCategoryIdQuery("vendorList"));
            supportSQLiteDatabase.execSQL("delete from categoryList where isDefault = 1");
            supportSQLiteDatabase.execSQL("insert into categoryList (id, name, iconType,isDefault) select new_id, name, name iconType, 1 isDefault from (select '1' new_id, 'Attire & accessories' name  union select '2', 'Health & beauty' union select '3','Music & show' union select '4','Flower & decor' union select '5','Accessories' union select '6','Jewelry' union select '7','Photo & video' union select '8','Ceremony' union select '9','Reception' union select '10','Transportation' union select '11','Accommodation' union select '12','Miscellaneous') nc  order by cast(new_id as int)");
            Log.d("AppDatabase", "migration ended");
        }
    };
    private static AppDataBase instance;

    public abstract CategoryDao categoryDao();

    public abstract CostDao costDao();

    public abstract DbVersionDao dbVersionDao();

    public abstract GuestDao guestDao();

    public abstract PaymentDao paymentDao();

    public abstract ProfileDao profileDao();

    public abstract SubTaskDao subTaskDao();

    public abstract TaskDao taskDao();

    public abstract VendorDao vendorDao();

    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, Constants.APP_DB_NAME).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build();
            Log.d("AppDatabase", "instance Created");
            if (!AppPref.isDbVersionInserted(context)) {
                insertDbVersion(context);
            }
        }
        return instance;
    }

    private static void insertDbVersion(Context context) {
        getAppDatabase(context).dbVersionDao().deleteAlldbVersion();
        getAppDatabase(context).dbVersionDao().insert(new DbVersionRowModel(2));
        AppPref.setIsDbVersionInserted(context, true);
        Log.d("AppDatabase", "db version inserted");
    }


    public static String updateCategoryIdQuery(String str) {
        return "Update " + str + " set CategoryId = ifnull((  select new_id from categoryList c   left join (select '1' new_id, '" + Constants.COST_CAT_TYPE_ATTIRE_ACCESSORIES + "' name  union select '2', '" + Constants.COST_CAT_TYPE_HEALTH_BEAUTY + "' union select '3','" + Constants.COST_CAT_TYPE_MUSIC_SHOW + "' union select '4','" + Constants.COST_CAT_TYPE_FLOWER_DECOR + "' union select '5','" + Constants.COST_CAT_TYPE_ACCESSORIES + "' union select '6','" + Constants.COST_CAT_TYPE_JEWELRY + "' union select '7','" + Constants.COST_CAT_TYPE_PHOTO_VIDEO + "' union select '8','" + Constants.COST_CAT_TYPE_CEREMONY + "' union select '9','" + Constants.COST_CAT_TYPE_RECEPTION + "' union select '10','" + Constants.COST_CAT_TYPE_TRANSPORTATION + "' union select '11','" + Constants.COST_CAT_TYPE_ACCOMMODATION + "' union select '12','" + Constants.COST_CAT_TYPE_MISCELLANEOUS + "') nc  on c.name = nc.name where isDefault = 1 and " + str + ".CategoryId = c.id), " + str + ".CategoryId)";
    }
}
