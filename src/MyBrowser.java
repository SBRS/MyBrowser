import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MyBrowser extends Application {
	
	TextField txtUrl;
	WebView wv;
	String Home = "http://www.unitec.ac.nz/";
	
	
	public static void main(String[] args) {
		launch(args);		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		HBox hb = new HBox();
		VBox root = new VBox();
		hb.setSpacing(10);
		
		wv = new WebView();
		
		BorderPane bp = new BorderPane(); 
		bp.setCenter(wv);
		root.getChildren().addAll(hb,bp);

		wv.getEngine().load(Home);
		
	    Label lbl = new Label("URL:");
	    lbl.setFont(Font.font("Amble CN", FontWeight.BOLD, 20));
	    hb.getChildren().add(lbl);
		
		txtUrl = new TextField();
		txtUrl.setPrefWidth(300);
		txtUrl.setText(Home);
		hb.getChildren().addAll(txtUrl);
		
		Button btnGo = new Button("Go");
		btnGo.setOnMouseClicked(this::clickGo);
		hb.getChildren().addAll(btnGo);
		
		Button btnBack = new Button("<==");
		btnBack.setOnMouseClicked(this::clickBack);
		hb.getChildren().addAll(btnBack);
		
		Button btnForward = new Button("==>");
		btnForward.setOnMouseClicked(this::clickForward);
		hb.getChildren().addAll(btnForward);
		
		Button btnHome = new Button("Home");
		btnHome.setOnMouseClicked(this::clickHome);
		hb.getChildren().addAll(btnHome);
		
		Button btnOpen = new Button("Open");
		hb.getChildren().addAll(btnOpen);
		FileChooser fileChooser = new FileChooser();
		btnOpen.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
	                public void handle(final ActionEvent e) {
	                    File file = fileChooser.showOpenDialog(stage);
	                    if (file != null) {
	                    		wv.getEngine().load(file.toURI().toString());
	                    }
					}
				});
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
//		wv.getEngine().getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
//		    if (newState == State.SUCCEEDED) {
		        String titleText = wv.getEngine().getTitle();
		        stage.setTitle(titleText);
//		    }
//		});

		stage.show();			
	}
	
	void clickGo(MouseEvent event){
		wv.getEngine().load(txtUrl.getText());
	}
	
	void clickHome(MouseEvent event) {
		wv.getEngine().load(Home);
		txtUrl.setText(Home);
	}
	
	void clickBack(MouseEvent event) {
		wv.getEngine().load(goBack());
		txtUrl.setText(goBack());
	}
	
	public String goBack()
	{    
	    WebHistory history=wv.getEngine().getHistory();
	    ObservableList<WebHistory.Entry> entryList=history.getEntries();
	    int currentIndex=history.getCurrentIndex();
	    
	    Platform.runLater(() -> {
	    		wv.getEngine().executeScript("history.back()");
	    });
	    
	    return entryList.get(currentIndex>0?currentIndex-1:currentIndex).getUrl();
	}	
	
	void clickForward(MouseEvent event) {
		wv.getEngine().load(goForward());
		txtUrl.setText(goForward());
	}
	
	public String goForward()
	{    
	    WebHistory history=wv.getEngine().getHistory();
	    ObservableList<WebHistory.Entry> entryList=history.getEntries();
	    int currentIndex=history.getCurrentIndex();
	    
	    Platform.runLater(() -> {
	    		wv.getEngine().executeScript("history.forward()");
	    });
	    
	    return entryList.get(currentIndex<entryList.size()-1?currentIndex+1:currentIndex).getUrl();
	}
}
