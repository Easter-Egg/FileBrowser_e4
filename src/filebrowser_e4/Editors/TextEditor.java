
package filebrowser_e4.Editors;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class TextEditor {
	private StyledText styledText = null;

	@Inject
	private ESelectionService ss;

	@Inject
	public TextEditor() {

	}

	@SuppressWarnings("resource")
	@PostConstruct
	public void postConstruct(Composite parent) {
		styledText = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		String loc = ss.getSelection().toString();

		File file = new File(loc);
		
		if (file.getName().endsWith(".txt")) {
			try {
				styledText.setText(new Scanner(file).useDelimiter("\\A").next());
			} catch (IOException e) {
				MessageDialog.openError(parent.getShell(), "Error opening file", "File " + file.getName() + " could not be opened.");
			}
		}
	}

	@Focus
	public void onFocus() {
		styledText.setFocus();
	}
	
	public StyledText getST(){
		return styledText;
	}
}