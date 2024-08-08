package com.example.yandexmapeat

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import model.Coordinates
import network.api.RequestBody
import network.api.YandexMapEatApi
import network.util.toToken
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiServiceTest {
    private lateinit var mockWebServer: MockWebServer

    @Inject
    private lateinit var apiService: YandexMapEatApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val gson: Gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/51.250.39.97:8080/"))
            //.baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(YandexMapEatApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetTasks() = runBlocking {
        // Подготовка тестового ответа
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                  "items": [
                    {
                      "id": "1",
                      "coordinates": {
                        "lat": 0,
                        "lon": 0
                      },
                      "name": "Test Restaurant",
                      "description": "A great place to eat.",
                      "address": "123 Test St",
                      "is_approved": true,
                      "rating": 4.5,
                      "price_lower_bound": 10,
                      "price_upper_bound": 50,
                      "tags": ["Italian", "Pizza"],
                      "is_favorite": true
                    }
                  ]
                }
            """.trimIndent()
            )

        mockWebServer.enqueue(mockResponse)

        // Запрос к API
        val response = apiService.getRestaurants("Asd".toToken(), requestBody = RequestBody(Coordinates(55.0, 37.0), Coordinates(56.0, 38.0,))
             )

        // Проверка результата
        assertEquals(1, response.items.size)
        assertEquals("Test Restaurant", response.items[0].name)
        assertEquals(4.5, response.items[0].rating, 0.01)
    }

    @Test
    fun testGetTasks_badRequest() = runBlocking {
        // Подготовка тестового ответа с ошибкой 400
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody("Bad Request")

        mockWebServer.enqueue(mockResponse)

        try {
            val list = apiService.getRestaurants("Asd".toToken(), requestBody = RequestBody(Coordinates(55.0, 37.0), Coordinates(56.0, 38.0,))
            )
            assertTrue(false)
        } catch (e: Exception) {

            // Проверка типа исключения
            assertTrue(e.message?.contains("400") == true)
        }

    }

    @Test
    fun testGetTasks_unauthorized() = runBlocking {
        // Подготовка тестового ответа с ошибкой 401
        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody("Unauthorized")

        mockWebServer.enqueue(mockResponse)

        // Запрос к API
        try {
            apiService.getRestaurants("Asd".toToken(), requestBody = RequestBody(Coordinates(55.0, 37.0), Coordinates(56.0, 38.0,))
            )
            assertTrue(false)
        } catch (e: Exception) {

            // Проверка типа исключения
            assertTrue(e.message?.contains("401") == true)
        }

    }

}