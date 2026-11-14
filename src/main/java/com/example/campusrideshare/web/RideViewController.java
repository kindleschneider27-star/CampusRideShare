package com.example.campusrideshare.web;


import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.domain.Ride;
import com.example.campusrideshare.services.RideService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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

    public String viewRide(@PathVariable UUID id, Model model, Authentication auth) {
        Optional<Ride> ride = rideService.getRideById(id);
        if(ride.isEmpty()){
            logger.debug("Ride: {} not found", id);
            return "redirect:/rides/list";
        }
        Ride current = ride.get();

        boolean canModify = auth != null
                && (auth.getName().equals(current.getDriver().getUsername())
                || auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("canDelete", canModify);

        model.addAttribute("current", current);
        model.addAttribute("Ride to", current.getDestination());
        return "viewRide";
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    public String deleteRide(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        if (rideService.deleteRideById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "ride deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "ride not deleted");
        }

        return "redirect:/rides/list";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    public String viewEditRideForm(@PathVariable UUID id, Model model){
        Optional<Ride> ride = rideService.getRideById(id);
        if (ride.isEmpty()) {
            logger.debug("Ride with id: {} not found to edit", id);
            return "redirect:/rides/list";
        }

        Ride current = ride.get();

        model.addAttribute("ride", ride.get());
        model.addAttribute("departments", Department.values());

        return "rideModifyForm";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN') or @rideAuthorization.isOwner(#id, authentication.name)")
    public String modifyRide(@PathVariable UUID id, @Valid Ride ride, Errors errors, Model model,RedirectAttributes redirectAttributes) {
        logger.debug("Ride received: {}", ride);

        if (errors.hasErrors()) {

            model.addAttribute("ride", ride);
            model.addAttribute("departments", Department.values());

            return "rideModifyForm";
        }

        if(rideService.updateRide(id, ride)) {
            redirectAttributes.addFlashAttribute("successMessage", "ride updated successfully");
        }

        else {
            redirectAttributes.addFlashAttribute("errorMessage", "ride not updated");
        }
        return "redirect:/rides/view/" + id;
    }

}
