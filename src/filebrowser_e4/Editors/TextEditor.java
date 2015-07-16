 
package filebrowser_e4.Editors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class TextEditor {
public StyledText styledText = null;
	
	@Inject
	public TextEditor() {
	
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {		
		styledText = new StyledText(parent, SWT.BORDER);
	}
	
	@Focus
	public void onFocus() {
		styledText.setFocus();
	}
}