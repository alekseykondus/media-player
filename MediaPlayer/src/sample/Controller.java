package sample;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Controller {

    private MediaPlayer mediaPlayer;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider sceneSlider;

    @FXML
    private MediaView mediaView;

    @FXML
    Label timeLabel;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select file", "*.mp4", "*.mp3", "*.wav", "*.aiff", "*.fxm", "*.flv");
        fileChooser.getExtensionFilters().add(filter);

        String filePath = fileChooser.showOpenDialog(null).toURI().toString();

        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            volumeSlider.setValue(mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });

            sceneSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.seek(Duration.seconds(sceneSlider.getValue()));
                }
            });
            mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    moveSceneSlider();
                }
            });
            //sceneSlider.setValue(0);
            mediaPlayer.play();
        }
    }
    private void moveSceneSlider(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sceneSlider.setValue(mediaPlayer.getCurrentTime().toMinutes()/mediaPlayer.getTotalDuration().toMinutes()*100);
                long hours = (int)mediaPlayer.getCurrentTime().toHours();
                long minutes = (int)mediaPlayer.getCurrentTime().toMinutes() - hours*60;
                long seconds = (int)mediaPlayer.getCurrentTime().toSeconds() - minutes*60;

                timeLabel.setText(hours + ":" + minutes + ":" + seconds);
            }
        });
    }
    @FXML
    private void playMedia(ActionEvent event) {
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }
    @FXML
    private void stopMedia(ActionEvent event) {
        mediaPlayer.stop();
        //sceneSlider.setValue(0);
    }
    @FXML
    private void pauseMedia(ActionEvent event) {
        mediaPlayer.pause();
    }
    @FXML
    private void slowMedia(ActionEvent event) {
        mediaPlayer.setRate(0.75);
    }
    @FXML
    private void verySlowMedia(ActionEvent event) {
        mediaPlayer.setRate(0.5);
    }
    @FXML
    private void fastMedia(ActionEvent event) {
        mediaPlayer.setRate(1.5);
    }
    @FXML
    private void veryFastMedia(ActionEvent event) {
        mediaPlayer.setRate(2);
    }
    @FXML
    private void exitMedia(ActionEvent event) {
        System.exit(0);
    }
}
