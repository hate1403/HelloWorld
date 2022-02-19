package Pack;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
class ListenThread extends Thread{
	Socket socket;
	static String s1;
	public ListenThread(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			InputStream is = socket.getInputStream();
			byte[] recvData = new byte[512];
			while(true) {
				int size = is.read(recvData); 
				s1 = new String(recvData, 0, size );
				System.out.println("test2 " + s1);
			}
		} catch (Exception e) {e.printStackTrace();}
	}
}
public class Client2 extends Application {
	Socket cs = new Socket();
	@Override
	public void start(Stage Stage) throws Exception {
		VBox root = new VBox();
		root.setPrefSize(400, 300);
		root.setSpacing(5);
		//------------------------------------------------------------------
		Button btn1 = new Button("접속 버튼");
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cs = new Socket();
				try {   //0.0.0.0
					cs.connect(new InetSocketAddress("localhost", 5001));
				} catch (Exception e) {
					e.printStackTrace();
				}
				ListenThread lt = new ListenThread(cs);
				lt.start();
			}
		});
		//------------------------------------------------------------------
//		Button btn2 = new Button("데이터 전송");
//		btn2.setOnAction(new EventHandler<ActionEvent>() {
//			int count = 0;
//			//데이터를 전송할때는 바이트 타입만 전송할 수 있다.
//			@Override
//			public void handle(ActionEvent arg0) {			
//				try {
//					OutputStream os = cs.getOutputStream();
//					String s = "apple"+ count++;
//					byte[]data = s.getBytes(); //생략된건 보류하고
//					os.write(data);
//					System.out.println("데이터 보냄");
//				} catch (Exception e) {e.printStackTrace();}
//			}
//		});
		//------------------------------------------------------------------
		Button btn3 = new Button("접속 종료");
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {cs.close();} catch (Exception e) {e.printStackTrace();}
			}
		});
		//TextArea textArea = new TextArea();
		TextArea textArea = new TextArea();
		TextField textField = new TextField();
		textField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					OutputStream os = cs.getOutputStream();
					System.out.println("데이터 보냄");
					String s = "client2 : " + textField.getText();
					byte[]data = s.getBytes();
					os.write(data);
					textField.setText(" "); 
					System.out.println("test" + ListenThread.s1);
					textArea.appendText(ListenThread.s1 + "\n");
					//textArea.appendText(ListenThread.getS1() + "\n");	
				} catch (Exception e) {e.printStackTrace();}
			}
		});
		root.getChildren().addAll(btn1, btn3, textField);
		//--------------------
		Scene scene = new Scene(root);
		Stage.setScene(scene);
		Stage.setTitle("Client2");
		Stage.show();
	}
	public static void main(String[] args) {
		launch();
	}
}
