package main.java.model;

import java.lang.reflect.Field;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UTextArea extends UComponent {

	private JTextArea jta;
	
	public UTextArea(String value, Field relatedField, UForm uform) {
		super(relatedField, uform);
		jta = new JTextArea(value);
		setComponent(jta);
		
		// Agregamos el notifyUpdate
		jta.getDocument().addDocumentListener(new DocumentListener() {	
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
		((JTextArea) this.getComponent()).setText("");
	}

	@Override
	public Object getData()
	{
		return jta.getText();
	}
}
