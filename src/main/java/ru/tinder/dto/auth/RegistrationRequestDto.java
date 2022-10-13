package ru.tinder.dto.auth;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinder.model.user.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String patronymic;

    @NotNull
    private String speciality;

    @NotNull
    private String jobTitle;

    @NotNull
    private String areaOfScientificInterests;

    @NotNull
    private String academicTitle;

    @NotNull
    private String academicDegree;

    @NotNull
    private String linksToQualifyingPapers;

    @NotNull
    private String linksToPublications;

    @NotNull
    private Role role;

}
