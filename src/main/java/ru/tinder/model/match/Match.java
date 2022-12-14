package ru.tinder.model.match;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    private Long id;
    private Long whoId;
    private Long whomId;
    private boolean match;

    private LocalDateTime matchedTime;

}
