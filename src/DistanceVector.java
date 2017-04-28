import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DistanceVector {

	public DistanceVector() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		String fileName = args[1];
		Boolean flag = true;
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		int portNumber = Integer.parseInt(args[0]);
		InetAddress IPAddress = InetAddress.getByName("localhost");
		
		DatagramSocket clientSocket = new DatagramSocket(portNumber);
		
		// Neighbor Router
		Router neighborRouter = new Router();
		
		// Source Router
		Router srcRouter = new Router();

		while (true) {
			try {
				if (flag == true) {
					clientSocket.setSoTimeout(15000);
					flag = false;
				}
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				
				neighborRouter = (Router) Router.toObject(receiveData);
				srcRouter.getExistingDistanceVector(fileName);
				neighborRouter.getExistingDistanceVector(neighborRouter.getRouterName());
				srcRouter.recomputeDistanceVector(neighborRouter);
				srcRouter.addToFile();
				
			}
			
			catch (Exception e) {
				flag = true;
				srcRouter.getExistingDistanceVector(fileName);
				sendData = Router.toBytes(srcRouter);
				Iterator<Entry<String, Integer>> it = srcRouter.getNeighbourPort().entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<String, Integer> pair = (Entry<String, Integer>)it.next();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, pair.getValue());
					clientSocket.send(sendPacket);
			        it.remove();
			    }
				List<String> nodeCosts = new ArrayList<String>(srcRouter.getNeighbourCost().keySet());
				for (int i = 0; i < nodeCosts.size(); i++) {
					System.out.println("Shortest Path " + srcRouter.getRouterName() + "-" + nodeCosts.get(i) + ":" + 
								"Next Hop is " + srcRouter.getNeighbourNextHop().get(nodeCosts.get(i)) + " and the cost is" + 
								srcRouter.getNeighbourCost().get(nodeCosts.get(i)));
				}
				System.out.println("\n");

			}
		}
	}
}