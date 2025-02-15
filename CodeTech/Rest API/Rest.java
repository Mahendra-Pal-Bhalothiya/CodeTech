import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {

    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key from OpenWeatherMap
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        String city = "London"; // You can change the city
        String urlString = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);

        try {
            // Create a URL object with the API endpoint
            URL url = new URL(urlString);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // If the response code is 200 (OK), parse the JSON response
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // Read the response line by line
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract and display data in a structured format
                System.out.println("Weather Data for " + city + ":");
                System.out.println("------------------------------------------------");
                System.out.println("Temperature: " + jsonResponse.getJSONObject("main").getDouble("temp") + "°C");
                System.out.println("Humidity: " + jsonResponse.getJSONObject("main").getInt("humidity") + "%");
                System.out.println("Pressure: " + jsonResponse.getJSONObject("main").getInt("pressure") + " hPa");
                System.out.println("Weather Description: " + jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description"));
                System.out.println("Wind Speed: " + jsonResponse.getJSONObject("wind").getDouble("speed") + " m/s");
                System.out.println("------------------------------------------------");

            } else {
                System.out.println("Error: Unable to fetch weather data. HTTP Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
