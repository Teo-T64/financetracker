package in.teotunjic.financetracker.service;

import in.teotunjic.financetracker.dto.ProfileDTO;
import in.teotunjic.financetracker.entity.ProfileEntity;
import in.teotunjic.financetracker.repo.ProfileRepo;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepo profileRepo;
    private final EmailService emailService;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){
        ProfileEntity newProfile = toProfileEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepo.save(newProfile);
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your financeTracker account";
        String body = "Click on the link to activate account" + activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject,body);
        return toProfileDTO(newProfile);
    }

    public ProfileEntity toProfileEntity(ProfileDTO profileDTO){
        return ProfileEntity.builder().id(profileDTO.getId()).fullName(profileDTO.getFullName()).email(profileDTO.getEmail()).password(profileDTO.getPassword()).pfpImageUrl(profileDTO.getPfpImageUrl()).createdAt(profileDTO.getCreatedAt()).updatedAt(profileDTO.getUpdatedAt()).build();

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

}
