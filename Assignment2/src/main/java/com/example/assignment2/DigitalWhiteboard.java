package com.example.assignment2;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DigitalWhiteboard extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private TextArea textArea;
    private VBox toolbar;

    @Override
    public void start(Stage primaryStage) {
        // Initialize Canvas
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Ensure white background

        // Enable Drawing on Canvas
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Toolbar
        Label titleLabel = new Label("Whiteboard Tools");
        titleLabel.getStyleClass().add("title-label");

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> gc.setStroke(colorPicker.getValue()));

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveCanvas(primaryStage));

        textArea = new TextArea();
        textArea.setPromptText("Enter text here...");
        textArea.setPrefRowCount(2);

        Button addTextButton = new Button("Add Text");
        addTextButton.setOnAction(e -> gc.fillText(textArea.getText(), 50, 50));

        Button addImageButton = new Button("Add Image");
        addImageButton.setOnAction(e -> addImage(primaryStage));

        Button addAudioButton = new Button("Add Audio");
        addAudioButton.setOnAction(e -> addAudio(primaryStage));

        Button addVideoButton = new Button("Add Video");
        addVideoButton.setOnAction(e -> addVideo(primaryStage));

        // Align buttons properly in a VBox
        toolbar = new VBox(15, titleLabel, colorPicker, clearButton, saveButton, textArea, addTextButton, addImageButton, addAudioButton, addVideoButton);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.getStyleClass().add("toolbar");

        for (Button button : new Button[]{clearButton, saveButton, addTextButton, addImageButton, addAudioButton, addVideoButton}) {
            button.getStyleClass().add("button");
        }

        BorderPane root = new BorderPane();
        root.setLeft(toolbar);
        root.setCenter(canvas);
        root.getStyleClass().add("root");

        Scene scene = new Scene(root, 1000, 700);

        // Internal CSS Styling
        String css = """
            .root {
                -fx-background-color: #ecf0f1;
            }
            .toolbar {
                -fx-background-color: #2c3e50;
                -fx-padding: 20px;
                -fx-spacing: 15px;
                -fx-alignment: center;
                -fx-min-width: 220px;
            }
            .title-label {
                -fx-text-fill: white;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
            }
            .button {
                -fx-background-color: #3498db;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 10px;
                -fx-min-width: 180px;
                -fx-border-radius: 5px;
                -fx-cursor: hand;
            }
            .button:hover {
                -fx-background-color: #2980b9;
            }
            .text-area {
                -fx-background-color: white;
                -fx-border-color: #2980b9;
                -fx-padding: 8px;
                -fx-min-width: 180px;
            }
        """;

        scene.getStylesheets().add("data:text/css," + css.replace("\n", ""));

        primaryStage.setTitle("Interactive Digital Whiteboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveCanvas(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to save image.");
            }
        }
    }

    private void addImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            toolbar.getChildren().add(imageView);
        }
    }

    private void addAudio(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
    }

    private void addVideo(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                Media media = new Media(file.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(300);
                mediaView.setPreserveRatio(true);

                toolbar.getChildren().add(mediaView);

                mediaPlayer.setOnReady(() -> mediaPlayer.play()); // Ensure it starts when ready
                mediaPlayer.setOnError(() -> showAlert("Error", "Could not play video."));

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load video.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
