package com.schoolproject.seconde2.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.schoolproject.seconde2.model.Email;
import java.util.List;

@Dao
public interface EmailDao {
    @Insert
    void insert(Email email);

    @Update
    void update(Email email);

    @Query("SELECT * FROM emails WHERE folder = :folder ORDER BY timestamp DESC")
    LiveData<List<Email>> getEmailsByFolder(String folder);

    @Query("SELECT * FROM emails ORDER BY timestamp DESC")
    LiveData<List<Email>> getAllEmails();

    @Query("DELETE FROM emails WHERE folder = :folder")
    void deleteByFolder(String folder);

    @Query("UPDATE emails SET is_read = 1 WHERE id = :emailId")
    void markAsRead(int emailId);

    @Query("SELECT COUNT(*) FROM emails WHERE folder = :folder AND is_read = 0")
    int getUnreadCount(String folder);
}