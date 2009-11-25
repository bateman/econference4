package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;

import org.eclipse.jface.dialogs.IDialogConstants;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * <p>This dialog provide a 2-columns grid layout in which
 * each row contains a label and a text box.</p>
 * 
 * <p>Labels text is taken from the first String array parameter.
 * 
 * <br>Any possible initial values for text box is taken from 
 * the second String array parameter.
 * 
 * <br>Each text box could be validated by the relativ Input
 * Validator see {@link IInputValidator}.
 * 
 * <br>In the end there are the Yed and Cancel buttons.</p>
 * 
 * 
 * @author Alessandro Brucoli
 *
 */
public class UserInputsProviderDialog extends Dialog {
	
		private Text[] textContainers;
		
		private String[] values;
	
		private String title = null;
		private String question = null;
		private String[] userInputs = null;
		private String[] initialValues = null;
		private IInputValidator[] validators = null;
		

		private Text errorMessageText;

		public UserInputsProviderDialog(Shell shell, String title, String question,
				String[] userInputs, String[] initialValues, IInputValidator[] validators) {
			super(shell);
			this.title = title;			
			this.question = question;
			this.userInputs = userInputs;
			this.initialValues = initialValues;
			this.validators = validators;			
			textContainers = new Text[userInputs.length];
		}		

		protected Control createDialogArea(Composite parent) {
			
			Composite composite = (Composite) super.createDialogArea(parent);
			composite.setLayout(new GridLayout(2, false));
			
            GridData gridData = new GridData(GridData.GRAB_HORIZONTAL
                    | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
                    | GridData.VERTICAL_ALIGN_CENTER);
            gridData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH-50);
            gridData.horizontalSpan = 2;
            
            Label questionLabel = new Label(composite, SWT.WRAP);
			questionLabel.setText(question);
            questionLabel.setLayoutData(gridData);
            questionLabel.setFont(parent.getFont());
                       
			for (int i = 0; i < userInputs.length; i++) {
				gridData = new GridData();
				gridData.verticalAlignment = SWT.TOP;

				Label label = new Label(composite, SWT.NONE);
				label.setText(userInputs[i]);			
				label.setLayoutData(gridData);

				gridData = new GridData();
				gridData.horizontalAlignment = SWT.FILL;
				gridData.grabExcessHorizontalSpace = true;

				Text text = new Text(composite, SWT.NONE | SWT.BORDER | SWT.SINGLE);			
				text.setText(initialValues[i]);
				text.setLayoutData(gridData);	

				textContainers[i] = text;

			}

			gridData = new GridData( GridData.GRAB_HORIZONTAL
					| GridData.HORIZONTAL_ALIGN_FILL );
			gridData.horizontalSpan = 2;
			
			errorMessageText = new Text( composite, SWT.READ_ONLY );
			errorMessageText.setLayoutData( gridData );
			errorMessageText.setBackground( errorMessageText.getDisplay().getSystemColor(
					SWT.COLOR_WIDGET_BACKGROUND ) );

			return composite;
		}
		
	    /*
	     * (non-Javadoc)
	     * 
	     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	     */
	    protected void configureShell(Shell shell) {
	        super.configureShell(shell);
	        if (title != null) {
				shell.setText(title);
			}
	    }
		
		
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.OK_ID,
					IDialogConstants.YES_LABEL, true);
			createButton(parent, IDialogConstants.CANCEL_ID,
					IDialogConstants.CANCEL_LABEL, false);
		}
		
		/**
	     * Validates the input. <p> The default implementation of this framework method delegates the
	     * request to the supplied input validator object; if it finds the input invalid, the error
	     * message is displayed in the dialog's message line. This hook method is called whenever the
	     * text changes in the input field. </p>
	     */
	    protected boolean validateInput() {
	    	boolean error = false;
	        String[] errorMessage = null;
	        if (validators != null) {
	        	errorMessage = new String[validators.length];
	        	for (int i = 0; i < validators.length; i++) {
	        		if(validators[i]!= null){
	        			 errorMessage[i] = validators[i].isValid( textContainers[i].getText() );
	        			 error = error | errorMessage[i]!=null;
	        		}					
				}
	        	 setErrorMessage( errorMessage );
	        }
	        // Bug 16256: important not to treat "" (blank error) the same as null
	        // (no error)	       
	        return !error;
	    }
			    
		@Override
		protected void okPressed() {
			if(validateInput()){
				values = new String[textContainers.length];
				for (int i = 0; i < textContainers.length; i++) {
					values[i] = textContainers[i].getText();
				}
				super.okPressed();
			}
		}
		
		public String[] getValues(){
			return values;
		}
			
		
	    /**
	     * Sets or clears the error message. If not <code>null</code>, the OK button is disabled.
	     * 
	     * @param errorMessage
	     *        the error message, or <code>null</code> to clear
	     * @since 3.0
	     */
	    public void setErrorMessage( String[] errorMessage ) {
	    	if(errorMessage!=null){
	    		errorMessageText.setText("");
		    	for (int i = 0; i < errorMessage.length; i++) {
		    		errorMessageText.setText(errorMessageText.getText()+ (errorMessage[i] == null ? "" : errorMessage[i]+"\n") );        			        
				}
		    	//errorMessageText.getParent().update();		 
	    	}        
	    }

}
