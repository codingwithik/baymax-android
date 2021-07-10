package com.francisumeanozie.baymax

import com.francisumeanozie.baymax.data.api.Api
import com.francisumeanozie.baymax.data.responses.CurrencyResponse
import com.francisumeanozie.baymax.data.responses.ExchangeResponse
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class HomeViewModelTest {

    private lateinit var server: MockWebServer
    private val MOCK_WEBSERVER_PORT = 8000
    private lateinit var api: Api


    @Before
    fun init() {

        server = MockWebServer()
        server.start(MOCK_WEBSERVER_PORT)

        api = Retrofit.Builder()
            .baseUrl(server.url("http://api.currencylayer.com/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(Api::class.java)
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun `read sample currency success json file`(){
        val reader = MockResponseFileReader("success_currency_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `read sample quotes success json file`(){
        val reader = MockResponseFileReader("success_quotes_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `fetch currencies and check response success value returned`(){
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_currency_response.json").content)
        server.enqueue(response)

        val mockResponse = response.getBody()?.readUtf8()

        var actual: CurrencyResponse? = null
        // Act
        api.getCurrencies(BuildConfig.ACCESS_KEY, 1)
            .subscribe({
                actual = it
            }, {})
        // Assert
        assertEquals(mockResponse?.let { `parse mocked JSON response`(it) }, actual?.success)
    }

    // a point to note: the currency and quotes API can't be called at once probably because I am on a free version
    @Test
    fun `fetch quotes and check response success value returned`(){
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_quotes_response.json").content)
        server.enqueue(response)

        val mockResponse = response.getBody()?.readUtf8()

        var actual: ExchangeResponse? = null
        // Act
        api.getQuotes(BuildConfig.ACCESS_KEY, "", "", 1)
            .subscribe({
                actual = it
            }, {})

        // Assert
        assertEquals(mockResponse?.let { `parse mocked JSON response`(it) }, true)
    }

    private fun `parse mocked JSON response`(mockResponse: String): Boolean {
        val reader = JSONObject(mockResponse)
        return reader.getBoolean("success")
    }
}