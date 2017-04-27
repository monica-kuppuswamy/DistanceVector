import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Router {
	
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
			neighbourCost.put(parameters[0], Double.parseDouble(parameters[1]));
			setNeighbourCost(neighbourCost);
			
			neighbourNextHop.put(parameters[0], parameters[2]);
			setNeighbourNextHop(neighbourNextHop);
			try{
				neighbourPort.put(parameters[0], Integer.parseInt(parameters[3]));
				setNeighbourPort(neighbourPort);
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
		br.close();
	}
	
	public void addToFile(Router r) throws IOException {
		String filePath = (System.getProperty("user.dir")).toString() + "\\routerinfo\\" + r.routerName + ".txt";
		FileWriter fw = new FileWriter(filePath, false);
		fw.write(Integer.toString(r.noOfNeighbours));
		for (String key : r.neighbourCost.keySet()) {
			if (r.neighbourPort.get(key) != null) {
				fw.write("\n" + key +  " " + r.neighbourCost.get(key) + " " + r.neighbourNextHop.get(key) + " " + r.neighbourPort.get(key));
			} else {
				fw.write("\n" + key +  " " + r.neighbourCost.get(key) + " " + r.neighbourNextHop.get(key));
			}
		}
		fw.close();
	}
	
	public void recomputeDistanceVector(Router advertisingRouter) throws IOException {
		String nextHop = advertisingRouter.getRouterName();
		double costFromRcvToAd = neighbourCost.get(advertisingRouter.getRouterName());
		for (String adNeighbour : advertisingRouter.neighbourCost.keySet()) {
			if (neighbourCost.keySet().contains(adNeighbour) && !(adNeighbour.equals(routerName))) {
				if (neighbourCost.get(adNeighbour) != null)
				{
					if (advertisingRouter.neighbourCost.get(adNeighbour) + costFromRcvToAd < neighbourCost.get(adNeighbour)) {
						neighbourCost.put(adNeighbour, advertisingRouter.neighbourCost.get(adNeighbour) + costFromRcvToAd);
						neighbourNextHop.put(adNeighbour, nextHop);
					}
				}
			} else {
				if (!(adNeighbour.equals(routerName))) {
					System.out.println(adNeighbour);
					neighbourCost.put(adNeighbour, costFromRcvToAd + advertisingRouter.neighbourCost.get(adNeighbour));
					neighbourNextHop.put(adNeighbour, nextHop);
				}
			}
		}
		
	}	
}