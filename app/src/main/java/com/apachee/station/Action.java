package com.apachee.station;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Action {

    private JLabel xmlLabel, csvLabel;
    private JTextField xmlTextField, csvTextField;
    private JButton xmlButton, csvButton;
    private JButton xmlModel, csvModel;
    private JButton generateButton, resetButton;
    private JTextArea infoTextArea;

    private File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
    private String csv = "";
    private String xml = "";

    private SAXReader reader = new SAXReader();

    private JLabel paintJLabel(String txt, int x, int y, int width, int height) {
        JLabel label = new JLabel(txt);
        label.setFont(new Font("Serif", Font.BOLD, 40));
        label.setBounds(x, y, width, height);
        return label;
    }

    private JTextField paintJTextField(int x, int y) {
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

    private JButton paintJButton(String txt, int x, int y, String type) {
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
                generateFile();
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
    private JButton paintJButton(int y, String type) {
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

    private void placeComponents(JPanel panel) {
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

    private void generateFile() {
        if ("".equals(xml) || "".equals("csv")) {
            infoTextArea.append("请先指定CSV和XML文件！\n");
        } else {
            // 根据规则，转换xml
            try {
                Document inputXml = reader.read(new File(xml));
                Element rootElement = inputXml.getRootElement();

                // 1、创建document对象
                Document document = DocumentHelper.createDocument();
                // 2、创建根节点rss
                Element rss = document.addElement("raml");
                // 3、向rss节点添加version属性
                rss.addAttribute("version", "2.1");
                rss.addAttribute("xmlns", "raml21.xsd");
                // 4、生成子节点及子节点内容
                Element cmData = rss.addElement("cmData");
                cmData.addAttribute("xmlns", "");
                cmData.addAttribute("type", "plan");
                cmData.addAttribute("scope", "changes");
                cmData.addAttribute("name", "Default_plan");

                Element header = cmData.addElement("header");
                Element log = header.addElement("log");
                log.addAttribute("user", "yuhao.zhang@nokia-sbell-huanuo.com");
                log.addAttribute("action", "created");
                log.addAttribute("dateTime", new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()));
                log.addAttribute("appInfo", "Nokia NetAct Plan Editor");
                log.setText("No description");

                FileReader fr = new FileReader(new File(csv));
                BufferedReader br = new BufferedReader(fr);
                String line = "";
                String MRBTS_ID, Vlan_ID, IPv4, Gateway, MME;

                while ((line = br.readLine()) != null) {
                    String[] cols = line.split(",");
                    MRBTS_ID = cols[0];
                    Vlan_ID = cols[1];
                    IPv4 = cols[2];
                    Gateway = cols[3];
                    MME = cols[4] + cols[5] + cols[6] + cols[7] + cols[8];

                    // VLANIF
                    String VLANIF_distName = "PLMN-PLMN/MRBTS-" + MRBTS_ID + "/TNLSVC-1/TNL-1/ETHSVC-1/ETHIF-1/VLANIF-";
                    String VLANIF_ID = Vlan_ID;
                    ArrayList<String> VLANIF_distList = new ArrayList<>();       //所有的VLANIF_distName匹配的列表
                    String VLANIF_newdistName = ""; //需要添加的VLANIF_distName

                    // IPIF
                    String IPIF_distName = "PLMN-PLMN/MRBTS-" + MRBTS_ID + "/TNLSVC-1/TNL-1/IPNO-1/IPIF-";
                    String IPIF_newdistName = "";
                    String IPIF_ipMtu = "1500";
                    // IPADDRESSV4

                    String IPADDRESSV4_distName = "";
                    String IPADDRESSV4_localIpAddr = IPv4;

                    // IPRT
                    String IPRT_distName = "PLMN-PLMN/MRBTS-" + MRBTS_ID + "/TNLSVC-1/TNL-1/IPNO-1/IPRT-1";
                    String IPRT_gateWay = Gateway;

                    // LTAC
                    String LTAC_distName = "PLMN-PLMN/MRBTS-" + MRBTS_ID + "/MNL-1/MNLENT-1/TAC-1/LTAC-";
                    ArrayList<String> LTAC_distList = new ArrayList<>();  // 所有的VLANIF_distName匹配的列表
                    String LTAC_newdistName = "";  // 需要添加的LTAC_distName

                    // TRSNW
                    String TRSNW_distName = "PLMN-PLMN/MRBTS-" + MRBTS_ID + "/LNBTS-" + MRBTS_ID + "/TRSNW-1";
                    String TRSNW_version = "xL";  // 需要拆分IPADDRESSV4的版本号

                    // LNMME

                    String LNMME_distName = "PLMN-PLMN/MRBTS-" + MRBTS_ID + "/LNBTS-" + MRBTS_ID + "/LNMME-";

                    ArrayList<String> LNMME_distList = new ArrayList<>();  // 所有的LNMME_distName匹配的列表
                    String LNMME_newdistName = "";  // 需要添加的LNMME_distName
                    String[] LNMME_sourceList = MME.split(",");
                    ArrayList<HashMap<String, String>> LNMME_newdistList = new ArrayList<>();  // 将最新的名字存储在改列表

                    /**
                     * 根据规则，生成最新的distName
                     */
                    for (Object o1 : rootElement.elements()) {
                        Element level1 = (Element) o1;
                        for (Object o2 : level1.elements()) {
                            Element level2 = (Element) o2;
                            if (level2.attributeCount() > 0) {
                                // 提取所需的配置信息
                                /**
                                 * VLANIF
                                 */
                                if (VLANIF_distName.equals(level2.attributeValue("distName"))) {
                                    VLANIF_distList.add(level2.attributeValue("distName"));
                                }

                                /**
                                 * LTAC
                                 */
                                if (LTAC_distName.equals(level2.attributeValue("distName"))) {
                                    LTAC_distList.add(level2.attributeValue("distName"));
                                }

                                /**
                                 * LNMME
                                 */
                                if (LNMME_distName.equals(level2.attributeValue("distName"))) {
                                    LNMME_distList.add(level2.attributeValue("distName"));
                                }
                            }
                        }
                    }

                    // VLANIF_distList=['PLMN-PLMN/MRBTS-180440/TNLSVC-1/TNL-1/ETHSVC-1/ETHIF-1/VLANIF-1']
                    if (VLANIF_distList.size() > 0) {
                        Collections.sort(VLANIF_distList);
                        String[] VLANIF_distNamelist = VLANIF_distList.get(VLANIF_distList.size() - 1).split(VLANIF_distName);
                        // 生成最新的VLANIF_distName,原来的基础上+1
                        VLANIF_newdistName = VLANIF_distName + (Integer.parseInt(VLANIF_distNamelist[VLANIF_distNamelist.length - 1]) + 1);

                        // 生成最新的IPIF_distName,原来的基础上+1
                        IPIF_newdistName = IPIF_distName + (Integer.parseInt(VLANIF_distNamelist[VLANIF_distNamelist.length - 1]) + 1);
                    }

                    // LTAC_distList=['PLMN-PLMN/MRBTS-180440/MNL-1/MNLENT-1/TAC-1/LTAC-1']
                    if (LTAC_distList.size() > 0) {
                        Collections.sort(LTAC_distList);
                        String[] LTAC_distNameList = LTAC_distList.get(LTAC_distList.size() - 1).split(LTAC_distName);
                        LTAC_newdistName = LTAC_distName + (Integer.parseInt(LTAC_distNameList[LTAC_distNameList.length - 1]) + 1);
                    }

                    // LNMME_distList = ['PLMN-PLMN/MRBTS-180440/LNBTS-180440/LNMME-0']
                    if (LNMME_distList.size() > 0) {
                        Collections.sort(LNMME_distList);
                        String[] LNMME_distNameList = LNMME_distList.get(LNMME_distList.size() - 1).split(LNMME_distName);

                        for (int i = 0; i < LNMME_distNameList.length; i++) {
                            HashMap<String, String> LNMME_newdistDict = new HashMap<>();  // 将最新的名字存储在改列表
                            LNMME_newdistDict.put("LNMME_newdistName", LNMME_distName + (Integer.parseInt(LNMME_distNameList[LNMME_distNameList.length - 1]) + i + 1));
                            LNMME_newdistDict.put("ipAddrPrim", LNMME_distNameList[i]);
                            LNMME_newdistList.add(LNMME_newdistDict);
                        }
                    }

                    /**
                     * 开始筛选过滤属性
                     */
                    for (Object o1 : rootElement.elements()) {
                        Element level1 = (Element) o1;
                        for (Object o2 : level1.elements()) {
                            Element level2 = (Element) o2;

                            if (level2.attributeCount() > 0) {

                                // 提取所需的配置信息
                                /**
                                 * VLANIF
                                 */
                                if (VLANIF_distList.get(VLANIF_distList.size() - 1).equals(level2.attributeValue("distName"))) {
                                    Element managedObject = cmData.addElement("managedObject");
                                    managedObject.addAttribute("class", "com.nokia.srbts.tnl:VLANIF");
                                    managedObject.addAttribute("operation", "create");
                                    managedObject.addAttribute("version", level2.attributeValue("version"));
                                    managedObject.addAttribute("distName", VLANIF_newdistName);

                                    Element p = managedObject.addElement("p");
                                    p.setText(Vlan_ID);


                                    /**
                                     * IPIF
                                     */
                                    managedObject = cmData.addElement("managedObject");
                                    managedObject.addAttribute("class", "com.nokia.srbts.tnl:IPIF");
                                    managedObject.addAttribute("operation", "create");
                                    managedObject.addAttribute("version", level2.attributeValue("version"));
                                    managedObject.addAttribute("distName", IPIF_newdistName);
                                    Map<String, String> p_dict = new HashMap<>();
                                    String[] splits = VLANIF_newdistName.split("PLMN-PLMN/");
                                    p_dict.put("interfaceDN", splits[splits.length - 1]);
                                    p_dict.put("ipMtu", IPIF_ipMtu);
                                    for (String key : p_dict.keySet()) {
                                        p = managedObject.addElement("p");
                                        p.addAttribute("name", key);
                                        p.setText(p_dict.get(key));
                                    }

                                    /**
                                     * IPADDRESSV4
                                     */
                                    IPADDRESSV4_distName = IPIF_newdistName + "/IPADDRESSV4-1";
                                    managedObject = cmData.addElement("managedObject");
                                    managedObject.addAttribute("class", "com.nokia.srbts.tnl:IPADDRESSV4");
                                    managedObject.addAttribute("operation", "create");
                                    managedObject.addAttribute("version", level2.attributeValue("version"));
                                    managedObject.addAttribute("distName", IPADDRESSV4_distName);
                                    p_dict.clear();
                                    p_dict.put("ipAddressAllocationMethod", "0");
                                    p_dict.put("localIpAddr", IPADDRESSV4_localIpAddr);
                                    p_dict.put("localIpPrefixLength", "30");
                                    for (String key : p_dict.keySet()) {
                                        p = managedObject.addElement("p");
                                        p.addAttribute("name", key);
                                        p.setText(p_dict.get(key));
                                    }

                                    String[] split = level2.attributeValue("version").split("TNL");
                                    TRSNW_version = TRSNW_version + split[split.length - 1];

                                    /**
                                     * TRSNW
                                     */
                                    managedObject = cmData.addElement("managedObject");
                                    managedObject.attributeValue("class", "NOKLTE:TRSNW");
                                    managedObject.attributeValue("operation", "create");
                                    managedObject.attributeValue("version", TRSNW_version);
                                    managedObject.attributeValue("distName", TRSNW_distName);

                                    p_dict.clear();
                                    String[] split1 = IPADDRESSV4_distName.split("PLMN-PLMN/");
                                    p_dict.put("cPlaneipV4AddressDN1", split1[split1.length - 1]);
                                    p_dict.put("transportNwId", "1");
                                    p_dict.put("transportNwInUse", "1");
                                    String[] split2 = IPADDRESSV4_distName.split("PLMN-PLMN/");
                                    p_dict.put("ipV4AddressDN1", split2[split2.length - 1]);
                                    for (String key : p_dict.keySet()) {
                                        p = managedObject.addElement("p");
                                        p.addAttribute("name", key);
                                        p.setText(p_dict.get(key));
                                    }
                                }

                                /**
                                 * IPRT
                                 */
                                if (IPRT_distName.equals(level2.attributeValue("distName"))) {
                                    Element managedObject = cmData.addElement("managedObject");
                                    managedObject.addAttribute("class", "com.nokia.srbts.tnl:IPRT");
                                    managedObject.addAttribute("operation", "update");
                                    managedObject.addAttribute("version", level2.attributeValue("version"));
                                    managedObject.addAttribute("distName", IPRT_distName);

                                    Element list = managedObject.addElement("list");
                                    Element item = list.addElement("item");
                                    Map<String, String> item_dict = new HashMap<>();
                                    item_dict.put("destinationIpPrefixLength", "16");
                                    item_dict.put("destIpAddr", "6.55.0.0");
                                    item_dict.put("gateway", IPRT_gateWay);
                                    item_dict.put("preference", "1");
                                    item_dict.put("preSrcIpv4Addr", "0.0.0.0");
                                    for (String key : item_dict.keySet()) {
                                        Element p = item.addElement("p");
                                        p.addAttribute("name", key);
                                        p.setText(item_dict.get(key));
                                    }
                                    for (Object o3 : level2.elements()) {
                                        Element level3 = (Element) o3;
                                        for (Object o4 : level3.elements()) {
                                            Element level4 = (Element) o4;
                                            Element element = list.addElement("item");
                                            for (Object o5 : level4.elements()) {
                                                Element level5 = (Element) o5;
                                                Element p = item.addElement("p");
                                                p.addAttribute("name", level5.attributeValue("name"));
                                                p.setText(level5.getText());
                                            }
                                        }
                                    }
                                }

                                /**
                                 * LTAC
                                 */
                                if (LTAC_distList.get(LTAC_distList.size() - 1).equals(level2.attributeValue("distName"))) {
                                    Element managedObject = cmData.addElement("managedObject");
                                    managedObject.addAttribute("class", "com.nokia.srbts.mnl:LTAC");
                                    managedObject.addAttribute("operation", "create");
                                    managedObject.addAttribute("version", level2.attributeValue("version"));
                                    managedObject.addAttribute("distName", LTAC_newdistName);

                                    Map<String, String> p_dict = new HashMap<>();
                                    p_dict.put("tacLimitGbrEmergency", "1000000");
                                    p_dict.put("tacLimitGbrHandover", "1000000");
                                    p_dict.put("tacLimitGbrNormal", "1000000");
                                    p_dict.put("tacOverbookingLimit", "0");
                                    p_dict.put("transportNwId", "1");
                                    for (String key : p_dict.keySet()) {
                                        Element p = managedObject.addElement("p");
                                        p.addAttribute("name", key);
                                        p.setText(p_dict.get(key));
                                    }
                                }

                                /**
                                 * LNMME
                                 */
                                if (LNMME_distList.get(LNMME_distList.size() - 1).equals(level2.attributeValue("distName"))) {
                                    Map<String, String> p_dict = new HashMap<>();
                                    p_dict.put("administrativeState", "1");
                                    p_dict.put("ipAddrPrim", "1");
                                    p_dict.put("mmeRatSupport", "1");
                                    p_dict.put("relMmeCap", "255");
                                    p_dict.put("s1LinkStatus", "1");
                                    p_dict.put("transportNwId", "1");
                                    LNMME_newdistName = "";
                                    for (HashMap<String, String> LNMME_dict : LNMME_newdistList) {
                                        for (String key : LNMME_dict.keySet()) {
                                            p_dict.put("ipAddrPrim", LNMME_dict.get("ipAddrPrim"));
                                            LNMME_newdistName = LNMME_dict.get("LNMME_newdistName");
                                        }
                                        Element managedObject = cmData.addElement("managedObject");
                                        managedObject.addAttribute("class", "NOKLTE:LNMME");
                                        managedObject.addAttribute("operation", "create");
                                        managedObject.addAttribute("version", level2.attributeValue("version"));
                                        managedObject.addAttribute("distName", LNMME_newdistName);

                                        for (String key : p_dict.keySet()) {
                                            Element p = managedObject.addElement("p");
                                            p.addAttribute("name", key);
                                            p.setText(p_dict.get(key));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                // 5、设置生成xml的格式
                OutputFormat format = OutputFormat.createPrettyPrint();
                // 设置编码格式
                format.setEncoding("UTF-8");
                // 6、生成xml文件
                File file = new File("rss.xml");
                XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
                // 设置是否转义，默认使用转义字符
                writer.setEscapeText(false);
                writer.write(document);
                writer.close();
                infoTextArea.append("生成rss.xml成功" + "\n");
            } catch (Exception e) {
                infoTextArea.append(e.toString() + "\n");
            }
        }
    }

    @Test
    public void a() {

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
