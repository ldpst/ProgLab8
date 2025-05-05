package server.utils;

public enum TextColors {
    RED("red"),
    GREEN("green"),
    RESET("reset");

    private final String color;
    TextColors(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
