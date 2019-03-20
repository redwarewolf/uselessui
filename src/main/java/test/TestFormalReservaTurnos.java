package main.java.test;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.java.annotations.BackButton;
import main.java.annotations.ClearButton;
import main.java.annotations.Control;
import main.java.annotations.OnModified;
import main.java.annotations.SubmitButton;
import main.java.core.UselessCore;
import main.java.model.UCheckBox;
import main.java.model.UComboBox;
import main.java.model.UForm;
import main.java.model.URadioButton;
import main.java.model.UTable;
import main.java.model.UTextArea;
import main.java.model.UTextField;

@ClearButton(label = "Limpiar")
@BackButton(label = "Atras")
@SubmitButton(label = "Siguiente", nextForm = TestFormalFinalize.class)
public class TestFormalReservaTurnos extends UForm
{
	public static UselessCore uselessCore = UselessCore.getInstance();
	
	@Control(label = "Nombre", type = UTextField.class)
	private String nombre;
	
	@Control(label = "Apellido", type = UTextField.class)
	private String apellido;
	
	@Control(label = "Número de afiliado", type = UTextField.class)
	private String numAfiliado;
	
	@Control(label = "Tipo de doc.", type = UComboBox.class, options = {"DNI", "LC", "PP", "CI"})
	private String tipoDoc;
	
	@Control(label = "Número de documento", type = UTextField.class)
	private String numDoc;
	
	@Control(label = "Sexo", type = URadioButton.class, options = {"Masculino", "Femenino", "Indefinido"})
	private String sexo;
	
	@Control(label = "Cónyuge a cargo (esposa/esposo)", type = UCheckBox.class)
	private boolean conyugeACargo;
	
	@Control(label = "Hijos a cargo", type = URadioButton.class, options = {"Un hijo", "Dos hijos", "Tres o más hijos"})
	private String hijosACargo;
	
	@Control(label = "Tipo de doctor", type = UComboBox.class, options = {"Otorrino", "Pediatra", "Traumatólogo"})
	@OnModified(method = "onEspecialidadModified")
	private String especialidad;
	
	@Control(label = "Turnos disponibles", type = UTable.class)
	private String[] turno;
	
	@Control(label = "Consideraciones", type = UTextArea.class)
	private String consideraciones;
	
	public TestFormalReservaTurnos()
	{
		super("Sistema de Turnos", 1024, 768);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void onClear()
	{
		super.onClear();
		JComboBox jcmb = (JComboBox) uselessCore.getGlobalComponent("TestFormalReservaTurnos", "especialidad");
		jcmb.addItem("Otorrino");
		jcmb.addItem("Pediatra");
		jcmb.addItem("Traumatólogo");
		
		jcmb = (JComboBox) uselessCore.getGlobalComponent("TestFormalReservaTurnos", "tipoDoc");
		jcmb.addItem("DNI");
		jcmb.addItem("LC");
		jcmb.addItem("PP");
		jcmb.addItem("CI");
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onInit()
	{
		super.onInit();
		JComboBox jcmb = (JComboBox) uselessCore.getGlobalComponent("TestFormalReservaTurnos", "especialidad");
		jcmb.setSelectedIndex(-1);
	}
	
	public void onEspecialidadModified()
	{
		JTable tbl = (JTable) uselessCore.getGlobalComponent("TestFormalReservaTurnos", "turno");
		DefaultTableModel tblModel = new DefaultTableModel();
		tblModel.addColumn("Especialista");
		tblModel.addColumn("Fecha");
		
		switch (especialidad)
		{
			case "Otorrino":
				tblModel.addRow(new String[]{"Juan P. López", "17-04-2018"});
				tblModel.addRow(new String[]{"Erik Rodriguez", "18-04-2018"});
			break;
			case "Pediatra":
				tblModel.addRow(new String[]{"Lucas Romero", "17-03-2018"});
				tblModel.addRow(new String[]{"Javier Esteban Paz", "11-05-2018"});
				tblModel.addRow(new String[]{"Claudio Páez", "17-05-2018"});
				tblModel.addRow(new String[]{"Fernando Goncalves", "18-05-2018"});
			break;
			case "Traumatólogo":
				tblModel.addRow(new String[]{"Vladimir Putin", "11-02-2018"});
				tblModel.addRow(new String[]{"Gustavo Vera", "18-03-2018"});
				tblModel.addRow(new String[]{"Armando Casas", "17-05-2018"});
			break;
		}
		tbl.setModel(tblModel);
		
	}
	
	@Override
	public boolean onSubmit()
	{
		return true;
	}
}
