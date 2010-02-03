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
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;

public class TranslateView extends ViewPart implements ITranslateView, IBackendEventListener {
	
	private Composite top = null;
	private SashForm sashForm = null;
	
	protected StyledText translations = null;
	
	public static final String ID = TranslatePlugin.ID + ".views.translateView";
    private static final String SEPARATOR = System.getProperty("line.separator");
	
    public TranslateView() {
    	super();
    }
    
	@Override
	public void createPartControl(Composite parent) {
		System.out.println("TranslateView.createPartControl()");
		
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new FillLayout());
		
        createSashForm();        
        appendMessage("Qui appariranno le traduzioni");
        
        TranslatePlugin.getDefault().addListener(this);
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
    public void messageReceived(final String text, final String who) {
        appendMessage(String.format("[%s] %s", who, text));
    }
    
    @SwtAsyncExec
    public void appendMessage(final String message) {
    	System.out.println("TranslateView.appendMessage()");
    	
        String textToAppend = message + SEPARATOR;
        translations.append(textToAppend);
        scrollToEnd();
    }

    @SwtAsyncExec
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

    @SwtAsyncExec
	@Override
	public void onBackendEvent(final IBackendEvent event) {
		System.out.println("TranslateView.onBackendEvent() - event is " + event.getClass().toString());
		messageReceived(event.getClass().toString(), "System");
	}
    
}