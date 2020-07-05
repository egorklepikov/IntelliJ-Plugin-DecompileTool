package com.plugin.decompiletool.ui;

import com.plugin.decompiletool.controllers.ApplicationDataController;
import decompiletool.DecompileTool;
import decompiletool.network.AppInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsDialog extends JDialog {
  private JPanel contentPane;
  private JButton processButton;
  private JButton buttonCancel;
  private JRadioButton localLoadType;
  private JTextField apkPathField;
  private JButton chooseApkButton;
  private JRadioButton remoteLoadType;
  private JTextField bundleIDField;
  private JButton loadDataButton;
  private JComboBox<String> selectAppComboBox;
  private JLabel bundleIDLabel;
  private JTextArea textArea1;
  private JComboBox<String> selectVersionComboBox;
  private JLabel chooseAppLabel;
  private JLabel chooseVersionLabel;
  private JLabel searchProgress;
  private JPanel settingsPanel;

  private HashMap<String, AppInformation> applications;
  private HashMap<String, AppInformation.AppRelease> releases;

  public SettingsDialog() {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(processButton);
    setSize(900, 700);
    setDialogLocation();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    searchProgress.setVisible(false);

    loadDataButton.addActionListener(e -> onSearch());
    processButton.addActionListener(e -> onOK());
    buttonCancel.addActionListener(e -> onCancel());
    chooseApkButton.addActionListener(e -> onApkLoad());
    localLoadType.addActionListener(e -> changeLoadingMethodUI(1));
    remoteLoadType.addActionListener(e -> changeLoadingMethodUI(2));
    bundleIDField.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (bundleIDField.getText().length() == 0)
          loadDataButton.setEnabled(false);
        else {
          loadDataButton.setEnabled(true);
        }
      }
    });

    selectAppComboBox.addActionListener(e -> {
      if (applications != null) {
        selectVersionComboBox.removeAllItems();
        AppInformation application = applications.get(selectAppComboBox.getSelectedItem());
        releases = ApplicationDataController.getInstance().getAppReleaseInfo(application);
        if (releases != null) {
          for (Map.Entry<String, AppInformation.AppRelease> release : releases.entrySet()) {
            selectVersionComboBox.addItem(release.getKey());
          }
        }
      }
    });

    contentPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    ButtonGroup radioButtonGroup = new ButtonGroup();
    radioButtonGroup.add(localLoadType);
    radioButtonGroup.add(remoteLoadType);
  }

  private void changeLoadingMethodUI(int uiType) {
    if (uiType == 1) {
      bundleIDLabel.setEnabled(false);
      bundleIDField.setText("");
      bundleIDField.setEnabled(false);
      chooseAppLabel.setEnabled(false);
      chooseVersionLabel.setEnabled(false);
      selectAppComboBox.setEnabled(false);
      selectVersionComboBox.setEnabled(false);
      loadDataButton.setEnabled(false);
      chooseApkButton.setEnabled(true);
      processButton.setEnabled(false);
      selectAppComboBox.removeAllItems();
      selectAppComboBox.setEnabled(false);
    } else if (uiType == 2) {
      apkPathField.setText("");
      apkPathField.setEnabled(false);
      chooseApkButton.setEnabled(false);
      bundleIDLabel.setEnabled(true);
      bundleIDField.setEnabled(true);
      chooseAppLabel.setEnabled(true);
      chooseVersionLabel.setEnabled(true);
      processButton.setEnabled(false);
      selectAppComboBox.removeAllItems();
      selectAppComboBox.setEnabled(false);
    }
  }

  private void changeOnSearchUI(int uiType) {
    if (uiType == 1) {
      loadDataButton.setEnabled(false);
      bundleIDField.setEnabled(false);
      remoteLoadType.setEnabled(false);
      localLoadType.setEnabled(false);
      searchProgress.setVisible(true);
      selectAppComboBox.removeAllItems();
      selectAppComboBox.setEnabled(false);
      selectVersionComboBox.removeAllItems();
      selectVersionComboBox.setEnabled(false);
      processButton.setEnabled(false);
    } else if (uiType == 2) {
      loadDataButton.setEnabled(true);
      bundleIDField.setEnabled(true);
      bundleIDField.setText("");
      remoteLoadType.setEnabled(true);
      localLoadType.setEnabled(true);
      searchProgress.setVisible(false);
      processButton.setEnabled(false);
    } else if (uiType == 3) {
      loadDataButton.setEnabled(true);
      bundleIDField.setEnabled(true);
      remoteLoadType.setEnabled(true);
      localLoadType.setEnabled(true);
      searchProgress.setVisible(false);
      chooseAppLabel.setEnabled(true);
      selectAppComboBox.setEnabled(true);
      chooseVersionLabel.setEnabled(true);
      selectVersionComboBox.setEnabled(true);
      processButton.setEnabled(true);
    }
  }

  private void setDialogLocation() {
    final Toolkit toolkit = Toolkit.getDefaultToolkit();
    final Dimension screenSize = toolkit.getScreenSize();
    final int x = (screenSize.width - this.getWidth()) / 2;
    final int y = (screenSize.height - this.getHeight()) / 2;
    this.setLocation(x, y);
  }

  private void onApkLoad() {
    JFileChooser fileChooser = new JFileChooser();
    int dialogResult = fileChooser.showOpenDialog(contentPane);
    if (dialogResult == JFileChooser.APPROVE_OPTION) {
      String apkPath = fileChooser.getSelectedFile().getAbsolutePath();
      apkPathField.setText(apkPath);
    }
  }

  private void onOK() {
    processButton.setEnabled(false);
    if (localLoadType.isSelected()) {
      DecompileTool.getInstance().selectApp(apkPathField.getText());
    } else if (remoteLoadType.isSelected()) {
      DecompileTool.getInstance().selectApp(getAppInformation(), getAppReleaseInformation());
    }

    new Thread(() -> {
      DecompileTool.getInstance().startAppProcessing();
    }).start();
  }

  private AppInformation.AppRelease getAppReleaseInformation() {
    AppInformation.AppRelease appRelease = null;
    for (Map.Entry<String, AppInformation.AppRelease> release : releases.entrySet()) {
      String selectedItem = (String) selectVersionComboBox.getSelectedItem();
      if (selectedItem != null) {
        String releaseVersion = selectedItem.split("\\[")[0];
        String releaseDate = selectedItem.split("\\[")[1];
        if (releaseVersion.contains(release.getValue().getReleaseVersion()) && releaseDate.contains(release.getValue().getReleaseDate())) {
          appRelease = release.getValue();
          break;
        }
      } else {
        throw new NullPointerException();
      }
    }
    return appRelease;
  }

  private AppInformation getAppInformation() {
    AppInformation appInformation = null;
    for (Map.Entry<String, AppInformation> application : applications.entrySet()) {
      String selectedItem = (String) selectAppComboBox.getSelectedItem();
      if (selectedItem != null) {
        String appName = selectedItem.split("\\[")[0];
        String appVendor = selectedItem.split(String.valueOf("\\["))[1];
        if (appName.contains(application.getValue().getAppName()) && appVendor.contains(application.getValue().getVendor())) {
          appInformation = application.getValue();
          break;
        }
      } else {
        throw new NullPointerException();
      }
    }
    return appInformation;
  }

  private void onCancel() {
    dispose();
  }

  private void onSearch() {
    changeOnSearchUI(1);
    new Thread(() -> {
      int searchResult = ApplicationDataController.getInstance().loadData(bundleIDField.getText());
      changeOnSearchUI(searchResult);
      if (searchResult == 3) {
        applications = ApplicationDataController.getInstance().getAppsList();
        for(Map.Entry<String, AppInformation> application : applications.entrySet()) {
          selectAppComboBox.addItem(application.getKey());
        }
      }
    }).start();
  }
}
