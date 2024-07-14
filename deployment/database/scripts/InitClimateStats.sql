
DROP TABLE IF EXISTS public.climate_data;
DROP TABLE IF EXISTS public.climate_data_raw;
DROP SEQUENCE IF EXISTS climate_data_id_seq;

CREATE SEQUENCE climate_data_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE climate_data (
  id SERIAL PRIMARY KEY,
  country VARCHAR(255) UNIQUE,
  avg_temperature DOUBLE PRECISION,
  avg_rainfall DOUBLE PRECISION,
  avg_humidity DOUBLE PRECISION
);


CREATE TABLE climate_data_raw (
                 data JSONB
);

\COPY climate_data_raw(JSONB) FROM 'ClimateStats.json';

INSERT INTO climate_data (id, country, avg_temperature, avg_rainfall, avg_humidity)
SELECT
    nextval('climate_data_id_seq') AS Id,
    key AS country,
    (value->>'avgTemperature')::DOUBLE PRECISION AS avg_temperature,
    (value->>'avgRainfall')::DOUBLE PRECISION AS avg_rainfall,
    (value->>'avgHumidity')::DOUBLE PRECISION AS avg_humidity
FROM climate_data_raw,
    jsonb_each(data) AS each(key, value);

DROP TABLE climate_data_raw;