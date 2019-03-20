package main.java.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import main.java.utils.ScreenUtils;

public class UForm{
	public static final int BACK_BUTTON = 0;
	public static final int CLEAR_BUTTON = 1;
	public static final int SUBMIT_BUTTON = 2;
	
	private JFrame frame;
	
	private double width, height;

	private JPanel contentPane;

	@SuppressWarnings("unused")
	private boolean initialized = false;
	private boolean hasNextForm = false;

	private JButton btnBack;
	private JButton btnClear;
	private JButton btnSubmit;

	private JPanel contentPanel;

	private ArrayList<UComponent> components;

	private GridBagConstraints gbc = new GridBagConstraints();

	public UForm(String title, double width, double height) {
		frame = new JFrame();
		contentPane = new JPanel();
		frame.setBounds(ScreenUtils.getCenteredShape(width, height));
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		frame.setLayout(new GridBagLayout());
		contentPanel = new JPanel(new GridBagLayout());
		components = new ArrayList<UComponent>();
		this.width = width;
		this.height = height;
		buildInterface();
	}

	public UForm(String title) {
		this(title, 1280, 720);
	}

	public void setVisible(boolean value) {
		frame.setVisible(value);
	}

	public UComponent getComponent() {
		// TODO: agregar la logica para obtener los components.
		return null;
	}

	private void buildInterface() {
		// Acá se hace la parte de los Buttons. Por defecto invisibles.
		btnBack = new JButton();
		btnBack.setPreferredSize(new Dimension(100, 30));
		btnClear = new JButton();
		btnClear.setPreferredSize(new Dimension(100, 30));
		btnSubmit = new JButton();
		btnSubmit.setPreferredSize(new Dimension(100, 30));

		// Se hacen invisibles por defecto
		btnBack.setVisible(false);
		btnClear.setVisible(false);
		btnSubmit.setVisible(false);

		// Creo un gridlayout para tener los tres botones
		// También creo un JPanel que contendrá el grid
		JPanel panelBtnLayout = new JPanel(new GridLayout(1, 3));

		// Y un panel por cada botón, para quedar centrado
		JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel clearPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		// Se hacen los respectivos add.
		backPanel.add(btnBack);
		clearPanel.add(btnClear);
		submitPanel.add(btnSubmit);

		panelBtnLayout.add(backPanel);
		panelBtnLayout.add(clearPanel);
		panelBtnLayout.add(submitPanel);

		// Tema de constraints para el GridBagLayout
		GridBagConstraints gbcBtn = new GridBagConstraints();

		gbcBtn.gridy = 0;
		gbcBtn.weighty = 0.9;
		gbcBtn.fill = GridBagConstraints.HORIZONTAL;

		JScrollPane scp = new JScrollPane(contentPanel);
		scp.setBorder(BorderFactory.createEmptyBorder());
		scp.setMinimumSize(new Dimension((int) width, (int) (height - (height/6))));
		scp.setPreferredSize(new Dimension((int) width, (int) (height - (height/6))));
		scp.setSize((int) width, (int) height); 
		frame.add(scp, gbcBtn);

		gbcBtn.anchor = GridBagConstraints.PAGE_END;
		gbcBtn.gridy = 1;
		gbcBtn.weighty = 0.1;
		gbcBtn.weightx = 1;

		// Add final
		frame.add(panelBtnLayout, gbcBtn);
	}

	protected void enableButton(int type, String label) {
		JButton button = null;
		switch (type) {
		case BACK_BUTTON:
			button = btnBack;
			break;
		case CLEAR_BUTTON:
			button = btnClear;
			break;
		case SUBMIT_BUTTON:
			button = btnSubmit;
			break;
		}
		button.setText(label);
		button.setVisible(true);
	}
	
	protected ArrayList<URadioButton> addURadioButtons(String[] radioButtons, String label, Field relatedField, UForm uform){
		
		ArrayList<URadioButton> buttons = new ArrayList<URadioButton>();
		ButtonGroup group = new ButtonGroup();
	   
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.ipady = 20;

		gbc.anchor = (gbc.gridx == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;

		gbc.weightx = 0.1;
		
		JLabel lbl = new JLabel(label);
		
		contentPanel.add(lbl, gbc);
		JPanel radioPanel = new JPanel();
		for(int i = 0; i < radioButtons.length; i++){
			URadioButton newRadioButton = new URadioButton(radioButtons[i], group, relatedField, uform);
			
			
			radioPanel.add(newRadioButton.getComponent(), gbc);
			
			components.add(newRadioButton);
			group.add((AbstractButton)newRadioButton.getComponent());
			buttons.add(newRadioButton);
		}
		gbc.gridx++;
		
		contentPanel.add(radioPanel, gbc);

		gbc.weightx = 1;
		gbc.gridx++;
		gbc.ipady = 0;
		
		return buttons;

	}
	
	protected UComponent addUComponent(UComponent uComponent,String label){
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.ipady = 20;

		gbc.anchor = (gbc.gridx == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;

		gbc.weightx = 0.1;
		
		JLabel lbl = new JLabel(label);
		
		contentPanel.add(lbl, gbc);

		gbc.weightx = 1;
		gbc.gridx++;
		gbc.ipady = 0;
		
		if (uComponent.getComponent() instanceof JTable){
			JScrollPane scroll = new JScrollPane(uComponent.getComponent());
			scroll.setMinimumSize(uComponent.getComponent().getMinimumSize());
			scroll.setSize(uComponent.getComponent().getSize());
			contentPanel.add(scroll, gbc);
		}
		else
			contentPanel.add(uComponent.getComponent(), gbc);

		components.add(uComponent);
		
		return uComponent;		
	}

	// Se invoca al iniciar el form
	protected void onInit() {
		initialized = true;
		System.out.println("Ejecutando onInit de superclase");
	}

	// Se invoca al limpiar el form
	protected void onClear() {
		System.out.println("Ejecutando onClear de superclase");
		for (UComponent c : this.components)
		{
			c.clean();
		}
	}

	// Se invoca al hacer el submit
	protected boolean onSubmit() {
		System.out.println("Ejecutando onSubmit de superclase");
		if (hasNextForm) {
			this.setVisible(false);
		}
		return hasNextForm;
	}

	public void dispose() {
		frame.dispose();
	}

	public JFrame getFrame() {
		return frame;
	}
}
