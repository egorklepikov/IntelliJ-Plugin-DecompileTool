package com.plugin.decompiletool.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.JBColor;
import com.plugin.decompiletool.settings.AppSettingsState;
import decompiletool.CmdProcessor;
import decompiletool.DecompileTool;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SettingsDialog extends JDialog {
  private JPanel contentPane;
  private JButton processButton;
  private JButton cancelButton;
  private JPanel chooseApkPanel;
  private TextFieldWithBrowseButton apkChooser;
  private JLabel selectApkLabel;
  private JTextArea outputArea;

  private final HashMap<String, Thread> decompileToolThreads = new HashMap<>();

  public SettingsDialog() {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(processButton);
    setSize(1024, 768);
    setLocationRelativeTo(null);
    setTitle("Decompile Tool");

    apkChooser.addActionListener(e -> onAPKSelect());
    apkChooser.addBrowseFolderListener(
        "APK Location",
        null,
        null,
        new FileChooserDescriptor(
            true,
            false,
            false,
            false,
            false,
            false
        )
    );

    MessageConsole messageConsole = new MessageConsole(outputArea);
    messageConsole.redirectOut();
    messageConsole.redirectErr(JBColor.RED, null);
    messageConsole.setMessageLines(2000);

    processButton.addActionListener(e -> onOK());
    cancelButton.addActionListener(e -> onCancel());

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(
        e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
    );
  }

  private void onAPKSelect() {
    processButton.setEnabled(!apkChooser.getText().isEmpty());
  }

  private void onOK() {
    processButton.setEnabled(false);
    apkChooser.setEnabled(false);
    outputArea.setText("");

    DecompileTool.getInstance().selectApp(apkChooser.getText());
    DecompileTool.getInstance().setTargetApkPath(AppSettingsState.getInstance().apkStoragePath);

    Thread processThread = new Thread(() -> {
      DecompileTool.getInstance().startAppProcessing();
      processButton.setEnabled(false);
      apkChooser.setEnabled(true);
      apkChooser.setText("");
    });
    processThread.start();
    decompileToolThreads.put("DecompileToolProcessThread", processThread);
  }

  private void onCancel() {
    for (Iterator<Map.Entry<String, Thread>> iterator = decompileToolThreads.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<String, Thread> thread = iterator.next();
      if (!thread.getValue().isInterrupted()) {
        thread.getValue().interrupt();
        System.out.println(thread.getKey() + " was successfully interrupted");
      } else {
        System.out.println(thread.getKey() + " already interrupted");
      }
      iterator.remove();
      System.out.println(thread.getKey() + " was removed from collection");
    }
    CmdProcessor.forceProcessInterrupt = true;
    dispose();
  }
}
