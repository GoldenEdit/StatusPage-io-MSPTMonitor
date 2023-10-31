package dev.goldenedit.statuspagemsptmonitor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public final class StatusPageMSPTMonitor extends JavaPlugin {

    private String apiKey;
    private String pageId;
    private String metricId;
    private String apiBase;
    private long frequency; // in ticks
    private boolean debug;

    private final HttpClient client = HttpClient.newHttpClient();
    private BukkitRunnable task;


    @Override
    public void onEnable() {
        // Load configuration
        loadConfig();

        // Schedule the task to run asynchronously
        scheduleTask();

        // Register commands
        getCommand("reloadstatuspage").setExecutor((sender, command, label, args) -> {
            loadConfig();
            scheduleTask();
            sender.sendMessage("Config reloaded and task rescheduled.");
            return true;
        });
    }

    private void loadConfig() {
        saveDefaultConfig();
        reloadConfig();

        apiKey = getConfig().getString("apiKey");
        pageId = getConfig().getString("pageId");
        metricId = getConfig().getString("metricId");
        apiBase = getConfig().getString("apiBase");
        frequency = getConfig().getLong("frequency") * 20; // convert to ticks
        debug = getConfig().getBoolean("debug");
    }

    private void scheduleTask() {
        if (task != null) {
            task.cancel();
        }

        task = new BukkitRunnable() {
            public void run() {
                reportMspt();
            }
        };

        task.runTaskTimerAsynchronously(this, 0, frequency);
    }
    private void reportMspt() {
        // Get the MSPT from the server
        double mspt = Bukkit.getServer().getAverageTickTime();

        // Prepare the data
        long epochInSeconds = System.currentTimeMillis() / 1000;
        String json = String.format("{\"data\":{\"timestamp\": \"%d\", \"value\": \"%f\"}}", epochInSeconds, mspt);

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/pages/%s/metrics/%s/data.json", apiBase, pageId, metricId)))
                .header("Authorization", "OAuth " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(5))
                .build();

        // Send the request asynchronously
        // Send the request asynchronously
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    // Handle the response
                    if (response.statusCode() == 200 || response.statusCode() == 201) {
                        if (debug) {
                            getLogger().info("MSPT reported successfully");
                        }
                    } else {
                        if (debug) {
                            getLogger().warning("Failed to report MSPT: " + response.body());
                            getLogger().warning("HTTP Status Code: " + response.statusCode());
                        }
                    }
                })
                .exceptionally(e -> {
                    // Handle any exceptions
                    if (debug) {
                        getLogger().warning("Exception while reporting MSPT: " + e.getMessage());
                    }
                    return null;
                });
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
