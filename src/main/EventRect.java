package main;

import java.awt.*;

// EventRect class represents an area in the game where events are triggered.
// Extends Rectangle to inherit dimensions and position properties.
public class EventRect extends Rectangle {
    int eventRectDefaultX, eventRectDefaultY; // Default position of the event rectangle.
    boolean eventDone = false; // Tracks if the event has already been triggered.
}

