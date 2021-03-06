package ct60a2411.harkkatyö;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class
 * 
 * @author Petri Rämö
 * opiskelijanro: 0438578
 * 
 * @author Ilari Sahi
 * opiskelijanro: 0438594
 * 
 * 16.12.2016
 * 
 * Main class of the program
 */
public class TimoMain extends Application {
    
    MainWindowController controller;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        
        // Sets the style sheet
        root.getStylesheets().addAll(getClass().getResource("style.css").toExternalForm());
        
        // Gets the controller for easier log writing upon exit
        controller = loader.getController();
        
        // Sets visuals for window (title, icons)
        stage.setTitle("TIMO");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("assets/timo_icon.png")));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("assets/timo_icon_big.png")));
        stage.setMinHeight(500.0);
        stage.setMinWidth(760.0);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    /**
     * Calls for final log write upon program exit
     */
    public void stop() {
        controller.closeAction();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
