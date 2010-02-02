package org.apertium.api.translate.actions;

import org.apertium.api.translate.TranslateHelper;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.actions.ActionDelegate;

public class TranslateAction extends ActionDelegate {
	
	private static TranslateHelper translateHelper;

	public TranslateAction() throws Exception {
		translateHelper = new TranslateHelper();
	}

	public void run(IAction action) {
		String selectedText = "";

		ConfigTranslateAction configTranslateAction = ConfigTranslateAction.getInstance();
		String response = "";

		try {
			response = translateHelper.translate(selectedText, configTranslateAction.getLangPair());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
