package admin.tasks;

import admin.allocations.AllocationsController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.QueueManagementDTO;
import dto.RequestsDTO;
import dto.StatusDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;

import static http.url.Constants.CONTENT_TYPE;
import static http.url.URLconst.FETCH_REQUESTS_URL;

public class FetchRequestsTimer extends TimerTask {
    private final OkHttpClient client;
    private final AllocationsController allocationsController;

    public FetchRequestsTimer(OkHttpClient client, AllocationsController allocationsController) {
        this.client = client;
        this.allocationsController = allocationsController;
    }

    @Override
    public void run() {
        Request request = new Request.Builder()
                .url(FETCH_REQUESTS_URL)
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
                    RequestsDTO requestsDTO = gson.fromJson(responseBody, RequestsDTO.class);
                    Platform.runLater(() -> allocationsController.updateRequests(requestsDTO));
                } else {
                    StatusDTO statusDTO = gson.fromJson(responseBody, StatusDTO.class);
                    Platform.runLater(() -> {});
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //System.out.println("FetchWorldsDetailsTimer: onFailure");
                Platform.runLater(() -> allocationsController.showAlert(new StatusDTO(false, e.getMessage())));
            }
        });
    }
}
