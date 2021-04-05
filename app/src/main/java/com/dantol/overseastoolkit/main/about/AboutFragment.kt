package com.dantol.overseastoolkit.main.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dantol.overseastoolkit.R
import com.dantol.overseastoolkit.databinding.AboutFragmentBinding


class AboutFragment : Fragment() {

	private lateinit var binding: AboutFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.about_fragment, container, false)

		binding.apply {
			about.movementMethod = LinkMovementMethod.getInstance()
		}

		return binding.root
	}

}