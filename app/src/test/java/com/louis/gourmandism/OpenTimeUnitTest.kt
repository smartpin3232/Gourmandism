package com.louis.gourmandism

import com.louis.gourmandism.data.OpenTime
import com.louis.gourmandism.data.source.DefaultRepository
import com.louis.gourmandism.data.source.remote.RemoteDataSource
import com.louis.gourmandism.detail.DetailViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.Before

class OpenTimeUnitTest {

    private lateinit var businessTime: MutableList<OpenTime>
    private val dataSource = RemoteDataSource
    private val repository = DefaultRepository(dataSource,dataSource)
    private val viewModel= DetailViewModel(repository)

    @Before
    fun init(){
        businessTime = mutableListOf<OpenTime>(
            OpenTime(
                day = "0",
                startTime = "09:00",
                endTime = "22:30"
            ),
            OpenTime(
                day = "1",
                startTime = "09:00",
                endTime = "16:00"
            ), OpenTime(
                day = "2",
                startTime = "09:00",
                endTime = "22:30"
            ), OpenTime(
                day = "3",
                startTime = "09:00",
                endTime = "22:00"
            ), OpenTime(
                day = "4",
                startTime = "09:00",
                endTime = "22:30"
            ), OpenTime(
                day = "5",
                startTime = "09:00",
                endTime = "22:00"
            ), OpenTime(
                day = "6",
                startTime = "09:00",
                endTime = "22:30"
            )
        )
    }

    @Test
    fun checkBusinessStatus() {

        assertTrue(viewModel.checkBusinessStatus( getTodayBusinessTime(businessTime, "1"),"14:00"))
        assertTrue(viewModel.checkBusinessStatus( getTodayBusinessTime(businessTime, "3"),"21:00"))
        assertTrue(viewModel.checkBusinessStatus( getTodayBusinessTime(businessTime, "4"),"17:00"))
//        assertTrue(checkBusinessStatus( getTodayBusinessTime(businessTime, "10"),"16:00"))

        assertFalse(viewModel.checkBusinessStatus( getTodayBusinessTime(businessTime, "1"),"22:00"))
        assertFalse(viewModel.checkBusinessStatus( getTodayBusinessTime(businessTime, "3"),"08:00"))
        assertFalse(viewModel.checkBusinessStatus( getTodayBusinessTime(businessTime, "4"),"40:30"))
    }

    private fun getTodayBusinessTime(time: MutableList<OpenTime>, today: String) : OpenTime{
        return time.filter { it.day == today }[0]
    }

}