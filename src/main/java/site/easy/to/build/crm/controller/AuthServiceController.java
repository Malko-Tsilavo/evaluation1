package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.config.JwtTokenProvider;
import site.easy.to.build.crm.config.CrmUserDetails;
import site.easy.to.build.crm.config.CustomerUserDetails;

@RestController
public class AuthServiceController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CrmUserDetails crmUserDetails;

    @Autowired
    private CustomerUserDetails customerUserDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login_service")
    public String login(@RequestBody LoginRequest loginRequest) {
        // Déterminez quel UserDetailsService utiliser en fonction du type d'utilisateur
        UserDetails userDetails;
        if (loginRequest.getUserType().equals("CRM")) {
            userDetails = crmUserDetails.loadUserByUsername(loginRequest.getUsername());
        } else if (loginRequest.getUserType().equals("CUSTOMER")) {
            userDetails = customerUserDetails.loadUserByUsername(loginRequest.getUsername());
        } else {
            throw new RuntimeException("Type d'utilisateur non valide");
        }

        // Vérifiez le mot de passe
        if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        // Authentifiez l'utilisateur
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Générez le JWT
        return jwtTokenProvider.generateToken(loginRequest.getUsername());
    }
}