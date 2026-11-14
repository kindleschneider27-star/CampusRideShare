package com.example.campusrideshare.web;

import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.domain.RideStatus;
import com.example.campusrideshare.domain.User;
import com.example.campusrideshare.services.RideService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/register")
public class RegisterRideController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterRideController.class);
    private final RideService rideService;

    public RegisterRideController(RideService rs) {
        this.rideService = rs;
    }

    @ModelAttribute("pageTitle")
    public String addPageTitle() {return "Register Ride";}


    @ModelAttribute
    public Ride ride(){return new Ride();}

    @GetMapping
    public String registerForm(){return "rideRegistrationForm";}

    @PostMapping
    public String processRideRegistration(@Valid Ride ride,
                                          Errors errors,
                                          @AuthenticationPrincipal User user,
                                          RedirectAttributes redirectAttributes){
        logger.debug("Ride received {}", ride);

        if(errors.hasErrors()){
            return "rideRegistrationForm";
        }

        Ride addRide = rideService.saveRideForUser(ride, user.getUsername());
        redirectAttributes.addFlashAttribute("successMessage", "Ride registered successfully");
        return "redirect:/rides/view/" + addRide.getId();
    }
}
