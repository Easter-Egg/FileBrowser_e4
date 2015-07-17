package filebrowser_e4.Views;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import filebrowser_e4.Utils.FTCP;
import filebrowser_e4.Utils.FTLP;
import filebrowser_e4.Utils.FileOpen;

@SuppressWarnings("restriction")
public class BrowserView {
	
	private Text location;
	private Tree tree;
	private TreeViewer treeViewer;
	
	@Inject
	private ESelectionService selectionService;
	@Inject
	private EPartService partService;
	@Inject
	private EModelService modelService;
	@Inject
	private MWindow window;
	
	
	private IDoubleClickListener doubleClickLisntener = new IDoubleClickListener() {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			Object first =((IStructuredSelection)event.getSelection()).getFirstElement();
			selectionService.setSelection(first);
			String loc = selectionService.getSelection().toString();
			File file = new File(loc);
			Date dt = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, H:mm:ss:SSS"); 
			System.out.println(dateFormat.format(dt).toString() +  "   >>>>>   " + loc);
			
			if (file.isDirectory()) {
				if(treeViewer.getExpandedState(file))
					treeViewer.setExpandedState(file, false);
				else
					treeViewer.setExpandedState(file, true);
			} else {
				FileOpen fo = new FileOpen(selectionService,window,partService,modelService);
				fo.open();
			}

		}
	};
	
	private ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent e) {
			location.setText(e.getSelection().toString());
		}
	};
	
	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		location = new Text(parent, SWT.BORDER);
		location.setMessage("print double clicked file location");
		location.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		tree = new Tree(parent, SWT.V_SCROLL | SWT.H_SCROLL);

		int curStyle = OS.GetWindowLong(tree.handle, OS.GWL_STYLE);
		int newStyle = curStyle | OS.TVS_HASLINES;
		OS.SetWindowLong(tree.handle, OS.GWL_STYLE, newStyle);
		
		treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(new FTCP());
		treeViewer.setLabelProvider(new FTLP());
		treeViewer.setInput(File.listRoots());
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		treeViewer.addDoubleClickListener(doubleClickLisntener);
		treeViewer.addSelectionChangedListener(selectionChangedListener);
	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	public TreeViewer getTreeViewer(){
		return treeViewer;
	}
}
