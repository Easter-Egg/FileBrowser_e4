package filebrowser_e4.Handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class AboutHandler {
	
	@Execute
	public void execute(final Shell shell) {
              
		MessageDialog.openInformation(shell, "About File Browser", "Product Name : File Browser\nVersion : 1.0.0");
	}
	
}
