package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.AuthDTO;
import in.teotunjic.financetracker.dto.ProfileDTO;
import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.ProfileRepo;
import in.teotunjic.financetracker.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepo profileRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){
        ProfileEntity newProfile = toProfileEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepo.save(newProfile);
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your financeTracker account";
        String body = "Click on the link to activate account " + activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject,body);
        return toProfileDTO(newProfile);
    }

    public ProfileEntity toProfileEntity(ProfileDTO profileDTO){
        return ProfileEntity.builder().id(profileDTO.getId()).fullName(profileDTO.getFullName()).email(profileDTO.getEmail()).password(passwordEncoder.encode(profileDTO.getPassword())).pfpImageUrl(profileDTO.getPfpImageUrl()).createdAt(profileDTO.getCreatedAt()).updatedAt(profileDTO.getUpdatedAt()).build();

    }
    public ProfileDTO toProfileDTO(ProfileEntity profileEntity){
        return ProfileDTO.builder().id(profileEntity.getId()).fullName(profileEntity.getFullName()).email(profileEntity.getEmail()).pfpImageUrl(profileEntity.getPfpImageUrl()).createdAt(profileEntity.getCreatedAt()).updatedAt(profileEntity.getUpdatedAt()).build();

    }

    public boolean activateProfile(String activationToken){
        return profileRepo.findByActivationToken(activationToken).map(profile->{
            profile.setIsActive(true);
            profileRepo.save(profile);
            return true;
        }).orElse( false);

    }

    public boolean isProfileActive(String email){
        return profileRepo.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);

    }

    public ProfileEntity getCurrProfile(){
       Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
       return profileRepo.findByEmail(auth.getName()).orElseThrow(()->new UsernameNotFoundException("Profile not found with e-mail: "+ auth.getName()));

    }

    public ProfileDTO getPublicProfile(String email) {
        ProfileEntity currUser = (email == null)
                ? getCurrProfile()
                : profileRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not found"));

        return ProfileDTO.builder()
                .id(currUser.getId())
                .fullName(currUser.getFullName())
                .email(currUser.getEmail())
                .pfpImageUrl(currUser.getPfpImageUrl())
                .createdAt(currUser.getCreatedAt())
                .updatedAt(currUser.getUpdatedAt())
                .build();
    }

    public Map<String,Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
            String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of("token",token,"user",getPublicProfile(authDTO.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid e-mail or password");
        }
    }
}
