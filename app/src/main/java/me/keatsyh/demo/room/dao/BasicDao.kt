package me.keatsyh.demo.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import me.keatsyh.demo.BasicData

@Dao
interface BasicDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBasicEntity(basicEntity: BasicData)

    @Query("SELECT * FROM basic")
    fun queryBasicEntity(): Maybe<BasicData>


}