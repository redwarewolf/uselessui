package main.java.model;

import java.lang.reflect.Field;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UTextField extends UComponent
{
	private JTextField jtf;
	
	public UTextField(String value, Field relatedField, UForm uform)
	{
		super(relatedField, uform);
		jtf = new JTextField(value);
		setComponent(jtf);
		
		// Agregamos el notifyUpdate
		jtf.getDocument().addDocumentListener(new DocumentListener() {	
			@Override
			public void removeUpdate(DocumentEvent e) {
				notifyUpdate();
			}		
			@Override
			public void insertUpdate(DocumentEvent e) {
				notifyUpdate();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				notifyUpdate();
			}
		});
	}

	@Override
	public void clean()
	{
		((JTextField) this.getComponent()).setText("");
	}

	@Override
	public Object getData()
	{
		return jtf.getText();
	} 
}
