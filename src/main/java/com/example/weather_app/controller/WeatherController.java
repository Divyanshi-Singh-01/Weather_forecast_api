package com.example.weather_app.controller;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.weather_app.model.WeatherResponse;

@Controller
public class WeatherController {
        private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Value("${api.key}")
    private String apiKey;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }
    // System.out.println("heelong weather");
    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model) {
        logger.info("Received request for city: {}", city);

        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        logger.info("Constructed URL: {}", url);

        // http://api.openweathermap.org/geo/1.0/direct?q=London&limit=5&appid=b8e3cfedbe8b3e63d5a5178948bc8e17

        RestTemplate restTemplate = new RestTemplate();
        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

        if (weatherResponse != null) {
            model.addAttribute("city", weatherResponse.getName());
            model.addAttribute("country", weatherResponse.getSys().getCountry());
            model.addAttribute("weatherDescription", weatherResponse.getWeather().get(0).getDescription());
            model.addAttribute("temperature", weatherResponse.getMain().getTemp());
            model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
            model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());
            String weatherIcon = "wi wi-owm-" + weatherResponse.getWeather().get(0).getId();
            model.addAttribute("weatherIcon", weatherIcon);
        } else {
            model.addAttribute("error", "City not found.");
        }

        return "weather";
    }
}
