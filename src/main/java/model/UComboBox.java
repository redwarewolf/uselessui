package main.java.model;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;

import javax.swing.JComboBox;

@SuppressWarnings("rawtypes")
public class UComboBox extends UComponent
{
	private JComboBox jcb;
	
	public UComboBox(Field relatedField, UForm uform)
	{
		super(relatedField, uform);
		jcb = new JComboBox();
		setComponent(jcb);
		
		jcb.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED && jcb.getItemCount() > 1) {
					notifyUpdate();
			    }
			}
			
		});
	}

	@Override
	public void clean()
	{
		((JComboBox) this.getComponent()).removeAllItems();
	}

	@Override
	public Object getData()
	{
		return jcb.getSelectedItem();
	}
}
