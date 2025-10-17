package com.schoolproject.seconde2.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.schoolproject.seconde2.model.Email;
import java.util.List;

@Dao
public interface EmailDao {
    @Insert
    void insert(Email email);

    @Query("SELECT * FROM emails WHERE folder = :folder ORDER BY id DESC")
    LiveData<List<Email>> getEmailsByFolder(String folder);

    @Query("SELECT * FROM emails ORDER BY id DESC")
    LiveData<List<Email>> getAllEmails();

    @Query("DELETE FROM emails WHERE folder = :folder")
    void deleteByFolder(String folder);
}