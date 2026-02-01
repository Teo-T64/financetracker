package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.ProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final ProfileRepo profileRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        ProfileEntity existingProfile = profileRepo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Profile not found with e-mail: "+ email));

        return User.builder().username(existingProfile.getEmail())
                .password(existingProfile.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }


}
