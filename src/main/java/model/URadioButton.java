package main.java.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class URadioButton extends UComponent
{
	private ButtonGroup buttons;

	private JRadioButton jrb;
	
	public URadioButton(String value, ButtonGroup buttons, Field relatedField, UForm uform)
	{
		super(relatedField, uform);
		
		jrb = new JRadioButton(value);
		setComponent(jrb);
		this.buttons = buttons;
		
		jrb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyUpdate();
			}
		});
	}

	public ButtonGroup getButtons()
	{
		return buttons;
	}
	
	@Override
	public void clean()
	{
		this.buttons.clearSelection();
		((JRadioButton) this.getComponent()).setSelected(false);
	}

	@Override
	public Object getData()
	{
		Enumeration<AbstractButton> en = buttons.getElements();
		while (en.hasMoreElements())
		{
			AbstractButton btn = en.nextElement();
			if (btn.isSelected())
				return btn.getText();
		}
		return "";
	}
}
