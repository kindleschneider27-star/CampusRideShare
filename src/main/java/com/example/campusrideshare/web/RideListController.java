package com.example.campusrideshare.web;

import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.services.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("rides/list")
public class RideListController {
    private static final Logger logger = LoggerFactory.getLogger(RideListController.class);
    private final RideService rideService;

    public RideListController(RideService rideService) {
        this.rideService = rideService;
    }

    @ModelAttribute("pageTitle")
    public String PageTitle() {
        return "Ride List";
    }

    @GetMapping
    public String listRides(Model model) {
        model.addAttribute("rides", rideService.getAllRides());
        model.addAttribute("departments", Department.values());
        return "rideList";
    }

}
