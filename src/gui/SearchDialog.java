package gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import poketext.PokeText;

import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.List;

/**
 * @author JackHack96 (aka Matteo Iervasi)
 * @copyright © 2014
 * @license GPLv3
 * @website http://www.hackromtools.altervista.org/
 */
public class SearchDialog extends Dialog {

	protected Object result;
	protected Shell shlSearchForText;
	private Text txtTextToSearch;
	private Button btnCaseSensitive;
	private ProgressBar progressBar;
	private List lstSearchResults;
	private ArrayList<Integer> banks;
	private ArrayList<Integer> mess;
	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SearchDialog(Shell parent, int style) {
		super(parent, style);
		setText("");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlSearchForText.open();
		shlSearchForText.layout();
		Display display = getParent().getDisplay();
		while (!shlSearchForText.isDisposed()) {
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
		shlSearchForText = new Shell(getParent(), SWT.DIALOG_TRIM);
		shlSearchForText.setSize(766, 427);
		shlSearchForText.setText(Messages.getString("SearchDialog.shlSearchForText.text")); //$NON-NLS-1$
		shlSearchForText.setLayout(new GridLayout(3, false));
		
		Label lblTypeYourText = new Label(shlSearchForText, SWT.NONE);
		lblTypeYourText.setText(Messages.getString("SearchDialog.lblTypeYourText.text")); //$NON-NLS-1$
		
		btnCaseSensitive = new Button(shlSearchForText, SWT.CHECK);
		btnCaseSensitive.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCaseSensitive.setText(Messages.getString("SearchDialog.btnCaseSensitive.text")); //$NON-NLS-1$
		
		Group grpSearchResults = new Group(shlSearchForText, SWT.NONE);
		grpSearchResults.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_grpSearchResults = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4);
		gd_grpSearchResults.widthHint = 193;
		grpSearchResults.setLayoutData(gd_grpSearchResults);
		grpSearchResults.setText(Messages.getString("SearchDialog.group.text"));
		
		lstSearchResults = new List(grpSearchResults, SWT.BORDER | SWT.V_SCROLL);
		lstSearchResults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MainWindow.getLstMessages().setSelection(banks.get(lstSearchResults.getSelectionIndex()));
				MainWindow.getLstSubMessages().removeAll();
				ArrayList<String> list = new ArrayList<String>();
				PokeText.readText(MainWindow.nrc.getFimgEntry().get(MainWindow.getLstMessages().getSelectionIndex()),list);
				for(int i=0;i<list.size();i++)
					MainWindow.getLstSubMessages().add(list.get(i));
				MainWindow.getLstSubMessages().setSelection(mess.get(lstSearchResults.getSelectionIndex()));
				MainWindow.getTxtMessage().setText(MainWindow.getLstSubMessages().getItem(MainWindow.getLstSubMessages().getSelectionIndex()));
				MainWindow.getLblOriginalLenght().setText(Integer.toString(MainWindow.getLstSubMessages().getItem(MainWindow.getLstSubMessages().getSelectionIndex()).length()));
			}
		});
		
		txtTextToSearch = new Text(shlSearchForText, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_txtTextToSearch = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_txtTextToSearch.widthHint = 492;
		gd_txtTextToSearch.heightHint = 86;
		txtTextToSearch.setLayoutData(gd_txtTextToSearch);
		
		Composite composite = new Composite(shlSearchForText, SWT.NONE);
		composite.setLayout(new GridLayout(3, true));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_composite.widthHint = 202;
		composite.setLayoutData(gd_composite);
		
		Button btnSearchText = new Button(composite, SWT.NONE);
		btnSearchText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(banks!=null && mess!=null)
				{
					banks.clear();
					mess.clear();
				}
				banks=new ArrayList<Integer>();
				mess=new ArrayList<Integer>();
				lstSearchResults.removeAll();
				if(txtTextToSearch.getText().length()>0)
				{
					ArrayList<String> tmpText=new ArrayList<String>();
					progressBar.setMaximum((int) MainWindow.nrc.getnumEntries());
					for(int i=0;i<MainWindow.nrc.getnumEntries();i++)
					{
						PokeText.readText(MainWindow.nrc.getFimgEntry().get(i), tmpText);
						for (int j=0;j<tmpText.size();j++)
						{
							if (btnCaseSensitive.getSelection())
							{
								if(tmpText.get(j).contains(txtTextToSearch.getText()))
								{
									lstSearchResults.add(Messages.getString("Bank")+i+Messages.getString("Message")+j);
									banks.add(i);
									mess.add(j);
								}
							}
							else
							{
								if(tmpText.get(j).toLowerCase().contains(txtTextToSearch.getText().toLowerCase()))
								{
									lstSearchResults.add(Messages.getString("Bank")+i+Messages.getString("Message")+j);
									banks.add(i);
									mess.add(j);
								}
							}
						}
						tmpText.clear();
						progressBar.setSelection(i);
					}
				}
			}
		});
		btnSearchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnSearchText.setText(Messages.getString("SearchDialog.btnSearchText.text")); //$NON-NLS-1$
		
		Button btnClearText = new Button(composite, SWT.NONE);
		btnClearText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				txtTextToSearch.setText("");
			}
		});
		btnClearText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearText.setText(Messages.getString("SearchDialog.btnClearText.text")); //$NON-NLS-1$
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlSearchForText.close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCancel.setText(Messages.getString("SearchDialog.btnCancel.text"));
		
		Group grpSearchProgress = new Group(shlSearchForText, SWT.NONE);
		grpSearchProgress.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_grpSearchProgress = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_grpSearchProgress.heightHint = 26;
		grpSearchProgress.setLayoutData(gd_grpSearchProgress);
		grpSearchProgress.setText(Messages.getString("SearchDialog.grpSearchResults.text"));
		
		progressBar = new ProgressBar(grpSearchProgress, SWT.SMOOTH);

	}
	public Button getBtnCaseSensitive() {
		return btnCaseSensitive;
	}
	public Text getTxtTextToSearch() {
		return txtTextToSearch;
	}
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	public List getLstSearchResults() {
		return lstSearchResults;
	}
}
