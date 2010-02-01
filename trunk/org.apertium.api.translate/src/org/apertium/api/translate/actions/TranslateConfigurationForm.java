package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;

import javax.swing.*;

import java.awt.Dimension;
import java.util.*;

public class TranslateConfigurationForm {
	private Box rootComponent = null;
	
	private Services services = null;
	private JComboBox serviceComboBox = null;

	private JComboBox srcComboBox = null;
	private JComboBox destComboBox = null;

	private JTextField urlTextField = null;

	public TranslateConfigurationForm() {
		System.out.println("TranslateConfigurationForm()");
		
		services = new Services();
		
		rootComponent = Box.createHorizontalBox();
		
		Box mainComponentSx = Box.createVerticalBox();
		Box mainComponentDx = Box.createVerticalBox();
		
		System.out.println("TranslateConfigurationForm() 2");
		
		serviceComboBox = new JComboBox();
		serviceComboBox.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Service type:"));
		mainComponentDx.add(serviceComboBox);
		
		System.out.println("TranslateConfigurationForm() 3");
		
		mainComponentSx.add(Box.createHorizontalStrut(5));
		mainComponentDx.add(Box.createHorizontalStrut(5));
		
		urlTextField = new JTextField();
		urlTextField.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Service URL:"));
		mainComponentDx.add(urlTextField);
		
		System.out.println("TranslateConfigurationForm() 4");
		
		mainComponentSx.add(Box.createHorizontalStrut(5));
		mainComponentDx.add(Box.createHorizontalStrut(5));
		
		srcComboBox = new JComboBox();
		srcComboBox.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Source language:"));
		mainComponentDx.add(srcComboBox);
		
		mainComponentSx.add(Box.createHorizontalStrut(5));
		mainComponentDx.add(Box.createHorizontalStrut(5));
		
		destComboBox = new JComboBox();
		destComboBox.setMaximumSize(new Dimension(250, 20));
		
		mainComponentSx.add(new JLabel("Destination language:"));
		mainComponentDx.add(destComboBox);
		
		System.out.println("TranslateConfigurationForm() 5");
		
		rootComponent.add(mainComponentSx);
		rootComponent.add(Box.createHorizontalStrut(10));
		rootComponent.add(mainComponentDx);
		
		System.out.println("TranslateConfigurationForm() 6");
		
		ISO639 iso = new ISO639();
		
		System.out.println("TranslateConfigurationForm() 6.1");
		
		serviceComboBox.removeAllItems();
		
		System.out.println("TranslateConfigurationForm() 6.2");
		
		serviceComboBox.setModel(createServiceModel(services));
		
		System.out.println("TranslateConfigurationForm() 6.3");
		
		serviceComboBox.setRenderer(new EntryRenderer());
		
		System.out.println("TranslateConfigurationForm() 7");
		
		srcComboBox.removeAllItems();
		srcComboBox.setModel(createLanguageModel(iso));
		srcComboBox.setRenderer(new EntryRenderer());

		if (srcComboBox.getModel().getSize() > 0) {
			srcComboBox.setSelectedIndex(0);
		}
		
		System.out.println("TranslateConfigurationForm() 8");
		
		destComboBox.removeAllItems();
		destComboBox.setModel(createLanguageModel(iso));
		destComboBox.setRenderer(new EntryRenderer());

		if (destComboBox.getModel().getSize() > 0) {
			destComboBox.setSelectedIndex(0);
		}
		
		System.out.println("TranslateConfigurationForm() 9");
	}

	public JComboBox getSrcComboBox() {
		return srcComboBox;
	}

	public JComboBox getDestComboBox() {
		return destComboBox;
	}

	private String[] setToArray(Set<String> set) {
		String[] ret = new String[set.size()];
		int count = 0;
		for (String s : set) {
			ret[count++] = s;
		}
		return ret;
	}
	
	private ComboBoxModel createServiceModel(Services services) {
		Set<String> items = null;
		
		System.out.println("TranslateConfigurationForm.createServiceModel()");
		
		try {
			items = services.getServices();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		
		System.out.println("TranslateConfigurationForm.createServiceModel() 2");
		
		String[] ret = setToArray(items);
		
		System.out.println("TranslateConfigurationForm.createServiceModel() 3");
		
		java.util.Arrays.sort(ret);
		
		System.out.println("TranslateConfigurationForm.createServiceModel() 4");
		
		return new DefaultComboBoxModel(ret);
	}
	
	private ComboBoxModel createLanguageModel(ISO639 iso) {
		Set<String> items;
		try {
			items = iso.getLanguages();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		String[] ret = setToArray(items);
		java.util.Arrays.sort(ret);
		return new DefaultComboBoxModel(ret);
	}

	public JComponent getRootComponent() {
		return rootComponent;
	}

	public void setData(TranslateConfiguration data) {

		System.out.println("TranslateConfigurationForm.setData()");

		if (data.getLangPair() != null) {
			ComboBoxModel model = srcComboBox.getModel();
			boolean ok = false;

			System.out.println("TranslateConfigurationForm.setData() 1");

			for (int i = 0; i < model.getSize() && !ok; i++) {
				String item = (String) model.getElementAt(i);

				System.out.println("TranslateConfigurationForm.setData() 1.1 "
						+ item);
				System.out.println("TranslateConfigurationForm.setData() 1.2 "
						+ data);

				if (item.equals(data.getLangPair().getSrcLang().getName())) {
					srcComboBox.setSelectedItem(item);
					ok = true;
				}
			}

			System.out.println("TranslateConfigurationForm.setData() 2");

			model = destComboBox.getModel();
			ok = false;
			for (int i = 0; i < model.getSize() && !ok; i++) {
				String item = (String) model.getElementAt(i);
				if (item.equals(data.getLangPair().getDestLang().getName())) {
					destComboBox.setSelectedItem(item);
					ok = true;
				}
			}
		}

		if (data.getService() != null) {

			System.out.println("TranslateConfigurationForm.setData() 3");

			ComboBoxModel model = serviceComboBox.getModel();
			boolean ok = false;

			for (int i = 0; i < model.getSize() && !ok; i++) {
				String item = (String) model.getElementAt(i);
				if (item.equals(services.getService(data.getService()))) {
					serviceComboBox.setSelectedItem(item);
					ok = true;
				}
			}

		}

		System.out.println("TranslateConfigurationForm.setData() 4");

		if (data.getUrl() != null) {
			urlTextField.setText(data.getUrl());
		}
	}

	public TranslateConfiguration getData() {
		TranslateConfiguration ret = new TranslateConfiguration();
		
		String serviceSelectedItem = (String)serviceComboBox.getSelectedItem();
		ret.setService(services.getServiceType(serviceSelectedItem));
		
		String srcSelectedItem = (String)srcComboBox.getSelectedItem();
		String destSelectedItem = (String)destComboBox.getSelectedItem();
		
		ISO639 iso = new ISO639();
		
		Language src = new Language(iso.getCode(srcSelectedItem));
		Language dest = new Language(iso.getCode(destSelectedItem));
		
		LanguagePair pair = new LanguagePair(src, dest);
		
		ret.setLangPair(pair);
		
		if (urlTextField != null) {
			ret.setUrl(urlTextField.getText().trim());
		}
		
		return ret;
	}

	public boolean isModified(TranslateConfiguration data) {
		TranslateConfiguration tc = getData();
		boolean ret = tc.equals(data);
		return ret;
	}

}
