package com.schoolproject.seconde2.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.schoolproject.seconde2.model.Email;

@Database(entities = {Email.class}, version = 3) // Increase to version 3
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmailDao emailDao();

    private static volatile AppDatabase INSTANCE;

    // Migration from version 1 to 2
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE emails ADD COLUMN timestamp INTEGER DEFAULT 0");
        }
    };

    // Migration from version 2 to 3 - Add the new HTML columns
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE emails ADD COLUMN html_content TEXT");
            database.execSQL("ALTER TABLE emails ADD COLUMN is_html INTEGER NOT NULL DEFAULT 0");
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class, "email_db"
                            )
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add both migrations
                            .fallbackToDestructiveMigration() // This will handle any issues
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}