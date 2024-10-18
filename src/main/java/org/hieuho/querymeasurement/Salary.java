package org.hieuho.querymeasurement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Salary {
    private final int personID;
    private final String personName;
    private final String schoolID;
    private final String schoolName;
    private final String schoolCampus;
    private final String departmentName;
    private final String departmentID;
    private final LocalDate birthDate;
    private final String stillWorking;
    private final String jobID;
    private final String jobTitle;
    private final int earnings;
    private final int earningsYear;

    public Salary(String[] csvRecord) {
        this.personID = Integer.parseInt(csvRecord[0]);
        this.personName = csvRecord[1];
        this.schoolID = csvRecord[2];
        this.schoolName = csvRecord[3];
        this.schoolCampus = csvRecord[4];
        this.departmentName = csvRecord[5];
        this.departmentID = csvRecord[6];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        this.birthDate = LocalDate.parse(csvRecord[7], formatter);
        this.stillWorking = csvRecord[8];
        this.jobID = csvRecord[9];
        this.jobTitle = csvRecord[10];
        this.earnings = Integer.parseInt(csvRecord[11]);
        this.earningsYear = Integer.parseInt(csvRecord[12]);
    }

    public int getPersonID() {
        return personID;
    }

    public String getPersonName() {
        return personName;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolCampus() {
        return schoolCampus;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getStillWorking() {
        return stillWorking;
    }

    public String getJobID() {
        return jobID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public int getEarnings() {
        return earnings;
    }

    public int getEarningsYear() {
        return earningsYear;
    }
}
