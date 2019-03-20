package main.java.test;

import javax.swing.JOptionPane;

import main.java.annotations.Control;
import main.java.annotations.SubmitButton;
import main.java.core.UselessCore;
import main.java.model.UForm;
import main.java.model.UPasswordField;
import main.java.model.UTextField;

@SubmitButton(label = "Login", nextForm = TestFormalReservaTurnos.class)
public class TestFormalLogin extends UForm
{
	public static final UselessCore uselessCore = UselessCore.getInstance();

	@Control(label = "Usuario", type = UTextField.class)
	private String usuario;
	
	@Control(label = "Contraseña", type = UPasswordField.class)
	private String password;
	
	public TestFormalLogin()
	{
		super("Login - Por favor inicie sesión en el sistema", 400, 400);
	}
	
	@Override
	public void onInit()
	{
		super.onInit();
	}
	
	@Override
	public boolean onSubmit()
	{
		if ("useless".equals(usuario) && "algoritmos2".equals(password))
			return super.onSubmit();
		JOptionPane.showMessageDialog(null , "Error de credenciales!", "ERROR", JOptionPane.ERROR_MESSAGE);
		return false;
	}
	
	
	public static void main(String[] args)
	{
		uselessCore.registerForm(TestFormalLogin.class);
		uselessCore.registerForm(TestFormalReservaTurnos.class);
		uselessCore.registerForm(TestFormalFinalize.class);
		uselessCore.showForm(TestFormalLogin.class);
	}

}
