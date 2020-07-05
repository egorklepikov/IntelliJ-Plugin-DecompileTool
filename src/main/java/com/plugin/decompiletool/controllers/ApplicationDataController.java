package com.plugin.decompiletool.controllers;

import decompiletool.DecompileTool;
import decompiletool.network.AppInformation;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationDataController {
  private static ApplicationDataController instance;
  private ArrayList<AppInformation> applications = new ArrayList<>();

  private ApplicationDataController() {
  }

  public static ApplicationDataController getInstance() {
    if (instance == null) {
      instance = new ApplicationDataController();
    }
    return instance;
  }

  public int loadData(String searchQuery) {
    applications = DecompileTool.getInstance().getAppsList(searchQuery);
    if (applications == null) {
      return 2;
    } else {
      return 3;
    }
  }

  public HashMap<String, AppInformation> getAppsList() {
    if (applications == null) {
      throw new RuntimeException("Applications aren't loaded or null.");
    }
    HashMap<String, AppInformation> appsMap = new HashMap<>();
    for (AppInformation application : applications) {
      String comboboxPresentation = application.getAppName() + "\t" + "[" + application.getVendor() + "]";
      appsMap.put(comboboxPresentation, application);
    }
    return appsMap;
  }

  public HashMap<String, AppInformation.AppRelease> getAppReleaseInfo(AppInformation appInformation) {
    ArrayList<AppInformation.AppRelease> versions = appInformation.getAppVersions();
    if (versions != null) {
      HashMap<String, AppInformation.AppRelease> appReleases = new HashMap<>();
      for (AppInformation.AppRelease version : versions) {
        String releasePresentation = "Version: " + version.getReleaseVersion() + "\t [" + version.getReleaseDate() + ", " + version.getReleaseSize() + "]";
        appReleases.put(releasePresentation, version);
      }
      return appReleases;
    } else {
      return null;
    }
  }
}
