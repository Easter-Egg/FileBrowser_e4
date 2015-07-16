package filebrowser_e4.Views;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import filebrowser_e4.Editors.TextEditor;
import filebrowser_e4.Utils.FTCP;
import filebrowser_e4.Utils.FTLP;

@SuppressWarnings("restriction")
public class BrowserView {
	
	private Text location;
	private Tree t;
	private TreeViewer tv;
	
	@Inject
	ESelectionService selectionService;
	private IDoubleClickListener l = new IDoubleClickListener() {
		
		@Override
		public void doubleClick(DoubleClickEvent event) {
			// TODO Auto-generated method stub
			Object first =((IStructuredSelection)event.getSelection()).getFirstElement();
			selectionService.setSelection(first);
			String loc = selectionService.getSelection().toString();
			location.setText(loc);
			
			File file = new File(loc);

			if (file.isDirectory()) {
				if(tv.getExpandedState(file))
					tv.setExpandedState(file, false);
				
				else
					tv.setExpandedState(file, true);
			}
			
			//TextEditor editor = new TextEditor();
			if (file.getName().endsWith(".txt")) {
				try {
					String content = new Scanner(file).useDelimiter("\\A").next();
					System.out.println(content);
					TextEditor e = new TextEditor();
					//editor.styledText.setText(new Scanner(file).useDelimiter("\\A").next());
				}
				catch(IOException e) {
				    e.printStackTrace();
				}
			}
			
		}
	};
	
	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		location = new Text(parent, SWT.BORDER);
		location.setMessage("print double clicked file location");
		location.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		t = new Tree(parent, SWT.V_SCROLL | SWT.H_SCROLL);

		int curStyle = OS.GetWindowLong(t.handle, OS.GWL_STYLE);
		int newStyle = curStyle | OS.TVS_HASLINES;
		OS.SetWindowLong(t.handle, OS.GWL_STYLE, newStyle);
		
		tv = new TreeViewer(t);
		tv.setContentProvider(new FTCP());
		tv.setLabelProvider(new FTLP());
		tv.setInput(File.listRoots());
		tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		tv.addDoubleClickListener(l);
	}

	@Focus
	public void setFocus() {
		tv.getTree().setFocus();
	}
	

	public TreeViewer getTreeViewer(){
		return tv;
	}

}
