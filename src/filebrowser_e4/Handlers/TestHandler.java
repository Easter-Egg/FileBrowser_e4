package filebrowser_e4.Handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class TestHandler{
	@Execute
	public void execute(final Shell shell) {
              
	    System.out.println("TEST");

	  }

}
