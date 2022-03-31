package com.abdulaziz.provalyutakurslari.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.work.*
import com.abdulaziz.provalyutakurslari.viewmodel.CurrencyViewModel
import com.abdulaziz.provalyutakurslari.R
import com.abdulaziz.provalyutakurslari.adapters.ArchiveAdapter
import com.abdulaziz.provalyutakurslari.adapters.PagerAdapter
import com.abdulaziz.provalyutakurslari.databinding.FragmentHomeBinding
import com.abdulaziz.provalyutakurslari.databinding.ItemTabBinding
import com.abdulaziz.provalyutakurslari.db.CurrencyDatabase
import com.abdulaziz.provalyutakurslari.models.Currency
import com.abdulaziz.provalyutakurslari.models.Information
import com.abdulaziz.provalyutakurslari.models.InformationWithCurrency
import com.abdulaziz.provalyutakurslari.services.CurrencyWork
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewModel: CurrencyViewModel
    private var currencyListLast: List<Currency>? = null
    private var archiveList: ArrayList<Currency>? = null
    private lateinit var currencyDatabase: CurrencyDatabase
    private val WORK_NAME = "my_work"
    private var informationWithCurrencyList: List<InformationWithCurrency>? = null
    private lateinit var archiveAdapter: ArchiveAdapter
    private var databaseIsEmpty = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        archiveList = arrayListOf()
        currencyListLast = arrayListOf()

        currencyDatabase = CurrencyDatabase.getInstance(requireContext())
        if (!currencyDatabase.currencyDao().getInformationWithCurrency().isNullOrEmpty()) {
            informationWithCurrencyList =
                currencyDatabase.currencyDao().getInformationWithCurrency()
            currencyListLast = informationWithCurrencyList!!.last().list
            val pageList = arrayListOf<String>()
            currencyListLast?.forEach {
                pageList.add(it.code)
            }
            informationWithCurrencyList!!.forEach {
                it.list.forEach {
                    archiveList!!.add(it)
                }
            }
            loadData(pageList, currencyListLast!!, container)

            archiveList!!.removeAll(currencyListLast!!)
        } else {
            runWork()
            databaseIsEmpty = true
        }

        viewModel = ViewModelProvider(requireActivity()).get(CurrencyViewModel::class.java)
        viewModel.getCurrency().observe(requireActivity()) { currencylist ->

            val pageList = arrayListOf<String>()
            currencylist.forEach {
                pageList.add(it.code)
            }

            if (currencylist != null)
                loadData(pageList, currencylist, container)

            if (databaseIsEmpty) {
                saveData(currencylist)
            }

        }

        setArchive()

        if(!databaseIsEmpty) {
            TabLayoutMediator(binding.hw1Tablayout, binding.viewpager) { tab, position ->
            }.attach()
        }
        binding.rv.addItemDecoration(DividerItemDecoration(binding.root.context,
            DividerItemDecoration.VERTICAL))

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.getCurrency().removeObservers(requireActivity())
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadData(
        pageList: ArrayList<String>,
        mlist: List<Currency>?,
        container: ViewGroup?,
    ) {
        if (isAdded)
            pagerAdapter = PagerAdapter(requireActivity(), mlist!!)
        binding.viewpager.adapter = pagerAdapter

        currencyListLast = mlist

        binding.tabLayout.removeOnTabSelectedListener(this)
        binding.tabLayout.removeAllTabs()

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = pageList[position]
            binding.viewpager.setCurrentItem(tab.position, true)
        }.attach()
        val count: Int = binding.tabLayout.tabCount
        for (i in 0 until count) {
            val tabView = ItemTabBinding.inflate(layoutInflater, container, false)
            val cardView = tabView.card
            tabView.tabTitle.text = pageList[i]
            if (i == 0) {
                tabView.tabTitle.setTextColor(Color.parseColor("#ffffff"))
                cardView.setCardBackgroundColor(resources.getColor(R.color.green_color,
                    resources.newTheme()))
            }
            binding.tabLayout.getTabAt(i)?.customView = tabView.root

        }
        binding.tabLayout.addOnTabSelectedListener(this)

        if (databaseIsEmpty){
            TabLayoutMediator(binding.hw1Tablayout, binding.viewpager) { tab, position ->
            }.attach()
        }

    }


    private fun saveData(currencylist: List<Currency>) {
        var informationId = 1
        if (currencyDatabase.currencyDao().getLastInformation() != null) {
            informationId = currencyDatabase.currencyDao().getLastInformation()!!.informationId + 1
        }
        currencyDatabase.currencyDao()
            .insertInformationForCurrency(Information(informationId), currencylist)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onTabSelected(tab: TabLayout.Tab?) {
        val tabView = tab?.customView!!
        val textView = tabView.findViewById<TextView>(R.id.tab_title)
        val cardView = tabView.findViewById<CardView>(R.id.card)
        textView.setTextColor(Color.parseColor("#ffffff"))
        cardView.setCardBackgroundColor(resources.getColor(R.color.green_color,
            resources.newTheme()))
        setArchive()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

        val tabView = tab?.customView!!
        val textView = tabView.findViewById<TextView>(R.id.tab_title)
        val cardView = tabView.findViewById<CardView>(R.id.card)
        textView.setTextColor(Color.parseColor("#858585"))
        cardView.setCardBackgroundColor(Color.WHITE)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    private fun runWork() {
        val constraints: Constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val periodicRefreshRequest = PeriodicWorkRequest.Builder(CurrencyWork::class.java,
            2, TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(requireContext())
            .enqueueUniquePeriodicWork(WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRefreshRequest)
    }

    private fun setArchive() {
        val list: ArrayList<Currency> = arrayListOf()
        val code =
            binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString()
        archiveList!!.forEach {
            if (it.code == code) {
                list.add(it)
            }
        }
        list.reverse()
        archiveAdapter = ArchiveAdapter(list)
        binding.rv.adapter = archiveAdapter

    }

}