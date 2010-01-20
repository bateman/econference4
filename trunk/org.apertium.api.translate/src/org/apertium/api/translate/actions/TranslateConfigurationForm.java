package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;

import javax.swing.*;
import java.util.*;

public class TranslateConfigurationForm {
	private JPanel rootComponent = null;

	private JComboBox pairsComboBox = null;
	private JLabel label = null;

	private JTextField apertiumServerURLTextField = null;

	public TranslateConfigurationForm() {
		
		rootComponent = new JPanel();
		rootComponent.setLayout(new BoxLayout(rootComponent, BoxLayout.PAGE_AXIS));
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		pairsComboBox = new JComboBox();
		label = new JLabel("Select translation:");

		apertiumServerURLTextField = new JTextField(25);
		
		panel1.add(new JLabel("Apertium XML-RPC server URL: "));
		panel1.add(apertiumServerURLTextField);
		
		panel2.add(label);
		panel2.add(pairsComboBox);
		
		rootComponent.add(panel1);
		rootComponent.add(panel2);

		pairsComboBox.removeAllItems();
		pairsComboBox.setModel(createModel());
		pairsComboBox.setRenderer(new LanguageEntryRenderer());

		if (pairsComboBox.getModel().getSize() > 0) {
			pairsComboBox.setSelectedIndex(0);
		}
	}

	public JComboBox getPairsComboBox() {
		return pairsComboBox;
	}

	private ComboBoxModel createModel() {
		Set<String> items;
		try {
			TranslateHelper translateHelper = new TranslateHelper();
			items = translateHelper.getLangPairs().keySet();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}

		return new DefaultComboBoxModel(items.toArray());
	}

	public JComponent getRootComponent() {
		return rootComponent;
	}

	public void setData(TranslateConfiguration data) {
		String langPair = "";

		ComboBoxModel model = pairsComboBox.getModel();

		boolean ok = false;

		for (int i = 0; i < model.getSize() && !ok; i++) {
			String item = (String) model.getElementAt(i);

			if (item.equals(langPair)) {
				pairsComboBox.setSelectedItem(item);
				ok = true;
			}
		}

		pairsComboBox.setSelectedItem(data.getLangPair());
		apertiumServerURLTextField.setText(data.getApertiumServerURL());
	}

	public void getData(TranslateConfiguration data) {
		String selectedItem = (String)pairsComboBox.getSelectedItem();

		if (selectedItem != null) {
			TranslateHelper translateHelper = null;
			try {
				translateHelper = new TranslateHelper();
			} catch (Exception e) {
				e.printStackTrace();
			}
			data.setLangPair(translateHelper.getLangPairs().get(selectedItem));
		}

		if (apertiumServerURLTextField != null) {
			data.setApertiumServerURL(apertiumServerURLTextField.getText().trim());
		}

	}

	public boolean isModified(TranslateConfiguration data) {
		String selectedItem = (String) pairsComboBox.getSelectedItem();

		boolean isModified = (selectedItem != null ? !selectedItem.equals(data.getLangPair()) : data.getLangPair() != null);

		isModified |= apertiumServerURLTextField != null ? !apertiumServerURLTextField.getText()
				.equals(data.getApertiumServerURL()) : data.getApertiumServerURL() != null;

		return isModified;
	}

}
