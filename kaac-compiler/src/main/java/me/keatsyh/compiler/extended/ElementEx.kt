package me.keatsyh.compiler.extended

import java.util.ArrayList
import java.util.HashMap
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement


fun Set<Element>.classifyWithEnclosing(classifyElement: MutableMap<TypeElement, MutableList<Element>>) {
    if (this.isNotEmpty()) {
        this.forEach { element ->
            val enclosing = element.enclosingElement as TypeElement
            if (classifyElement.containsKey(enclosing)) {
                classifyElement[enclosing]?.add(element)
            } else {
                val elementList: MutableList<Element> = ArrayList()
                elementList.add(element)
                classifyElement[enclosing] = elementList
            }
            Unit
        }
    }
}

inline fun <reified A : Annotation,reified E: Element> Element.getChildElement(): List<E> {
    return this.enclosedElements.filter {
        it.hasAnnotation(A::class.java)
    }.map {
        it as E
    }
}

fun Element.toPropertyName(suffix: String = "PERMISSION_ARRAY"):String {
    val funName = this.simpleName.toString().toUpperCase()
    val splitter = "_"
    return "$funName$splitter$suffix"
}

fun <A : Annotation> Element.hasAnnotation(annotationClass: Class<A>): Boolean {
    return this.getAnnotation(annotationClass) != null
}

inline fun <reified A: Annotation>Element.getAnnotation(): A {
    return this.getAnnotation(A::class.java)
}