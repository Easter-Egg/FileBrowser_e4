package filebrowser_e4.Utils;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import filebrowser_e4.Views.OutlineView;

public class FileOpen {
	
	private String loc;
	private boolean alreadyPartExisted = false;
	private MWindow window;
	private EPartService partService;
	private EModelService modelService;
	
	@Inject
	public FileOpen(ESelectionService ss, MWindow window, EPartService partService, EModelService modelService){
		this.loc = ss.getSelection().toString();
		this.window = window;
		this.partService = partService;
		this.modelService = modelService;
	}
	
	public void open(){
		File file = new File(loc);
		List<MPartStack> stacks = modelService.findElements(window, null, MPartStack.class, null);
		
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
		MPart mp = (MPart) stacks.get(3).getChildren().get(0);
		OutlineView olv = (OutlineView) mp.getObject();
		olv.getText().setText("File Name : " + file.getName() + "\nFile Size : " + file.length() + " Bytes");
	}		
}
