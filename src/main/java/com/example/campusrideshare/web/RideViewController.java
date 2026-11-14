package com.example.campusrideshare.web;


import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.services.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/rides/view")
public class RideViewController {
    private static final Logger logger = LoggerFactory.getLogger(RideViewController.class);
    private final RideService rideService;

    public RideViewController(RideService rs){
        this.rideService = rs;
    }

    @GetMapping("/{id}")
    public String viewRide(@PathVariable UUID id, Model model){
        Optional<Ride> ride = rideService.getRideById(id);
        if(ride.isEmpty()){
            logger.debug("Ride: {} not found", id);
            return "redirect:/list";
        }

        Ride current = ride.get();

        model.addAttribute("current", current);
        model.addAttribute("Ride to", current.getDestination());
        return "viewRide";
    }

    //@PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    //@PostMapping("/current/{id}/delete")
    //do i need the edit page?

    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    @PostMapping("/current/{id}/delete")
    public String deleteRide(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        if (rideService.deleteRideById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "ride deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "ride not deleted");
        }

        return "redirect:/list";
    }



}
