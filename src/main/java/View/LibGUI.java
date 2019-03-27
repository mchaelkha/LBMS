package View;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class LibGUI extends Application implements EventHandler<ActionEvent> {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("LBMS");

        BorderPane layout = new BorderPane();

        HBox terminal = new HBox();
        terminal.setMinSize(600,460);

        HBox field = new HBox();
        field.setMinWidth(600);

        TextField textField = new TextField();
        textField.setMinWidth(600);
        textField.setOnAction(this);

        Text results = new Text("Library");


        field.getChildren().add(textField);
        terminal.getChildren().add(results);

        layout.setCenter(terminal);
        layout.setBottom(field);


        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
