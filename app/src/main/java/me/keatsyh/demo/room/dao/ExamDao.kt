package me.keatsyh.demo.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import me.keatsyh.demo.BasicData

@Dao
interface ExamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBasicEntity(basicEntity: BasicData)

    @Query("SELECT * FROM basic")
    fun queryBasicEntity(): Maybe<BasicData>


}