package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;

import javax.swing.*;
import java.util.*;

public class TranslateConfigurationForm {
	private JPanel rootComponent = null;

	private JComboBox serviceComboBox = null;
	private Map<String, TranslateConfiguration.ServiceType> serviceMap = null;
	
	private JComboBox srcComboBox = null;
	private JComboBox destComboBox = null;

	private JTextField urlTextField = null;

	public TranslateConfigurationForm() {
		
		serviceMap = new HashMap<String, TranslateConfiguration.ServiceType>();
		
		serviceMap.put("Apertium XML-RPC", TranslateConfiguration.ServiceType.APERTIUM);
		serviceMap.put("Google", TranslateConfiguration.ServiceType.GOOGLE);
		
		rootComponent = new JPanel();
		rootComponent.setLayout(new BoxLayout(rootComponent, BoxLayout.PAGE_AXIS));
		
		JPanel panel0 = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		serviceComboBox = new JComboBox();
		
		panel0.add(new JLabel("Service type: "));
		panel0.add(serviceComboBox);
		
		urlTextField = new JTextField(25);
		
		panel1.add(new JLabel("Service URL: "));
		panel1.add(urlTextField);
		
		srcComboBox = new JComboBox();
		destComboBox = new JComboBox();
		
		panel2.add(new JLabel("Language pair: "));
		panel2.add(srcComboBox);
		panel2.add(destComboBox);
		
		rootComponent.add(panel1);
		rootComponent.add(panel2);

		ISO639 iso = new ISO639();
		
		serviceComboBox.removeAllItems();
		serviceComboBox.setModel(createServiceModel());
		serviceComboBox.setRenderer(new EntryRenderer());
		
		srcComboBox.removeAllItems();
		srcComboBox.setModel(createLanguageModel(iso));
		srcComboBox.setRenderer(new EntryRenderer());

		if (destComboBox.getModel().getSize() > 0) {
			destComboBox.setSelectedIndex(0);
		}
		
		destComboBox.removeAllItems();
		destComboBox.setModel(createLanguageModel(iso));
		destComboBox.setRenderer(new EntryRenderer());

		if (destComboBox.getModel().getSize() > 0) {
			destComboBox.setSelectedIndex(0);
		}
	}

	public JComboBox getSrcComboBox() {
		return srcComboBox;
	}

	public JComboBox getDestComboBox() {
		return destComboBox;
	}

	private ComboBoxModel createServiceModel() {
		Set<String> items;
		try {
			items = serviceMap.keySet();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		return new DefaultComboBoxModel(items.toArray());
	}
	
	private ComboBoxModel createLanguageModel(ISO639 iso) {
		Set<String> items;
		try {
			items = iso.getLanguages();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		return new DefaultComboBoxModel(items.toArray());
	}

	public JComponent getRootComponent() {
		return rootComponent;
	}

	public void setData(TranslateConfiguration data) {
		ComboBoxModel model = srcComboBox.getModel();
		
		boolean ok = false;
		
		for (int i = 0; i < model.getSize() && !ok; i++) {
			String item = (String) model.getElementAt(i);

			if (item.equals(data.getLangPair().getSrcLang().getName())) {
				srcComboBox.setSelectedItem(item);
				ok = true;
			}
		}
		
		model = destComboBox.getModel();
		
		ok = false;
		
		for (int i = 0; i < model.getSize() && !ok; i++) {
			String item = (String) model.getElementAt(i);

			if (item.equals(data.getLangPair().getDestLang().getName())) {
				srcComboBox.setSelectedItem(item);
				ok = true;
			}
		}
		
		urlTextField.setText(data.getUrl());
	}

	public TranslateConfiguration getData() {
		TranslateConfiguration ret = new TranslateConfiguration();
		
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
