package filebrowser_e4.Editors;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import filebrowser_e4.Views.OutlineView;

public class ImageEditor {
	private ScrollBar hBar = null;
	private ScrollBar vBar = null;
	private Point origin = new Point (0, 0);
	private Image image;
	private Canvas canvas;
	
	@Inject
	private EModelService ms;
	
	@Inject
	private MWindow window;
			
	@Inject
	private ESelectionService ss;
	
	@Inject
	public ImageEditor() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(">>>>> width : " + image.getBounds().width + ", height : " + image.getBounds().height + " (canvas : " + canvas.getClientArea().width + "x" + canvas.getClientArea().height +")");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
								
			}
			
		});
		tltmNewItem.setWidth(16);
		tltmNewItem.setText("INFO");
		

		List<MPartStack> stacks = ms.findElements(window, null, MPartStack.class, null);
		String loc = ss.getSelection().toString();
	
		File file = new File(loc);
		image = new Image(parent.getDisplay(), file.getAbsolutePath());
		canvas = new Canvas(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.None);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		hBar = canvas.getHorizontalBar();
		
		vBar = canvas.getVerticalBar();
		
		canvas.addListener (SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Rectangle rect = image.getBounds();
				Rectangle client = canvas.getClientArea();
				
				hBar.setMaximum (rect.width);
				vBar.setMaximum (rect.height);
				hBar.setThumb (Math.min(rect.width, client.width));
				vBar.setThumb (Math.min(rect.height, client.height));
				
				int hPage = rect.width - client.width;
				int vPage = rect.height - client.height;
				int hSel = hBar.getSelection();
				int vSel = vBar.getSelection();
				
				if (hSel >= hPage){
					if (hPage <= 0) 
						hSel = 0;
					origin.x = -hSel;
				}
				
				if (vSel >= vPage){
					if (vPage <= 0) 
						vSel = 0;
					origin.y = -vSel;
				}
				
				canvas.redraw ();
			}
		});
		
		canvas.addListener (SWT.Paint, new Listener () {
			@Override
			public void handleEvent (Event e) {
				GC gc = e.gc;
				gc.drawImage (image, origin.x, origin.y);
				Rectangle rect = image.getBounds ();
				Rectangle client = canvas.getClientArea ();
				int marginWidth = client.width - rect.width;
				if (marginWidth > 0) {
					gc.fillRectangle (rect.width, 0, marginWidth, client.height);
				}
				int marginHeight = client.height - rect.height;
				if (marginHeight > 0) {
					gc.fillRectangle (0, rect.height, client.width, marginHeight);
				}
			}
		});
		hBar.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				int hSel = hBar.getSelection();
				int destX = -hSel - origin.x;
				Rectangle rect = image.getBounds();
				canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				origin.x = -hSel;
			}
		});
		vBar.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				int vSel = vBar.getSelection();
				int destY = -vSel - origin.y;
				Rectangle rect = image.getBounds();
				canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				origin.y = -vSel;
			}
		});
		
		canvas.addFocusListener(new FocusListener(){

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
	
	@Focus
	public void onFocus() {
		canvas.setFocus();
	}
}
