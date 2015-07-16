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
	private Tree t;
	private TreeViewer tv;
	
	@Inject
	private ESelectionService ss;
	@Inject
	private EPartService partService;
	@Inject
	private EModelService modelService;
	@Inject
	private MWindow window;
	
	
	private IDoubleClickListener l = new IDoubleClickListener() {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			Object first =((IStructuredSelection)event.getSelection()).getFirstElement();
			ss.setSelection(first);
			String loc = ss.getSelection().toString();
			File file = new File(loc);

			
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, H:mm:ss:SSS"); 
			System.out.println(sdf.format(dt).toString() +  "   >>>>>   " + loc);
			
			if (file.isDirectory()) {
				if(tv.getExpandedState(file))
					tv.setExpandedState(file, false);
				else
					tv.setExpandedState(file, true);
			} else {
				FileOpen fo = new FileOpen(ss,window,partService,modelService);
				fo.open();
			}

		}
	};
	
	private ISelectionChangedListener l2 = new ISelectionChangedListener() {
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
		tv.addSelectionChangedListener(l2);
	}

	@Focus
	public void setFocus() {
		tv.getTree().setFocus();
	}

	public TreeViewer getTreeViewer(){
		return tv;
	}
}
