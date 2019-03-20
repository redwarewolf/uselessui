package main.java.test;

import main.java.annotations.BackButton;
import main.java.annotations.ClearButton;
import main.java.annotations.Control;
import main.java.annotations.OnModified;
import main.java.annotations.SubmitButton;
import main.java.core.UselessCore;
import main.java.model.UComboBox;
import main.java.model.UForm;
import main.java.model.UTextArea;
import main.java.model.UTextField;

@SubmitButton(label = "Enviar")
@ClearButton(label = "Limpiar")
@BackButton(label = "Atrás")
public class SegundoForm extends UForm
{	
	@Control(label = "Nombre", type = UTextField.class)
	//@OnModified(method = "onNombreModified")
	//@OnBlur(method = "onNombreSet")
	private String nombre;

	@Control(label = "Apellido", type = UTextField.class)
	//@OnFocus(method = "validateNombre")
	//@OnModified(method = "onApellidoChanged")
	private String apellido;
	
	@Control(label = "Hobbies", type = UTextArea.class)
	@OnModified(method = "onHobbiesChanged")
	private String hobbies;
	
	@Control(label = "Artista favorito", type = UComboBox.class)
	private String[] artistas = new String[]{"Freddie King", "Howlin' Wolf", "B.B. King", "Chano", "El Indio Salario"};
	
	public SegundoForm()
	{
		super("Demo UselessUI", 800, 600);
		nombre = "Pablo";
		apellido = "Sznajdleder";
		hobbies = "Ingrese sus hobbies";
	}
	
	@Override
	public void onInit()
	{
		super.onInit();
		System.out.println("Ejecutando onInit de AnotherTestForm");
	}

	@Override
	public void onClear()
	{
		super.onClear();
		System.out.println("Ejecutando onClear de AnotherTestForm");
	}

	@Override
	public boolean onSubmit()
	{
		System.out.println("Ejecutando onSubmit de AnotherTestForm");
		return super.onSubmit();
	}

	public void onNombreModified()
	{
		// esto se ejecuta cada vez que se ingresa o saca un char del nombre
	}

	public void onNombreSet()
	{
		//mostrarCartel("Así que tu nombre es " + nombre + ". Ok!");
	}

	public void validateNombre()
	{
			//if (nombre.equals(""))
			//	mostrarError("Cuidado, tenés el nombre vacío!");
	}

	public void onHobbiesChanged()
	{
		System.out.println("Cambiando hobby a " + hobbies + UselessCore.getInstance().getGlobalComponent("SegundoForm","hobbies"));
		System.out.println("HOBBIES: " + UselessCore.getInstance().getGlobalVar("SegundoForm", "hobbies"));
	}
	

}
