package main.java.model;

import java.lang.reflect.Field;

import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UPasswordField extends UComponent
{
	private JPasswordField jpf;
	
	public UPasswordField(String value, Field relatedField, UForm uform)
	{
		super(relatedField, uform);
		jpf = new JPasswordField(value);
		setComponent(jpf);
		
		jpf.getDocument().addDocumentListener(new DocumentListener() {	
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
		((JPasswordField) this.getComponent()).setText("");		
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object getData()
	{
		return jpf.getText();
	}
}
