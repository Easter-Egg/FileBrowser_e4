package filebrowser_e4.Views;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ConsoleView {
	private Text text;
	
	@Inject
	public ConsoleView() {
		
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_text.widthHint = 94;
		text.setLayoutData(gd_text);
		OutputStream out = new OutputStream(){
			@Override
			public void write(int i) throws IOException {
				if(text.isDisposed()) return;
				text.append(String.valueOf((char) i));
			}
		};
		
		final PrintStream oldOut = System.out;
		System.setOut(new PrintStream(out));
		text.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				System.setOut(oldOut);
			}
		});
	}

	@Focus
	public void setFocus() {
		text.setFocus();
	}

}
