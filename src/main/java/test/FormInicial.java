package main.java.test;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;

import main.java.annotations.ClearButton;
import main.java.annotations.Control;
import main.java.annotations.OnBlur;
import main.java.annotations.OnFocus;
import main.java.annotations.OnModified;
import main.java.annotations.SubmitButton;
import main.java.core.UselessCore;
import main.java.model.UCheckBox;
import main.java.model.UComboBox;
import main.java.model.UForm;
import main.java.model.UList;
import main.java.model.UPasswordField;
import main.java.model.URadioButton;
import main.java.model.UTable;
import main.java.model.UTextField;

@SubmitButton(label = "Siguiente", nextForm = SegundoForm.class)
@ClearButton(label = "Limpiar")
//@BackButton(label = "Atrás")
public class FormInicial extends UForm {
	@Control(label = "Nombre de Usuario", type = UTextField.class)
	@OnModified(method = "onNombreModified")
	// @OnBlur(method = "onNombreSet")
	private String usuario;
	
	@Control(label = "Fecha", type = UTextField.class)
	@OnBlur(method = "fechaCapo")
	private String fecha;
	
	@Control(label = "Fecha", type = UTextField.class)
	private String fechaf;
	@Control(label = "Fecha", type = UTextField.class)
	private String fechad;
	@Control(label = "Fecha", type = UTextField.class)
	private String fechas;
	@Control(label = "Fecha", type = UTextField.class)
	private String fechaas;
	@Control(label = "Fecha", type = UTextField.class)
	private String fechaaa;
	@Control(label = "Fecha", type = UTextField.class)
	private String fechaa;
	
	@Control(label = "Nombre real", type = UTextField.class)
	@OnFocus(method = "focusName")
	private String realName;
	
	@Control(label = "Apellido", type = UTextField.class)
	private String apellido;

	@Control(label = "Password", type = UPasswordField.class)
	@OnModified(method = "PassChanged")
	private String password;

	@Control(label = "ComboBox", type = UComboBox.class)
	@OnModified(method = "sexoChanged")
	@OnFocus(method = "focusCombo")
	private String sexo;
	
	@Control(label = "Check1", type = UCheckBox.class)
	@OnModified(method = "onCheck1Modified")
	@OnFocus(method = "focusCHeck")
	private boolean check1 = true;
	
	@Control(label = "Check2", type = UCheckBox.class)
	@OnModified(method = "onCheck2Modified")
	@OnBlur(method = "check2Blur")
	private boolean check2 = false;
	
	@Control(label="Calate esta", type = URadioButton.class, options = {"Epa", "La", "Papa"})
	@OnModified(method = "onRadio")
	@OnFocus(method = "focusbtn")
	private String radioButtons;
	
	@Control(label="Seguro?", type = URadioButton.class, options = {"No", "Te", "Lo", "Creo", "Rafa"})
	@OnBlur(method = "focusbtn2")
	private String btns2;
	
	@Control(label="Calate esta LISTA", type = UList.class, options = {"hO","la"})
	@OnModified(method = "listitaMod")
	private String listita;
	
	@Control(label="Calate esta TABLA", type = UTable.class)
	@OnModified(method = "tablaMod")
	@OnBlur(method = "blurTabla")
	private String[] uTableTest;

	public FormInicial() {
		super("Demo UselessUI", 800, 600);
	}
		

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void onInit() {
		super.onInit();
		System.out.println("Ejecutando onInit de TestForm");
		JComboBox cmbx = (JComboBox) UselessCore.getInstance().getGlobalComponent("FormInicial","sexo");
		cmbx.addItem("Macho");
		cmbx.addItem("Hembra");
		cmbx.addItem("Indefinido");
		
		// popular lista
		JList list = (JList) UselessCore.getInstance().getGlobalComponent("FormInicial" ,"listita");
		((DefaultListModel) list.getModel()).addElement("Pikachu");
		((DefaultListModel) list.getModel()).addElement("Jigglypuff");
		((DefaultListModel) list.getModel()).addElement("Sznajdleder");
		
		JTable table = (JTable) UselessCore.getInstance().getGlobalComponent("FormInicial","uTableTest");
		((DefaultTableModel) table.getModel()).addColumn("Nombre");
		((DefaultTableModel) table.getModel()).addColumn("Apellido");
		((DefaultTableModel) table.getModel()).addColumn("DNI");
		((DefaultTableModel) table.getModel()).addRow(new String[]{"Pepe", "Perez", "1234"});
		((DefaultTableModel) table.getModel()).addRow(new String[]{"Pablo", "Gonzxalez", "5124"});
		((DefaultTableModel) table.getModel()).addRow(new String[]{"Axel", "Qaaasjh", "1564"});
	}

	@Override
	public void onClear() {
		super.onClear();
		System.out.println("Ejecutando onClear de TestForm");
	}

	@Override
	public boolean onSubmit() {
		System.out.println("Ejecutando onSubmit de TestForm");
		return super.onSubmit();
	}

	public void onRadio() {
		System.out.println("Se modificó el Radio!" + UselessCore.getInstance().getGlobalVar("FormInicial", "radioButtons"));
		System.out.println("Radio group: " + UselessCore.getInstance().getGlobalComponent("FormInicial","radioButtons"));
	}
	
	public void PassChanged() {
		System.out.println("Pass: " + UselessCore.getInstance().getGlobalVar("FormInicial", "password"));
		System.out.println("Pass Object: " + UselessCore.getInstance().getGlobalComponent("FormInicial","password"));
	}
	
	public void focusCHeck(){
		System.out.println("focuseando check");
	}
	
	public void focusName(){
		System.out.println("Focuseando nombre real");
	}
	
	public void focusbtn2(){
		
	System.out.println("Focus btn 2 !");	
	}
	
	public void onNombreModified() {
		// esto se ejecuta cada vez que se ingresa o saca un char del nombre
		System.out.println("Se modifica el Nombre: " + usuario + " Object: " + UselessCore.getInstance().getGlobalComponent("FormInicial","usuario"));
		System.out.println("Nombre: " + UselessCore.getInstance().getGlobalVar("FormInicial", "usuario"));
	}

	public void onNombreSet() {
		// mostrarCartel("Así que tu nombre es " + nombre + ". Ok!");
	}
	
	public void tablaMod(){
		System.out.println("Nuevo objeto " + ((String[]) UselessCore.getInstance().getGlobalVar("FormInicial","uTableTest"))[0]);
		System.out.println(UselessCore.getInstance().getGlobalComponent("FormInicial","uTableTest") + "UITABLETEST");
	}
	
	public void listitaMod() {
		System.out.println("Opa se modifico la listita: " + UselessCore.getInstance().getGlobalVar("FormInicial","listita"));
		System.out.println(UselessCore.getInstance().getGlobalComponent("FormInicial","listita"));
	}
	
	public void focusbtn()
	{
		System.out.println("se focuseoooo");
	}
	
	public void onCheck1Modified(){
		// esto se ejecuta cada vez que se cambia el valor del checkbox
		System.out.println("se presiona el check1 " + check1 + " " + UselessCore.getInstance().getGlobalComponent("FormInicial", "check1"));
		System.out.println("check1: " + UselessCore.getInstance().getGlobalVar("FormInicial", "check1"));
	}
	
	public void onCheck2Modified(){
		// esto se ejecuta cada vez que se cambia el valor del checkbox
		System.out.println("se presiona el check2" + " " + UselessCore.getInstance().getGlobalComponent("FormInicial", "check2"));
		System.out.println("check2: " + UselessCore.getInstance().getGlobalVar("FormInicial", "check2"));
	}
	
	public void fechaCapo(){
		System.out.println("blur de fecha!");
	}
	
	public void blurTabla(){
		System.out.println("Blur de talba");
	}
	
	public void check2Blur(){
		System.out.println("blur de check2");
	}
	
	public void focusCombo(){
		
		System.out.println("Focuseando al combo... dale que ganas la tf");
	}
	
	public void sexoChanged()
	{
		System.out.println("cambio tu sexo: " + UselessCore.getInstance().getGlobalVar("FormInicial", "sexo"));
		System.out.println("cambio tu sexo: " + UselessCore.getInstance().getGlobalComponent("FormInicial", "sexo"));
		JTable table = (JTable) UselessCore.getInstance().getGlobalComponent("FormInicial","uTableTest");
		((DefaultTableModel) table.getModel()).addRow(new String[]{"Pepe", "Perez", "1234"});
		((DefaultTableModel) table.getModel()).addRow(new String[]{"Pablo", "Gonzxalez", "5124"});
		((DefaultTableModel) table.getModel()).addRow(new String[]{"Axel", "Qaaasjh", "1564"});
	}

	public static void main(String[] args) {
		final UselessCore useless = UselessCore.getInstance();
		useless.registerForm(FormInicial.class);
		useless.registerForm(SegundoForm.class);
		useless.showForm(FormInicial.class);
	}
}
