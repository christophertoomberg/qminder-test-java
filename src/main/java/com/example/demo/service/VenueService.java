package com.example.demo.service;

import com.example.demo.model.Venue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.*;

@Service
public class VenueService {

    @Value("${apiKey}")
    private String apiKey;

    private static final String VENUES_URL
            = "https://api.foursquare.com/v3/places/search?query=burger%20joint&ll=58.37%2C26.72&radius=4000";
    private static final String RECOGNISER_URL
            = "https://pplkdijj76.execute-api.eu-west-1.amazonaws.com/prod/recognize";


    public List<Venue> getVenues() throws IOException, InterruptedException {
        List<Venue> venues = new ArrayList<>();

        HttpResponse<String> response = makeGetRequest(VENUES_URL);

        JSONArray venueObjects = new JSONObject(response.body()).getJSONArray("results");

        for (int i = 0; i < venueObjects.length(); i++) {
            JSONObject venueObject = venueObjects.getJSONObject(i);
            String venueObjectId = venueObject.getString("fsq_id");

            Venue venue = new Venue(
                    venueObjectId,
                    venueObject.getString("name"),
                    venueObject.getJSONObject("location").getString("formatted_address"));
            venues.add(venue);
        }

        return venues;
    }


    public Venue getVenueByIdWithAnalyzedPictures(String id) throws IOException, InterruptedException {

        Venue venueDetails = getVenueDetails(id);
        List<String> venuePicturesData = getPictureUrlsForVenue(id);

        if (venuePicturesData.isEmpty()) {
            venueDetails.setHamburgerPictureUrl("No pictures for this venue");
            return venueDetails;
        }
        String firstHamburgerPicture = getHamburgerPictureLink(venuePicturesData);
        venueDetails.setHamburgerPictureUrl(firstHamburgerPicture);

        return venueDetails;
    }

    private Venue getVenueDetails(String id) throws IOException, InterruptedException {

        String url = "https://api.foursquare.com/v3/places/" + id;

        HttpResponse<String> response = makeGetRequest(url);
        System.out.println(response.body());
        JSONObject jsonObject = new JSONObject(response.body());
        return new Venue(
                jsonObject.getString("fsq_id"),
                jsonObject.getString("name"),
                jsonObject.getJSONObject("location").getString("formatted_address"));
    }


    private List<String> getPictureUrlsForVenue(String id) throws IOException, InterruptedException {

        String pictureRequestUrl = "https://api.foursquare.com/v3/places/"
                + id
                + "/photos?sort=NEWEST";

        HttpResponse<String> response = makeGetRequest(pictureRequestUrl);

        JSONArray jsonArray = new JSONArray(response.body());
        List<String> pictureUrls = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String pictureUrl = jsonObject.getString("prefix") + "original" + jsonObject.getString("suffix");
            pictureUrls.add(pictureUrl);
        }

        return pictureUrls;
    }

    private String getHamburgerPictureLink(List<String> pictures) throws IOException, InterruptedException {
        HashMap<String, List<String>> pictureMap = new HashMap<>();
        pictureMap.put("urls", pictures);

        HttpResponse<String> response = makePostRequest(JSONObject.valueToString(pictureMap));

        JSONObject jsonObject = new JSONObject(response.body());
        if (jsonObject.has("error")) {
            return "No pictures of burgers :(";
        }
        return jsonObject.getString("urlWithBurger");
    }


    private HttpResponse<String> makeGetRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> makePostRequest(String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VenueService.RECOGNISER_URL))
                .header("Accept", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(body))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
