package filebrowser_e4.Handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import filebrowser_e4.Views.BrowserView;

public class CollapseHandler {
	@Execute
	public void execute(MPart part) {
		BrowserView bv = (BrowserView) part.getObject();
		bv.getTreeViewer().collapseAll();
	}
}
