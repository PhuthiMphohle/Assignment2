package com.example.assignment2;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class WhiteboardImplementation implements WhiteboardInterface {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Pane mediaPane;
    private MediaPlayer mediaPlayer;

    public WhiteboardImplementation(Canvas canvas, Pane mediaPane) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.mediaPane = mediaPane;
        canvas.setMouseTransparent(false);
        initializeDrawingTools(gc);
    }

    @Override
    public void initializeDrawingTools(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.setOnMousePressed(e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        canvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });
    }

    @Override
    public void setStrokeColor(Color color) {
        gc.setStroke(color);
    }

    @Override
    public void addText(String text) {
        if (!text.isEmpty()) {
            gc.setFill(gc.getStroke());
            gc.fillText(text, canvas.getWidth() / 2, canvas.getHeight() / 2);
        } else {
            showAlert("Warning", "Please enter some text first.");
        }
    }

    @Override
    public void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        mediaPane.getChildren().clear();
    }

    @Override
    public void saveCanvas(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                javafx.scene.image.WritableImage snapshot = canvas.snapshot(null, null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                showAlert("Success", "Canvas saved successfully.");
            } catch (IOException e) {
                showAlert("Error", "Failed to save canvas.");
            }
        }
    }

    @Override
    public void addImage(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ImageView view = new ImageView(image);
            view.setFitWidth(200);
            view.setPreserveRatio(true);
            view.setLayoutX(300);
            view.setLayoutY(200);
            mediaPane.getChildren().add(view);
        }
    }

    @Override
    public void addAudio(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav"));
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            Button play = new Button("▶");
            Button pause = new Button("⏸");

            play.setOnAction(e -> mediaPlayer.play());
            pause.setOnAction(e -> mediaPlayer.pause());

            HBox controls = new HBox(10, play, pause);
            controls.setLayoutX(50);
            controls.setLayoutY(500);
            controls.setAlignment(Pos.CENTER);

            mediaPane.getChildren().add(controls);
        }
    }

    @Override
    public void addVideo(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video", "*.mp4", "*.avi"));
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(400);
            mediaView.setFitHeight(300);
            mediaView.setLayoutX(200);
            mediaView.setLayoutY(150);

            Button play = new Button("▶");
            Button pause = new Button("⏸");

            play.setOnAction(e -> mediaPlayer.play());
            pause.setOnAction(e -> mediaPlayer.pause());

            HBox controls = new HBox(10, play, pause);
            controls.setAlignment(Pos.CENTER);

            VBox mediaBox = new VBox(10, mediaView, controls);
            mediaBox.setLayoutX(200);
            mediaBox.setLayoutY(150);

            mediaPane.getChildren().add(mediaBox);
        }
    }

    @Override
    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
