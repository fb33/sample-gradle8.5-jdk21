package com.dummy.extension

import org.springframework.boot.test.system.CapturedOutput

val SEPARATOR: String = System.lineSeparator()

fun CapturedOutput.outToLines(): List<String> {
    return this.out.split(SEPARATOR).filter { it.isNotEmpty() }
}