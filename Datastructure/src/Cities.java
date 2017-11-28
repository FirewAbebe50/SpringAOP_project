
/**
 
 * @author Heruy
 */
import java.util.HashMap;

public class Cities {
	private String name;
	private String citAb;
	private HashMap<Cities, Integer> edges;
	private HashMap<Cities, Integer> exactPathDis;
	Integer fVal = null;
	Integer hValue = null;
	Integer gValue = null;
	Cities parent;

	public Cities(String name, String cityAbb) {
		this.name = name;
		this.edges = new HashMap<Cities, Integer>();
		this.exactPathDis = new HashMap<Cities, Integer>();
		this.citAb = cityAbb;
	}

	public void addEdge(Cities city, int distance) {
		edges.put(city, distance);
	}

	public void addActualDistanceEdge(Cities city, int distance) {
		exactPathDis.put(city, distance);
	}

	public String toString() {
		return this.name + " F:" + this.fVal + " G:" + this.gValue + " H:" + this.hValue;
	}

	public HashMap<Cities, Integer> getEdges() {
		return edges;
	}

	public HashMap<Cities, Integer> getActualDistanceEdges() {
		return exactPathDis;
	}

	public String getName() {
		return name;
	}

	public Integer getfValue() {
		return fVal;
	}

	public void setfValue(Integer fValue) {
		this.fVal = fValue;
	}

	public Integer gethValue() {
		return hValue;
	}

	public void sethValue(Integer hValue) {
		this.hValue = hValue;
	}

	public Integer getgValue() {
		return gValue;
	}

	public void setgValue(Integer gValue) {
		this.gValue = gValue;
	}

	public Cities getParent() {
		return parent;
	}

	public void setParent(Cities parent) {
		this.parent = parent;
	}

	public String getCityAbb() {
		return citAb;
	}

}
