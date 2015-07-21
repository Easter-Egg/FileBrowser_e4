package filebrowser_e4.Utils;

import java.io.PrintStream;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class E4_ConsoleFactory implements IConsoleFactory{

	@Override
	public void openConsole() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		try {
			IConsoleView consoleView = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
			MessageConsole console = new MessageConsole("out/err/log", null);
			ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
			consoleView.display(console);
			
			MessageConsoleStream stream = console.newMessageStream();
			System.setOut(new PrintStream(stream));
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
	}
	

}
