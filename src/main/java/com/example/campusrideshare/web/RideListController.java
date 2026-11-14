package com.example.campusrideshare.web;

import com.example.campusrideshare.data.UserRepository;
import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.domain.User;
import com.example.campusrideshare.services.RideService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("rides/list")
public class RideListController {
    private static final Logger logger = LoggerFactory.getLogger(RideListController.class);
    private final RideService rideService;
    private final UserRepository userRepo;

    public RideListController(RideService rs, UserRepository ur) {
        this.rideService = rs;
        this.userRepo = ur;
    }

    @ModelAttribute("pageTitle")
    public String PageTitle() {
        return "Ride List";
    }

    @ModelAttribute("currentUser")
    public User currentUser(Authentication auth) {
        if (auth == null) return null;
        return userRepo.findByUsername(auth.getName()).orElse(null);
    }

    @GetMapping
    public String listRides(@RequestParam(required = false) String filterType, Model model) {
        Department selected = null;
        List<Ride> rides;
        if (filterType != null && !filterType.isBlank()) {
            try {
                selected = Department.valueOf(filterType);
                rides = rideService.getRidesByDriver_Department(selected);
            } catch (IllegalArgumentException e) {
                logger.debug("Unknown department filter: {}", filterType);
                rides = rideService.getAllRides();
            }
        } else {
            rides = rideService.getAllRides();
        }
        model.addAttribute("rides", rides);
        model.addAttribute("departments", Department.values());
        model.addAttribute("filterType", selected);
        return "rideList";
    }
}
