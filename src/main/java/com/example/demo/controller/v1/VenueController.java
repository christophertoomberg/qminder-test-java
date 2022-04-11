package com.example.demo.controller.v1;

import com.example.demo.model.Venue;
import com.example.demo.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class VenueController {

    private VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/")
    public String index() {
        return "Welcome to the burger app";
    }

    @GetMapping("/venues")
    public List<Venue> getVenues() throws IOException, InterruptedException {
        return venueService.getVenues();
    }

    @GetMapping(path = "/venues/{id}")
    public Venue getVenueById(@PathVariable String id) throws IOException, InterruptedException {
        return venueService.getVenueByIdWithAnalyzedPictures(id);
    }
}
