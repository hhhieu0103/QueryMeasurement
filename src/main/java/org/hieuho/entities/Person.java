package org.hieuho.entities;

import java.time.LocalDate;

public record Person(int personID, String personName, LocalDate birthDate) { }
