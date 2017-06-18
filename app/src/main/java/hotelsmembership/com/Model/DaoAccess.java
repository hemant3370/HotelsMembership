package hotelsmembership.com.Model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hotelsmembership.com.Model.Hotel.Hotel;

@Dao
public interface DaoAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleRecord(Hotel... hotels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleListRecord(List<Hotel> hotels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleRecord(Hotel hotel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleRecord(Membership membership);

    @Query("SELECT * FROM Hotels")
    LiveData<List<Hotel>> fetchAllData();

    @Query("SELECT * FROM memberships")
    LiveData<List<Membership>> fetchAllMemberships();

    @Query("SELECT * FROM Hotels WHERE hotelName =:hotel_name")
    Hotel getSingleRecord(String hotel_name);
    @Query("SELECT hotelName FROM Hotels")
    LiveData<String[]> fetchAllNames();
    @Update
    void updateRecord(Hotel hotel);

    @Query("DELETE FROM memberships WHERE cardNumber =:card_number")
    void deleteCard(String card_number);

    @Delete()
    void deleteMembership(Membership membership);
}
