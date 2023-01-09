package application;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ChangePWController {
   @FXML Button changePW;
   @FXML PasswordField nowPW;
   @FXML PasswordField newPW;
   @FXML PasswordField checkNewPW;
   @FXML TextField nowID;
@FXML Button Backbtn2;
	
    String DRIVER="com.mysql.cj.jdbc.Driver";
   String URL="jdbc:mysql://localhost/signup";
   String USER="root";
   String PASS="0000";
   
   public Connection getConn() { //����
        Connection con=null;
        try {
           Class.forName(DRIVER);
           con=DriverManager.getConnection(URL,USER,PASS);
        }catch(Exception e) {
           e.printStackTrace();
        }
        return con;
    }
   
   
   @FXML public void Change_pw(ActionEvent event) throws Exception{
      String fID = nowID.getText();
      String fPW = nowPW.getText();
      String sPW = newPW.getText();
      String scPW = checkNewPW.getText();
      
      Connection con = null;
      PreparedStatement pst = null;
      ResultSet rs = null;
      
      Connection con1 = null;
      PreparedStatement pst1 = null;
      boolean rs1 = true;
      
      try {
         con = getConn();
         String sql="select id,pwd from signup where id=? and pwd=?"; //sql��
         pst = con.prepareStatement(sql);
         pst.setString(1,fID);
         pst.setString(2,fPW);
         rs = pst.executeQuery();
         
         if(rs.next()) {
            if(rs.getString("id").equals(fID) && rs.getString("pwd").equals(fPW)) { //���̵� ��� ��ġ
               if(sPW.equals(scPW)) { // ���� ��� �ΰ� ��ġ
                  // update ������ �����ؾߵ�
                  try {
                     con1 = getConn();
                     String sql1 = "update signup set pwd=? where pwd=?"; // ���� ��й�ȣ�� *�� ���� �� ��й�ȣ�� ����
                     pst1 = con.prepareStatement(sql1);
                     pst1.setString(1, sPW);
                     pst1.setString(2, fPW);
                     rs1 = pst1.execute();
                     System.out.println("7");
                     if(rs1 == false) {
                        Alert ChangeSuccess = new Alert(AlertType.CONFIRMATION);
                        ChangeSuccess.setHeaderText("��й�ȣ ���� �Ϸ�");
                        ChangeSuccess.setContentText("��й�ȣ�� ���������� �����߽��ϴ�.");
                        ChangeSuccess.showAndWait();
                        
                     }
                     else {
                        Alert FailChange = new Alert(AlertType.ERROR);
                        FailChange.setHeaderText("��й�ȣ ���� ����");
                        FailChange.setContentText("����.");
                        FailChange.showAndWait();
                     }
                  }catch(Exception e) {
                     e.printStackTrace();
                  }finally {
                     if(con1 != null) try {con1.close();} catch (SQLException e) {e.printStackTrace();}
                     if(pst1 != null) try {pst1.close();} catch (SQLException e) {e.printStackTrace();}
                  }
               }
               else {
                  Alert FailChange = new Alert(AlertType.ERROR);
                  FailChange.setHeaderText("��й�ȣ ���� ����");
                  FailChange.setContentText("�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
                  FailChange.showAndWait();   
               }
            }
            else {
               Alert FailChange = new Alert(AlertType.ERROR);
               FailChange.setHeaderText("��й�ȣ ���� ����");
               FailChange.setContentText("���̵�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
               FailChange.showAndWait();
            }
         }
         else {
            Alert FailChange = new Alert(AlertType.ERROR);
            FailChange.setHeaderText("��й�ȣ ���� ����");
            FailChange.setContentText("���̵�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
            FailChange.showAndWait();   
         }
      }catch(Exception e) {
         e.printStackTrace();
         
      }finally {
         if(con != null) try {con.close();} catch (SQLException e) {e.printStackTrace();}
         if(pst != null) try {pst.close();} catch (SQLException e) {e.printStackTrace();}
          if(rs != null) try {rs.close();} catch (SQLException e) {e.printStackTrace();}
          
      }
   }
   
   @FXML public void back2() throws Exception { //ȸ������ �������� �Ѿ��
	     
	      Stage primaryStage = new Stage();
	      Parent back = FXMLLoader.load(getClass().getResource("Main.fxml"));
	      primaryStage.getIcons().add(new Image("images/FRAME_IMG.png"));
	      primaryStage.setTitle("Main");
	      primaryStage.setScene(new Scene(back));
	      primaryStage.show();
	      primaryStage.setResizable(false);
	        
	        Stage stage = (Stage)Backbtn2.getScene().getWindow();
	        stage.close();
	   }
}