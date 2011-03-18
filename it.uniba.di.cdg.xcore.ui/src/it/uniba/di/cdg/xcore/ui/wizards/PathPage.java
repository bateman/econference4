/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2011 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package it.uniba.di.cdg.xcore.ui.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

/**
 * Wizard page for choice the location where to store ECX files
 * 
 * @see WizardPage
 */
public class PathPage extends WizardPage {

	private Text path;				//Path text line
	
	private Label warningLabel;		//Change warning label
	
	private boolean change = false;	//Change flag

	/**
	 * The constructor
	 */
	protected PathPage() {
		super("Event folder");
		setTitle("Event folder");
		setDescription("Select folder where to store events (ECX File)");
	}

	/**
	 * Show the form
	 * 
	 * @see WizardPage#createControl(Composite)
	 */
	public void createControl(final Composite parent) {
		/* Search data in preferences */
		Preferences preferences = new ConfigurationScope().getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
		Preferences pathPref = preferences.node(IConfigurationConstant.PATH);
		final String dirPref = pathPref.get(IConfigurationConstant.DIR, "");

		/* Setup container layout */
		Composite container = new Composite(parent, SWT.NULL);
	    FillLayout layout = new FillLayout(SWT.VERTICAL);
	    container.setLayout(layout);
		setControl(container);

		/* Setup group layout */
		Group pathGroup = new Group(container, SWT.SHADOW_IN);
		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 3;
		pathGroup.setText("Storage location");
		pathGroup.setLayout(groupLayout);

		/* Add path label */
		Label pathLabel = new Label(pathGroup, SWT.NONE);
		GridData pathGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		pathGrid.horizontalIndent = 20;
		pathLabel.setLayoutData(pathGrid);
		pathLabel.setText("Store in:");

		/* Add path text line */
		path = new Text(pathGroup, SWT.BORDER);
		if (dirPref.isEmpty()){
			/* If there isn't a preferred path, set default in path text line */
			path.setText(System.getProperty("user.home")
					     + System.getProperty("file.separator")
					     + ".econference");
		} else {
			/* If there is a preferred path, set it in path text line */
			path.setText(dirPref);
		}
		path.addListener(SWT.Modify, new Listener() {
			
			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
				if((path.getText() + System.getProperty("file.separator")).compareTo(dirPref) == 0
					|| path.getText().compareTo(dirPref) == 0){
					warningLabel.setVisible(false);
					change = false;
				}else{
					warningLabel.setVisible(true);
					change = true;
				}
			}
		});
		path.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/* Add path button */
		Button pathButton = new Button(pathGroup, SWT.NONE);
		pathButton.addListener(SWT.Selection, new Listener() {

			/* Run directory dialog when the button is pressed */
			@Override
			public void handleEvent(Event event) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
				directoryDialog.setMessage("Please select a directory");
				directoryDialog.setFilterPath(path.getText());
				String location = directoryDialog.open();
				if (location != null){
					/* If user choose a path, set it in path text line */
					path.setText(location);
				}
			}
		});
		pathButton.setText("Browse...");
		
		/* Add a spacer */
		Label spacer1Label = new Label(pathGroup, SWT.NONE);
		GridData spacer1Grid = new GridData();
		spacer1Grid.horizontalSpan = 3;
		spacer1Label.setLayoutData(spacer1Grid);

		/* Add password label */
		warningLabel = new Label(pathGroup, SWT.RIGHT);
		GridData warningGrid = new GridData();
		warningGrid.horizontalSpan = 3;
		warningGrid.horizontalIndent = 20;
		warningLabel.setLayoutData(warningGrid);
		warningLabel.setText("This change will cause application restart at the end of wizard");
		warningLabel.setVisible(false);
		Color color = new Color(this.getShell().getDisplay(), 200, 0, 0);
		warningLabel.setForeground(color);
	}

	/**
	 * Checks the validity of user entered data and, if necessary, prevents from
	 * continuing in the wizard
	 */
	private void dataControl() {
		setPageComplete(false);
		setErrorMessage(null);

		if (path.getText().length() == 0){
			/* If path text line is empty, prevents from continuing in the wizard */
			return;
		}

		IPath location = new Path(path.getText().trim());
		location = location.makeAbsolute();
		if (location == null || !location.toFile().exists()) {
			/* If path doesn't exists, show error message and prevents from continuing in the wizard */
			setErrorMessage("Please select an existing directory");
			return;
		}

		setPageComplete(true);
		setErrorMessage(null);
	}

	/**
	 * Returns the path where to store the event
	 * 
	 * @return path
	 */
	public String getPath() {
		return path.getText();
	}
	
	/**
	 * Returns true if the path has been changed
	 * 
	 * @return change
	 */
	public boolean getChange() {
		return change;
	}
}
