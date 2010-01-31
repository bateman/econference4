package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;

import javax.swing.*;
import java.util.*;

public class TranslateConfigurationForm {
	private JPanel rootComponent = null;

	private JComboBox srcComboBox = null;
	private JComboBox destComboBox = null;
	
	private JLabel label = null;

	private JTextField urlTextField = null;

	public TranslateConfigurationForm() {
		
		rootComponent = new JPanel();
		rootComponent.setLayout(new BoxLayout(rootComponent, BoxLayout.PAGE_AXIS));
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		srcComboBox = new JComboBox();
		destComboBox = new JComboBox();
		
		label = new JLabel("Select translation:");

		urlTextField = new JTextField(25);
		
		panel1.add(new JLabel("Apertium XML-RPC server URL: "));
		panel1.add(urlTextField);
		
		panel2.add(label);
		panel2.add(srcComboBox);
		panel2.add(destComboBox);
		
		rootComponent.add(panel1);
		rootComponent.add(panel2);

		srcComboBox.removeAllItems();
		srcComboBox.setModel(createModel());
		srcComboBox.setRenderer(new LanguageEntryRenderer());

		if (destComboBox.getModel().getSize() > 0) {
			destComboBox.setSelectedIndex(0);
		}
		
		destComboBox.removeAllItems();
		destComboBox.setModel(createModel());
		destComboBox.setRenderer(new LanguageEntryRenderer());

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


	private ComboBoxModel createModel() {
		Set<String> items;
		try {
			ISO639 iso = new ISO639();
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
