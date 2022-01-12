package com.apachee.station;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Action {

    private JLabel xmlLabel, csvLabel;
    private JTextField xmlTextField, csvTextField;
    private JButton xmlButton, csvButton;
    private JButton xmlModel, csvModel;
    private JButton generateButton, resetButton;
    private JTextArea infoTextArea;

    private File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
    private String csv, xml;

    JLabel paintJLabel(String txt, int x, int y, int width, int height) {
        JLabel label = new JLabel(txt);
        label.setFont(new Font("Serif", Font.BOLD, 40));
        label.setBounds(x, y, width, height);
        return label;
    }

    JTextField paintJTextField(int x, int y) {
        JTextField text = new JTextField(200);
        text.setFont(new Font("Serif", Font.PLAIN, 20));
        text.setBounds(x, y, 250, 50);

        text.addActionListener(e -> {
            String actionCommand = e.getActionCommand();
            System.out.println(actionCommand);
            e.getSource();
        });
        return text;
    }

    JButton paintJButton(String txt, int x, int y, String type) {
        JButton button = new JButton(txt);
        button.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 35));
        button.setBounds(x, y, 260, 50);

        // 添加功能
        button.addActionListener(e -> {
            if (type.equals("xml") || type.equals("csv")) {
                JFileChooser chooser = new JFileChooser(desktopDir);
                chooser.setSelectedFile(new File(this.getClass().getResource("/station/demo." + type).getPath()));
                int returnVal = chooser.showSaveDialog(button);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = new File(chooser.getSelectedFile().getAbsolutePath());
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        InputStream inputStream = this.getClass().getResourceAsStream("/station/demo." + type);
                        byte[] buff = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buff)) != -1) {
                            fos.write(buff, 0, len);
                        }
                        infoTextArea.append(type.toUpperCase() + "模板下载成功，位置：" + file.getAbsolutePath() + "\n");
                    } catch (Exception ex) {
                        infoTextArea.append(ex.getMessage() + "\n");
                    }
                }
            } else if (type.equals("generate")) {

            } else if (type.equals("reset")) {
                xml = "";
                csv = "";
                csvTextField.setText("");
                xmlTextField.setText("");
            }
        });
        return button;
}

    // 文件选择按钮
    JButton paintJButton(int y, String type) {
        JButton button = new JButton("...");
        button.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 25));
        button.setBounds(410, y, 50, 50);
        button.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(desktopDir);
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith("." + type);
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            int returnVal = chooser.showOpenDialog(button);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filepath = chooser.getSelectedFile().getPath();
                if (type.equals("csv")) {
                    csvTextField.setText(filepath);
                    csv = filepath;
                } else {
                    xmlTextField.setText(filepath);
                    xml = filepath;
                }
            }
        });
        return button;
    }

    void placeComponents(JPanel panel) {
        panel.setLayout(null);

        csvLabel = paintJLabel("CSV", 40, 20, 110, 40);
        csvTextField = paintJTextField(160, 20);
        csvButton = paintJButton(20, "csv");
        csvModel = paintJButton("CSV模板下载", 480, 20, "csv");
        panel.add(csvLabel);
        panel.add(csvTextField);
        panel.add(csvButton);
        panel.add(csvModel);

        xmlLabel = paintJLabel("XML", 40, 90, 110, 40);
        xmlTextField = paintJTextField(160, 90);
        xmlButton = paintJButton(90, "xml");
        xmlModel = paintJButton("XML模板下载", 480, 90, "xml");
        panel.add(xmlLabel);
        panel.add(xmlTextField);
        panel.add(xmlButton);
        panel.add(xmlModel);

        generateButton = paintJButton("生成", 40, 170, "generate");
        resetButton = paintJButton("重置", 350, 170, "reset");
        panel.add(generateButton);
        panel.add(resetButton);

        infoTextArea = new JTextArea("信息：\n");
        infoTextArea.setLineWrap(true);
        infoTextArea.setBounds(40, 250, 700, 290);
        panel.add(infoTextArea);
    }

    public static void main(String[] args) {
        Action action = new Action();
        JFrame frame = new JFrame("基站转换");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JPanel panelOperation = new JPanel();
        panelOperation.setBounds(0, 0, 800, 600);
        panelOperation.setBackground(Color.LIGHT_GRAY);
        action.placeComponents(panelOperation);
        frame.add(panelOperation);

        // 设置界面可见
        frame.setVisible(true);
    }


}
