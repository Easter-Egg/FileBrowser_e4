package filebrowser_e4.Utils;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ToolControl {
	
	private Label fileOpenLabel;
	private Label fileSizeLabel;
	private String location;
	
	@SuppressWarnings("unused")
	private Label label;

	
	@Inject @Optional
	private void FileOpenHandler(@UIEventTopic("FILEOPEN") org.osgi.service.event.Event event){
		location = ((Object) event.getProperty("org.eclipse.e4.data")).toString();
		File file = new File(location);
		fileOpenLabel.setText(file.getName() + " is opened");
		fileSizeLabel.setText(file.length() + " Bytes");
	}
	
	@Inject
	public ToolControl() {
		
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		GridData gd_sashForm = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_sashForm.heightHint = 20;
		sashForm.setLayoutData(gd_sashForm);
		
		fileOpenLabel = new Label(sashForm, SWT.NONE);
		fileOpenLabel.setText("Status Line is ready");
		
		label = new Label(sashForm, SWT.SEPARATOR | SWT.VERTICAL);
		
		fileSizeLabel = new Label(sashForm, SWT.NONE);
		fileSizeLabel.setText("File size is here");
		sashForm.setWeights(new int[] {269, 10, 153});
	}

	@Focus
	public void setFocus() {
	}
	
}
