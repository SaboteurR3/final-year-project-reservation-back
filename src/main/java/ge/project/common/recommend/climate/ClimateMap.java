package ge.project.common.recommend.climate;

import ge.project.common.recommend.ComparisonType;
import ge.project.common.repository.Country;
import lombok.Getter;

import java.util.*;

@Getter
public class ClimateMap {
    private Map<Country, ClimateData> climateMap;

    public ClimateMap() {
        climateMap = new HashMap<>();
    }

    public void addCountry(Country country, double avgTemperature, double avgRainfall, double avgHumidity) {
        climateMap.put(country, new ClimateData(avgTemperature, avgRainfall, avgHumidity));
    }

    public void addCountry(Country country, ClimateData data) {
        climateMap.put(country, data);
    }

    public ClimateData getClimateData(Country country) {
        return climateMap.get(country);
    }

    public double calculateEuclideanDistance(Country country1, Country country2) {
        ClimateData data1 = getClimateData(country1);
        ClimateData data2 = getClimateData(country2);

        if (data1 == null || data2 == null) {
            throw new IllegalArgumentException("One or both countries not found in the map.");
        }

        return data1.euclidianDifference(data2);
    }

    //Finds a country with similar or opposite climate
    public List<String> findComparableCountry(Country country, ComparisonType type) {
        String resultCountry = null;
        PriorityQueue<Map.Entry<String, Double>> priorityQueue;

        if (type == ComparisonType.SIMILAR) {
            priorityQueue = new PriorityQueue<>(Map.Entry.comparingByValue());
        } else { // OPPOSITE
            priorityQueue = new PriorityQueue<>(Map.Entry.<String, Double>comparingByValue().reversed());
        }

        for (Map.Entry<Country, ClimateData> entry : climateMap.entrySet()) {
            Country currentCountry = entry.getKey();
            if (!currentCountry.equals(country)) {
                double distance = calculateEuclideanDistance(country, currentCountry);
                priorityQueue.offer(new AbstractMap.SimpleEntry<>(currentCountry.toString(), distance));
                if (priorityQueue.size() > 5) {
                    priorityQueue.poll();
                }
            }
        }

        List<String> resultCountries = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            resultCountries.add(priorityQueue.poll().getKey());
        }

        Collections.reverse(resultCountries);
        return resultCountries;
    }
}
