package ru.tinder.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinder.model.user.Role;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

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
