package com.plugin.decompiletool.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.plugin.decompiletool.ui.SettingsDialog;
import org.jetbrains.annotations.NotNull;

public class ProcessApkAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    SettingsDialog settingsDialog = new SettingsDialog();
    settingsDialog.setVisible(true);
  }
}
