package me.keatsyh.demo.room.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "basic")
data class BasicEntity constructor(@ColumnInfo(name = "id")
                       @PrimaryKey(autoGenerate = true)
                       val id: Int = 0,
                       @ColumnInfo(name = "bp_uuid")
                       val bpUuid: String? = null,
                       @ColumnInfo(name = "class_name")
                       val className: String? = null,
                       @ColumnInfo(name = "class_uuid")
                       val classUuid: String? = null,
                       @ColumnInfo(name = "grade_name")
                       val gradeName: String? = null,
                       @ColumnInfo(name = "grade_uuid")
                       val gradeUuid: String? = null,
                       @ColumnInfo(name = "now_is_lock")
                       val nowIsLock: String? = null,
                       @ColumnInfo(name = "now_pattern")
                       val nowPattern: String? = null,
                       @ColumnInfo(name = "off_time")
                       val offTime: Long = 0,
                       @ColumnInfo(name = "on_time")
                       val onTime: Long = 0,
                       @ColumnInfo(name = "org_name")
                       val orgName: String? = null,
                       @ColumnInfo(name = "org_uuid")
                       val orgUuid: String? = null,
                       @Embedded
                       val weather: Weather? = null) {
    constructor():this(0)

}

data class Weather(
        @ColumnInfo(name = "code_day")
        val codeDay: String,
        @ColumnInfo(name = "date")
        val date: String,
        @ColumnInfo(name = "high")
        val high: String,
        @ColumnInfo(name = "low")
        val low: String,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "text_day")
        val textDay: String
)