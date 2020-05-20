package controlador;

import java.io.IOException;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class IniciBotonsController extends Application {

	private ResourceBundle texts;
	Locale localitzacioDisplay = Locale.getDefault(Category.DISPLAY);
	
	//Injecció dels panells i controls de la UI definida al fitxer fxml
	@FXML private Button btnPersones;
	@FXML private Button btnSortir; 

	@Override
	public void start(Stage primaryStage) throws IOException {

		//Carrega el fitxer amb la interficie d'usuari inicial (Scene)
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/IniciBotonsView.fxml"));
		
		//Carregar fitxer de textos multiidioma de la localització actual
		texts = ResourceBundle.getBundle("vista.Texts", localitzacioDisplay);
		//fins aquí tot igual, només falta assignar el fitxer de recursos al formulari
		loader.setResources(texts);

		Scene fm_inici = new Scene(loader.load());
		
		//Li assigna la escena a la finestra inicial (primaryStage) i la mostra
		primaryStage.setScene(fm_inici);
		primaryStage.getIcons().add(new Image("team.png"));
		primaryStage.setTitle(texts.getString("title.contactbook"));
		primaryStage.setResizable(false);
		primaryStage.show();       
	}

	@FXML
	private void onAction(ActionEvent e) throws IOException {
		if(e.getSource() == btnPersones){			
			
			//Carrega el fitxer amb la interficie d'usuari
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PersonesView.fxml"));
			//Carregar fitxer de textos multiidioma de la localització actual
			texts = ResourceBundle.getBundle("vista.Texts", localitzacioDisplay);
			loader.setResources(texts);
			
			//Crea una nova finestra i l'obre 
			Stage stage = new Stage();
			Scene fm_personas = new Scene(loader.load());
			stage.setTitle(texts.getString("title.people"));
			stage.getIcons().add(new Image("team.png"));
			stage.setResizable(false);
			stage.setScene(fm_personas);
			stage.show();
			
			//Crear un objecte de la clase PersonasController ja que necessitarem accedir al mètodes d'aquesta classe
			PersonesController personasControler = loader.getController();
			personasControler.setVentana(stage);
			
			//Programem l'event que s'executará quan es tanqui la finestra
			stage.setOnCloseRequest((WindowEvent we) -> {
				personasControler.sortir();
			});
		}else if(e.getSource() == btnSortir){
			Platform.exit();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
