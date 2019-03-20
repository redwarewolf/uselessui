package main.java.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JCheckBox;

public class UCheckBox extends UComponent
{
	private JCheckBox jcb;
	
	public UCheckBox(String label, boolean defaultValue, Field relatedField, UForm uform)
	{
		super(relatedField, uform);
		jcb = new JCheckBox("", defaultValue);
		setComponent(jcb);
		
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyUpdate();
			}
		});
	}

	@Override
	public void clean()
	{
		((JCheckBox) this.getComponent()).setSelected(false);
	}

	@Override
	public Object getData()
	{
		return jcb.isSelected();
	}
}
