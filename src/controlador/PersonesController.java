package controlador;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Persona;
import model.PersonesDAO;

public class PersonesController{

	//Objecte per gestionar la persistència de les dades
	private PersonesDAO persones;
	private ResourceBundle texts;
	Locale localitzacioDisplay = Locale.getDefault(Category.DISPLAY);

	//Elements gràfics de la UI
	private Stage ventana;
	@FXML private TextField idTextField;
	@FXML private TextField nomTextField;
	@FXML private TextField cognomsTextField;
	@FXML private TextField emailTextField;
	@FXML private TextField telefonTextField;

	/**
	 * Inicialitza la classe. JAVA l'executa automàticament després de carregar el fitxer fxml
	 */
	@FXML private void initialize() {
		//Obrir el fitxer de persones
		persones = new PersonesDAO();
		persones.openAll();
		texts = ResourceBundle.getBundle("vista.Texts", localitzacioDisplay);
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML private void onKeyPressedId(KeyEvent e) throws IOException {
		
		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
			//Comprovar si existeix la persona indicada en el control idTextField
			Persona persona = persones.find(Integer.parseInt(idTextField.getText()));
			if(persona != null){ 
				//posar els valors per modificarlos
				nomTextField.setText(persona.getNom());
				cognomsTextField.setText(persona.getApellidos());
				emailTextField.setText(persona.getEmail());
				telefonTextField.setText(persona.getTelefon());
			}else{ 
				//nou registre
				nomTextField.setText("");
				cognomsTextField.setText("");
				emailTextField.setText("");
				telefonTextField.setText("");
			}
		}
	}

	@FXML private void onActionGuardar(ActionEvent e) throws IOException {
		//verificar si les dades són vàlides
		if(isDatosValidos()){
			Persona persona = new Persona(Integer.parseInt(idTextField.getText()), nomTextField.getText(), cognomsTextField.getText(),
					emailTextField.getText(), telefonTextField.getText());

			persones.save(persona);
			limpiarFormulario();
			persones.showAll();
		}
	}

	@FXML private void onActionEliminar(ActionEvent e) throws IOException {

		if(isDatosValidos()){
			if(persones.delete(Integer.parseInt(idTextField.getText()))){
				limpiarFormulario();
				persones.showAll();
			}
		}
	}
	
	@FXML private void onActionSortir(ActionEvent e) throws IOException {

		sortir();

		ventana.close();
	}

	public void sortir(){
		persones.saveAll();
		persones.showAll();
	}
	
	private boolean isDatosValidos() {
		String error = "";

		if (nomTextField.getText() == null || nomTextField.getText().length() == 0) {
			error += texts.getString("error.incorrect_name") + "\n";
		}
		if (cognomsTextField.getText() == null || cognomsTextField.getText().length() == 0) {
			error += texts.getString("error.incorrect_surname") + "\n";
		}
		if (emailTextField.getText() == null || emailTextField.getText().length() == 0) {
			error += texts.getString("error.incorrect_email") + "\n";
		}

		if (telefonTextField.getText() == null || telefonTextField.getText().length() == 0) {
			error += texts.getString("error.incorrect_phone") + "\n";
		} else {
			try {
				Integer.parseInt(telefonTextField.getText());
			} catch (NumberFormatException e) {
				error += texts.getString("error.incorrect_phone_format") + "\n";
			}
		}

		if (error.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(ventana);
			alert.setTitle(texts.getString("error.title_incorrect"));
			alert.setHeaderText(texts.getString("error.header_text"));
			alert.setContentText(error);

			alert.showAndWait();

			return false;
		}
	}

	private void limpiarFormulario(){
		idTextField.setText("");
		nomTextField.setText("");
		cognomsTextField.setText("");
		emailTextField.setText("");
		telefonTextField.setText("");
	}
}
