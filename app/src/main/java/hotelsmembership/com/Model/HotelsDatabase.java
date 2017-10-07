package hotelsmembership.com.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import hotelsmembership.com.Model.Hotel.Hotel;

@Database(entities = {Hotel.class,Membership.class}, version = 8)
public abstract class HotelsDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}

