import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Router implements Serializable {
	
	String routerName;
	int noOfNeighbours;
	HashMap<String, Double> neighbourCost = new HashMap<String, Double>();
	HashMap<String, String> neighbourNextHop = new HashMap<String, String>();
	HashMap<String, Integer> neighbourPort = new HashMap<String, Integer>();
	int portNumber;

	public String getRouterName() {
		return routerName;
	}

	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	public HashMap<String, Double> getNeighbourCost() {
		return neighbourCost;
	}

	public void setNeighbourCost(HashMap<String, Double> neighbourCost) {
		this.neighbourCost = neighbourCost;
	}

	public HashMap<String, String> getNeighbourNextHop() {
		return neighbourNextHop;
	}

	public void setNeighbourNextHop(HashMap<String, String> neighbourNextHop) {
		this.neighbourNextHop = neighbourNextHop;
	}
	
	public HashMap<String, Integer> getNeighbourPort() {
		return neighbourPort;
	}

	public void setNeighbourPort(HashMap<String, Integer> neighbourPort) {
		this.neighbourPort = neighbourPort;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public int getNoOfNeighbours() {
		return noOfNeighbours;
	}

	public void setNoOfNeighbours(int noOfNeighbours) {
		this.noOfNeighbours = noOfNeighbours;
	}

	public Router() {
		// TODO Auto-generated constructor stub
	}
	
	public void getExistingDistanceVector(String nodeName) throws IOException {
		String filePath = (System.getProperty("user.dir")).toString() + "\\routerinfo\\" + nodeName + ".txt";
		File f = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		noOfNeighbours = Integer.parseInt(br.readLine());
		setNoOfNeighbours(noOfNeighbours);

		routerName = nodeName;
		setRouterName(routerName);
		
		String fileLine = null;
		while((fileLine = br.readLine()) != null) {
			String []parameters = fileLine.split(" ");
			neighbourCost.put(parameters[0], Double.parseDouble(parameters[1].trim()));
			setNeighbourCost(neighbourCost);
			
			neighbourNextHop.put(parameters[0], parameters[2]);
			setNeighbourNextHop(neighbourNextHop);
			
			if (parameters.length == 4) {	
				neighbourPort.put(parameters[0], Integer.parseInt(parameters[3].trim()));
				setNeighbourPort(neighbourPort);
			}
		}
		br.close();
	}
	
	public void addToFile() throws IOException {
		String filePath = (System.getProperty("user.dir")).toString() + "\\routerinfo\\" + this.routerName + ".txt";
		FileWriter fw = new FileWriter(filePath, false);
		fw.write(Integer.toString(this.noOfNeighbours));
		for (String key : this.neighbourCost.keySet()) {
			if (this.neighbourPort.get(key) != null) {
				fw.write("\n" + key +  " " + this.neighbourCost.get(key) + " " + this.neighbourNextHop.get(key) + " " + 
							this.neighbourPort.get(key));
			} else {
				fw.write("\n" + key +  " " + this.neighbourCost.get(key) + " " + this.neighbourNextHop.get(key));
			}
		}
		fw.close();
	}
	
	public void recomputeDistanceVector(Router advertisingRouter) throws IOException {
		String nextHop = advertisingRouter.getRouterName();
		double costFromRcvToAd = this.neighbourCost.get(advertisingRouter.getRouterName());
		for (String adNeighbour : advertisingRouter.neighbourCost.keySet()) {
			if (this.neighbourCost.keySet().contains(adNeighbour) && !(adNeighbour.equals(routerName))) {
				if (this.neighbourCost.get(adNeighbour) != null)
				{
					if (advertisingRouter.neighbourCost.get(adNeighbour) + costFromRcvToAd <= this.neighbourCost.get(adNeighbour)) {
						this.neighbourCost.put(adNeighbour, advertisingRouter.neighbourCost.get(adNeighbour) + costFromRcvToAd);
						this.neighbourNextHop.put(adNeighbour, nextHop);
					}
				}
			} else {
				if (!(adNeighbour.equals(routerName))) {
					this.neighbourCost.put(adNeighbour, costFromRcvToAd + advertisingRouter.neighbourCost.get(adNeighbour));
					this.neighbourNextHop.put(adNeighbour, nextHop);
				}
			}
		}
	}
	
	/**
	 * Converts an object to byte
	 * @param o The object which has to converted to byte
	 * @return The byte equivalent for the object passed
	 * @throws IOException
	 */
	public static byte[] toBytes(Object o) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		objStream.writeObject(o);
		return byteStream.toByteArray();
	}
	
	/**
	 * Converts a byte to Object
	 * @param b The byte which has to converted to object
	 * @return The converted data from a byte to object
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object toObject(byte[] b) throws ClassNotFoundException, IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
		ObjectInputStream objStream = new ObjectInputStream(byteStream);
		return objStream.readObject();
	}
}