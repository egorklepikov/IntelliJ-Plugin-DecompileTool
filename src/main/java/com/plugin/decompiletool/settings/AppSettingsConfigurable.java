package com.plugin.decompiletool.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AppSettingsConfigurable implements Configurable {

  private AppSettingsComponent settingsComponent;

  @Override
  public String getDisplayName() {
    return "Decompile Tool";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return settingsComponent.getPreferredFocusedComponent();
  }

  @Override
  public @Nullable JComponent createComponent() {
    settingsComponent = new AppSettingsComponent();
    return settingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    AppSettingsState settings = AppSettingsState.getInstance();
    return !settingsComponent.getTargetPath().equals(settings.apkStoragePath);
  }

  @Override
  public void apply() {
    AppSettingsState settings = AppSettingsState.getInstance();
    settings.apkStoragePath = settingsComponent.getTargetPath();
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
    settingsComponent.setTargetPath(settings.apkStoragePath);
  }

  @Override
  public void disposeUIResources() {
    settingsComponent = null;
  }
}
