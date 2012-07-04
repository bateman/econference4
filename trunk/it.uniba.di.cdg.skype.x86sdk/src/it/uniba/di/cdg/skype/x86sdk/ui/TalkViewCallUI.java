package it.uniba.di.cdg.skype.x86sdk.ui;



import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


import it.uniba.di.cdg.skype.x86sdk.action.SkypeCallAction;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.views.ITalkViewUI;


public class TalkViewCallUI  implements ITalkViewUI {



	protected static Button callButton;
	protected Button endButton;
	protected IAction call;


	public void addButtons(Composite callComposite, final String receiver) {
		if (!receiver.equals("conference")){

			if( callComposite.getChildren().length==0){

				callButton = new Button( callComposite, SWT.NONE );
				callButton.setText( "Call" );
				callButton.setImage(UiPlugin.getDefault().getImage(IImageResources.ICON_CALL));

				endButton = new Button( callComposite, SWT.NONE );
				endButton.setText( "End" );
				endButton.setImage(UiPlugin.getDefault().getImage(IImageResources.ICON_END_CALL));



				callButton.addSelectionListener( new SelectionAdapter() {
					public void widgetSelected( SelectionEvent e ) {       		



						SkypeCallAction callAction = new SkypeCallAction();
						if(callAction.isCalling(receiver)){
							callAction.finishCall(receiver);
						}else{
							callAction.call(receiver);
						}



					}
				} );


				// When the user click 'end', notify listeners
				endButton.addSelectionListener( new SelectionAdapter() {
					public void widgetSelected( SelectionEvent e ) {

						SkypeCallAction callAction = new SkypeCallAction();
						callAction.finishCall(receiver);
					}


				} );


				final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				Point size = shell.getSize();


				shell.setSize( size.x+5, size.y+5);   
				shell.setSize( size.x-5, size.y-5);  
			}
		}
	}

}
