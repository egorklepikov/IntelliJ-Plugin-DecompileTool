package com.plugin.decompiletool.ui;

import decompiletool.DecompileTool;
import decompiletool.network.AppInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
  private JComboBox selectAppComboBox;
  private JLabel bundleIDLabel;
  private JTextArea textArea1;
  private JComboBox selectVersionComboBox;
  private JLabel chooseAppLabel;
  private JLabel chooseVersionLabel;

  public SettingsDialog() {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(processButton);
    setSize(800, 700);
    setDialogLocation();

    loadDataButton.addActionListener(e -> onSearch());
    processButton.addActionListener(e -> onOK());
    buttonCancel.addActionListener(e -> onCancel());
    chooseApkButton.addActionListener(e -> onApkLoad());
    localLoadType.addActionListener(e -> changeUI(1));
    remoteLoadType.addActionListener(e -> changeUI(2));
    bundleIDField.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if(bundleIDField.getText().length() == 0)
          loadDataButton.setEnabled(false);
        else {
          loadDataButton.setEnabled(true);
        }
      }
    });

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
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

  private void changeUI(int uiType) {
    if (uiType == 1) {
      bundleIDLabel.setEnabled(false);
      bundleIDField.setText("");
      bundleIDField.setEnabled(false);
      chooseAppLabel.setEnabled(false);
      chooseVersionLabel.setEnabled(false);
      selectAppComboBox.setEnabled(false);
      selectVersionComboBox.setEnabled(false);
      loadDataButton.setEnabled(false);
      selectAppComboBox.removeAllItems();
      selectAppComboBox.setEnabled(false);
      chooseApkButton.setEnabled(true);
    } else if (uiType == 2) {
      apkPathField.setText("");
      apkPathField.setEnabled(false);
      chooseApkButton.setEnabled(false);
      bundleIDLabel.setEnabled(true);
      bundleIDField.setEnabled(true);
      chooseAppLabel.setEnabled(true);
      chooseVersionLabel.setEnabled(true);
      selectAppComboBox.removeAllItems();
      selectAppComboBox.setEnabled(false);
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
    dispose();
  }

  private void onCancel() {
    dispose();
  }

  private void onSearch() {
    ArrayList<AppInformation> applications = DecompileTool.getInstance().getAppsList(bundleIDField.getText());
  }

  private void onBundleFieldChanged() {
    if (bundleIDField.getText().isEmpty()) {
      loadDataButton.setEnabled(false);
    } else {
      loadDataButton.setEnabled(true);
    }
  }
}
