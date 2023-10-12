package user.tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SimulationIDListDTO;
import dto.StatusDTO;
import http.url.Constants;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.results.scene.ResultsController;

import java.io.IOException;
import java.util.TimerTask;

import static http.url.Constants.CONTENT_TYPE;
import static http.url.URLconst.FETCH_SIMULATION_ID_LIST_URL;

public class FetchSimulationListTimer extends TimerTask {
    private final OkHttpClient client;
    private final ResultsController resultsController;

    public FetchSimulationListTimer(OkHttpClient client, ResultsController resultsController) {
        this.client = client;
        this.resultsController = resultsController;
    }

    @Override
    public void run() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(FETCH_SIMULATION_ID_LIST_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.LAST_ITEM, String.valueOf(resultsController.getMaxExecutionID()));
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, "text/plain")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Gson gson = new GsonBuilder()
                        .create();
                if (response.code() == 200) {
                    SimulationIDListDTO simulationIDListDTO = gson.fromJson(responseBody, SimulationIDListDTO.class);
                    Platform.runLater(() -> resultsController.updateSimulationList(simulationIDListDTO));
                } else {
                    StatusDTO statusDTO = gson.fromJson(responseBody, StatusDTO.class);
                    Platform.runLater(() -> {});
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //System.out.println("FetchWorldsDetailsTimer: onFailure");
                Platform.runLater(() -> resultsController.showAlert(new StatusDTO(false, e.getMessage())));
            }
        });
    }
}
