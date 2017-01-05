package getTicket;
//import com.sun.tools.javac.comp.Flow;
//import com.sun.tools.javac.comp.Check;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;

public class TicketMonitor extends  JFrame {
	
	RunTask runtask=null;
    JPanel SearchMode = new JPanel(),
            MonitorMode = new JPanel(),
            SearchUp = new JPanel(),
            SearchDown = new JPanel(new BorderLayout()),
            InputField = new JPanel(),
            CheckField = new JPanel(),
            CheckBoxes = new JPanel(),
            mPanel1 = new JPanel(),
            mPanel2 = new JPanel(),
            mPanel3 = new JPanel(),
            mPanel4 = new JPanel(),
            mPanel5 = new JPanel(),
            mPanel6 = new JPanel(),
            mPanel7 = new JPanel(),
            mPanel8 = new JPanel(),
            mPanelLeft = new JPanel(),
            mPanelRight = new JPanel(),
            selectShortCut = new JPanel(),
            sPanel = new JPanel();
    JMenuBar mb = new JMenuBar();
    JMenu mode = new JMenu("模式"),
            utils = new JMenu("功能");
    JMenuItem refresh = new JMenuItem("车站信息更新"),
            reset = new JMenuItem("重置文本域"),
            switchSearch = new JMenuItem("查询模式"),
            switchMonitor = new JMenuItem("监测模式"),
            help = new JMenuItem("操作手册"),
            quit = new JMenuItem("退出监测系统");
    JLabel tip = new JLabel("* 请确认信息无误");
    static JLabel mtip = new JLabel("");
    JTextField tf1 = new JTextField(9),
            tf2 = new JTextField(9),
            tf3 = new JTextField(9),
            mtf1 = new JTextField(9),
            mtf2 = new JTextField(9),
            mtf4 = new JTextField(9),
            mtf5 = new JTextField(9),
            mtf6 = new JTextField(9);
    JPasswordField mtf3 = new JPasswordField(9);
    static JTextArea TextField = new JTextArea("",5,2);
    JScrollPane sp = new JScrollPane(TextField);
    ButtonGroup rb = new ButtonGroup(),
                tb = new ButtonGroup();
    JRadioButton rb1 = new JRadioButton("成人"),
            rb2 = new JRadioButton("学生");
    JCheckBox []cb= {
            new JCheckBox("商务座"),
            new JCheckBox("特等座"),
            new JCheckBox("一等座"),
            new JCheckBox("二等座"),
            new JCheckBox("高级软卧"),
            new JCheckBox("软卧"),
            new JCheckBox("硬卧"),
            new JCheckBox("软座"),
            new JCheckBox("硬座"),
            new JCheckBox("无座")
    };
    JButton bt1 = new JButton("查询"),
            bt2 = new JButton("重置"),
            bt5 = new JButton("测试"),
            bt6 = new JButton("重置"),
            selectBt1 = new JButton("全选"),
            selectBt2 = new JButton("反选"),
            selectBt3 = new JButton("清空");
   static JToggleButton
            bt3 = new JToggleButton("开始监测"),
            bt4 = new JToggleButton("停止监测");
    com.toedter.calendar.JDateChooser dc = new JDateChooser();
    static String fromSta, toSta, date="", type, senderMail, pwd, smtp, receiverMail, interval, targetTrainNum,
            ticketType[] ={"swz_num","tz_num","zy_num","ze_num","gr_num","rw_num","yw_num","rz_num","yz_num","wz_num"};
    static boolean working,noneSelected,selectedType[]={false,false,false,false,false,false,false,false,false,false},checkSafe,testSafe,monitorSafe;

    public TicketMonitor() throws HeadlessException {
        super("12306火车票查询监测系统");
        init();
        searchInit();
        monitorInit();
        addWindowListener(new WindowAdapter(){
            public void  windowClosing (WindowEvent e) 		{
                dispose();
                System.exit(0);
            }
        });
    }
    void init() {
        setSize(525,400);
        setResizable(false);
        add(SearchMode,BorderLayout.CENTER);
        setJMenuBar(mb);

        mb.add(mode);
        mb.add(utils);
        mode.add(switchSearch);
        mode.add(switchMonitor);
        mode.addSeparator();
        mode.add(quit);
        utils.add(refresh);
        utils.addSeparator();
        utils.add(help);

        Toolkit tkt = Toolkit.getDefaultToolkit();
        Dimension dim = tkt.getScreenSize();
        int x = dim.width/2 - getSize().width/2;
        int y = dim.height/2 - getSize().height/2;
        setLocation(x, y);

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        switchSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckField.setLayout(new GridLayout(1,6));
                SearchDown.remove(MonitorMode);
                TextField.setVisible(true);
                tip.setVisible(true);
                bt1.setVisible(true);
                SearchDown.add(sp,BorderLayout.CENTER);
                repaint();
                revalidate();
            }
        });
        switchMonitor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckField.setLayout(new FlowLayout());
                SearchDown.remove(sp);
                TextField.setVisible(false);
                tip.setVisible(false);
                bt1.setVisible(false);
                SearchDown.add(MonitorMode,BorderLayout.CENTER);
                repaint();
                revalidate();

            }
        });
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // *****调用更新火车站信息接口*****
            	try {
					Test.updateStationList();
				} catch (UnsupportedEncodingException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(TextField.isVisible())JOptionPane.showMessageDialog(null,"在查询模式下，您可以通过输入：    \n    出发地\n    目的地\n    形如YYYY-MM-DD的查询日期\n    票型\n来查询目标日期的火车票余量。\n您还可以通过勾选右侧的复选框筛去选中的票类。");
                else JOptionPane.showMessageDialog(null,"在监测模式下，您可以通过设置一个开通了SMTP功能的邮箱作为发送方，\n在开启监测后保持程序运行时，自动指定监测车次的发售情况并及时将消息\n反馈到您的目标邮箱，列车号可以通过查询模式获得。");
            }
        });
    }

    void searchInit() {
        SearchMode.setLayout(new BorderLayout());
        SearchUp.setLayout(new GridLayout(2,1));
        CheckField.setLayout(new GridLayout(1,6));

        SearchMode.add(SearchUp,BorderLayout.NORTH);
        SearchMode.add(SearchDown,BorderLayout.CENTER);

        SearchUp.add(InputField);
        SearchUp.add(CheckField);
        InputField.add(new JLabel("出发地"));
        InputField.add(tf1);
        InputField.add(new JLabel("目的地"));
        InputField.add(tf2);
        InputField.add(new JLabel("日期"));
//        InputField.add(tf3);
        InputField.add(dc);
        rb.add(rb1);
        rb.add(rb2);
        CheckField.add(rb1);
        CheckField.add(rb2);
        CheckField.add(tip);
        CheckField.add(new JLabel(" "));
        CheckField.add(bt1);
        CheckField.add(bt2);

        SearchDown.remove(MonitorMode);
        SearchDown.add(sp,BorderLayout.CENTER);
        SearchDown.add(new Label(" "),BorderLayout.SOUTH);
        SearchDown.add(new Label("    "),BorderLayout.WEST);
        SearchDown.add(new Label("    "),BorderLayout.EAST);


        bt1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
                if (checkSafe) {
                    // ***** 检查各个参数情况进行查询 *****
                	RunTask.search();
                } else JOptionPane.showMessageDialog(null,"请确认出发地、目的地、查询日期输入完全后进行查询！");
            }
        });
        bt2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tf1.setText("");
                tf2.setText("");
                tf3.setText("");
                rb2.setSelected(true);
                for (int i = 0;i < 10; i++) cb[i].setSelected(false);
                TextField.setText("");
            }
        });

        rb2.setSelected(true);
        tip.setFont(new Font("黑体", Font.PLAIN,10));
        tip.setForeground(Color.red);

    }

    public void monitorInit() {
        MonitorMode.setLayout(new GridLayout(1,2));
        mPanelLeft.setLayout(new GridLayout(8,1));
        mPanelRight.setLayout(new GridLayout(2,1));

        MonitorMode.add(mPanelLeft);
        MonitorMode.add(mPanelRight);
//        MonitorMode.add(CheckBoxes);
        mPanelLeft.add(mPanel1);
        mPanelLeft.add(mPanel2);
        mPanelLeft.add(mPanel3);
        mPanelLeft.add(mPanel4);
        mPanelLeft.add(mPanel5);
        mPanelLeft.add(mPanel6);
        mPanelLeft.add(mPanel7);
        mPanelLeft.add(mPanel8);
        mPanelRight.add(CheckBoxes);
        mPanelRight.add(selectShortCut);
        CheckBoxes.setLayout(new GridLayout(5,2));
        selectShortCut.setLayout(new GridLayout(5,1));
        sPanel.setLayout(new GridLayout(1,3,10,0));
        selectShortCut.add(sPanel);
        selectShortCut.add(mtip);
        CheckBoxes.add(cb[0]);
        CheckBoxes.add(cb[1]);
        CheckBoxes.add(cb[2]);
        CheckBoxes.add(cb[3]);
        CheckBoxes.add(cb[4]);
        CheckBoxes.add(cb[5]);
        CheckBoxes.add(cb[6]);
        CheckBoxes.add(cb[7]);
        CheckBoxes.add(cb[8]);
        CheckBoxes.add(cb[9]);
        mPanel1.add(new Label("SMTP服务器地址"));
        mPanel1.add(mtf1);
        mPanel2.add(new Label("           邮箱地址"));
        mPanel2.add(mtf2);
        mPanel3.add(new Label("                 密码"));
        mPanel3.add(mtf3);
        mPanel4.add(new Label("        收件人地址"));
        mPanel4.add(mtf4);
        mPanel5.add(new Label("           时间间隔"));
        mPanel5.add(mtf5);
        mPanel6.add(new Label("           监测车次"));
        mPanel6.add(mtf6);
        tb.add(bt3);
        tb.add(bt4);
        mPanel7.add(bt3);
        mPanel7.add(bt4);
        mPanel8.add(bt5);
        mPanel8.add(bt6);
        sPanel.add(selectBt1);
        sPanel.add(selectBt2);
        sPanel.add(selectBt3);

        mtip.setForeground(Color.red);


        bt3.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
                if (!working) {
                    TicketMonitor.mtip.setText("");
                    if (monitorSafe) {
                        noneSelected = true;
                        for (int i = 0; i < 10;i++) {
                            if (selectedType[i]) {
                            noneSelected = false;
                            break;}
                        }
                        // ***** 开始监测相关接口调用 *****
                        if (!noneSelected) {
                            working = true;
                            runtask = new RunTask();
                            runtask.start();
                            TicketMonitor.mtip.setText("监测中...");
                        } else {
                            JOptionPane.showMessageDialog(null,"请选择需要查询的票型后开始监测!");
                            TicketMonitor.bt3.setSelected(false);
                        }
                    } else {JOptionPane.showMessageDialog(null, "请确认所有信息输入完全后进行监测！");TicketMonitor.bt3.setSelected(false);}
                } else JOptionPane.showMessageDialog(null,"正在监测中...");
            }
        });
        bt4.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TicketMonitor.mtip.setText("");
                // ***** 停止监测相关接口调用 *****
            	if(runtask!=null){
            	    runtask.interrupt();
            	    TicketMonitor.mtip.setText("服务已停止");
                    runtask=null;
                    working = false;
                }
                TicketMonitor.bt3.setSelected(false);
                TicketMonitor.bt4.setSelected(false);
            }
        });
        bt5.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
                TicketMonitor.mtip.setText("");
                if (testSafe) {
                    // ***** SMTP测试相关接口调用 *****
                    RunTask.testMail();
                } else JOptionPane.showMessageDialog(null,"请确认SMTP邮箱、邮箱、邮箱密码、收件人地址输入完全后进行测试！");
            }
        });
        bt6.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mtf1.setText("");
                mtf2.setText("");
                mtf3.setText("");
                mtf4.setText("");
                mtf5.setText("");
                mtf6.setText("");
            }
        });
        selectBt1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < 10; i++) cb[i].setSelected(true);
            }
        });
        selectBt2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < 10; i++) {
                    if (cb[i].isSelected()) cb[i].setSelected(false);
                    else cb[i].setSelected(true);
                }
            }
        });
        selectBt3.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < 10; i++) cb[i].setSelected(false);
            }
        });
    }

    public void refresh() {
        fromSta = tf1.getText();
        toSta = tf2.getText();
        if (dc.getDate()!=null) date = dc.getDate().toInstant().toString().substring(0,10);
        if (rb1.isSelected()) type = "ADULT";
        else type = "0X00";
        smtp = mtf1.getText();
        senderMail = mtf2.getText();
        pwd = mtf3.getText();
        receiverMail = mtf4.getText();
        interval = mtf5.getText();
        targetTrainNum = mtf6.getText();
        for(int i = 0; i < 10; i++)selectedType[i] = cb[i].isSelected();
        if (fromSta.length() != 0&&toSta.length() != 0&&date.length() != 0) checkSafe = true;
        if (senderMail.length() != 0&&pwd.length() != 0&&smtp.length() != 0&&receiverMail.length() != 0) testSafe = true;
        if (checkSafe&&testSafe&&interval.length() != 0&&targetTrainNum.length() != 0) monitorSafe = true;
    }

    public static void main(String[] args){
        TicketMonitor mainWindow = new TicketMonitor();
        mainWindow.setVisible(true);
    }
}

