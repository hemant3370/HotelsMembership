package loyaltywallet.com.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import loyaltywallet.com.Model.Hotel.Hotel;

@Database(entities = {Hotel.class,Membership.class}, version = 10)
public abstract class HotelsDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}

