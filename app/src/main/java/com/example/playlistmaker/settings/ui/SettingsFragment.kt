package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.utility.App
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val switch by lazy {
        activity?.application as App
    }

    private val viewModelSetting by viewModel<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelSetting.isTurn.observe(viewLifecycleOwner) { isDarkThemeEnabled ->
            binding.switchSettingsDarkTheme.isChecked = isDarkThemeEnabled
            switch.switchTheme(isDarkThemeEnabled)
            (requireContext().applicationContext as App).switchTheme(isDarkThemeEnabled)
        }

        binding.switchSettingsDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModelSetting.changeAppTheme(isChecked)
        }

        binding.btnImShare.setOnClickListener {
            viewModelSetting.shareApp(getString(R.string.send_to))
        }

        binding.btnImSupport.setOnClickListener {
            viewModelSetting.sendToSupport(
                EmailData(
                    sender = getString(R.string.my_mail),
                    subject = getString(R.string.TEXT_EXTRA_SUBJEC),
                    message = getString(R.string.BODY_EXTRA_TEXT)
                )
            )
        }

        binding.btnImNext.setOnClickListener {
            viewModelSetting.openTerms(getString(R.string.adress))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
