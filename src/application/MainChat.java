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
	      
	public Connection getConn() { //연결
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

    //아이콘 생성 및 사이즈 조절
    //createBTN 생성 및 크기 조절
    ImageIcon c1 = new ImageIcon("src/images/createBTN.png");
    Image c2 = c1.getImage();
    Image c3 = c2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon createBTN = new ImageIcon(c3);
	
    //enterBTN 생성 및 크기 조절
    ImageIcon enter1 = new ImageIcon("src/images/enterBTN.png");
    Image enter2 = enter1.getImage();
    Image enter3 = enter2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon enterBTN = new ImageIcon(enter3);
    
    //exitBTN 생성 및 크기 조절
    ImageIcon exit1 = new ImageIcon("src/images/exitBTN.png");
    Image exit2 = exit1.getImage();
    Image exit3 = exit2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon exitBTN = new ImageIcon(exit3);
    
    //changeBTN 생성 및 크기 조절
    ImageIcon change1 = new ImageIcon("src/images/renameBTN.png");
    Image change2 = change1.getImage();
    Image chanege3 = change2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
    ImageIcon renameBTN = new ImageIcon(chanege3);
    
	public MainChat(String name) {	
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image frame_img = kit.getImage("src/images/FRAME_IMG.png");
		setIconImage(frame_img);
		setTitle("대기실");
		
		cc = new ChatClient();
		re = new ReName();
	    roomInfo = new JList<String>();
//	    roomInfo.setBorder(new TitledBorder("방정보"));
	    roomInfo.addMouseListener(new MouseAdapter() {
	        @Override
	       public void mouseClicked(MouseEvent e) {
	           String str = roomInfo.getSelectedValue(); //"자바방--1"
	           if(str==null)return;
	           
	          System.out.println("STR="+str);
	          selectedRoom = str.substring(0, str.indexOf("-"));
	          //대화방 내의 인원정보
//	          sendMsg("170|"+selectedRoom);
	       }    
	    });
		
		sp_roomInfo = new JScrollPane(roomInfo);
		
		Color b1 = new Color(116,114,114);
		nameInfo = new JLabel("a",JLabel.CENTER);  //추가
		nameInfo.setFont(new Font("monospaced", Font.BOLD, 20));
		nameInfo.setForeground(b1);
	    bt_create = new JButton(createBTN);
	    bt_enter = new JButton(enterBTN);
	    bt_exit = new JButton(exitBTN);
	    bt_change = new JButton(renameBTN);
	    
	    bt_change.setBorderPainted(false);
	    bt_change.setContentAreaFilled(false);
	    bt_change.setFocusPainted(false);
	    
	    //색변경 (버튼)
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
	    p.add(nameInfo); //추가
	    p.add(bt_create);
	    p.add(bt_enter);
	    p.add(bt_exit);
	    p.add(bt_change);
	    
	    add(p);
	    setBounds(300,200, 400, 500);
	    setVisible(true);
	    setDefaultCloseOperation(EXIT_ON_CLOSE); 
	     
	    connect();//서버연결시도
	    new Thread(this).start();//서버메시지 대기
	    sendMsg("100|");//(대기실)접속 알림
	    this.nickName = name;
	    sendMsg("150|"+nickName);//대화명 전달

	     eventUp();
	}
	
    private void eventUp(){//이벤트소스
        //대기실(MainChat)
        bt_create.addActionListener(this);
        bt_enter.addActionListener(this);
        bt_exit.addActionListener(this);
        bt_change.addActionListener(this);

      //대화방(ChatClient)
        cc.sendTF.addActionListener(this);
        cc.bt_file.addActionListener(this); //추가
        cc.bt_exit.addActionListener(this);
        
      //이름변경(ReName)
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
	     
	     if(ob==bt_create){//방만들기 요청
	       String title = JOptionPane.showInputDialog(this,"방제목:");
	       //방제목을 서버에게 전달
	       sendMsg("160|"+title);
	       cc.setTitle("채팅방-["+title+"]");
	       

	          try {
	             con1 = getConn();
	             String sql1 = "insert into chatting_rec values (?,?)";
	             pst1 = con1.prepareStatement(sql1);
	             pst1.setString(1, title);
	             pst1.setString(2, "");
	             rs1 = pst1.execute();

	             if(rs1 == false) {
	                System.out.println("정상적으로 저장됨");
	                }
	             } catch (SQLException e1) {
	         // TODO Auto-generated catch block
	         e1.printStackTrace();
	         }
	       
	       sendMsg("175|");//대화방내 인원정보 요청    
	       setVisible(false);
	       cc.setVisible(true); //대화방이동
	     }else if(ob==bt_enter){//방들어가기 요청
	       if(selectedRoom == null){
	         JOptionPane.showMessageDialog(this, "방을 선택!!");
	         return;
	       }
	       sendMsg("200|"+ selectedRoom);
	       sendMsg("175|");//대화방내 인원정보 요청
	       setVisible(false);
	       cc.setVisible(true);
	     }else if(ob==cc.bt_file) {
	        try {
	         openfile = new openFile();
	      } catch (Exception e1) {
	         e1.printStackTrace();
	      }
	     }else if(ob==cc.bt_exit){//대화방 나가기 요청
//	       sendMsg("400|");
	       if(num>0)
	    	   sendMsg("450|");
	       else
	    	   sendMsg("400|");
	       cc.setVisible(false);
	       setVisible(true); 
	     }else if(ob==cc.sendTF){//(TextField입력)메시지 보내기 요청
	       String msg = cc.sendTF.getText();
	       if(msg.length()>0){
	         sendMsg("300|"+msg); 
	         cc.sendTF.setText("");
	       }
	     }   
	     else if(ob==bt_exit){//나가기(프로그램종료) 요청
	        System.exit(0);//현재 응용프로그램 종료하기
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
	
	public void connect() {//(소켓)서버연결 요청
		try {
			Socket s = new Socket("220.68.27.123",8080); //연결시도 
			
	        //in: 서버메시지 읽기객체    서버-----msg------>클라이언트
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        //out: 메시지 보내기, 쓰기객체    클라이언트-----msg----->서버
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
	public void run() {//서버가 보낸 메시지 읽기
	    try {
	        while(true){
	              String msg = in.readLine(); //msg: 서버가 보낸 메시지
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

	                case "160"://방만들기 
	                   if(msgs.length > 1){
	                      //개설된 방이 한개 이상이었을때 실행
	                     String roomNames[] = msgs[1].split(",");
	                       roomInfo.setListData(roomNames);
	                   }
	                     break;
	               
	                case "175"://(대화방에서) 대화방 인원정보
	                     String myRoomInwons[] = msgs[1].split(",");
	                     cc.li_inwon.setListData(myRoomInwons);
	                     break;
	                     
	                case "180"://대기실 인원정보
	                   String waitNames[] = msgs[1].split(",");
//	                   waitInfo.setListData(waitNames);
	                   nameInfo.setText(waitNames[0]);
	                   break;
	                     	
	                case "200"://대화방 입장
	                     cc.ta.append("=========["+msgs[1]+"]님 입장=========\n");
	                    cc.ta.setCaretPosition(cc.ta.getText().length());
	                     break;

	                case "400"://대화방 퇴장
	                   cc.ta.append("=========["+msgs[1]+"]님 퇴장=========\n");
	                   cc.ta.setCaretPosition(cc.ta.getText().length());
	                   break;

	                case "202"://개설된 방의 타이틀 제목 얻기
	                   cc.setTitle("채팅방-["+msgs[1]+"]");
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
