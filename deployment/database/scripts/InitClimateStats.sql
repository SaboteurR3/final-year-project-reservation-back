
DROP TABLE IF EXISTS climate_data;
DROP TABLE IF EXISTS climate_data_raw;

CREATE SEQUENCE climate_data_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE climate_data (
  Id INT PRIMARY KEY DEFAULT nextval('climate_data_id_seq'),
  country VARCHAR(255),
  avg_temperature DOUBLE PRECISION,
  avg_rainfall DOUBLE PRECISION,
  avg_humidity DOUBLE PRECISION
);


CREATE TABLE climate_data_raw (
                 data JSONB
);

\COPY climate_data_raw(JSONB) FROM 'ClimateStats.json';

INSERT INTO climate_data (country, avg_temperature, avg_rainfall, avg_humidity)
SELECT
    nextval('climate_data_id_seq') AS Id,
    key AS country,
    (value->>'avgTemperature')::DOUBLE PRECISION AS avg_temperature,
    (value->>'avgRainfall')::DOUBLE PRECISION AS avg_rainfall,
    (value->>'avgHumidity')::DOUBLE PRECISION AS avg_humidity
FROM climate_data_raw,
    jsonb_each(data) AS each(key, value);

DROP TABLE climate_data_raw;

CREATE TABLE ratings (
     countryId INT REFERENCES climate_data(country),
     preferredTemperature FLOAT,
     preferredHumidity FLOAT,
     preferredRain FLOAT,
     userId INT REFERENCES sec_user(id),
     rating INT CHECK (rating BETWEEN 0 AND 5),
     PRIMARY KEY (userId, countryId)
);