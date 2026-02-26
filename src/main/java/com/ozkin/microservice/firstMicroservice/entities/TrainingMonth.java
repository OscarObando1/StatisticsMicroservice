package com.ozkin.microservice.firstMicroservice.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingMonth {

    private Integer monthNumber;
    private Integer duration;
}
