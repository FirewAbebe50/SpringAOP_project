import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DocumentClass {
	static File actual = new File("MnDOTactualDistances-commas.txt");
	static File heuristic = new File("MnDOTheuristicDistances-commas.txt");

	public static ArrayList<String[]> importCities() throws IOException {
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(actual)));
			String line;
			ArrayList<String[]> cityMatrix = new ArrayList<String[]>();
			String[] lineArray;

			while ((line = br.readLine()) != null) {
				lineArray = line.split(",");
				cityMatrix.add(lineArray);
			}
			br.close();
			return cityMatrix;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String[]> importHeuristicCities() throws IOException {
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(heuristic)));
			String line;
			ArrayList<String[]> cityMatrix = new ArrayList<String[]>();
			String[] lineArray;

			while ((line = br.readLine()) != null) {
				lineArray = line.split(",");
				cityMatrix.add(lineArray);
			}
			br.close();
			return cityMatrix;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Cities> processCities(ArrayList<String[]> matrix) {
		ArrayList<Cities> cities = new ArrayList<Cities>();
		Integer count = -1;
		String[] names = matrix.get(0);
		for (String s : names) {
			System.out.println(s.toString());
		}

		for (String[] string : matrix) {
			count++;

			if (!string[0].matches("TOWN")) {
				cities.add(new Cities(string[0], names[count]));
			}
		}

		return cities;
	}

	public static void addActualDistances(ArrayList<String[]> matrix, ArrayList<Cities> cityList) {
		int count = -1;
		Cities to = null;
		Cities from = null;

		System.out.println("Matrix Size: " + matrix.get(0).length);
		for (String[] s : matrix) {
			int innerCount = -1;
			for (String inner : s) {
				if (inner.matches("[0-9]*")) {
					from = cityList.get(count);
					to = cityList.get(innerCount);
					from.addActualDistanceEdge(to, Integer.valueOf(inner));
					to.addActualDistanceEdge(from, Integer.valueOf(inner));
				}
				innerCount++;
			}
			count++;
		}
	}

	public static void addHeuristicDistances(ArrayList<String[]> matrix, ArrayList<Cities> cityList) {
		int count = -1;
		Cities to = null;
		Cities from = null;
		System.out.println("Matrix Size: " + matrix.get(0).length);
		for (String[] s : matrix) {
			int innerCount = -1;
			for (String inner : s) {
				if (inner.matches("[0-9]*")) {
					from = cityList.get(count);
					to = cityList.get(innerCount);
					from.addEdge(to, Integer.valueOf(inner));
					to.addEdge(from, Integer.valueOf(inner));
				}
				innerCount++;
			}
			count++;
		}
	}

	public static void writeToFile(List<Cities> finalSearch, List<Cities> traversal, Cities start, Cities end)
			throws IOException {
		BufferedWriter bw = null;
		try {
			String fileName = start.getName() + "to" + end.getName() + ".txt";
			File file = new File("" + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write("Graph Traversal: ");
			bw.newLine();
			for (Cities c : traversal) {
				bw.write(c.getCityAbb());
				bw.newLine();
			}

			bw.write("\nSearch Order:");
			bw.newLine();

			for (Cities c : finalSearch) {
				bw.write(c.getCityAbb());
				bw.newLine();
			}
			System.out.println("Successfully display " + fileName + "\n Location: " + file.getAbsolutePath());

			bw.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println("BufferedWriter Error due to closing" + ex);
			}
		}

	}
}
