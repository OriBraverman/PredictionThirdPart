package user.results.scene;

import dto.*;
import user.app.AppController;
import user.results.simulation.SimulationController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import user.tasks.FetchSimulationTimer;

import java.util.Timer;


public class ResultsController {
    @FXML private AnchorPane resultsAnchorPane;
    @FXML private ListView<Integer> executionList;
    @FXML private SimulationController simulationComponentController;
    @FXML private AnchorPane simulationComponent;

    private AppController appController;
    private ExecutionListManager executionListManager;
    public final static int REFRESH_RATE = 1000;
    private Timer fetchSimulationTimer;
    private FetchSimulationTimer fetchSimulationTimerTask;
    private boolean setActive = false;
    public void initialize(){
        executionListManager = new ExecutionListManager();
        executionList.setCellFactory(param -> new ExecutionListCell());
        ObservableList<Integer> items = FXCollections.observableArrayList();
        executionList.setItems(items);
        executionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Platform.runLater(() -> {
            fetchSimulationTimer = new Timer();
            fetchSimulationTimerTask = new FetchSimulationTimer(this);
            fetchSimulationTimer.schedule(fetchSimulationTimerTask, 0, 200);
        });
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    public void onExecutionListClicked(MouseEvent event) {
        updateSimulation();
    }

    public void updateSimulation() {
        if (!setActive) {
            return;
        }
        if (executionListManager.isEmpty() || executionList.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        Integer selectedID = executionList.getSelectionModel().getSelectedItem();
        if (selectedID != null) {
            appController.getConnection().getSimulationExecutionDetails(selectedID);
        }
    }

    public void updateSimulationComponent(SimulationExecutionDetailsDTO sedDTO) {
        simulationComponentController.updateSimulationComponent(sedDTO);
        if (sedDTO.isCompleted()) {
            simulationComponentController.getInformationComponentController().updateInformationComponent(sedDTO.getId());
        }
    }
    public SimulationController getSimulationComponentController() {
        return simulationComponentController;
    }

    public int getMaxExecutionID() {
        return executionListManager.getMaxExecutionID();
    }

    public void showAlert(StatusDTO statusDTO) {
        appController.showAlert(statusDTO);
    }

    public void addAndSelectToSimulationList(int simulationId) {
        if (executionListManager.isEmpty()) {
            setActive = true;
        }
        Integer item = executionListManager.add(simulationId);
        if (item != null) {
            executionList.getItems().add(item);
        }
        executionList.getSelectionModel().select(simulationId);
    }
}