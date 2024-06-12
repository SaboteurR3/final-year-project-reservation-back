package ge.project.common.recommend;

import java.util.*;

public class ClimateMap {
    private Map<String, ClimateData> climateMap;

    public ClimateMap() {
        climateMap = new HashMap<>();
    }

    public void addCountry(String country, double avgTemperature, double avgRainfall, double avgHumidity) {
        climateMap.put(country, new ClimateData(avgTemperature, avgRainfall, avgHumidity));
    }

    public ClimateData getClimateData(String country) {
        return climateMap.get(country);
    }

    public double calculateEuclideanDistance(String country1, String country2) {
        ClimateData data1 = getClimateData(country1);
        ClimateData data2 = getClimateData(country2);

        if (data1 == null || data2 == null) {
            throw new IllegalArgumentException("One or both countries not found in the map.");
        }

        return data1.euclidianDifference(data2);
    }

    //Finds a country with similar or opposite climate
    public List<String> findComparableCountry(String country, ComparisonType type) {
        String resultCountry = null;
        PriorityQueue<Map.Entry<String, Double>> priorityQueue;

        if (type == ComparisonType.SIMILAR) {
            priorityQueue = new PriorityQueue<>(Map.Entry.comparingByValue());
        } else { // OPPOSITE
            priorityQueue = new PriorityQueue<>(Map.Entry.<String, Double>comparingByValue().reversed());
        }

        for (Map.Entry<String, ClimateData> entry : climateMap.entrySet()) {
            String currentCountry = entry.getKey();
            if (!currentCountry.equals(country)) {
                double distance = calculateEuclideanDistance(country, currentCountry);
                priorityQueue.offer(new AbstractMap.SimpleEntry<>(currentCountry, distance));
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
