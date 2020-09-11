SELECT count(*) FROM tracks;

INSERT INTO tracks
(Name, AlbumId, MediaTypeId, GenreId,
 Composer, Milliseconds, Bytes, UnitPrice)
VALUES ("Example", 1, 1, 1,
        "Carson", 1000, 22, 1000.00);

select * from tracks;

SELECT last_insert_rowid();

INSERT INTO artists (name)
VALUES
("Robert Parker"),
("Le Matos"),
("Zombie Hyperdrive");

INSERT INTO artists_bak
SELECT ArtistId, Name
FROM artists;

UPDATE employees
SET FirstName='Carson';

SELECT FirstName FROM employees
where EmployeeId=1;

UPDATE employees
SET email = LOWER(
            firstname || "." ||
            lastname || "@montana.edu"
    );

DELETE FROM artists
WHERE name LIKE '%Santana%';

REPLACE INTO employees
    (FirstName, LastName, Email)
VALUES
    ("Carson", "Gross", "carson@bigsky.software");


