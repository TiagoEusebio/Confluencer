package com.rabbit.confluencer.parts;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;


import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ChatViewClass {
	private Label myLabelInView;

@PostConstruct
public void createPartControl(Composite parent) {
    System.out.println("Enter in SampleE4View postConstruct");

    // Create a GridLayout with two columns
    GridLayout layout = new GridLayout(2, false);
    parent.setLayout(layout);

    // Create a Label and Text for user input in the first column
    Label userInputLabel = new Label(parent, SWT.NONE);
    userInputLabel.setText("Your question:");
    Text userInput = new Text(parent, SWT.BORDER);
    userInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    // Add a KeyListener to get user input when Enter is pressed


    // Create a Label for the response in the second column
    Text responseText = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    GridData responseTextLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
    responseText.setLayoutData(responseTextLayout);
    Text thoughtsText = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    thoughtsText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    // Add a KeyListener to get user input when Enter is pressed
    userInput.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent event) {
            // Check if the user pressed Enter
            if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
                // Process the question and generate a response
                String question = userInput.getText();
                if (question.equals("clear")) {
                    // Clear the console
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    // Clear the user input
                    userInput.setText("");
                    responseText.setText("");
                    return;
                }else{
                String response = generateResponse(question,thoughtsText);
                // Append the question and response to the chat history
                responseText.append("You: " + question + "\n\n");
                responseText.append("Confluencer: " + response + "\n");
                // Clear the user input
                userInput.setText("");}
            }
        }
    });
}


	protected String generateResponse(String question, Text thoughts ) {
		// TODO Auto-generated method stub
		ConFluencerAPI confluencerAPI = new ConFluencerAPI();
		thoughts.append("Thinking\n");
		return question;
	}


	@Focus
	public void setFocus() {
		myLabelInView.setFocus();

	}
    /**
	 * This method is kept for E3 compatiblity. You can remove it if you do not
	 * mix E3 and E4 code. <br/>
	 * With E4 code you will set directly the selection in ESelectionService and
	 * you do not receive a ISelection
	 * 
	 * @param s
	 *            the selection received from JFace (E3 mode)
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection s) {
		if (s==null || s.isEmpty())
			return;

		if (s instanceof IStructuredSelection) {
			IStructuredSelection iss = (IStructuredSelection) s;
			if (iss.size() == 1)
				setSelection(iss.getFirstElement());
			else
				setSelection(iss.toArray());
		}
	}

	/**
	 * This method manages the selection of your current object. In this example
	 * we listen to a single Object (even the ISelection already captured in E3
	 * mode). <br/>
	 * You should change the parameter type of your received Object to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current object received
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {

		// Remove the 2 following lines in pure E4 mode, keep them in mixed mode
		if (o instanceof ISelection) // Already captured
			return;

		// Test if label exists (inject methods are called before PostConstruct)
		if (myLabelInView != null)
			myLabelInView.setText("Current single selection class is : " + o.getClass());
	}

	/**
	 * This method manages the multiple selection of your current objects. <br/>
	 * You should change the parameter type of your array of Objects to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current array of objects received in case of multiple selection
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object[] selectedObjects) {

		// Test if label exists (inject methods are called before PostConstruct)
		if (myLabelInView != null)
			myLabelInView.setText("This is a multiple selection of " + selectedObjects.length + " objects");
	}
}
