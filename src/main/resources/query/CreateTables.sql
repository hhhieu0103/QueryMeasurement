CREATE TABLE Person (
                        PersonID INTEGER NOT NULL,
                        PersonName TEXT NOT NULL,
                        BirthDate TEXT NOT NULL,
                        PRIMARY KEY (PersonID)
);

CREATE TABLE School (
                        SchoolID TEXT NOT NULL,
                        SchoolName TEXT NOT NULL,
                        PRIMARY KEY (SchoolID)
);

CREATE TABLE Campus (
                        SchoolID TEXT NOT NULL,
                        SchoolCampus TEXT NOT NULL,
                        PRIMARY KEY (SchoolID, SchoolCampus),
                        FOREIGN KEY (SchoolID) REFERENCES School(SchoolID)
);

CREATE TABLE Department (
                            DepartmentID TEXT NOT NULL,
                            DepartmentName TEXT NOT NULL,
                            PRIMARY KEY (DepartmentID)
);

CREATE TABLE Job (
                     JobID TEXT NOT NULL,
                     JobTitle TEXT NOT NULL,
                     SchoolID TEXT NOT NULL,
                     SchoolCampus TEXT NOT NULL,
                     DepartmentID TEXT NOT NULL,
                     PRIMARY KEY (JobID, SchoolID, SchoolCampus, DepartmentID),
                     FOREIGN KEY (SchoolID, SchoolCampus) REFERENCES Campus,
                     FOREIGN KEY (DepartmentID) REFERENCES Department
);

CREATE TABLE FinancialRecord (
                                 PersonID INTEGER NOT NULL,
                                 JobID TEXT NOT NULL,
                                 StillWorking INTEGER NOT NULL,
                                 Earnings INTEGER NOT NULL,
                                 EarningsYear INTEGER NOT NULL,
                                 PRIMARY KEY (PersonID, JobID, StillWorking, Earnings, EarningsYear),
                                 FOREIGN KEY (PersonID) REFERENCES Person,
                                 FOREIGN KEY (JobID) REFERENCES Job
);
