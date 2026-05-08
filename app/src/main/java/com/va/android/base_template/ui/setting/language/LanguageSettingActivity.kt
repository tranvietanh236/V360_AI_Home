package com.va.android.base_template.ui.setting.language

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.va.android.base_template.R
import com.va.android.base_template.base.BaseActivity
import com.va.android.base_template.databinding.ActivityLanguageSettingBinding
import com.va.android.base_template.ui.main.MainActivity
import com.va.android.base_template.ui.setting.language.adapter.LanguageAdapter
import com.va.android.base_template.ui.setting.language.model.LanguageModel
import com.va.android.base_template.utils.SystemUtilApp

class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding>() {
    private var languageAdapter: LanguageAdapter? = null
    private var lang: String = ""
    override fun inflateViewBinding(): ActivityLanguageSettingBinding {
        return ActivityLanguageSettingBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val listLanguage = mutableListOf<LanguageModel>()
        val listLang = listOf(
            LanguageModel("English", "en", false, R.drawable.ic_language_en),
            LanguageModel("Hindi", "hi", false, R.drawable.ic_language_hi),
            LanguageModel("Spanish", "es", false, R.drawable.ic_language_es),
            LanguageModel("French", "fr", false, R.drawable.ic_language_fr),
            LanguageModel("Portuguese", "pt", false, R.drawable.ic_language_pt),
            LanguageModel("German", "de", false, R.drawable.ic_language_de),
            LanguageModel("Japanese", "ja", false, R.drawable.ic_language_jp),
            LanguageModel("Korean", "ko", false, R.drawable.ic_language_ko),
            LanguageModel("Russian", "ru", false, R.drawable.ic_language_ru),
        )

        listLanguage.addAll(listLang)

        val linearLayoutManager = LinearLayoutManager(this)

        for (i in listLang.indices) {
            val languageModel = listLang[i]
            if (languageModel.code.contains(SystemUtilApp.getPreLanguage(this).toString())) {
                listLanguage.remove(languageModel)
                listLanguage.add(0, languageModel)
                break
            }
        }
        languageAdapter =
            LanguageAdapter(this, listLanguage, object : LanguageAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    lang = listLanguage[position].code
                }

            })
        viewBinding.rvLanguage.layoutManager = linearLayoutManager
        viewBinding.rvLanguage.adapter = languageAdapter
        languageAdapter?.selectedItemPosition = 0
    }

    override fun initOnClickListener() {
        viewBinding.ivRightIcon1.visibility = View.VISIBLE
        viewBinding.ivLeftIcon.setOnClickListener {
            finish()
        }

        viewBinding.ivRightIcon1.setImageDrawable(
            ContextCompat.getDrawable(
                this@LanguageSettingActivity,
                R.drawable.ic_next_language
            )
        )

        viewBinding.ivRightIcon1.setOnClickListener {
            SystemUtilApp.saveLocale(this@LanguageSettingActivity, lang)
            nextActivityProcess()
        }
    }

    private fun nextActivityProcess() {
        finishAffinity()
        overridePendingTransition(0, 0)
        startActivity(Intent(this, MainActivity::class.java))
    }
}