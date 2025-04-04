package com.example.assignment2;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public interface WhiteboardInterface {
    void initializeDrawingTools(GraphicsContext gc);
    void saveCanvas(Stage stage);
    void clearCanvas();
    void addImage(Stage stage);
    void addAudio(Stage stage);
    void addVideo(Stage stage);
    void showAlert(String title, String message);
    void addText(String text);
    void setStrokeColor(javafx.scene.paint.Color color);
}
