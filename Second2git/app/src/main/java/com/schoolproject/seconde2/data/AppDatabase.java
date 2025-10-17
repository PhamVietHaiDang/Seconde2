package com.schoolproject.seconde2.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.schoolproject.seconde2.model.Email;

@Database(entities = {Email.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmailDao emailDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, "email_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}