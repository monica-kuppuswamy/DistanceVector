import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;

public class DistanceVector {

	public DistanceVector() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
//		String filePath = (System.getProperty("user.dir")).toString() + "\\routerinfo\\" + args[0];
//		File f = new File(filePath);
//		BufferedReader br = new BufferedReader(new FileReader(f));
//		String fileLine = br.readLine();
//		System.out.println(fileLine);
//		br.close();
		String fileName = args[1];
		Boolean flag = true;
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		int portNumber = Integer.parseInt(args[0]);
		InetAddress IPAddress = InetAddress.getByName("localhost");
		DatagramSocket clientSocket = new DatagramSocket(portNumber);

		while(true){
			try{
				System.out.println("In try block");
				if (flag == true){
					clientSocket.setSoTimeout(15000);
					flag = false;
				}
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				File f = new File((System.getProperty("user.dir")).toString()+ "\\routerinfo\\" + fileName);
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(receiveData);
				fos.close();
				
			}
			catch(Exception e){
//				String path = "";
//				File initialFile = new File(path);
//				FileInputStream targetStream = new FileInputStream(initialFile);
//				for (int i=0;i<1024;i++)
//			     {
//			         sendData[i]=(byte)targetStream.read();
//			     }
				System.out.println("In catch block");
				flag = true;
				Files.readAllBytes(new File((System.getProperty("user.dir")).toString()+ "\\routerinfo\\" + fileName).toPath());

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
				clientSocket.send(sendPacket);
				
			}
		}
	}
}
