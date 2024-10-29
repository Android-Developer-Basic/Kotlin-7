package ru.kotlin

fun main() {
    val strBox: Box<String> = Box("String 1")
    val nStrBox: Box<String?> = Box(null)

    val strValue: String = strBox.getValue()
    val strLength: Int = strBox.getLength()
    val nStrValue: String? = nStrBox.getValue()
    val nStrLength: Int = nStrBox.getLength()
}

class Box<T : CharSequence?>(private val value: T) {
    fun getValue(): T = value
    fun getLength(): Int = value?.length ?: 0
}