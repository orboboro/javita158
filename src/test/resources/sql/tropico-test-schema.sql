-- Solo DDL da src/main/resources/sql/tropicoDDLpostgresql.sql.
-- Mantenere allineati i vincoli quando cambia lo schema della demo.
-- Nessun INSERT, BEGIN, COMMIT o setval: ogni test crea le proprie fixture.

CREATE TABLE IF NOT EXISTS Resource (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    base_market_price DECIMAL(10, 2) NOT NULL,
    is_processed BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Faction (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS BuildingType (
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

CREATE TABLE IF NOT EXISTS Building (
    id SERIAL PRIMARY KEY,
    building_type_id INTEGER NOT NULL REFERENCES BuildingType(id),
    budget_level INTEGER DEFAULT 3 CHECK (budget_level BETWEEN 1 AND 5),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS Citizen (
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

CREATE TABLE IF NOT EXISTS Production_Batch (
    id SERIAL PRIMARY KEY,
    building_id INTEGER REFERENCES Building(id),
    production_date DATE,
    amount INTEGER,
    market_value NUMERIC(10,2)
);
