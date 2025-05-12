package server.commands;

import server.managers.ConfigManager;
import server.requests.Request;
import server.response.Response;
import server.utils.TextColors;

import java.io.IOException;

public abstract class Command {
    private final String name;
    private final String description;
    private final String task;

    protected static final String RED = ConfigManager.getColor(TextColors.RED);
    protected static final String RESET = ConfigManager.getColor(TextColors.RESET);
    protected static final String GREEN = ConfigManager.getColor(TextColors.GREEN);

    public Command(String name, String description, String task) {
        this.name = name;
        this.description = description;
        this.task = task;
    }

    public abstract Response execute(Request request) throws IOException;

    public String getName() {
        return name;
    }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }
}
