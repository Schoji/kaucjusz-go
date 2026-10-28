package com.kaucjuszGo.domain.machines;

import java.io.Serializable;

public record OpeningHours(
        String monday,
        String tuesday,
        String wednesday,
        String thursday,
        String friday,
        String saturday,
        String sunday) implements Serializable {
}