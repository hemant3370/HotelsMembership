package hotelsmembership.com.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Hotel.class,Membership.class}, version = 3)
public abstract class HotelsDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}

