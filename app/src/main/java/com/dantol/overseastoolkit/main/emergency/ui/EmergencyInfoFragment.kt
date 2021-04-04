package com.dantol.overseastoolkit.main.emergency.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dantol.overseastoolkit.R
import com.dantol.overseastoolkit.databinding.EmergencyInfoFragmentBinding
import com.dantol.overseastoolkit.main.emergency.database.model.EmergencyInfo
import com.dantol.overseastoolkit.utils.applyFilter
import com.skydoves.powerspinner.DefaultSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmergencyInfoFragment : Fragment() {

	private val viewModel: EmergencyInfoViewModel by viewModels()
	private lateinit var binding: EmergencyInfoFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding =
			DataBindingUtil.inflate(inflater, R.layout.emergency_info_fragment, container, false)
		viewModel.emergencyInfoFragmentState.observe(viewLifecycleOwner) {
			render(it)
		}

		return binding.root
	}

	override fun onResume() {
		super.onResume()
		viewModel.getEmergencyInfo()
	}

	private fun render(state: EmergencyInfoFragmentState) {
		when (state) {
			is EmergencyInfoFragmentState.FetchingData -> showLoading()
			is EmergencyInfoFragmentState.ErrorFetchingData -> showErrorMessage(state.errorMsg)
			is EmergencyInfoFragmentState.DataLoaded -> showEmergencyInfoUi(state.emergencyInfoList)
		}
	}

	private fun showLoading() {
		binding.apply {
			progressBar.show()
			emergencyInfo.visibility = View.GONE
			emergencyInfoSelector.visibility = View.GONE
			errorMessage.visibility = View.GONE
		}
	}

	private fun showErrorMessage(errorMsg: String) {
		binding.apply {
			progressBar.hide()
			emergencyInfo.visibility = View.GONE
			emergencyInfoSelector.visibility = View.GONE
			errorMessage.visibility = View.VISIBLE
			errorMessage.text = errorMsg
		}
	}

	private fun showEmergencyInfoUi(emergencyInfoList: List<EmergencyInfo>) {
		binding.apply {
			progressBar.hide()
			errorMessage.visibility = View.GONE
			emergencyInfo.visibility = View.VISIBLE
			emergencyInfoSelector.visibility = View.VISIBLE
			setEmergencyInfoCountries(emergencyInfoList)
			searchBar.applyFilter(emergencyInfoList,
				{ emergencyInfo, query ->
					emergencyInfo.countryName.startsWith(
						query,
						ignoreCase = true
					)
				}
			) {
				setEmergencyInfoCountries(it)
			}
		}
	}

	private fun EmergencyInfoFragmentBinding.setEmergencyInfoCountries(
		emergencyInfoList: List<EmergencyInfo>
	) {
		emergencyInfoSelector.setSpinnerAdapter(DefaultSpinnerAdapter(emergencyInfoSelector))
		emergencyInfoSelector.setItems(emergencyInfoList.map { it.countryName })
		emergencyInfoSelector.setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
			showEmergencyInfo(emergencyInfoList[newIndex])
		}
		if (emergencyInfoList.isNotEmpty())
			emergencyInfoSelector.selectItemByIndex(0)
	}

	private fun showEmergencyInfo(emergencyInfo: EmergencyInfo) {
		binding.emergencyInfo.text = getString(
			R.string.emergency_info,
			emergencyInfo.countryCode,
			emergencyInfo.countryName,
			emergencyInfo.ambulance,
			emergencyInfo.fire,
			emergencyInfo.police,
			emergencyInfo.dispatch,
			emergencyInfo.dispatchGSM,
			emergencyInfo.dispatchFixed,
		)
	}

}