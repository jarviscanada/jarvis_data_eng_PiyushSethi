-- QUERY: adding a new facility - a spa. We need to add it into the facilities table. Use the following values: facid: 9, Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.
INSERT INTO cd.facilities
(facid,name,membercost,guestcost,initialoutlay,monthlymaintenance)
VALUES (9,'Spa',20,30,100000,800);

--QUERY:adding the spa to the facilities table again. This time, though, we want to automatically generate the value for the next facid, rather than specifying it as a constant. Use the following values for everything else: Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.
insert into cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    select (select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

--QUERY: Updating the data  to change The initial outlay was 10000 rather than 8000 for the second tennis court
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;

--QUERY:alter the price of the second tennis court so that it costs 10% more than the first one. Try to do this without using constant values
update cd.facilities fac
    set
        membercost = (select membercost * 1.1 from cd.facilities where facid = 0),
        guestcost = (select guestcost * 1.1 from cd.facilities where facid = 0)
    where fac.facid = 1;


--QUERY:delete all bookings from the cd.bookings table
DELETE FROM cd.bookings

--QUERY:remove member 37, who has never made a booking
DELETE FROM cd.members
WHERE memid=37;

--
--QUERY: produce a list of facilities that charge a fee to members, and that fee is less than 1/50th of the monthly maintenance cost?
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < 0.02 * monthlymaintenance and membercost > 0;

--QUERY:produce a list of all facilities with the word 'Tennis' in their name
select *
	from cd.facilities
	where
		name like '%Ten%';

--QUERY:retrieve the details of facilities with ID 1 and 5? W/O using OR
SELECT *
from cd.facilities
WHERE facid IN (1,5);

--QUERY: list of members who joined after the start of September 2012?
select memid, surname, firstname, joindate
	from cd.members
	where joindate >= '2012-09-01';

--QUERY:a combined list of all surnames and all facility names.
SELECT name from cd.facilities
UNION SELECT surname from cd.members;

--QUERY:produce a list of the start times for bookings by members named 'David Farrell'
SELECT starttime FROM cd.bookings bks INNER JOIN cd.members mems
ON mems.memid = bks.memid
WHERE firstname='David' and surname='Farrell';

--QUERY:produce a list of the start times for bookings for tennis courts, for the date '2012-09-21'? Return a list of start time and facility name pairings, ordered by the time.
SELECT starttime as start, name FROM cd.bookings bks INNER Join cd.facilities fac
ON fac.facid = bks.facid
WHERE name LIKE '%Tennis Court%' and starttime >= '2012-09-21' and starttime< '2012-09-22'
ORDER BY starttime;

--QUERY:output a list of all members, including the individual who recommended them (if any)? Ensure that results are ordered by (surname, firstname).
SELECT mems.surname AS memsurname,mems.firstname as memfirstname,rec.surname as recsurname,rec.firstname as recfirstname FROM cd.members mems
LEFT OUTER JOIN cd.members rec ON
rec.memid = mems.recommendedby
ORDER BY mems.surname, mems.firstname;

--QUERY:output a list of all members, including the individual who recommended them (if any), without using any joins? Ensure that there are no duplicates in the list, and that each firstname + surname pairing is formatted as a column and ordered.
SELECT mems.firstname || mems.surname as member,
(SELECT rec.firstname || rec.surname as recommended_by FROM cd.members rec
 WHERE rec.memid = mems.recommendedby)
 FROM cd.members mems
 ORDER BY member;

--QUERY:Produce a count of the number of recommendations each member has made. Order by member ID.
SELECT recommendedby, COUNT(*) FROM cd.members
WHERE recommendedby is not NULL
GROUP BY recommendedby
ORDER BY recommendedby;

--QUERY:list of the total number of slots booked per facility.sorted by facility id
SELECT facid, SUM(slots) FROM cd.bookings
WHERE facid >= 0
GROUP BY facid
ORDER BY facid;

--QUERY:Produce a list of the total number of slots booked per facility in the month of September 2012. Produce an output table consisting of facility id and slots, sorted by the number of slots.
SELECT facid, SUM(slots) FROM cd.bookings
WHERE starttime >= '2012-09-01' and starttime < '2012-10-01'
GROUP BY facid
ORDER BY sum(slots);

--QUERY: list of the total number of slots booked per facility per month in the year of 2012. Produce an output table consisting of facility id and slots, sorted by the id and month.
SELECT facid, extract(month from starttime) as month, SUM(slots) FROM cd.bookings
WHERE starttime >= '2012-01-01' and starttime <= '2012-12-31'
GROUP BY facid, month
ORDER BY facid,month;

--QUERY:Find the total number of members (including guests) who have made at least one booking.
SELECT COUNT (DISTINCT memid) from cd.bookings;

--QUERY:Output the names of all members, formatted as 'Surname, Firstname'
select surname || ', ' || firstname as fullname from cd.members
ORDER by surname;

--QUERY: You'd like to find all the telephone numbers that contain parentheses, returning the member ID and telephone number sorted by member ID.
SELECT memid, telephone from cd.members 
WHERE telephone ~ '[()]';
