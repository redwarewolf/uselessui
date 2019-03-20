package main.java.model;


import java.lang.reflect.Field;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
	
public class UList<T> extends UComponent
{
	private JList<String> lista;
	private DefaultListModel<String> listmodel = new DefaultListModel<String>();
	
	public UList(Field relatedField, UForm uform)
	{
		super(relatedField, uform);
				
		this.lista  = new JList<String>(listmodel);
			
		JScrollPane scroll = new JScrollPane(lista);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setMinimumSize(this.lista.getMinimumSize());
		lista.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		setComponent(lista);
		
		lista.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
					notifyUpdate();
			}
		});
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return lista.getSelectedValue();
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		DefaultListModel<String> listmodel = new DefaultListModel<String>();
		this.lista.setModel(listmodel);
	}
}
