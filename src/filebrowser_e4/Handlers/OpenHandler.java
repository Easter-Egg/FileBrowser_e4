package filebrowser_e4.Handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import filebrowser_e4.Utils.FileOpen;	

public class OpenHandler {
	private String fileName = null;

	@Inject
	ESelectionService selectionService;	
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MWindow window, EPartService partService, EModelService modelService, MPart vpart){
		
		FileDialog dialog = new FileDialog(shell);
		fileName = dialog.open();
		
		if(fileName != null){
			selectionService.setSelection(fileName);
			FileOpen fo = new FileOpen(selectionService, partService, modelService, window);
			fo.open();
		}
	}
}
