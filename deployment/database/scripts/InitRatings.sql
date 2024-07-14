CREATE TABLE public.ratings (
     countryId INT REFERENCES climate_data(Id),
     preferredTemperature FLOAT,
     preferredHumidity FLOAT,
     preferredRain FLOAT,
     userId INT REFERENCES bookings.public.sec_user(id),
     rating INT CHECK (rating BETWEEN 0 AND 10),
     PRIMARY KEY (userId, countryId)
);