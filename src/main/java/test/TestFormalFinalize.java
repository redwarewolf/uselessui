package main.java.test;

import javax.swing.JOptionPane;

import main.java.annotations.BackButton;
import main.java.annotations.Control;
import main.java.annotations.SubmitButton;
import main.java.model.UCheckBox;
import main.java.model.UForm;
import main.java.model.UTextArea;

@BackButton(label = "Atras")
@SubmitButton(label = "Confirmar")
public class TestFormalFinalize extends UForm
{
	@Control(label = "Confirmar turno", type = UCheckBox.class)
	boolean confirmar;
	
	@Control(label = "Comentarios", type = UTextArea.class)
	String comentarios;
	public TestFormalFinalize()
	{
		super("Éxito!", 600, 400);
	}
	
	@Override
	public void onInit()
	{
		super.onInit();
	}
	
	@Override
	public boolean onSubmit()
	{
		JOptionPane.showMessageDialog(null, "Gracias por tu turno!", "Gracias", JOptionPane.INFORMATION_MESSAGE);
		return super.onSubmit();
	}
}
