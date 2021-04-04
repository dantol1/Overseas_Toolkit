package com.dantol.overseastoolkit.main.timezone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dantol.overseastoolkit.R
import com.dantol.overseastoolkit.databinding.TimezoneFragmentBinding

class TimezoneFragment : Fragment() {

	private val viewModel: TimezoneViewModel by viewModels()
	private lateinit var binding: TimezoneFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.timezone_fragment, container, false)

		return binding.root
	}
}