package gui;

import java.io.File;
import java.util.ArrayList;

import narc.Narc;
import poketext.PokeText;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class MainWindow {
	private static Text txtMessage;
	public static Narc nrc;
	private static List lstMessages;
	private Button btnSaveSubMessage;
	private Button btnSearchText;
	private static List lstSubMessages;
	private Button btnSaveText;
	private static Label lblOriginalLenght;
	private Label lblLenght;
	private MenuItem mntmsaveNarc;
	private boolean unsaved_text=false;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		final Shell shlDsTextEditor = new Shell(display,SWT.SHELL_TRIM);
		shlDsTextEditor.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent arg0) {
				if(unsaved_text==true)
				{
					MessageBox msg = new MessageBox(shlDsTextEditor.getShell(),SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					msg.setMessage(Messages.getString("Unsaved"));
					msg.setText("DS Text Editor");
					if(msg.open()==SWT.YES)
					{
						FileDialog dlgSave=new FileDialog(shlDsTextEditor.getShell(),SWT.SAVE);
						dlgSave.setText("Save NARC...");
						String[] filter_ext = { "*msg.narc" };
						String[] filter_name = { "NARC Archive (*.narc)" };
						dlgSave.setFilterNames(filter_name);
						dlgSave.setFilterExtensions(filter_ext);
						if (dlgSave.open() != null) {
							try {
								nrc.writeNarc(new File(dlgSave.getFilterPath(),
										dlgSave.getFileName()).getAbsolutePath());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		shlDsTextEditor.setImage(SWTResourceManager.getImage(MainWindow.class, "/res/icon32x32.ico"));
		shlDsTextEditor.setSize(856, 537);
		shlDsTextEditor.setText("DS Text Editor");
		shlDsTextEditor.setLayout(new GridLayout(2, false));
		
		
		Menu menu = new Menu(shlDsTextEditor, SWT.BAR);
		shlDsTextEditor.setMenuBar(menu);
		
		MenuItem mntmfile = new MenuItem(menu, SWT.CASCADE);
		mntmfile.setText(Messages.getString("MainWindow.mntmfile.text")); //$NON-NLS-1$
		
		Menu menu_1 = new Menu(mntmfile);
		mntmfile.setMenu(menu_1);
		
		MenuItem mntmopenNarc = new MenuItem(menu_1, SWT.NONE);
		mntmopenNarc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dlgOpen = new FileDialog(shlDsTextEditor.getShell(),SWT.OPEN);
				dlgOpen.setText("Select NARC...");
				String[] filter_ext = { "*msg.narc" };
				String[] filter_name = { "NARC Archive (*.narc)" };
				dlgOpen.setFilterNames(filter_name);
				dlgOpen.setFilterExtensions(filter_ext);
				if (dlgOpen.open() != null) {
					try {
						nrc = new Narc(new File(dlgOpen.getFilterPath(),
								dlgOpen.getFileName()).getAbsolutePath());
						LoadNARC();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		mntmopenNarc.setText(Messages.getString("MainWindow.mntmopenNarc.text")); //$NON-NLS-1$
		mntmopenNarc.setAccelerator(SWT.CTRL+'O');
		
		mntmsaveNarc = new MenuItem(menu_1, SWT.NONE);
		mntmsaveNarc.setEnabled(false);
		mntmsaveNarc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dlgSave=new FileDialog(shlDsTextEditor.getShell(),SWT.SAVE);
				dlgSave.setText("Save NARC...");
				String[] filter_ext = { "*msg.narc" };
				String[] filter_name = { "NARC Archive (*.narc)" };
				dlgSave.setFilterNames(filter_name);
				dlgSave.setFilterExtensions(filter_ext);
				if (dlgSave.open() != null) {
					try {
						nrc.writeNarc(new File(dlgSave.getFilterPath(),
								dlgSave.getFileName()).getAbsolutePath());
						unsaved_text=false;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		mntmsaveNarc.setText(Messages.getString("MainWindow.mntmsaveNarc.text")); //$NON-NLS-1$
		mntmsaveNarc.setAccelerator(SWT.CTRL+'S');
		
		MenuItem menuItem = new MenuItem(menu_1, SWT.SEPARATOR);
		menuItem.setText(Messages.getString("MainWindow.other.text")); //$NON-NLS-1$
		
		MenuItem mntmexit = new MenuItem(menu_1, SWT.NONE);
		mntmexit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlDsTextEditor.close();
			}
		});
		mntmexit.setAccelerator(SWT.CTRL+'X');
		mntmexit.setText(Messages.getString("MainWindow.mntmexit.text")); //$NON-NLS-1$
		
		MenuItem mntmtools = new MenuItem(menu, SWT.CASCADE);
		mntmtools.setText(Messages.getString("MainWindow.mntmtools.text")); //$NON-NLS-1$
		
		Menu menu_2 = new Menu(mntmtools);
		mntmtools.setMenu(menu_2);
		
		MenuItem mntmsettings = new MenuItem(menu_2, SWT.NONE);
		mntmsettings.setText(Messages.getString("MainWindow.mntmsettings.text")); //$NON-NLS-1$
		
		MenuItem mntmhelp = new MenuItem(menu, SWT.CASCADE);
		mntmhelp.setText(Messages.getString("MainWindow.mntmhelp.text")); //$NON-NLS-1$
		
		Menu menu_3 = new Menu(mntmhelp);
		mntmhelp.setMenu(menu_3);
		
		MenuItem mntmabout = new MenuItem(menu_3, SWT.NONE);
		mntmabout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AboutDialog dlg=new AboutDialog(shlDsTextEditor, 0);
				dlg.open();
			}
		});
		mntmabout.setText(Messages.getString("MainWindow.mntmabout.text")); //$NON-NLS-1$
		
		Group grpMessages = new Group(shlDsTextEditor, SWT.NONE);
		GridData gd_grpMessages = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3);
		gd_grpMessages.widthHint = 234;
		gd_grpMessages.heightHint = 357;
		grpMessages.setLayoutData(gd_grpMessages);
		grpMessages.setText(Messages.getString("MainWindow.grpMessages.text")); //$NON-NLS-1$
		grpMessages.setLayout(new FillLayout(SWT.VERTICAL));
		
		lstMessages = new List(grpMessages, SWT.BORDER | SWT.V_SCROLL);
		lstMessages.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lstSubMessages.removeAll();
				ArrayList<String> list = new ArrayList<String>();
				PokeText.readText(nrc.getFimgEntry().get(lstMessages.getSelectionIndex()),list);
				for(int i=0;i<list.size();i++)
					lstSubMessages.add(list.get(i));
			}
		});
		lstMessages.setEnabled(false);
		
		Group grpSubMessages = new Group(shlDsTextEditor, SWT.NONE);
		GridData gd_grpSubMessages = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpSubMessages.heightHint = 127;
		gd_grpSubMessages.widthHint = 492;
		grpSubMessages.setLayoutData(gd_grpSubMessages);
		grpSubMessages.setText(Messages.getString("MainWindow.group.text")); //$NON-NLS-1$
		grpSubMessages.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		lstSubMessages = new List(grpSubMessages, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		lstSubMessages.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				txtMessage.setText(PokeText.simplifyText(lstSubMessages.getItem(lstSubMessages.getSelectionIndex())));
				lblOriginalLenght.setText(Integer.toString(lstSubMessages.getItem(lstSubMessages.getSelectionIndex()).length()));
			}
		});
		lstSubMessages.setEnabled(false);
		
		Group grpTextEditor = new Group(shlDsTextEditor, SWT.NONE);
		grpTextEditor.setLayout(new GridLayout(5, false));
		grpTextEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTextEditor.setText(Messages.getString("MainWindow.group_1.text")); //$NON-NLS-1$
		
		txtMessage = new Text(grpTextEditor, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtMessage.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if(nrc!=null)lblLenght.setText(Integer.toString(txtMessage.getCharCount()));
			}
		});
		txtMessage.setText(Messages.getString("MainWindow.txtMessage.text")); //$NON-NLS-1$
		txtMessage.setEnabled(false);
		GridData gd_txtMessage = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		gd_txtMessage.heightHint = 168;
		txtMessage.setLayoutData(gd_txtMessage);
		
		btnSaveSubMessage = new Button(grpTextEditor, SWT.NONE);
		btnSaveSubMessage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lstSubMessages.setItem(lstSubMessages.getSelectionIndex(), PokeText.translateText(txtMessage.getText()));
			}
		});
		btnSaveSubMessage.setEnabled(false);
		btnSaveSubMessage.setText(Messages.getString("MainWindow.btnSaveSubMessage.text")); //$NON-NLS-1$
		
		Label lblOriginalTextLenght = new Label(grpTextEditor, SWT.NONE);
		lblOriginalTextLenght.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOriginalTextLenght.setText(Messages.getString("MainWindow.lblNewLabel.text")); //$NON-NLS-1$
		
		lblOriginalLenght = new Label(grpTextEditor, SWT.NONE);
		GridData gd_lblOriginalLenght = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblOriginalLenght.widthHint = 80;
		lblOriginalLenght.setLayoutData(gd_lblOriginalLenght);
		lblOriginalLenght.setText(Messages.getString("MainWindow.lblOriginalLenght.text")); //$NON-NLS-1$
		
		Label lblTextLenght = new Label(grpTextEditor, SWT.NONE);
		lblTextLenght.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTextLenght.setText(Messages.getString("MainWindow.lblTextLenght.text")); //$NON-NLS-1$
		
		lblLenght = new Label(grpTextEditor, SWT.NONE);
		GridData gd_lblLenght = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblLenght.widthHint = 80;
		lblLenght.setLayoutData(gd_lblLenght);
		lblLenght.setText(Messages.getString("MainWindow.lblLenght.text")); //$NON-NLS-1$
		
		Composite composite = new Composite(shlDsTextEditor, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 31;
		composite.setLayoutData(gd_composite);
		
		btnSearchText = new Button(composite, SWT.NONE);
		btnSearchText.setImage(SWTResourceManager.getImage(MainWindow.class, "/res/find.ico"));
		btnSearchText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				SearchDialog src=new SearchDialog(shlDsTextEditor, 0);
				src.open();
			}
		});
		btnSearchText.setEnabled(false);
		btnSearchText.setText(Messages.getString("MainWindow.btnSearchText.text")); //$NON-NLS-1$
		
		btnSaveText = new Button(composite, SWT.NONE);
		btnSaveText.setImage(SWTResourceManager.getImage(MainWindow.class, "/res/save.ico"));
		btnSaveText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (lstMessages.getSelectionCount()>0) {
					ArrayList<String> list = new ArrayList<String>();
					for (int i = 0; i < lstSubMessages.getItemCount(); i++)
						list.add(lstSubMessages.getItem(i));
					PokeText.saveText(
							nrc.getFimgEntry().get(
									lstMessages.getSelectionIndex()), list);
					unsaved_text = true;
				}
			}
		});
		btnSaveText.setEnabled(false);
		btnSaveText.setText(Messages.getString("MainWindow.btnSaveText.text")); //$NON-NLS-1$
		
		Label lblCopyright = new Label(shlDsTextEditor, SWT.NONE);
		lblCopyright.setAlignment(SWT.CENTER);
		lblCopyright.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblCopyright.setText(Messages.getString("MainWindow.lblCopyright.text"));

		shlDsTextEditor.open();
		shlDsTextEditor.layout();
		while (!shlDsTextEditor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void LoadNARC() {
		for (int i = 0; i < (int) nrc.getnumEntries(); i++) {
			lstMessages.add(Messages.getString("Bank")+i);
		}
		lstMessages.setEnabled(true);
		lstSubMessages.setEnabled(true);
		txtMessage.setEnabled(true);
		btnSaveSubMessage.setEnabled(true);
		btnSearchText.setEnabled(true);
		btnSaveText.setEnabled(true);
		mntmsaveNarc.setEnabled(true);
	}
	public static List getLstMessages() {
		return lstMessages;
	}
	public Button getBtnSaveSubMessage() {
		return btnSaveSubMessage;
	}
	public Button getBtnSearchText() {
		return btnSearchText;
	}
	public static List getLstSubMessages() {
		return lstSubMessages;
	}
	public Button getBtnSaveText() {
		return btnSaveText;
	}
	public static Text getTxtMessage() {
		return txtMessage;
	}
	public static Label getLblOriginalLenght() {
		return lblOriginalLenght;
	}
	public Label getLblLenght() {
		return lblLenght;
	}
}
