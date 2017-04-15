import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DistanceVector {

	public DistanceVector() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String filePath = (System.getProperty("user.dir")).toString() + "\\routerinfo\\" + args[0];
		File f = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String fileLine = br.readLine();
		System.out.println(fileLine);
		br.close();
	}
}
