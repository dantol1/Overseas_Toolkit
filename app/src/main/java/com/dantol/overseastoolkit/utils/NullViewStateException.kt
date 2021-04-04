package com.dantol.overseastoolkit.utils

import java.lang.Exception

class NullViewStateException(msg: String): Exception(msg) {
	init {
		Logger.w(msg)
	}
}