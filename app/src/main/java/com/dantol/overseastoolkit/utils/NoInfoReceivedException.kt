package com.dantol.overseastoolkit.utils

import java.lang.Exception

class NoInfoReceivedException(msg: String): Exception(msg) {

	init {
		Logger.w(msg)
	}
}