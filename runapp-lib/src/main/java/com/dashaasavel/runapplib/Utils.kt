package com.dashaasavel.runapplib

import org.slf4j.LoggerFactory

fun Any.logger(): org.slf4j.Logger = LoggerFactory.getLogger(this::class.java)