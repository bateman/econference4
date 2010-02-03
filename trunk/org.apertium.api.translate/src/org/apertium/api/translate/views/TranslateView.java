package org.apertium.api.translate.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class TranslateView extends ViewPart implements ITranslateView {

	@Override
	public void createPartControl(Composite parent) {
		System.out.println("TranslateView.createPartControl()");
		
		Text text = new Text(parent, SWT.BORDER);
		text.setText("Qui appariranno le traduzioni");
	}

	@Override
	public void setFocus() {
		System.out.println("TranslateView.setFocus()");
	}

}
