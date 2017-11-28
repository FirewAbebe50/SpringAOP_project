
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestDriver extends Application {
	private static TextArea displayCityInfo = new TextArea();
	private static TextArea displayResult = new TextArea();
	private static TextArea searchResult = new TextArea();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("ICS_340");
		Button goBtn = new Button("Start");
		Label startLabel = new Label("Start City");
		Label endLabel = new Label("Finish City");
		Label cityInfo = new Label("City Info");
		Label finalPathLabel = new Label("Border");
		Label searchPathLabel = new Label("Search route");
		ObservableList<String> options = FXCollections.observableArrayList();
		ArrayList<String[]> cityMatrix = DocumentClass.importCities();
		ArrayList<Cities> cities = DocumentClass.processCities(cityMatrix);
		for (Cities c : cities) {
			options.add(c.getName());
		}
		ComboBox<String> startCity = new ComboBox<String>(options);
		ComboBox<String> endCity = new ComboBox<String>(options);
		DocumentClass.addActualDistances(cityMatrix, cities);
		DocumentClass.addHeuristicDistances(DocumentClass.importHeuristicCities(), cities);
		goBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (startCity.getValue() != null) {
					for (Cities c : cities) {
						if (c.getName() == startCity.getValue()) {
							break;
						}
					}

					try {
						search(stringToCity(startCity.getValue(), cities), stringToCity(endCity.getValue(), cities),
								cities);
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		});

		GridPane main = new GridPane();
		GridPane container = new GridPane();
		GridPane leftList = new GridPane();
		ColumnConstraints leftMainColumn = new ColumnConstraints();
		leftMainColumn.setPercentWidth(100);
		GridPane.setHalignment(leftList, HPos.CENTER);
		VBox btnWrapper = new VBox();
		btnWrapper.setAlignment(Pos.CENTER);
		Scene scene = new Scene(container, 1200, 600);
		scene.getStylesheets().add(getClass().getResource("fxStyle.css").toExternalForm());
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(100);
		container.getColumnConstraints().addAll(column1);
		ColumnConstraints lftColumn = new ColumnConstraints();
		ColumnConstraints rtColumn = new ColumnConstraints();
		lftColumn.setPercentWidth(15);
		rtColumn.setPercentWidth(85);
		main.getColumnConstraints().addAll(lftColumn);
		main.getColumnConstraints().addAll(rtColumn);
		btnWrapper.getChildren().add(goBtn);
		leftList.getChildren().add(btnWrapper);
		leftList.getChildren().add(startLabel);
		leftList.getChildren().add(endLabel);
		leftList.getChildren().add(startCity);
		leftList.getChildren().add(endCity);
		GridPane.setConstraints(startLabel, 0, 2);
		GridPane.setConstraints(endLabel, 0, 4);
		GridPane.setConstraints(btnWrapper, 0, 6);
		GridPane.setConstraints(startCity, 0, 3);
		GridPane.setConstraints(endCity, 0, 5);
		HBox resultContainer = new HBox();
		resultContainer.getStyleClass().add("hBoxResult");
		VBox finalPathBox = new VBox();
		VBox searchPathBox = new VBox();
		finalPathBox.getChildren().add(finalPathLabel);
		finalPathBox.getChildren().add(displayResult);
		searchPathBox.getChildren().add(searchPathLabel);
		searchPathBox.getChildren().add(searchResult);
		resultContainer.getChildren().add(finalPathBox);
		resultContainer.getChildren().add(searchPathBox);
		main.getChildren().add(leftList);
		main.getChildren().add(cityInfo);
		main.getChildren().add(displayCityInfo);
		main.getChildren().add(resultContainer);
		GridPane.setConstraints(leftList, 0, 0, 1, 4);
		GridPane.setConstraints(cityInfo, 1, 0);
		GridPane.setConstraints(displayCityInfo, 1, 1);
		GridPane.setConstraints(resultContainer, 1, 2);
		container.getChildren().add(main);
		GridPane.setConstraints(main, 0, 0);
		main.setPrefWidth(150);
		Insets emptyBdr = new Insets(10, 10, 10, 10);
		GridPane.setMargin(main, new Insets(30, 30, 30, 30));
		GridPane.setMargin(displayCityInfo, new Insets(10, 30, 30, 30));
		GridPane.setMargin(cityInfo, new Insets(10, 30, 0, 20));
		GridPane.setMargin(startCity, new Insets(2, 0, 0, 0));
		GridPane.setMargin(endCity, new Insets(2, 0, 0, 0));
		GridPane.setMargin(endLabel, new Insets(10, 0, 0, 0));
		GridPane.setMargin(startLabel, new Insets(10, 0, 0, 0));
		HBox.setMargin(finalPathBox, new Insets(10, 40, 30, 30));
		HBox.setMargin(searchPathBox, emptyBdr);
		VBox.setMargin(searchPathLabel, new Insets(0, 0, 10, 0));
		VBox.setMargin(finalPathLabel, new Insets(0, 0, 10, 0));
		goBtn.setMinWidth(100);
		goBtn.getStyleClass().add("btn");
		leftList.getStyleClass().add("leftList");
		btnWrapper.getStyleClass().add("vBox");
		displayCityInfo.setMinWidth(600);
		displayCityInfo.setMaxWidth(900);
		displayCityInfo.setPrefHeight(125);
		displayResult.setMaxSize(425, 600);
		displayResult.setMinSize(250, 300);
		searchResult.setMinSize(250, 300);
		searchResult.setMaxSize(425, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void search(Cities start, Cities end, ArrayList<Cities> cities) throws IOException {
		displayResult.clear();
		searchResult.clear();
		Cities thisCity = start;
		System.out.println("Start City: " + start.getName());
		ArrayList<Cities> visited = new ArrayList<Cities>();
		ArrayList<Cities> frontier = new ArrayList<Cities>();
		for (Cities city : cities) {
			city.sethValue(null);
			city.setgValue(null);
			city.setfValue(null);
		}

		for (Cities city : cities) {
			List<Entry<Cities, Integer>> cityHeuEdgeList = new LinkedList<Entry<Cities, Integer>>(
					city.getEdges().entrySet());
			for (Entry<Cities, Integer> cityHeur : cityHeuEdgeList) {
				if (cityHeur.getKey() == end) {
					city.sethValue(cityHeur.getValue());
				}
			}
		}

		displayCityInfo.setText("Base City Name: " + start.getName() + " Ab: " + start.getCityAbb());
		displayCityInfo.appendText("\nActual Edges: ");
		List<Entry<Cities, Integer>> startEdges = new LinkedList<Entry<Cities, Integer>>(
				start.getActualDistanceEdges().entrySet());
		Integer countActualEdges = 0;
		for (Entry<Cities, Integer> hEntry : startEdges) {
			if (countActualEdges % 6 == 0) {
				displayCityInfo.appendText("\n");
			}
			displayCityInfo.appendText(hEntry.getKey().getName() + " " + hEntry.getKey().gethValue());
			countActualEdges++;
		}

		displayCityInfo.appendText("\n\nHeuristic Edges: ");
		List<Entry<Cities, Integer>> startHeuristics = new LinkedList<Entry<Cities, Integer>>(
				start.getEdges().entrySet());
		Integer count = 0;
		for (Entry<Cities, Integer> hEntry : startHeuristics) {
			if (count % 6 == 0) {
				displayCityInfo.appendText("\n");
			}
			displayCityInfo.appendText(hEntry.getKey().getName() + " " + hEntry.getKey().gethValue());
			count++;
		}

		start.setgValue(0);
		start.setfValue(start.gethValue());
		end.sethValue(0);

		frontier.add(start);
		System.out.println("Start at " + start);

		int test = 3;

		List<Cities> traversal = new ArrayList<Cities>();

		while (test > 0) {
			visited.add(thisCity);
			traversal.add(thisCity);

			List<Entry<Cities, Integer>> actualDistances = new LinkedList<Entry<Cities, Integer>>(
					thisCity.getActualDistanceEdges().entrySet());
			for (Entry<Cities, Integer> dEntry : actualDistances) {
				if (dEntry.getKey().gValue == null) {
					dEntry.getKey().setgValue(thisCity.getgValue() + dEntry.getValue());
					dEntry.getKey().setParent(thisCity);
					dEntry.getKey().setfValue(dEntry.getKey().getgValue() + dEntry.getKey().gethValue());
				} else if (thisCity.gValue > thisCity.getgValue() + dEntry.getValue()) {
					dEntry.getKey().setgValue(thisCity.getgValue() + dEntry.getValue());
					dEntry.getKey().setParent(thisCity);
					dEntry.getKey().setfValue(dEntry.getKey().getgValue() + dEntry.getKey().gethValue());
				}
				if (!visited.contains(dEntry.getKey()) && !frontier.contains(dEntry.getKey())) {
					frontier.add(dEntry.getKey());
				}
			}
			frontier.remove(0);
			if (!frontier.isEmpty()) {
				frontier.sort(new Comparator<Cities>() {
					@Override
					public int compare(Cities o1, Cities o2) {
						if (o1.getfValue() > o2.getfValue()) {
							return 1;
						} else if (o1.getfValue() < o2.getfValue()) {
							return -1;
						} else {
							return 0;
						}
					}
				});
			}

			displayResult.appendText("Current Frontier at node " + thisCity.getName() + " \n");
			for (Cities c : frontier) {
				displayResult.appendText(c.toString() + "\n");
			}
			displayResult.appendText("\n");
			thisCity = frontier.get(0);
			if (thisCity == end) {
				test = 0;
				Integer totalMiles = thisCity.getfValue();
				Cities backTrack = thisCity;
				List<Cities> finalSearch = new ArrayList<Cities>();
				while (backTrack != start) {
					finalSearch.add(0, backTrack);
					backTrack = backTrack.getParent();
				}
				for (Cities listCity : finalSearch) {
					searchResult.appendText(listCity.getName() + "\n");
				}
				searchResult.appendText("\n\nTotal Distance: " + totalMiles);
				DocumentClass.writeToFile(finalSearch, traversal, start, end);
			}
		}
	}
	
	public Cities stringToCity(String name, ArrayList<Cities> list) {
		for (Cities c : list) {
			if (c.getName() == name) {
				return c;
			}
			;
		}
		return null;
	}
}
