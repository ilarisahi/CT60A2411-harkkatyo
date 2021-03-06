package ct60a2411.harkkatyö;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Petri Rämö
 * opiskelijanro: 0438578
 * 
 * @author Ilari Sahi
 * opiskelijanro: 0438594
 * 
 * 16.12.2016
 */
public class InfoBoxController implements Initializable {

    @FXML
    private Button returnBut;

    /**
     * This class contains function that closes infobox.
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void returnButAction(ActionEvent event) {
        Stage stage = (Stage) returnBut.getScene().getWindow();
        stage.close();
    }
    
}
