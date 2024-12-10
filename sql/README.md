# Introduction
SQL is a programmiing language used to manage and manipulate data within a relational database. Due to its simplicity and declarative nature it provides a way to interact with databases allowing us to store, update and delete data.

It's also considered an essential tool is the banking industry due to the language's usage in data analytics, business intelligence and database management. 

This project was predominently a learning activity designed to help build and enhance my SQL and relational database management system (RDBMS) skills.By solving a variety of SQL queries, I gained practical experience in structuring data, retrieving meaningful insights, and understanding database concepts.

# SQL Queries

###### Table Setup (DDL)
psql -h localhost -p 5432 -U postgres -d exercises

###### Question 1: Show all members 

```sql
SELECT *
FROM cd.members
```

###### Questions 2: Produce a list of the total number of slots booked per facility. For now, just produce an output table consisting of facility id and slots, sorted by facility id.

```sql
SELECT facid, SUM(slots) FROM cd.bookings WHERE facid >= 0 GROUP BY facid ORDER BY facid; 
```

###### Question 3: Adding a new facility - a spa. We need to add it into the facilities table. Use the following values: facid: 9, Name: Spa, membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance:800.

```sql
INSERT INTO cd.facilities
(facid,name,membercost,guestcost,initialoutlay,monthlymaintenance)
VALUES (9,'Spa',20,30,100000,800);
```
###### Question 4: Updating the data  to change The initial outlay was 10000 rather than 8000 for the second tennis court
```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;
```
###### Question 5: Alter the price of the second tennis court so that it costs 10% more than the first one. Try to do this without using 
```sql
update cd.facilities fac
    set
       	membercost = (select membercost * 1.1 from cd.facilities where facid = 0),
        guestcost = (select guestcost * 1.1 from cd.facilities where facid = 0)
    where fac.facid = 1;
```

###### Question 6:Delete all bookings from the cd.bookings table
```sql
DELETE FROM cd.bookings
```

###### Question 7:remove member 37, who has never made a booking
```sql
DELETE FROM cd.members
WHERE memid=37;
```
### Rest of the Queries could be found in queries.sql
 
