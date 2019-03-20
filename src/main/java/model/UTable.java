package main.java.model;

import java.lang.reflect.Field;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class UTable extends UComponent
{
	private JTable tabla;
	
	public UTable(Field relatedField, UForm uform){
		super(relatedField, uform);
				
		DefaultTableModel modelo = new DefaultTableModel();
		
		this.tabla = new JTable(modelo);
		this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setComponent(tabla);
		
		tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent e) {
	        	if (!e.getValueIsAdjusting())
	        		notifyUpdate();
	        }
	    });
		
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub				
		String[] ret = new String[tabla.getModel().getColumnCount()];
		for (int i = 0; i < tabla.getModel().getColumnCount(); i++)
			ret[i] = tabla.getValueAt(tabla.getSelectedRow(), i).toString();
			
		return ret;
	}

	@Override
	public void clean() {		
		DefaultTableModel modelo = new DefaultTableModel();
		this.tabla.setModel(modelo);

	}
}
