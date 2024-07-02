
DROP TABLE IF EXISTS climate_data;
DROP TABLE IF EXISTS climate_data_raw;


CREATE TABLE climate_data (
  country VARCHAR(255) PRIMARY KEY,
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
    key AS country,
    (value->>'avgTemperature')::DOUBLE PRECISION AS avg_temperature,
    (value->>'avgRainfall')::DOUBLE PRECISION AS avg_rainfall,
    (value->>'avgHumidity')::DOUBLE PRECISION AS avg_humidity
FROM climate_data_raw,
    jsonb_each(data) AS each(key, value);

DROP TABLE climate_data_raw;