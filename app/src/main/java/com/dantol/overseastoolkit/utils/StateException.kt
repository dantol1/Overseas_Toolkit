package com.dantol.overseastoolkit.utils

import java.lang.Exception

class StateException(msg: String): Exception(msg) {
	init {
		Logger.w(msg)
	}
}