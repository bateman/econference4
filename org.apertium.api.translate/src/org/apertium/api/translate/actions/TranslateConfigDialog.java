package org.apertium.api.translate.actions;

import javax.swing.*;

import org.apertium.api.translate.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TranslateConfigDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 3929003692312556323L;

	private TranslateConfigurationForm form = null;

	private JButton okButton = null;
	private JButton cancelButton = null;
	private JButton applyButton = null;

	private Services.ServiceType service = null;
	private LanguagePair langPair = null;
	private String url = null;

	private boolean answer = false;

	public TranslateConfigDialog(Frame owner) {
		super(owner, true);

		form = new TranslateConfigurationForm();

		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		applyButton = new JButton("Apply");
		
		setSize(500, 200);

		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		applyButton.addActionListener(this);

		Container contentPane = getContentPane();

		contentPane.add(form.getRootComponent(), "Center");

		JPanel panel = new JPanel();

		panel.add(okButton);
		panel.add(cancelButton);
		panel.add(applyButton);

		contentPane.add(panel, "South");
	}

	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action actionListener = new AbstractAction() {
			private static final long serialVersionUID = -6213167658429616784L;

			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		};
		InputMap inputMap = rootPane
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == okButton) {
			saveProperties();
			answer = true;
			dispose();
		} else if (source == cancelButton) {
			answer = false;
			dispose();
		} else if (source == applyButton) {
			answer = true;
			saveProperties();
		}
	}

	private void saveProperties() {
		TranslateConfiguration data = form.getData();
		
		service = data.getService();
		langPair = data.getLangPair();
		url = data.getUrl();
	}

	public void loadProperties() {
		TranslateConfiguration data = new TranslateConfiguration();
		
		data.setService(service);
		data.setLangPair(langPair);
		data.setUrl(url);

		form.setData(data);
	}

	public Services.ServiceType getService() {
		return service;
	}

	public void setService(Services.ServiceType s) {
		this.service = s;
	}
	
	public LanguagePair getLangPair() {
		return langPair;
	}

	public void setLangPair(LanguagePair langPair) {
		this.langPair = langPair;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAnswer() {
		return answer;
	}

}