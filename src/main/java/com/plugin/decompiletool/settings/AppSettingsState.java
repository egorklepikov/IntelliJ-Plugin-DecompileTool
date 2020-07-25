package com.plugin.decompiletool.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "com.plugin.decompiletool.settings.AppSettingsState", storages = {@Storage("decompiletool.settings.xml")})
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

  public String apkStoragePath = System.getProperty("user.home") + "/Desktop/decompile_tool_apk_files";

  public static AppSettingsState getInstance() {
    return ServiceManager.getService(AppSettingsState.class);
  }

  @Nullable
  @Override
  public AppSettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull final AppSettingsState state) {
      XmlSerializerUtil.copyBean(state, this);
  }
}
