package com.example.assignment2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DigitalWhiteboard extends Application {
    private Canvas canvas;
    private Pane mediaPane;
    private StackPane canvasContainer;
    private WhiteboardImplementation whiteboard;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(800, 600);
        mediaPane = new Pane();
        canvasContainer = new StackPane(canvas, mediaPane);
        canvas.setMouseTransparent(false); // allow drawing
        canvasContainer.setStyle("-fx-background-color: white;");

        whiteboard = new WhiteboardImplementation(canvas, mediaPane);

        Label title = new Label("Whiteboard Tools");

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> whiteboard.setStrokeColor(colorPicker.getValue()));

        Button drawButton = new Button("Draw");
        drawButton.setOnAction(e -> whiteboard.initializeDrawingTools(canvas.getGraphicsContext2D()));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> whiteboard.saveCanvas(primaryStage));

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> whiteboard.clearCanvas());

        TextArea textInput = new TextArea();
        textInput.setPromptText("Enter text...");
        textInput.setPrefRowCount(2);

        Button textButton = new Button("Add Text");
        textButton.setOnAction(e -> whiteboard.addText(textInput.getText()));

        Button imageButton = new Button("Add Image");
        imageButton.setOnAction(e -> whiteboard.addImage(primaryStage));

        Button audioButton = new Button("Add Audio");
        audioButton.setOnAction(e -> whiteboard.addAudio(primaryStage));

        Button videoButton = new Button("Add Video");
        videoButton.setOnAction(e -> whiteboard.addVideo(primaryStage));

        VBox toolbar = new VBox(12, title, colorPicker, drawButton, saveButton, clearButton,
                textInput, textButton, imageButton, audioButton, videoButton);
        toolbar.setAlignment(Pos.TOP_CENTER);
        toolbar.setStyle(
                "-fx-background-color: #34495e; " +
                        "-fx-padding: 20px; " +
                        "-fx-spacing: 10px; " +
                        "-fx-min-width: 220px;"
        );

        BorderPane root = new BorderPane();
        root.setLeft(toolbar);
        root.setCenter(canvasContainer);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Interactive Digital Whiteboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
