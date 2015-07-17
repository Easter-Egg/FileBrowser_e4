package filebrowser_e4.Utils;

import java.io.File;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

public class FileOpen {
	
	private String loc;
	private boolean alreadyPartExisted = false;
	private MWindow window;
	private EPartService partService;
	private EModelService modelService;
	
	public FileOpen(ESelectionService ss, EPartService ps, EModelService ms, MWindow w){
		this.loc = ss.getSelection().toString();
		this.partService = ps;
		this.modelService = ms;
		this.window = w;
	}
	
	@Execute
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
	}
}
