package be.ehb.dig_x.ricardo.werkstuk_android.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ShoeDao {

    @Query("SELECT * FROM shoes")
    List<ShoeEntry> loadAllShoes();

    @Insert
    void insertShoe(ShoeEntry shoeEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateShoe(ShoeEntry shoeEntry);

    @Delete
    void deleteShoe(ShoeEntry shoeEntry);

    @Query("DELETE FROM shoes")
    void deleteAllShoes();


    @Query("SELECT * FROM shoes WHERE id = :id")
    ShoeEntry loadShoeByID(int id);

}
