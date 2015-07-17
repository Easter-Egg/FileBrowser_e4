
package filebrowser_e4.Editors;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;

import filebrowser_e4.Views.OutlineView;

public class TextEditor {
	private StyledText styledText = null;
	
	@Inject
	private EModelService ms;
	
	@Inject
	private MWindow window;

	@Inject
	private ESelectionService ss;

	@Inject
	public TextEditor() {

	}

	@SuppressWarnings("resource")
	@PostConstruct
	public void postConstruct(Composite parent) {
		List<MPartStack> stacks = ms.findElements(window, null, MPartStack.class, null);
		styledText = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		if(ss.getSelection() != null){
			String loc = ss.getSelection().toString();
			File file = new File(loc);
			
			if (file.getName().endsWith(".txt")) {
				try {
					styledText.setText(new Scanner(file).useDelimiter("\\A").next());
				} catch (IOException e) {
					MessageDialog.openError(parent.getShell(), "Error opening file", "File " + file.getName() + " could not be opened.");
				}
			}
			
			styledText.addFocusListener(new FocusListener(){
				@Override
				public void focusGained(FocusEvent e) {
					MPart mp = (MPart) stacks.get(3).getChildren().get(0);
					OutlineView olv = (OutlineView) mp.getObject();
					olv.getText().setText("File Name : " + file.getName() + "\nFile Size : " + file.length() + " Bytes");
				}
				@Override
				public void focusLost(FocusEvent e) {
				}
			});
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