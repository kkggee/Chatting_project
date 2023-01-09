package application;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Toolkit;
import javax.sound.sampled.AudioInputStream; import javax.sound.sampled.AudioSystem; import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainChat extends JFrame implements ActionListener, Runnable{
	String DRIVER="com.mysql.cj.jdbc.Driver";
	String URL="jdbc:mysql://localhost/signup";
	String USER="root";
	String PASS="0000";
	      
	public Connection getConn() { //����
	      Connection con=null;
	      try {
	         Class.forName(DRIVER);
	         con=DriverManager.getConnection(URL,USER,PASS);
	      }catch(Exception e) { e.printStackTrace(); }
	      return con;
	      }
	JList<String> roomInfo,waitInfo;
	JScrollPane sp_roomInfo;
	JLabel nameInfo;
	JButton bt_create, bt_enter, bt_exit, bt_change;
	JPanel p;
	ChatClient cc;
    BufferedReader in;
    OutputStream out;
    String selectedRoom;
    String nickName, id, pwd , newname;
    ReName re;
    openFile openfile;
    int num=0;

    //������ ���� �� ������ ����
    //createBTN ���� �� ũ�� ����
    ImageIcon c1 = new ImageIcon("src/images/createBTN.png");
    Image c2 = c1.getImage();
    Image c3 = c2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon createBTN = new ImageIcon(c3);
	
    //enterBTN ���� �� ũ�� ����
    ImageIcon enter1 = new ImageIcon("src/images/enterBTN.png");
    Image enter2 = enter1.getImage();
    Image enter3 = enter2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon enterBTN = new ImageIcon(enter3);
    
    //exitBTN ���� �� ũ�� ����
    ImageIcon exit1 = new ImageIcon("src/images/exitBTN.png");
    Image exit2 = exit1.getImage();
    Image exit3 = exit2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon exitBTN = new ImageIcon(exit3);
    
    //changeBTN ���� �� ũ�� ����
    ImageIcon change1 = new ImageIcon("src/images/renameBTN.png");
    Image change2 = change1.getImage();
    Image chanege3 = change2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon renameBTN = new ImageIcon(chanege3);
    
	public MainChat(String name) {	
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image frame_img = kit.getImage("src/images/FRAME_IMG.png");
		setIconImage(frame_img);
		setTitle("����");
		
		cc = new ChatClient();
		re = new ReName();
	    roomInfo = new JList<String>();
//	    roomInfo.setBorder(new TitledBorder("������"));
	    roomInfo.addMouseListener(new MouseAdapter() {
	        @Override
	       public void mouseClicked(MouseEvent e) {
	           String str = roomInfo.getSelectedValue(); //"�ڹٹ�--1"
	           if(str==null)return;
	           
	          System.out.println("STR="+str);
	          selectedRoom = str.substring(0, str.indexOf("-"));
	          //��ȭ�� ���� �ο�����
//	          sendMsg("170|"+selectedRoom);
	       }    
	    });
		
		sp_roomInfo = new JScrollPane(roomInfo);
		
		Color b1 = new Color(116,114,114);
		nameInfo = new JLabel("a",JLabel.CENTER);  //�߰�
		nameInfo.setFont(new Font("monospaced", Font.BOLD, 20));
		nameInfo.setForeground(b1);
	    bt_create = new JButton(createBTN);
	    bt_enter = new JButton(enterBTN);
	    bt_exit = new JButton(exitBTN);
	    bt_change = new JButton(renameBTN);
	    
	    bt_change.setBorderPainted(false);
	    bt_change.setContentAreaFilled(false);
	    bt_change.setFocusPainted(false);
	    
	    //������ (��ư)
	    bt_create.setBackground(new Color(255,255,255));
	    bt_enter.setBackground(new Color(255,255,255));
	    bt_exit.setBackground(new Color(255,255,255));
	    bt_change.setBackground(new Color(255,255,255));
	    
	    p = new JPanel();
	    sp_roomInfo.setBounds(13,60,360,320);
	    nameInfo.setBounds(13,20,70,30);
	    bt_change.setBounds(82,18,30,30);
	    bt_create.setBounds(243,385,40,40);
	    bt_enter.setBounds(288,385,40,40);
	    bt_exit.setBounds(333,385,40,40);
	    
	    p.setLayout(null);
	    Color b = new Color(134,196,241);
	    p.setBackground(b);
	    p.add(sp_roomInfo);
	    p.add(nameInfo); //�߰�
	    p.add(bt_create);
	    p.add(bt_enter);
	    p.add(bt_exit);
	    p.add(bt_change);
	    
	    add(p);
	    setBounds(300,200, 400, 500);
	    setVisible(true);
	    setDefaultCloseOperation(EXIT_ON_CLOSE); 
	     
	    connect();//��������õ�
	    new Thread(this).start();//�����޽��� ���
	    sendMsg("100|");//(����)���� �˸�
	    this.nickName = name;
	    sendMsg("150|"+nickName);//��ȭ�� ����

	     eventUp();
	}
	
    private void eventUp(){//�̺�Ʈ�ҽ�
        //����(MainChat)
        bt_create.addActionListener(this);
        bt_enter.addActionListener(this);
        bt_exit.addActionListener(this);
        bt_change.addActionListener(this);

      //��ȭ��(ChatClient)
        cc.sendTF.addActionListener(this);
        cc.bt_file.addActionListener(this); //�߰�
        cc.bt_exit.addActionListener(this);
        
      //�̸�����(ReName)
        re.newname.addActionListener(this);
        re.ok.addActionListener(this);
        re.cancel.addActionListener(this);
	}
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	     Object ob = e.getSource();
	     Connection con1 = null;
	     PreparedStatement pst1 = null;
	     boolean rs1 = true;
	     
	     if(ob==bt_create){//�游��� ��û
	       String title = JOptionPane.showInputDialog(this,"������:");
	       //�������� �������� ����
	       sendMsg("160|"+title);
	       cc.setTitle("ä�ù�-["+title+"]");
	       

	          try {
	             con1 = getConn();
	             String sql1 = "insert into chatting_rec values (?,?)";
	             pst1 = con1.prepareStatement(sql1);
	             pst1.setString(1, title);
	             pst1.setString(2, "");
	             rs1 = pst1.execute();

	             if(rs1 == false) {
	                System.out.println("���������� �����");
	                }
	             } catch (SQLException e1) {
	         // TODO Auto-generated catch block
	         e1.printStackTrace();
	         }
	       
	       sendMsg("175|");//��ȭ�泻 �ο����� ��û    
	       setVisible(false);
	       cc.setVisible(true); //��ȭ���̵�
	     }else if(ob==bt_enter){//����� ��û
	       if(selectedRoom == null){
	         JOptionPane.showMessageDialog(this, "���� ����!!");
	         return;
	       }
	       sendMsg("200|"+ selectedRoom);
	       sendMsg("175|");//��ȭ�泻 �ο����� ��û
	       setVisible(false);
	       cc.setVisible(true);
	     }else if(ob==cc.bt_file) {
	        try {
	         openfile = new openFile();
	      } catch (Exception e1) {
	         e1.printStackTrace();
	      }
	     }else if(ob==cc.bt_exit){//��ȭ�� ������ ��û
//	       sendMsg("400|");
	       if(num>0)
	    	   sendMsg("450|");
	       else
	    	   sendMsg("400|");
	       cc.setVisible(false);
	       setVisible(true); 
	     }else if(ob==cc.sendTF){//(TextField�Է�)�޽��� ������ ��û
	       String msg = cc.sendTF.getText();
	       if(msg.length()>0){
	         sendMsg("300|"+msg); 
	         cc.sendTF.setText("");
	       }
	     }   
	     else if(ob==bt_exit){//������(���α׷�����) ��û
	        System.exit(0);//���� �������α׷� �����ϱ�
	     }     
	     else if(ob==bt_change) {
//	        re.oldname.setText(nickName);
	        if(num>0)
	        	re.oldname.setText(newname);
	        else
	        	re.oldname.setText(nickName);
	        re.setVisible(true);
	     }
	     else if(ob==re.newname || ob==re.ok) {
	        sendMsg("155|"+re.oldname.getText()+"|"+re.newname.getText());
	        num+=1;
	        newname = re.newname.getText();
	        re.oldname.setText("");
	        re.newname.setText("");
	        re.setVisible(false);
	     }
	     else if(ob==re.cancel) {
	        re.setVisible(false);
	        setVisible(true);
	     }
	}
	
	public void connect() {//(����)�������� ��û
		try {
			Socket s = new Socket("220.68.27.123",8080); //����õ� 
			
	        //in: �����޽��� �бⰴü    ����-----msg------>Ŭ���̾�Ʈ
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        //out: �޽��� ������, ���ⰴü    Ŭ���̾�Ʈ-----msg----->����
			out = s.getOutputStream();
			
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) {
		try {
			out.write( (msg+"\n").getBytes() );
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {//������ ���� �޽��� �б�
	    try {
	        while(true){
	              String msg = in.readLine(); //msg: ������ ���� �޽���
	              String msgs[] = msg.split("\\|");
	              String protocol = msgs[0];
	              switch(protocol){
	                case "300": 
	                     cc.ta.append(msgs[1]+"\n");
	                    cc.ta.setCaretPosition(cc.ta.getText().length());
	                    File a = new File("src/images/alarm.wav"); 
						AudioInputStream b = AudioSystem.getAudioInputStream(a); 
						Clip c = AudioSystem.getClip(); 
						c.open(b); 
						c.start(); 
						Thread.sleep(c.getMicrosecondLength()/1000);
						   
	                         break;

	                case "160"://�游��� 
	                   if(msgs.length > 1){
	                      //������ ���� �Ѱ� �̻��̾����� ����
	                     String roomNames[] = msgs[1].split(",");
	                       roomInfo.setListData(roomNames);
	                   }
	                     break;
	               
	                case "175"://(��ȭ�濡��) ��ȭ�� �ο�����
	                     String myRoomInwons[] = msgs[1].split(",");
	                     cc.li_inwon.setListData(myRoomInwons);
	                     break;
	                     
	                case "180"://���� �ο�����
	                   String waitNames[] = msgs[1].split(",");
//	                   waitInfo.setListData(waitNames);
	                   nameInfo.setText(waitNames[0]);
	                   break;
	                     	
	                case "200"://��ȭ�� ����
	                     cc.ta.append("=========["+msgs[1]+"]�� ����=========\n");
	                    cc.ta.setCaretPosition(cc.ta.getText().length());
	                     break;

	                case "400"://��ȭ�� ����
	                   cc.ta.append("=========["+msgs[1]+"]�� ����=========\n");
	                   cc.ta.setCaretPosition(cc.ta.getText().length());
	                   break;

	                case "202"://������ ���� Ÿ��Ʋ ���� ���
	                   cc.setTitle("ä�ù�-["+msgs[1]+"]");
	                   break;
	              }
	           }
	       }catch (IOException e) {
	        e.printStackTrace();
	      } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
