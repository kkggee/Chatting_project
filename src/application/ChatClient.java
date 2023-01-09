package application;
import java.awt.*;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTextArea;

import javax.swing.JTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChatClient extends JFrame{
   //채팅방
   JTextField sendTF;
   JLabel la_msg;
   
   JTextArea ta;
   JScrollPane sp_ta,sp_list;      

   JList<String> li_inwon;
   JButton bt_file,bt_exit;   
   JPanel p;
   
   public ChatClient() {		
	 Toolkit kit = Toolkit.getDefaultToolkit();
	 Image frame_img = kit.getImage("images/FRAME_IMG.png");
	 setIconImage(frame_img);
     setTitle("채팅방");
     sendTF = new JTextField(15);     
     la_msg = new JLabel("메세지");

     ta = new JTextArea();
     ta.setLineWrap(true);
     li_inwon = new JList<String>();
     
     sp_ta = new JScrollPane(ta);
     sp_list = new JScrollPane(li_inwon);

     bt_file = new JButton("파일");
     bt_exit = new JButton("나가기");

     p = new JPanel();
//     sp_ta.setBounds(10,10,380,390); 
     sp_ta.setBounds(10,10,280,390); 
//     la_msg.setBounds(10,410,60,30); 
     la_msg.setBounds(10,410,40,30); 
//     sendTF.setBounds(70,410,320,30);
     sendTF.setBounds(60,410,230,30);
     
//     sp_list.setBounds(400,10,120,330); 
//     bt_file.setBounds(400,370,120,30); 
//     bt_exit.setBounds(400,410,120,30);
     sp_list.setBounds(295,10,83,350); 
     bt_file.setBounds(295,370,83,30); 
     bt_exit.setBounds(295,410,83,30);
     
     
     p.setLayout(null);
//     Color b=new Color(94,78,78);
     Color b=new Color(134,196,241); 
     p.setBackground(b);

     p.add(sp_ta);
     p.add(la_msg);
     p.add(sendTF);
     p.add(sp_list);
     p.add(bt_file);
     p.add(bt_exit);
     
     add(p);
     setBounds(300,200,400,500);
     sendTF.requestFocus();   
   }   
   
}


