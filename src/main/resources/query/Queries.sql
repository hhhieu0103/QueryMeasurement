SELECT p.PersonName
FROM Person p, FinancialRecord fr1
                   JOIN (SELECT fr2.PersonID, max(fr2.EarningsYear) as MostRecentYear
                         FROM FinancialRecord fr2
                         GROUP BY fr2.PersonID) as temp
                        ON fr1.PersonID = temp.PersonID AND fr1.EarningsYear = temp.MostRecentYear
WHERE p.PersonID = fr1.PersonID
  AND p.BirthDate < '01/01/1975'
  AND fr1.Earnings > 130000;

SELECT p.PersonName, s.SchoolName
FROM FinancialRecord fr, Person p, School s
WHERE fr.PersonID = p.PersonID
  AND fr.SchoolID = s.SchoolID
  AND fr.Earnings > 400000
  AND fr.StillWorking = 0;

SELECT p.PersonName
FROM FinancialRecord fr, Person p, School s, Job j
WHERE fr.PersonID = p.PersonID
  AND fr.SchoolID = s.SchoolID
  AND fr.JobID = j.JobID
  AND s.SchoolName = 'University of Texas'
  AND j.JobTitle = 'Lecturer'
  AND fr.StillWorking = 0;

SELECT s.SchoolName, fr.SchoolCampus, COUNT(DISTINCT fr.PersonID) as NumberOfActiveFalcutyMembers
FROM FinancialRecord fr, School s
WHERE fr.StillWorking = 1 AND fr.SchoolID = s.SchoolID
GROUP BY fr.SchoolID, fr.SchoolCampus
ORDER BY COUNT(DISTINCT fr.PersonID) DESC
LIMIT 1;

SELECT p.PersonName, j.JobTitle, d.DepartmentName, s.SchoolName, fr.Earnings, fr.EarningsYear
FROM FinancialRecord fr, Person p, Job j, Department d, School s
WHERE fr.PersonID = p.PersonID
  AND fr.JobID = j.JobID
  AND fr.DepartmentID = d.DepartmentID
  AND fr.SchoolID = s.SchoolID
  AND p.PersonName = 'Hieu Ho'
  AND j.JobTitle = 'Engineer'
  AND d.DepartmentName = 'Computer Engineering'
  AND s.SchoolName = 'University of Massachusetts'
ORDER BY fr.EarningsYear DESC
LIMIT 1;

SELECT d.DepartmentName, AVG(fr.Earnings) as AverageEarnings
FROM FinancialRecord fr, Department d
WHERE fr.DepartmentID = d.DepartmentID
GROUP BY fr.DepartmentID
ORDER BY AVG(fr.Earnings) DESC
LIMIT 1;