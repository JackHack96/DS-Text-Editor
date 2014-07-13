package gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class AboutDialog extends Dialog {

	protected Object result;
	protected Shell shlAboutDsText;
	private Text txtCreditsToPichu;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAboutDsText.open();
		shlAboutDsText.layout();
		Display display = getParent().getDisplay();
		while (!shlAboutDsText.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlAboutDsText = new Shell(getParent(), SWT.BORDER | SWT.TITLE);
		shlAboutDsText.setSize(622, 541);
		shlAboutDsText.setText(Messages.getString("AboutDialog.shlAboutDsText.text")); //$NON-NLS-1$
		shlAboutDsText.setLayout(new GridLayout(1, false));
		
		Label lblImage = new Label(shlAboutDsText, SWT.NONE);
		lblImage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblImage.setImage(SWTResourceManager.getImage(AboutDialog.class, "/res/logo.png"));
		lblImage.setText(Messages.getString("AboutDialog.label.text")); //$NON-NLS-1$
		
		Label lblDsTextEditor = new Label(shlAboutDsText, SWT.NONE);
		lblDsTextEditor.setText(Messages.getString("AboutDialog.lblDsTextEditor.text")); //$NON-NLS-1$
		
		Link link = new Link(shlAboutDsText, SWT.NONE);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Program.launch("http://www.hackromtools.altervista.org/");
			}
		});
		link.setText(Messages.getString("AboutDialog.link.text")); //$NON-NLS-1$
		
		txtCreditsToPichu = new Text(shlAboutDsText, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtCreditsToPichu.setEditable(false);
		txtCreditsToPichu.setText(Messages.getString("AboutDialog.txtCreditsToPichu.text")); //$NON-NLS-1$
		txtCreditsToPichu.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnWow = new Button(shlAboutDsText, SWT.NONE);
		btnWow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlAboutDsText.close();
			}
		});
		GridData gd_btnWow = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnWow.widthHint = 110;
		btnWow.setLayoutData(gd_btnWow);
		btnWow.setText(Messages.getString("AboutDialog.btnWow.text")); //$NON-NLS-1$

	}
}
