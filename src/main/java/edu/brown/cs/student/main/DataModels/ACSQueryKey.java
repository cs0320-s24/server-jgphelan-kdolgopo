package edu.brown.cs.student.main.DataModels;

import java.util.Map;

public class ACSQueryKey {
  private String year;
  private String dataset;
  private String[] variables;
  private Map<String, String> geoFilters;
  private String apiKey;

  public ACSQueryKey(String year, String dataset, String[] variables, Map<String, String> geoFilters, String apiKey) {
    this.year = year;
    this.dataset = dataset;
    this.variables = variables;
    this.geoFilters = geoFilters;
    this.apiKey = apiKey;
  }

  // Getters for all fields
  public String getYear() {
    return year;
  }

  public String getDataset() {
    return dataset;
  }

  public String[] getVariables() {
    return variables;
  }

  public Map<String, String> getGeoFilters() {
    return geoFilters;
  }

  public String getApiKey() {
    return apiKey;
  }

  // Setters for all fields (optional, if you want to allow modification after creation)
  // ...
}
