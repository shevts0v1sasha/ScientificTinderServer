package ru.tinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TDate {

    private int seconds;
    private int minutes;
    private int hours;
    private int date;
    private int month;
    private int year;

}
