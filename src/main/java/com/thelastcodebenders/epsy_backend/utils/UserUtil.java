package com.thelastcodebenders.epsy_backend.utils;

import com.thelastcodebenders.epsy_backend.exceptions.EpsyException;
import com.thelastcodebenders.epsy_backend.exceptions.UserNotFoundException;
import com.thelastcodebenders.epsy_backend.models.entities.User;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@UtilityClass
public class UserUtil {
    public static User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new EpsyException("Authentication required");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User userDetails) {
            // If your AppUser implements UserDetails, you can cast directly
            return userDetails;
        }
        log.info("Couldn't fetch user from security context, principal is: {}", principal);
        throw new UserNotFoundException();
    }
}
