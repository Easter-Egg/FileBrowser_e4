package filebrowser_e4.Handlers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import filebrowser_e4.Editors.TextEditor;	

public class OpenHandler {
	private boolean alreadyPartExisted = false;
	private String fileName = null;
	
	@SuppressWarnings("resource")
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MWindow window, EPartService partService, EModelService modelService, MPart vpart){
		List<MPartStack> stacks = modelService.findElements(window, null, MPartStack.class, null);
		FileDialog dialog = new FileDialog(shell);
		
		fileName = dialog.open();
		
		if(fileName != null){
			File file = new File(fileName);
			// check parts
			for(MPart existedPart : partService.getParts()){
				if(existedPart.getLabel().equals(file.getName())){
					alreadyPartExisted = true;
					partService.showPart(existedPart, PartState.ACTIVATE);
					break;
				}
				
				else
					alreadyPartExisted = false;
			}
			if(!alreadyPartExisted){
				if(file.getName().endsWith(".txt")){
					MPart part = modelService.createModelElement(MPart.class);
					part.setLabel(file.getName());
					part.setContributionURI("bundleclass://FileBrowser_e4/filebrowser_e4.Editors.TextEditor");
					part.setCloseable(true);
					stacks.get(1).getChildren().add(part);
					partService.showPart(part, PartState.ACTIVATE);
					try {
						((TextEditor) part.getObject()).getST().setText(new Scanner(file).useDelimiter("\\A").next());
					} catch (IOException e) {
						MessageDialog.openError(shell, "Error opening file", "File " + file.getName() + " could not be opened.");
					}
				}
				// open image editor
				else if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png")){
					MPart part = modelService.createModelElement(MPart.class);
					part.setLabel(file.getName());
					part.setContributionURI("bundleclass://FileBrowser_e4/filebrowser_e4.Editors.ImageEditor");
					part.setCloseable(true);
					stacks.get(1).getChildren().add(part);
					partService.showPart(part, PartState.ACTIVATE);
				}
			}
		} else {
			// do Nothing
		}
	}
}
