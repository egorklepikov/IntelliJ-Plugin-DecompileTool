package com.plugin.decompiletool.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {
  private final JPanel mainPanel;
  private final TextFieldWithBrowseButton targetPathTextField;

  public AppSettingsComponent() {
    targetPathTextField = new TextFieldWithBrowseButton(new JTextField(AppSettingsState.getInstance().apkStoragePath));
    targetPathTextField.addBrowseFolderListener(
        "APK Downloading Location",
        null,
        null,
        new FileChooserDescriptor(
            false,
            true,
            false,
            false,
            false,
            false
        )
    );

    mainPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JLabel("APK downloading location"), targetPathTextField, 1, false)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return targetPathTextField;
  }

  @NotNull
  public String getTargetPath() {
    return targetPathTextField.getText();
  }

  public void setTargetPath(@NotNull String path) {
    targetPathTextField.setText(path);
  }
}
