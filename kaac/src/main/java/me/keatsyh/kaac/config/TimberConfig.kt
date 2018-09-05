package me.keatsyh.kaac.config

import android.util.Log
import me.keatsyh.kaac.BuildConfig
import timber.log.Timber


class TimberConfig {

    companion object {
        fun init():TimberConfig {
            Log.d("TimberConfig","init")
//            if (BuildConfig.DEBUG) {
//                Log.d("TimberConfig","VarTree")
//                Timber.plant(VarTree())
//            } else {
//                Log.d("TimberConfig","CrashTree")
//                Timber.plant(CrashTree())
//            }
            Timber.plant(VarTree())
            return TimberConfig()
        }
    }

}

class CrashTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        Log.d("TimberConfig","CrashTreeLog")
    }
}

class VarTree: Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var tag = tag
        if (tag != null) {
            val threadName = Thread.currentThread().name
            tag = "<$threadName> $tag"
        }

        super.log(priority, tag, message, t)

        Log.d("TimberConfig","VarTreeLog")
    }


    override fun createStackElementTag(element: StackTraceElement): String? {

        return super.createStackElementTag(element) + "(Line: ${element.lineNumber} -- Method: ${element.methodName} )"  //日志显示行号
    }


}