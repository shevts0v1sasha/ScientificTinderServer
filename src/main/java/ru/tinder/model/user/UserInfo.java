package ru.tinder.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

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

}
