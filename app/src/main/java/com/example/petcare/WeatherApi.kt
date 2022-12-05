package com.example.petcare

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
class WeatherApi : AppCompatActivity(){
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_api)

        val buttonback: ImageButton =findViewById(R.id.button_back)
        buttonback.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val buttonweather : Button = findViewById(R.id.btn_getweather)

        buttonweather.setOnClickListener{



            var URL="https://weather-by-api-ninjas.p.rapidapi.com/v1/weather?city=Kocaeli";
            if(URL.isNotEmpty()){
                val client = OkHttpClient();
                val request = Request.Builder()
                    .url(URL)
                    .get()
                    .addHeader("X-RapidAPI-Key",
                        "597dd55339msh561f185c9ef7eb5p1d9e4ejsna8ee03db294c")
                    .addHeader("X-RapidAPI-Host", "weather-by-api-ninjas.p.rapidapi.com")
                    .build();
                client.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }
                    override fun onResponse(call: Call, response:
                    Response
                    ) {
                        response.use {
                            if (!response.isSuccessful){
                                val x:String="something didn't load"
                            }
                            else{
                                var body=response.body?.string();
                                val gson=GsonBuilder().create();
                                val weather=gson.fromJson(body,getWeather::class.java)


                                val textWeather=findViewById<TextView>(R.id.textView8);
                                textWeather.text="TEMPERATURE:     "+weather.temp.toString()+"\n"+
                                        "\nHUMIDITY:       "+weather.humidity.toString()+"\n"+
                                        "\nMINIMUM TEMP:    "+weather.min_temp.toString()+"\n"+
                                        "\nMAXIMUM TEMP:    "+weather.max_temp.toString()+"\n"+
                                        "\nWIND SPEED:     "+weather.wind_speed.toString()




                            }
                        }
                    }
                })
            }
        }
    }
    //Api'den veri cekmek icin olusturdugumuz class
    class getWeather(val cloud_pct:Int,val temp:Double,val feel_like:Int,val humidity:Int,val min_temp:Int,val max_temp:Int,
                     val wind_speed:Double,val wind_degrees:Int,val sunrise:String,val sunset:String)}