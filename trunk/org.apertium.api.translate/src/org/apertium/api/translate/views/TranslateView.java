package org.apertium.api.translate.views;

import org.apertium.api.translate.TranslatePlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.part.ViewPart;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;

public class TranslateView extends ViewPart implements ITranslateView {
	
	private Composite top = null;
	private SashForm sashForm = null;
	
	protected StyledText translations = null;
	
	private static TranslateView instance = null;
	
	public static TranslateView getInstance() {
		return instance;
	}
	
	public static final String ID = TranslatePlugin.ID + ".views.translateView";
    private static final String SEPARATOR = System.getProperty("line.separator");
	
	@Override
	public void createPartControl(Composite parent) {
		System.out.println("TranslateView.createPartControl()");
		
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new FillLayout());
		
        createSashForm();
        
        appendMessage("Qui appariranno le traduzioni");
        
        instance = this;
		//TestThread tt = new TestThread(this);
		//tt.start();
	}

	@Override
	public void setFocus() {
		System.out.println("TranslateView.setFocus()");
		
		translations.setFocus();
	}
	
    private void createSashForm() {
        sashForm = new SashForm(top, SWT.NONE);
        sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
        
		translations = new StyledText(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		translations.setEditable(false);
    }
    
    @SwtAsyncExec
    public void appendMessage(String message) {
    	System.out.println("TranslateView.appendMessage()");
    	
        String textToAppend = message + SEPARATOR;
        translations.append(textToAppend);
        scrollToEnd();
    }

    protected void scrollToEnd() {
    	System.out.println("TranslateView.scrollToEnd()");
    	
        ScrollBar scrollBar = translations.getVerticalBar();
        scrollBar.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("TranslateView.widgetDefaultSelected()");
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("TranslateView.widgetSelected()");
			}
        });
        
        int n = translations.getCharCount();
        translations.setSelection(n, n);
        translations.showSelection();
    }
    
}