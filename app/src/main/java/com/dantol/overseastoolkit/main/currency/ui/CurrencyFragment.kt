package com.dantol.overseastoolkit.main.currency.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dantol.overseastoolkit.R
import com.dantol.overseastoolkit.databinding.CurrencyFragmentBinding
import com.dantol.overseastoolkit.main.currency.database.model.ExchangeRate
import com.dantol.overseastoolkit.utils.Logger
import com.dantol.overseastoolkit.utils.NullViewStateException
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.PowerSpinnerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyFragment : Fragment() {

	private val viewModel: CurrencyViewModel by viewModels()
	private lateinit var binding: CurrencyFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.currency_fragment, container, false)
		viewModel.currencyFragmentState.observe(viewLifecycleOwner) {
			render(it)
		}

		return binding.root
	}

	override fun onResume() {
		super.onResume()
		viewModel.getRates()
	}

	private fun render(state: CurrencyFragmentState) {
		if (state.viewState != null) {
			when (state) {
				is CurrencyFragmentState.LoadingData -> showProgress()
				is CurrencyFragmentState.ErrorLoadingData -> showError(state.viewState)
				is CurrencyFragmentState.DataLoaded -> showConvertUi(state.viewState)
				is CurrencyFragmentState.CurrencyConverted -> showConvertedAmount(state.viewState)
			}
			renderView(state.viewState)
		} else {
			throw NullViewStateException("Error showing state $state from $TAG")
		}
	}

	private fun renderView(viewState: CurrencyFragmentViewState) {
		binding.apply {
			if (viewState.isLoading) progressBar.show() else progressBar.hide()

			if (fromCurrency.getSpinnerAdapter<IconSpinnerAdapter>().getItemCount() == 0
				&& viewState.currenciesExchangeRate != null
			) {
				showConvertUi(viewState)
			}

			errorMessage.visibility = viewState.errorMessageVisibility
			fromCurrency.visibility = viewState.chosenFromCurrencyVisibility
			fromCurrencyAmount.visibility = viewState.chosenFromCurrencyAmountVisibility
			toCurrency.visibility = viewState.chosenToCurrencyVisibility
			toCurrencyAmount.visibility = viewState.amountConvertedVisibility
			swap.visibility = viewState.swapButtonVisibility
		}
	}

	private fun showProgress() {
		binding.apply {
			progressBar.show()
		}
	}

	private fun showError(viewState: CurrencyFragmentViewState) {
		binding.apply {
			viewState.errorMessage?.let { errorMessage.setText(it) }
		}
	}

	private fun showConvertUi(viewState: CurrencyFragmentViewState) {
		binding.apply {
			val currenciesList = createCurrenciesList(viewState.currenciesExchangeRate)
			fromCurrency.setCurrencies(currenciesList)
			toCurrency.setCurrencies(currenciesList)
			listenToAmountChanges(viewState)
			listenToSpinnerChanges(viewState)
			swap.setOnClickListener { swapCurrencies() }
		}
	}

	private fun CurrencyFragmentBinding.swapCurrencies() {
		val temp = fromCurrency.selectedIndex
		fromCurrency.selectItemByIndex(toCurrency.selectedIndex)
		toCurrency.selectItemByIndex(temp)
	}

	private fun CurrencyFragmentBinding.listenToSpinnerChanges(
		viewState: CurrencyFragmentViewState
	) {
		fromCurrency.setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, newIndex, newItem ->
			Logger.i("Changed item to $newItem")
			viewModel.uiChanged(
				amountString = fromCurrencyAmount.text.toString(),
				fromCurrency = viewState.currenciesExchangeRate?.get(newIndex),
				toCurrency = viewState.currenciesExchangeRate?.get(toCurrency.selectedIndex),
			)
		}
		toCurrency.setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, newIndex, newItem ->
			Logger.i("Changed item to $newItem")
			viewModel.uiChanged(
				amountString = fromCurrencyAmount.text.toString(),
				fromCurrency = viewState.currenciesExchangeRate?.get(fromCurrency.selectedIndex),
				toCurrency = viewState.currenciesExchangeRate?.get(newIndex),
			)
		}
	}

	private fun CurrencyFragmentBinding.listenToAmountChanges(
		viewState: CurrencyFragmentViewState
	) {
		fromCurrencyAmount.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(
				s: CharSequence?,
				start: Int,
				count: Int,
				after: Int
			) {
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				Logger.i("Amount changed to $s")

				viewModel.uiChanged(
					amountString = s?.toString(),
					fromCurrency = viewState.currenciesExchangeRate?.get(fromCurrency.selectedIndex),
					toCurrency = viewState.currenciesExchangeRate?.get(toCurrency.selectedIndex)
				)
			}

			override fun afterTextChanged(s: Editable?) {}
		})
	}

	private fun showConvertedAmount(viewState: CurrencyFragmentViewState) {
		Logger.i("converted to ${viewState.amountConverted}")
		binding.apply {
			toCurrencyAmount.text = viewState.amountConverted.toString()
		}
	}

	private fun createCurrenciesList(currencies: List<ExchangeRate>?): List<IconSpinnerItem> {
		val iconSpinnerItems = ArrayList<IconSpinnerItem>()

		currencies?.forEach {
			iconSpinnerItems.add(IconSpinnerItem(it.currency.name, iconRes = it.currency.flag()))
		}

		return iconSpinnerItems
	}

	private fun PowerSpinnerView.setCurrencies(currencies: List<IconSpinnerItem>) {
		setSpinnerAdapter(IconSpinnerAdapter(this))
		setItems(currencies)
		selectItemByIndex(0)
		lifecycleOwner = this@CurrencyFragment
	}

	companion object {
		private const val TAG = "CurrencyFragment"
	}
}