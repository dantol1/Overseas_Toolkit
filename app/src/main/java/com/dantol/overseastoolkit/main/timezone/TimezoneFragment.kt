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
import com.skydoves.powerspinner.DefaultSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimezoneFragment : Fragment() {

	private val viewModel: TimezoneViewModel by viewModels()
	private lateinit var binding: TimezoneFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.timezone_fragment, container, false)
		binding.apply {
			localTime.title.setText(R.string.local_time)
			localTime.clock.format24Hour = "HH:mm:ss"
			selectedTime.title.setText(R.string.selected_time)
			selectedTime.clock.format24Hour = "HH:mm:ss"
		}

		return binding.root
	}

	override fun onResume() {
		super.onResume()
		viewModel.getTimezones {
			binding.timeZoneSelector.apply {
				setSpinnerAdapter(DefaultSpinnerAdapter(this))
				setItems(it.map { timezone -> timezone.displayName })
				setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
					binding.selectedTime.clock.timeZone = it[newIndex].id
				}
			}
		}
	}
}