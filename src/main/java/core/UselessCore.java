package main.java.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;

import main.java.annotations.BackButton;
import main.java.annotations.ClearButton;
import main.java.annotations.Control;
import main.java.annotations.OnBlur;
import main.java.annotations.OnFocus;
import main.java.annotations.OnModified;
import main.java.annotations.SubmitButton;
import main.java.exceptions.UselessException;
import main.java.model.UCheckBox;
import main.java.model.UComboBox;
import main.java.model.UComponent;
import main.java.model.UForm;
import main.java.model.UList;
import main.java.model.UPasswordField;
import main.java.model.URadioButton;
import main.java.model.UTable;
import main.java.model.UTextArea;
import main.java.model.UTextField;

@SuppressWarnings({"rawtypes", "unchecked"})
public class UselessCore
{
	private static UselessCore instance;

	private ArrayList<Class> registeredForms;
	private Stack<Class> formsStack;

	private HashMap<Class,UForm> instances;
	private UForm currentForm;

	private HashMap<String,HashMap<String,Field>> globalVars;
	private HashMap<String,HashMap<String,Object>> globalComponents;

	/* Singleton que inicializa el framework */
	public synchronized static UselessCore getInstance()
	{
		if(instance==null) instance=new UselessCore();
		return instance;
	}

	private UselessCore()
	{
		registeredForms=new ArrayList<Class>();
		formsStack=new Stack<Class>();
		globalVars=new HashMap<String,HashMap<String,Field>>();
		globalComponents = new HashMap<String,HashMap<String,Object>>();
		
		instances=new HashMap<Class,UForm>();
	}

	/* Registra el form. Valida que herede de UForm */
	public void registerForm(Class formKlass)
	{
		if(formKlass==null) throw new UselessException("ERR-001 - La clase invocada es nula");

		if(!UForm.class.isAssignableFrom(formKlass)) throw new UselessException("ERR-002 - Se intentó registrar un form inválido.");

		if(!registeredForms.contains(formKlass)) registeredForms.add(formKlass);
		else throw new UselessException("ERR-003 - Este form ya está registrado");
	}

	/* Se invoca para mostrar el primer Form */
	public void showForm(Class formKlass)
	{
		try
		{
			if(!formsStack.isEmpty()) throw new UselessException("ERR-004 - Ya hay un form corriendo");
			initForm(formKlass);
		}
		catch(InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|NoSuchFieldException e)
		{
			e.printStackTrace();
			throw new UselessException("ERR-999 - Error desconocido: "+e.getClass().getName()+": "+e.getMessage());
		}
	}

	private void destroyCurrentForm(Class formKlass)
	{
		currentForm.dispose();
		formsStack.pop();
		instances.remove(formKlass);
		globalVars.remove(formKlass.getSimpleName());
		globalComponents.remove(formKlass.getSimpleName());
		currentForm=instances.get(formsStack.peek());
		currentForm.setVisible(true);
	}

	/* Inicializa un form */
	private void initForm(Class formKlass)
			throws InstantiationException,IllegalAccessException,IllegalArgumentException,InvocationTargetException,NoSuchMethodException,SecurityException,NoSuchFieldException
	{
		if(!registeredForms.contains(formKlass)) throw new UselessException("ERR-008 - No está registrado ese form");

		if (currentForm != null)
			currentForm.setVisible(false);
		
		currentForm=((UForm)formKlass.newInstance());
		instances.put(formKlass,currentForm);
		formsStack.push(formKlass);
		currentForm.setVisible(true);

		HashMap<String,Field> localFields=new HashMap<String,Field>();
		HashMap<String,Object> localComponents = new HashMap<String,Object>();
		// Registro las variables al hashmap de globales:
		for(Field f:formKlass.getDeclaredFields())
		{
			if(!f.isAccessible()) f.setAccessible(true);
			localFields.put(f.getName(),f);

			// Recorro las annotations de ese Field
			for(Annotation a:f.getDeclaredAnnotations())
			{
				switch(a.annotationType().getSimpleName())
				{
					case "Control":
						Control ctl=(Control)f.getAnnotation(Control.class);
						Method mAdd=formKlass.getSuperclass().getDeclaredMethod("addUComponent",UComponent.class,String.class);
						mAdd.setAccessible(true);
						UComponent comp = null;

						switch(ctl.type().getSimpleName())
						{
							case "UTextField":
								comp=(UTextField)mAdd.invoke(currentForm,new UTextField((String)f.get(currentForm),f,currentForm),ctl.label());
								
								if(f.isAnnotationPresent(OnBlur.class))
								{

								}

								break;
							case "UTextArea":
								comp = (UTextArea) mAdd.invoke(currentForm,new UTextArea((String)f.get(currentForm),f,currentForm),ctl.label());
								break;
							case "UTable":
								comp = (UTable) mAdd.invoke(currentForm,new UTable(f, currentForm), ctl.label());
								break;
							case "URadioButton":
								mAdd=formKlass.getSuperclass().getDeclaredMethod("addURadioButtons",String[].class,String.class,Field.class,UForm.class);
								mAdd.setAccessible(true);
								if (ctl.options().length < 1)
									throw new UselessException("ERR 014 - Los URadioButton deben tener al menos una opción!");
								ArrayList<URadioButton> btns = (ArrayList<URadioButton>) mAdd.invoke(currentForm, ctl.options(), ctl.label(),f,currentForm);
								localComponents.put(f.getName(),btns.get(0).getButtons());
								if(f.isAnnotationPresent(OnModified.class))
								{
									OnModified onMod=(OnModified)f.getAnnotation(OnModified.class);

									Method mToMod;

									Class<? extends Annotation> type=onMod.annotationType();

									mToMod=type.getDeclaredMethod("method");

									if(mToMod!=null)
									{
										Object value=mToMod.invoke(onMod,(Object[])null);

										String mOnModName=value.toString();
										Method mOnMod=formKlass.getDeclaredMethod(mOnModName);
										if(mOnMod!=null)
										{
											mOnMod.setAccessible(true);
											for (URadioButton btn : btns)
												btn.addChangeListener(mOnMod);
										}
										else
										{
											throw new UselessException("ERR-011' - No existe el método "+mOnModName);
										}
									}
									else
									{
										throw new UselessException("ERR-010' - No se definió el attribute 'method' de la clase OnModified");
									}
								}
								if(f.isAnnotationPresent(OnFocus.class))
								{
									OnFocus onFoc=(OnFocus)f.getAnnotation(OnFocus.class);

									Method mToFoc;

									Class<? extends Annotation> type=onFoc.annotationType();

									mToFoc=type.getDeclaredMethod("method");

									if(mToFoc!=null)
									{
										Object value=mToFoc.invoke(onFoc,(Object[])null);
										String mOnFocName=value.toString();
										Method mOnFoc=formKlass.getDeclaredMethod(mOnFocName);
										if(mOnFoc!=null)
										{
											mOnFoc.setAccessible(true);
											for (URadioButton btn : btns)
												btn.addFocusListener(mOnFoc);
										}
										else
										{
											throw new UselessException("ERR-011' - No existe el método "+mOnFocName);
										}
									}
									else
									{
										throw new UselessException("ERR-010' - No se definió el attribute 'method' de la clase OnModified");
									}
								}
								if(f.isAnnotationPresent(OnBlur.class))
								{
									OnBlur onBlu=(OnBlur)f.getAnnotation(OnBlur.class);

									Method mToBlu;

									Class<? extends Annotation> type=onBlu.annotationType();

									mToBlu=type.getDeclaredMethod("method");

									if(mToBlu!=null)
									{
										Object value=mToBlu.invoke(onBlu,(Object[])null);
										String mOnBluName=value.toString();
										Method mOnBlu=formKlass.getDeclaredMethod(mOnBluName);
										if(mOnBlu!=null)
										{
											mOnBlu.setAccessible(true);
											for (URadioButton btn : btns)
												btn.addBlurListener(mOnBlu);
										}
										else
										{
											throw new UselessException("ERR-011' - No existe el método "+mOnBluName);
										}
									}
									else
									{
										throw new UselessException("ERR-010' - No se definió el attribute 'method' de la clase OnModified");
									}
								}
								break;
							case "UList":
								comp = (UList) mAdd.invoke(currentForm, new UList(f, currentForm), ctl.label());
								if (ctl.options().length > 0)
								{
									for (String opt : ctl.options())
									{
										JList jlist = (JList) comp.getComponent();
										((DefaultListModel) jlist.getModel()).addElement(opt);
									}
								}
								break;
							case "UCheckBox":
								comp =(UCheckBox)mAdd.invoke(currentForm,new UCheckBox(ctl.label(),(Boolean)f.get(currentForm),f,currentForm),ctl.label());
								break;
							case "UPasswordField":
								comp = (UPasswordField) mAdd.invoke(currentForm,new UPasswordField((String)f.get(currentForm),f,currentForm),ctl.label());
								break;
							case "UComboBox":
								comp = (UComboBox) mAdd.invoke(currentForm,new UComboBox(f,currentForm),ctl.label());
								if (ctl.options().length > 0)
								{
									for (String opt : ctl.options())
									{
										JComboBox jcombo = (JComboBox) comp.getComponent();
										jcombo.addItem(opt);
									}
								}
								break;
							default:
								throw new UselessException("ERR-009 - Clase UComponent no admitida");
						}
						if (comp != null)
						{
							localComponents.put(f.getName(),comp.getComponent());
							checkOnModified(comp,f,formKlass);
							checkOnFocus(comp, f, formKlass);
							checkOnBlur(comp, f, formKlass);
						}
						break;
				}
			}
		}
		globalVars.put(currentForm.getClass().getSimpleName(),localFields);
		globalComponents.put(currentForm.getClass().getSimpleName(),localComponents);

		// Ejecutamos el onInit
		try
		{
			formKlass.getDeclaredMethod("onInit").invoke(currentForm);
		}
		catch(NoSuchMethodException e) // esto quiere decir que no está
										// definido. vamos al padre
		{
			formKlass.getSuperclass().getDeclaredMethod("onInit").invoke(currentForm);
		}

		// Validamos que se haya inicializado correctamente
		Field initializedField=formKlass.getSuperclass().getDeclaredField("initialized");
		initializedField.setAccessible(true);
		boolean initializedSuccessfully=(boolean)initializedField.get(currentForm);
		if(!initializedSuccessfully) throw new UselessException("ERR-007 - El formulario debe invocar al método onInit de su superclase (super.onInit())");

		// Definimos el metodo que vamos a utilizar para los SubmitButtons
		Method submitMethod=formKlass.getSuperclass().getDeclaredMethod("enableButton",int.class,String.class);
		submitMethod.setAccessible(true);

		// Analizamos si existe la anotación de SubmitButton
		if(formKlass.isAnnotationPresent(SubmitButton.class))
		{
			SubmitButton submitButtonAnnotation=(SubmitButton)formKlass.getAnnotation(SubmitButton.class);
			submitMethod.invoke(currentForm,UForm.SUBMIT_BUTTON,submitButtonAnnotation.label());

			Field submitButton=formKlass.getSuperclass().getDeclaredField("btnSubmit");
			submitButton.setAccessible(true);

			// Revisamos la proxima clase.
			Class nextFormKlass=(Class)submitButtonAnnotation.nextForm();
			if(nextFormKlass!=void.class)
			{
				Field hasNext=formKlass.getSuperclass().getDeclaredField("hasNextForm");
				hasNext.setAccessible(true);
				hasNext.set(currentForm,true);
			}

			// Invoca al onSubmit
			JButton btn=(JButton)submitButton.get(currentForm);
			btn.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent a)
				{
					try
					{
						boolean submitted=(boolean)formKlass.getDeclaredMethod("onSubmit").invoke(currentForm);
						if(submitted)
						{
							if(nextFormKlass!=void.class) initForm(nextFormKlass);
						}
					}
					catch(NoSuchMethodException e) // esto quiere decir que no
													// está definido. vamos al
													// padre
					{
						try
						{
							Method m=formKlass.getSuperclass().getDeclaredMethod("onSubmit");
							m.setAccessible(true);
							m.invoke(currentForm);
							if(nextFormKlass!=void.class) initForm(nextFormKlass);
						}
						catch(Exception e2)
						{
							e2.printStackTrace(); // Esto no debería pasar nunca
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						throw new UselessException("ERR-997 - Error desconocido en Submit: "+e.getClass().getName()+": "+e.getMessage());
					}
				}
			});
		}

		// Analizamos si existe la anotación de ClearButton
		if(formKlass.isAnnotationPresent(ClearButton.class))
		{
			ClearButton clearButtonAnnotation=(ClearButton)formKlass.getAnnotation(ClearButton.class);
			submitMethod.invoke(currentForm,UForm.CLEAR_BUTTON,clearButtonAnnotation.label());

			Field clearButton=formKlass.getSuperclass().getDeclaredField("btnClear");
			clearButton.setAccessible(true);

			// Invoca al onClear
			JButton btn=(JButton)clearButton.get(currentForm);
			btn.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent a)
				{
					try
					{
						formKlass.getDeclaredMethod("onClear").invoke(currentForm);
					}
					catch(NoSuchMethodException e) // esto quiere decir que no
													// está definido. vamos al
													// padre
					{
						try
						{
							Method m=formKlass.getSuperclass().getDeclaredMethod("onClear");
							m.setAccessible(true);
							m.invoke(currentForm);
						}
						catch(Exception e2)
						{
							e2.printStackTrace(); // Esto no debería pasar nunca
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						throw new UselessException("ERR-998 - Error desconocido en Clear: "+e.getClass().getName()+": "+e.getMessage());
					}
				}
			});
		}

		// Analizamos si existe la anotación de BackButton
		if(formKlass.isAnnotationPresent(BackButton.class))
		{
			BackButton backButtonAnnotation=(BackButton)formKlass.getAnnotation(BackButton.class);
			submitMethod.invoke(currentForm,UForm.BACK_BUTTON,backButtonAnnotation.label());

			Field backButton=formKlass.getSuperclass().getDeclaredField("btnBack");
			backButton.setAccessible(true);

			JButton btn=((JButton)backButton.get(currentForm));
			// Y si el stack está con 1 elemento o menos, deshabilito el botón
			// de Atrás
			if(formsStack.size()<=1)
			{
				btn.setEnabled(false);
			}
			else
			{
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent a)
					{
						try
						{
							destroyCurrentForm(formKlass);
						}
						catch(Exception e)
						{
							e.printStackTrace();
							throw new UselessException("ERR-996 - Error desconocido en Back: "+e.getClass().getName()+": "+e.getMessage());
						}
					}
				});
			}
		}
	}

	/* Permite trabajar con las variables de cada clase */
	public Object getGlobalVar(String formClassName, String property)
	{
		if(!globalVars.containsKey(formClassName)) throw new UselessException("ERR-005 - No existe una clase con ese nombre");

		HashMap<String,Field> classFields=globalVars.get(formClassName);

		if(!classFields.containsKey(property)) throw new UselessException("ERR-006 - No existe una variable/propiedad con ese nombre");

		Field requiredField=classFields.get(property);

		if(!requiredField.isAccessible()) requiredField.setAccessible(true); // Lo
																				// forzamos

		Class klazz=getClassFromName(formClassName);
		if(klazz==null) throw new UselessException("ERR-009 - No hay una instancia del form corriendo!");

		Object returnValue=null;

		try
		{
			returnValue=requiredField.get(instances.get(klazz));
		}
		catch(Exception e)
		{
			throw new UselessException("ERR-010 - Error al recuperar la variable "+e.getMessage());
		}
		return returnValue;
	}
	
	public Object getGlobalComponent(String formClassName, String property)
	{
		if(!globalComponents.containsKey(formClassName)) throw new UselessException("ERR-005 - No existe una clase con ese nombre");

		HashMap<String,Object> classComponents=globalComponents.get(formClassName);
		
		if(!classComponents.containsKey(property)) throw new UselessException("ERR-006 - No existe una variable/propiedad con ese nombre");

		Object requiredComponent = classComponents.get(property);

		return requiredComponent;
	}

	private Class getClassFromName(String className)
	{
		// Todo este quilombo es para obtener la key del hashmap..
		Iterator<?> it=instances.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry pair=(Map.Entry)it.next();
			Class klazz=(Class)pair.getKey();
			if(className.equals(klazz.getSimpleName())) return klazz;
		}
		return null;
	}
	
	private void checkOnBlur(UComponent comp, Field f, Class formKlass) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if(f.isAnnotationPresent(OnBlur.class))
		{
			OnBlur onBlu=(OnBlur)f.getAnnotation(OnBlur.class);

			Method mToBlu;

			Class<? extends Annotation> type=onBlu.annotationType();

			mToBlu=type.getDeclaredMethod("method");

			if(mToBlu!=null)
			{
				Object value=mToBlu.invoke(onBlu,(Object[])null);
				String mOnBluName=value.toString();
				Method mOnBlu=formKlass.getDeclaredMethod(mOnBluName);
				if(mOnBlu!=null)
				{
					mOnBlu.setAccessible(true);
					comp.addBlurListener(mOnBlu);
				}
				else
				{
					throw new UselessException("ERR-011' - No existe el método "+mOnBluName);
				}
			}
			else
			{
				throw new UselessException("ERR-010' - No se definió el attribute 'method' de la clase OnModified");
			}
		}

	}
	
	private void checkOnFocus(UComponent comp, Field f, Class formKlass) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if(f.isAnnotationPresent(OnFocus.class))
		{
			OnFocus onFoc=(OnFocus)f.getAnnotation(OnFocus.class);

			Method mToFoc;

			Class<? extends Annotation> type=onFoc.annotationType();

			mToFoc=type.getDeclaredMethod("method");

			if(mToFoc!=null)
			{
				Object value=mToFoc.invoke(onFoc,(Object[])null);
				String mOnFocName=value.toString();
				Method mOnFoc=formKlass.getDeclaredMethod(mOnFocName);
				if(mOnFoc!=null)
				{
					mOnFoc.setAccessible(true);
					comp.addFocusListener(mOnFoc);
				}
				else
				{
					throw new UselessException("ERR-011' - No existe el método "+mOnFocName);
				}
			}
			else
			{
				throw new UselessException("ERR-010' - No se definió el attribute 'method' de la clase OnModified");
			}
		}

	}
	
	private void checkOnModified(UComponent cmp, Field f, Class formKlass) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		if(f.isAnnotationPresent(OnModified.class))
		{
			OnModified onMod=(OnModified)f.getAnnotation(OnModified.class);

			Method mToMod;

			Class<? extends Annotation> type=onMod.annotationType();

			mToMod=type.getDeclaredMethod("method");

			if(mToMod!=null)
			{
				Object value=mToMod.invoke(onMod,(Object[])null);
				String mOnModName=value.toString();
				Method mOnMod=formKlass.getDeclaredMethod(mOnModName);
				if(mOnMod!=null)
				{
					mOnMod.setAccessible(true);
					cmp.addChangeListener(mOnMod);
				}
				else
				{
					throw new UselessException("ERR-011' - No existe el método "+mOnModName);
				}
			}
			else
			{
				throw new UselessException("ERR-010' - No se definió el attribute 'method' de la clase OnModified");
			}
		}
	}
}
