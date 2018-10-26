package me.keatsyh.demo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

data class BaseData<T>(var message: String, var result: T, var status: String)


data class TokenData(
        var token: String
)


@Entity(tableName = "basic")
data class BasicData constructor(@ColumnInfo(name = "id")
                                 @PrimaryKey(autoGenerate = false)
                                 var id: Int = 0,
                                 @ColumnInfo(name = "bp_uuid")
                                 var bpUuid: String? = null,
                                 @ColumnInfo(name = "class_name")
                                 var className: String? = null,
                                 @ColumnInfo(name = "class_uuid")
                                 var classUuid: String? = null,
                                 @ColumnInfo(name = "grade_name")
                                 var gradeName: String? = null,
                                 @ColumnInfo(name = "grade_uuid")
                                 var gradeUuid: String? = null,
                                 @ColumnInfo(name = "netty_port")
                                 var nettyPort: Int = -1,
                                 @ColumnInfo(name = "netty_server")
                                 var nettyServer: String? = null,
                                 @ColumnInfo(name = "now_is_lock")
                                 var nowIsLock: String? = null,
                                 @ColumnInfo(name = "now_pattern")
                                 var nowPattern: String? = null,
                                 @ColumnInfo(name = "off_time")
                                 var offTime: Long = 0,
                                 @ColumnInfo(name = "on_time")
                                 var onTime: Long = 0,
                                 @ColumnInfo(name = "org_name")
                                 var orgName: String? = null,
                                 @ColumnInfo(name = "org_uuid")
                                 var orgUuid: String? = null,
                                 @Embedded
                                 var weather: Weather? = null) {
    constructor() : this(0)

}

data class Weather(
        @ColumnInfo(name = "code_day")
        var codeDay: String,
        @ColumnInfo(name = "date")
        var date: String,
        @ColumnInfo(name = "high")
        var high: String,
        @ColumnInfo(name = "low")
        var low: String,
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "text_day")
        var textDay: String
)
//
//data class BasicData(
//    var bpUuid: String,
//    var className: String,
//    var classUuid: String,
//    var gradeName: String,
//    var gradeUuid: String,
//    var nowIsLock: String,
//    var nowPattern: String,
//    var offTime: Long,
//    var onTime: Long,
//    var orgName: String,
//    var orgUuid: String,
//    var weather: Weather
//)
//
//data class Weather(
//    var codeDay: String,
//    var date: String,
//    var high: String,
//    var low: String,
//    var name: String,
//    var textDay: String
//)





