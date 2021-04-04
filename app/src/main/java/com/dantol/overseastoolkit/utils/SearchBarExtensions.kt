package com.dantol.overseastoolkit.utils

import androidx.appcompat.widget.SearchView

fun <T> SearchView.applyFilter(list: List<T>, filterBy: (T, String) -> Boolean, onFilter: (List<T>) -> Unit) {
	setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
		SearchView.OnQueryTextListener {
		override fun onQueryTextSubmit(query: String?): Boolean {
			return filter(query, list, filterBy, onFilter)
		}

		override fun onQueryTextChange(newText: String?): Boolean {
			return filter(newText, list, filterBy, onFilter)
		}

	})
}

private fun <T> filter(query: String?, list: List<T>, filterBy: (T, String) -> Boolean, onFilter: (List<T>) -> Unit): Boolean {
	if (query != null) {
		onFilter(list.filter { t -> filterBy(t, query) })
	} else {
		onFilter(list)
	}

	return true
}