package main.java.model;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

import main.java.exceptions.UselessException;

public abstract class UComponent
{
	private JComponent component;
	private Method meth;
	private UForm uform;
	private Field relatedField;
	
	public UComponent(Field relatedField, UForm uform)
	{
		this.relatedField = relatedField;
		this.uform = uform;
	}
	
	public JComponent getComponent()
	{
		return component;
	}

	public void setComponent(JComponent component)
	{
		this.component = component;
	}
	
	public Method getMeth()
	{
		return meth;
	}

	public void setMeth(Method meth)
	{
		this.meth=meth;
	}

	public UForm getUform()
	{
		return uform;
	}

	public void setUform(UForm uform)
	{
		this.uform=uform;
	}

	public Field getRelatedField()
	{
		return relatedField;
	}

	public void setRelatedField(Field relatedField)
	{
		this.relatedField=relatedField;
	}
	
	public void addChangeListener(Method method){
		meth = method;
	}
	
	public void addFocusListener(Method method){
		UComponent thiz = this;
		this.getComponent().addFocusListener(new FocusListener()
		{
			
			@Override
			public void focusLost(FocusEvent e)
			{
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				if (method != null)
				try 
				{
					if (thiz instanceof URadioButton){
						URadioButton thizz = (URadioButton) thiz;
						ButtonGroup buttons = thizz.getButtons();
						if (e.getOppositeComponent() instanceof JRadioButton){
							Enumeration<AbstractButton> btns = buttons.getElements();
							while (btns.hasMoreElements()){
								AbstractButton btn = btns.nextElement();
								if (btn == e.getOppositeComponent())
									return;
							}
						}
						method.invoke(uform);
					}
					else
						method.invoke(uform);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) 
				{
					throw new UselessException("ERR-012 - Error al invokar el método " + method.getName());
				}
			}
		});
	}
	
	public void addBlurListener(Method method){
		UComponent thiz = this;
		this.getComponent().addFocusListener(new FocusListener()
		{
			
			@Override
			public void focusLost(FocusEvent e)
			{
				if (method != null)
					try 
					{
						if (thiz instanceof URadioButton){
							URadioButton thizz = (URadioButton) thiz;
							ButtonGroup buttons = thizz.getButtons();
							if (e.getOppositeComponent() instanceof JRadioButton){
								Enumeration<AbstractButton> btns = buttons.getElements();
								while (btns.hasMoreElements()){
									AbstractButton btn = btns.nextElement();
									if (btn == e.getOppositeComponent())
										return;
								}
							}
							method.invoke(uform);
						}
						else
							method.invoke(uform);
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) 
					{
						throw new UselessException("ERR-012 - Error al invokar el método " + method.getName());
					}
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
			}
		});
	}
	
	public void notifyUpdate()
	{
		try
		{
			if (!relatedField.isAccessible())
				relatedField.setAccessible(true);
			relatedField.set(uform, getData());
			if (meth != null)
			{
				try
				{
					meth.invoke(uform);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
				{
					e.printStackTrace();
					throw new UselessException("ERR-012 - Error al invokar el método " + meth.getName());
				}
			}
		}
		catch (Exception e)
		{
			throw new UselessException("ERR-013 - No se pudo cambiar un campo de clase " + e.getMessage());
		}
	}
		
	public abstract Object getData();
	
	public abstract void clean();
}
