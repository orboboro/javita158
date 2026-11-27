BEGIN;

-- Resource Table
CREATE TABLE Resource (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    base_market_price DECIMAL(10, 2) NOT NULL,
    is_processed BOOLEAN DEFAULT FALSE
);
INSERT INTO Resource VALUES(1,'Mais',1000,FALSE);
INSERT INTO Resource VALUES(2,'Tabacco',1500,FALSE);
INSERT INTO Resource VALUES(3,'Zucchero',1200,FALSE);
INSERT INTO Resource VALUES(4,'Legname',800,FALSE);
INSERT INTO Resource VALUES(5,'Oro',10000,FALSE);
INSERT INTO Resource VALUES(6,'Assi',2000,TRUE);
INSERT INTO Resource VALUES(7,'Rum',4000,TRUE);

-- Faction Table
CREATE TABLE Faction (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);
INSERT INTO Faction VALUES(1,'Capitalist','Amano il denaro e il libero mercato.');
INSERT INTO Faction VALUES(2,'Communist','Amano l''uguaglianza e il controllo statale.');
INSERT INTO Faction VALUES(3,'Militarist','Amano l''ordine e un esercito forte.');
INSERT INTO Faction VALUES(4,'Religious','Amano la fede e la moralità.');

-- BuildingType Table
CREATE TABLE BuildingType (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    category VARCHAR(20) CHECK (category IN ('Extraction', 'Industry', 'Housing', 'Service', 'Logistics', 'Government')),
    construction_cost INTEGER NOT NULL,
    maintenance_cost DECIMAL(10, 2) NOT NULL,
    max_workers INTEGER DEFAULT 0,
    input_resource_id INTEGER REFERENCES Resource(id),
    output_resource_id INTEGER REFERENCES Resource(id),
    production_rate DECIMAL(5, 2)
);
INSERT INTO BuildingType VALUES(1,'Piantagione di Mais','Extraction',500,20,4,NULL,1,10);
INSERT INTO BuildingType VALUES(2,'Piantagione di Tabacco','Extraction',600,25,4,NULL,2,8);
INSERT INTO BuildingType VALUES(3,'Piantagione di Zucchero','Extraction',550,22,4,NULL,3,9);
INSERT INTO BuildingType VALUES(4,'Casa di Campagna','Housing',400,10,0,NULL,NULL,NULL);
INSERT INTO BuildingType VALUES(5,'Palazzo','Government',10000,200,2,NULL,NULL,NULL);
INSERT INTO BuildingType VALUES(6,'Segheria','Industry',1000,40,4,4,6,5);
INSERT INTO BuildingType VALUES(7,'Distilleria di Rum','Industry',1200,50,4,3,7,4);

-- Building Table
CREATE TABLE Building (
    id SERIAL PRIMARY KEY,
    building_type_id INTEGER NOT NULL REFERENCES BuildingType(id),
    budget_level INTEGER DEFAULT 3 CHECK (budget_level BETWEEN 1 AND 5),
    is_active BOOLEAN DEFAULT TRUE
);
INSERT INTO Building VALUES(1,5,5,TRUE);
INSERT INTO Building VALUES(2,1,3,TRUE);
INSERT INTO Building VALUES(3,1,3,TRUE);
INSERT INTO Building VALUES(4,2,3,TRUE);
INSERT INTO Building VALUES(5,4,3,TRUE);
INSERT INTO Building VALUES(6,4,3,TRUE);

-- Citizen Table
CREATE TABLE Citizen (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    gender CHAR(1) CHECK (gender IN ('M', 'F','N')),
    age INTEGER,
    education_level VARCHAR(20) CHECK (education_level IN ('Illiterate', 'GradeSchool', 'HighSchool', 'College')),
    job_building_id INTEGER REFERENCES Building(id),
    salary DECIMAL(10, 2),
    home_building_id INTEGER REFERENCES Building(id),
    wealth_level VARCHAR(20) CHECK (wealth_level IN ('Broke', 'Poor', 'Well-off', 'Rich')),
    supported_faction_id INTEGER REFERENCES Faction(id),
    is_rebel BOOLEAN DEFAULT FALSE,
    happiness_total INTEGER CHECK (happiness_total BETWEEN 0 AND 100)
);
INSERT INTO Citizen VALUES(1,'El Presidente','Tropico','M',45,'College',1,100,1,'Rich',1,FALSE,100);
INSERT INTO Citizen VALUES(2,'Penultimo','Segretario','M',35,'HighSchool',1,20,1,'Well-off',3,FALSE,80);
INSERT INTO Citizen VALUES(3,'Ricardo','Gomez','M',27,'Illiterate',2,8,5,'Poor',2,FALSE,58);
INSERT INTO Citizen VALUES(4,'Maria','Cruz','F',35,'Illiterate',2,8,5,'Poor',2,FALSE,55);
INSERT INTO Citizen VALUES(5,'Javier','Morales','M',41,'Illiterate',2,8,6,'Poor',4,FALSE,48);
INSERT INTO Citizen VALUES(6,'Sofia','Reyes','F',22,'GradeSchool',2,9,6,'Poor',1,FALSE,65);
INSERT INTO Citizen VALUES(7,'Carlos','Diaz','M',33,'Illiterate',3,8,NULL,'Broke',3,FALSE,41);
INSERT INTO Citizen VALUES(8,'Lucia','Castillo','F',29,'GradeSchool',3,9,NULL,'Broke',4,FALSE,58);
INSERT INTO Citizen VALUES(9,'Miguel','Ortega','M',50,'Illiterate',3,8,NULL,'Broke',2,FALSE,39);
INSERT INTO Citizen VALUES(10,'Isabella','Perez','F',19,'GradeSchool',3,9,NULL,'Broke',1,FALSE,61);
INSERT INTO Citizen VALUES(11,'Alejandro','Santos','M',25,'Illiterate',4,10,NULL,'Poor',1,FALSE,68);
INSERT INTO Citizen VALUES(12,'Valeria','Jimenez','F',38,'GradeSchool',4,11,NULL,'Poor',4,FALSE,61);
INSERT INTO Citizen VALUES(13,'Hector','Moreno','M',45,'Illiterate',4,10,NULL,'Broke',3,TRUE,15);
INSERT INTO Citizen VALUES(14,'Elena','Ruiz','F',62,'Illiterate',4,10,NULL,'Broke',4,FALSE,33);
INSERT INTO Citizen VALUES(15,'Fidel','Castro','M',55,'College',NULL,0,NULL,'Poor',2,FALSE,50);
INSERT INTO Citizen VALUES(16,'Eva','Peron','F',33,'HighSchool',NULL,0,NULL,'Well-off',2,FALSE,68);
INSERT INTO Citizen VALUES(17,'Augusto','Pinochet','M',65,'College',NULL,0,NULL,'Rich',3,FALSE,45);
INSERT INTO Citizen VALUES(18,'Frida','Kahlo','F',47,'College',NULL,0,NULL,'Well-off',2,FALSE,75);
INSERT INTO Citizen VALUES(19,'Diego','Rivera','M',58,'College',NULL,0,NULL,'Well-off',2,FALSE,73);
INSERT INTO Citizen VALUES(20,'Penelope','Cruz','F',28,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,62);
INSERT INTO Citizen VALUES(21,'Antonio','Banderas','M',42,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,66);
INSERT INTO Citizen VALUES(22,'Juan','Garcia','M',25,'GradeSchool',NULL,0,NULL,'Poor',2,FALSE,52);
INSERT INTO Citizen VALUES(23,'Carmen','Rodriguez','F',41,'Illiterate',NULL,0,NULL,'Broke',4,FALSE,43);
INSERT INTO Citizen VALUES(24,'Jose','Martinez','M',36,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,58);
INSERT INTO Citizen VALUES(25,'Ana','Lopez','F',22,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,61);
INSERT INTO Citizen VALUES(26,'Francisco','Hernandez','M',53,'Illiterate',NULL,0,NULL,'Broke',3,FALSE,38);
INSERT INTO Citizen VALUES(27,'Laura','Gonzalez','F',30,'GradeSchool',NULL,0,NULL,'Poor',4,FALSE,55);
INSERT INTO Citizen VALUES(28,'David','Sanchez','M',28,'College',NULL,0,NULL,'Well-off',1,FALSE,70);
INSERT INTO Citizen VALUES(29,'Paula','Romero','F',19,'GradeSchool',NULL,0,NULL,'Poor',2,FALSE,49);
INSERT INTO Citizen VALUES(30,'Daniel','Sosa','M',48,'Illiterate',NULL,0,NULL,'Broke',3,TRUE,20);
INSERT INTO Citizen VALUES(31,'Sara','Navarro','F',34,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,64);
INSERT INTO Citizen VALUES(32,'Javier','Gomez','M',54,'Illiterate',NULL,0,NULL,'Broke',3,FALSE,34);
INSERT INTO Citizen VALUES(33,'Ricardo','Perez','M',63,'Illiterate',NULL,0,NULL,'Broke',1,FALSE,51);
INSERT INTO Citizen VALUES(34,'Che','Gomez','M',24,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,61);
INSERT INTO Citizen VALUES(35,'Maria','Cruz','F',67,'Illiterate',NULL,0,NULL,'Broke',2,FALSE,28);
INSERT INTO Citizen VALUES(36,'Eva','Ramirez','F',29,'GradeSchool',NULL,0,NULL,'Broke',2,FALSE,33);
INSERT INTO Citizen VALUES(37,'Valeria','Morales','F',41,'GradeSchool',NULL,0,NULL,'Poor',4,FALSE,53);
INSERT INTO Citizen VALUES(38,'Marta','Perez','F',28,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,49);
INSERT INTO Citizen VALUES(39,'Raul','Gomez','M',51,'GradeSchool',NULL,0,NULL,'Broke',3,FALSE,64);
INSERT INTO Citizen VALUES(40,'Elena','Gomez','F',58,'Illiterate',NULL,0,NULL,'Broke',3,FALSE,69);
INSERT INTO Citizen VALUES(41,'Javier','Gomez','M',23,'HighSchool',NULL,0,NULL,'Poor',2,FALSE,31);
INSERT INTO Citizen VALUES(42,'Maria','Castillo','F',78,'Illiterate',NULL,0,NULL,'Broke',3,FALSE,30);
INSERT INTO Citizen VALUES(43,'Valeria','Gomez','F',28,'HighSchool',NULL,0,NULL,'Well-off',4,FALSE,50);
INSERT INTO Citizen VALUES(44,'Hector','Diaz','M',67,'Illiterate',NULL,0,NULL,'Broke',3,FALSE,52);
INSERT INTO Citizen VALUES(45,'Francisco','Cruz','M',48,'GradeSchool',NULL,0,NULL,'Poor',3,FALSE,24);
INSERT INTO Citizen VALUES(46,'Ricardo','Perez','M',79,'Illiterate',NULL,0,NULL,'Broke',4,FALSE,33);
INSERT INTO Citizen VALUES(47,'Isabella','Diaz','F',29,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,61);
INSERT INTO Citizen VALUES(48,'Manuel','Gomez','M',41,'Illiterate',NULL,0,NULL,'Broke',3,FALSE,48);
INSERT INTO Citizen VALUES(49,'Carlos','Perez','M',78,'Illiterate',NULL,0,NULL,'Broke',4,FALSE,44);
INSERT INTO Citizen VALUES(50,'Juana','Gomez','F',29,'HighSchool',NULL,0,NULL,'Poor',2,FALSE,61);
INSERT INTO Citizen VALUES(51,'Ana','Perez','F',51,'Illiterate',NULL,0,NULL,'Broke',4,FALSE,71);
INSERT INTO Citizen VALUES(52,'Antonio','Gomez','M',61,'Illiterate',NULL,0,NULL,'Broke',1,FALSE,44);
INSERT INTO Citizen VALUES(53,'Che','Perez','M',42,'Illiterate',NULL,0,NULL,'Broke',3,FALSE,68);
INSERT INTO Citizen VALUES(54,'Maria','Cruz','F',58,'GradeSchool',NULL,0,NULL,'Poor',2,FALSE,56);
INSERT INTO Citizen VALUES(55,'Javier','Castillo','M',28,'HighSchool',NULL,0,NULL,'Poor',4,FALSE,39);
INSERT INTO Citizen VALUES(56,'Valeria','Gomez','F',31,'GradeSchool',NULL,0,NULL,'Poor',1,FALSE,50);
INSERT INTO Citizen VALUES(57,'Carlos','Perez','M',37,'GradeSchool',NULL,0,NULL,'Poor',1,FALSE,61);
INSERT INTO Citizen VALUES(58,'Eva','Diaz','F',37,'GradeSchool',NULL,0,NULL,'Poor',4,FALSE,32);
INSERT INTO Citizen VALUES(59,'Isabella','Perez','F',29,'GradeSchool',NULL,0,NULL,'Poor',1,FALSE,41);
INSERT INTO Citizen VALUES(60,'Frida','Castillo','F',24,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,66);
INSERT INTO Citizen VALUES(61,'Ricardo','Cruz','M',28,'HighSchool',NULL,0,NULL,'Poor',3,FALSE,71);
INSERT INTO Citizen VALUES(62,'Marta','Gomez','F',78,'Illiterate',NULL,0,NULL,'Broke',2,FALSE,63);
INSERT INTO Citizen VALUES(63,'Javier','Diaz','M',28,'HighSchool',NULL,0,NULL,'Poor',2,FALSE,30);
INSERT INTO Citizen VALUES(64,'Che','Perez','M',25,'HighSchool',NULL,0,NULL,'Well-off',1,FALSE,33);
INSERT INTO Citizen VALUES(65,'Antonio','Castillo','M',31,'GradeSchool',NULL,0,NULL,'Poor',4,FALSE,60);
INSERT INTO Citizen VALUES(66,'Ricardo','Diaz','M',25,'HighSchool',NULL,0,NULL,'Well-off',3,FALSE,35);
INSERT INTO Citizen VALUES(67,'Ricardo','Perez','M',20,'GradeSchool',NULL,0,NULL,'Poor',2,FALSE,48);
INSERT INTO Citizen VALUES(68,'Maria','Gomez','F',32,'GradeSchool',NULL,0,NULL,'Broke',3,FALSE,67);
INSERT INTO Citizen VALUES(69,'Che','Castillo','M',61,'Illiterate',NULL,0,NULL,'Broke',1,FALSE,68);
INSERT INTO Citizen VALUES(70,'Raul','Perez','M',41,'Illiterate',NULL,0,NULL,'Broke',4,FALSE,24);
INSERT INTO Citizen VALUES(71,'Eva','Diaz','F',51,'GradeSchool',NULL,0,NULL,'Poor',2,FALSE,29);
INSERT INTO Citizen VALUES(72,'Javier','Gomez','M',28,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,63);
INSERT INTO Citizen VALUES(73,'Maria','Castillo','F',25,'HighSchool',NULL,0,NULL,'Poor',4,FALSE,55);
INSERT INTO Citizen VALUES(74,'Ricardo','Diaz','M',69,'Illiterate',NULL,0,NULL,'Broke',2,FALSE,31);
INSERT INTO Citizen VALUES(75,'Valeria','Perez','F',29,'HighSchool',NULL,0,NULL,'Poor',2,FALSE,30);
INSERT INTO Citizen VALUES(76,'Marta','Gomez','F',44,'Illiterate',NULL,0,NULL,'Broke',1,FALSE,48);
INSERT INTO Citizen VALUES(77,'Antonio','Cruz','M',54,'Illiterate',NULL,0,NULL,'Broke',2,FALSE,49);
INSERT INTO Citizen VALUES(78,'Che','Diaz','M',28,'HighSchool',NULL,0,NULL,'Poor',3,FALSE,37);
INSERT INTO Citizen VALUES(79,'Javier','Castillo','M',79,'Illiterate',NULL,0,NULL,'Broke',1,FALSE,39);
INSERT INTO Citizen VALUES(80,'Frida','Gomez','F',51,'GradeSchool',NULL,0,NULL,'Broke',1,FALSE,68);
INSERT INTO Citizen VALUES(81,'Eva','Perez','F',41,'GradeSchool',NULL,0,NULL,'Poor',4,FALSE,38);
INSERT INTO Citizen VALUES(82,'Maria','Diaz','F',31,'GradeSchool',NULL,0,NULL,'Poor',1,FALSE,32);
INSERT INTO Citizen VALUES(83,'Ricardo','Castillo','M',21,'HighSchool',NULL,0,NULL,'Poor',2,FALSE,49);
INSERT INTO Citizen VALUES(84,'Isabella','Cruz','F',39,'GradeSchool',NULL,0,NULL,'Broke',1,FALSE,48);
INSERT INTO Citizen VALUES(85,'Antonio','Gomez','M',28,'HighSchool',NULL,0,NULL,'Poor',4,FALSE,63);
INSERT INTO Citizen VALUES(86,'Che','Perez','M',29,'HighSchool',NULL,0,NULL,'Poor',2,FALSE,70);
INSERT INTO Citizen VALUES(87,'Marta','Diaz','F',49,'Illiterate',NULL,0,NULL,'Broke',1,TRUE,21);
INSERT INTO Citizen VALUES(88,'Javier','Castillo','M',59,'Illiterate',NULL,0,NULL,'Broke',4,FALSE,39);
INSERT INTO Citizen VALUES(89,'Valeria','Gomez','F',39,'GradeSchool',NULL,0,NULL,'Poor',1,FALSE,41);
INSERT INTO Citizen VALUES(90,'Carlos','Perez','M',29,'HighSchool',NULL,0,NULL,'Poor',3,FALSE,65);
INSERT INTO Citizen VALUES(91,'Maria','Diaz','F',49,'Illiterate',NULL,0,NULL,'Broke',2,FALSE,28);
INSERT INTO Citizen VALUES(92,'Isabella','Cruz','F',39,'GradeSchool',NULL,0,NULL,'Poor',3,FALSE,44);
INSERT INTO Citizen VALUES(93,'Frida','Castillo','F',29,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,58);
INSERT INTO Citizen VALUES(94,'Kai','Sol','N',26,'HighSchool',NULL,0,NULL,'Poor',1,FALSE,59);

-- Production_Batch Table
CREATE TABLE Production_Batch (
    id SERIAL PRIMARY KEY,
    building_id INTEGER REFERENCES Building(id),
    production_date DATE,
    amount INTEGER,
    market_value NUMERIC(10,2)
);
INSERT INTO Production_Batch VALUES(1,2,'1960-05-15',150,150000);
INSERT INTO Production_Batch VALUES(2,4,'1960-10-20',100,150000);
INSERT INTO Production_Batch VALUES(3,3,'1961-06-01',160,160000);
INSERT INTO Production_Batch VALUES(4,2,'1961-11-10',140,145000);
INSERT INTO Production_Batch VALUES(5,4,'1962-05-25',110,165000);
INSERT INTO Production_Batch VALUES(6,3,'1962-10-15',155,158000);
INSERT INTO Production_Batch VALUES(7,2,'1963-06-12',165,165000);
INSERT INTO Production_Batch VALUES(8,4,'1963-11-05',90,140000);
INSERT INTO Production_Batch VALUES(9,3,'1964-05-18',170,170000);
INSERT INTO Production_Batch VALUES(10,2,'1964-10-22',145,150000);
INSERT INTO Production_Batch VALUES(11,4,'1965-06-03',120,180000);
INSERT INTO Production_Batch VALUES(12,3,'1965-11-11',160,162000);
INSERT INTO Production_Batch VALUES(13,2,'1966-05-20',175,175000);
INSERT INTO Production_Batch VALUES(14,4,'1966-10-30',115,172500);
INSERT INTO Production_Batch VALUES(15,3,'1967-06-05',180,180000);
INSERT INTO Production_Batch VALUES(16,2,'1967-11-15',150,155000);
INSERT INTO Production_Batch VALUES(17,4,'1968-05-28',125,187500);
INSERT INTO Production_Batch VALUES(18,3,'1968-10-18',170,172000);
INSERT INTO Production_Batch VALUES(19,2,'1969-06-10',185,185000);
INSERT INTO Production_Batch VALUES(20,4,'1969-11-08',100,155000);
INSERT INTO Production_Batch VALUES(21,3,'1970-05-22',190,190000);
INSERT INTO Production_Batch VALUES(22,2,'1970-10-25',160,165000);

-- Reset serial sequences to match imported data
SELECT setval(pg_get_serial_sequence('Building', 'id'), 6);
SELECT setval(pg_get_serial_sequence('Citizen', 'id'), 94);
SELECT setval(pg_get_serial_sequence('Production_Batch', 'id'), 22);

COMMIT;