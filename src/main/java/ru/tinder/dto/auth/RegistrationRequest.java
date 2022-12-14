package ru.tinder.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinder.model.user.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private String username;
    private String password;

    private String name;

    private String surname;

    private String patronymic;

    private String speciality;

    private String jobTitle;

    private String areaOfScientificInterests;

    private String academicTitle;

    private String academicDegree;

    private String linksToQualifyingPapers;

    private String linksToPublications;

    private Role role;

}
