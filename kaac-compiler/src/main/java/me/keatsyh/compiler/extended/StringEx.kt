package me.keatsyh.compiler.extended



fun String.toUpperCase(range: IntRange):String {
    val sb = StringBuilder()
    for (i in range) {
        val upperCase = this[i].toUpperCase()
        sb.append(upperCase)
    }
    return this.replaceRange(range, sb)
}



fun String.toUpperCase(start: Int, end: Int):String {
    val sb = StringBuilder()
    for (i in start until end) {
        val upperCase = this[i].toUpperCase()
        sb.append(upperCase)
    }

    return this.replaceRange(start,end, sb)

}