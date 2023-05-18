package com.example.mierda.tasks;

public enum TaskPriority {
    HIGH,
    MEDIUM,
    LOW;
    public String getRespectiveColor() {
        switch (this) {
        case HIGH:
            return "#fe9798";
        case MEDIUM:
            return "#fec399";
        case LOW:
            return "#f6fe97";
        default:
            return "#f8ad92";
        }
    }
    public String getRespectiveColorRGB() {
        switch (this) {
        case HIGH:
            return "254, 151, 152";
        case MEDIUM:
            return "254, 195, 153";
        case LOW:
            return "246, 254, 151";
        default:
            return "248, 173, 146";
        }
    }
    public int getValue() {
        switch (this) {
        case HIGH:
            return 0;
        case MEDIUM:
            return 1;
        case LOW:
            return 2;
        default:
            return 2;
        }
    }
}
