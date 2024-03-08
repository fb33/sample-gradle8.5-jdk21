package com.dummy.infrastructure

import com.dummy.extension.outToLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension

const val TIMESTAMP_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}Z"

@ExtendWith(OutputCaptureExtension::class)
class LogTest {

    @Test
    fun `info logger should format output according to logback config`(output: CapturedOutput) {
        val logger = LoggerFactory.getLogger(LogTest::class.java)

        logger.info("ok")
        val outputLines = output.outToLines()

        assertThat(outputLines.count()).isOne()
        assertThat(outputLines.first()).matches("""\{"timestamp":"$TIMESTAMP_REGEX","message":"ok","logger":"com\.dummy\.infrastructure\.LogTest","thread":"Test worker","level":"INFO"}""")
    }
}