import daddybot.DaddyBot;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private DaddyBot daddy;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image daddyImage = new Image(this.getClass().getResourceAsStream("/images/daddy.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(
                DialogBox.getDaddyDialog(
                        "What can daddy do for you?\n type 'please daddy' after every command",
                        daddyImage));
    }

    /** Injects the DaddyBot instance */
    public void setDaddy(DaddyBot d) {
        daddy = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * DaddyBot's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = daddy.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDaddyDialog(response, daddyImage));
        userInput.clear();
    }
}
