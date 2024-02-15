package edu.brown.cs.student.main.DataModels;

import java.util.Map;

public class ACSQueryKey {
  private String name;
  private String broadbandAccessPercentage;
  private String stateCode;
  private String countyCode;

  public ACSQueryKey(String name, String broadbandAccessPercentage, String stateCode,
      String countyCode) {
    this.name = name;
    this.broadbandAccessPercentage = broadbandAccessPercentage;
    this.stateCode = stateCode;
    this.countyCode = countyCode;
  }

  public String getName() {
    return this.name;
  }

  public String getBroadbandAccessPercentage() {
    return this.broadbandAccessPercentage;
  }

  public String getStateCode() {
    return this.stateCode;
  }

  public String getCountyCode() {
    return this.countyCode;
  }

}
