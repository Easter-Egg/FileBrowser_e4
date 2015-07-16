package filebrowser_e4.Editors;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

public class ImageEditor {
	public ScrollBar hBar = null;
	public ScrollBar vBar = null;
	public Point origin = new Point (0, 0);
	public Image image;
	public Canvas canvas;
	
	@Inject
	private ESelectionService ss;
	
	@Inject
	public ImageEditor() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
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
	}

	@Focus
	public void onFocus() {
		canvas.setFocus();
	}
}
