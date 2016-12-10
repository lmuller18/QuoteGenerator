import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class QuoterGUI extends Application{
//    private JFrame mainFrame;
//    private JPanel controlPanel;
    private String quote;
    private String webpage;

    @FXML
    private TextField quoteText;
    @FXML
    private TextField webpageText;
    @FXML
    private TextArea quoteArea;
    @FXML
    private TextField varianceText;

    private static Scene scene;
    public QuoterGUI(){
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        scene = new Scene(root);
        stage.setTitle("Quoter");
        stage.setScene(scene);
        stage.show();

        quoteText = (TextField) scene.lookup("#quoteText");
        webpageText = (TextField) scene.lookup("#webpageText");
        varianceText = (TextField) scene.lookup("#varianceText");
        quoteArea = (TextArea) scene.lookup("#quoteArea");

    }

    @FXML
    protected void handleSearchButton(ActionEvent event){
        quote = quoteText.getText();
        webpage = webpageText.getText();
        if(quote.length() == 0 && webpage.length() == 0) {
            quoteText.setStyle("-fx-border-color: red;");
            webpageText.setStyle("-fx-border-color: red;");
        }
        else if(quote.length() == 0){
            quoteText.setStyle("-fx-border-color: red;");
            webpageText.setStyle(null);
        }
        else if(webpage.length() == 0){
            webpageText.setStyle("-fx-border-color: red;");
            quoteText.setStyle(null);
        } else {
            quoteText.setStyle(null);
            webpageText.setStyle(null);
            quoteText.setText("");
            webpageText.setText("");
            Quoter.beginSearch(webpage, quote);
        }
    }

    public void setQuote(String finalQuote){
        quoteArea = (TextArea) scene.lookup("#quoteArea");
        quoteArea.setWrapText(true);
        quoteArea.setText(finalQuote);
    }

    public void setVariance(String variance){
        varianceText = (TextField) scene.lookup("#varianceText");
        varianceText.setText(variance);
    }

    public void setWarning(String warning){
        webpageText = (TextField) scene.lookup("#webpageText");
        webpageText.setStyle("-fx-text-inner-color: red; -fx-border-color: red;");
    }


}
