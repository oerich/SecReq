package secReq.controller.io;

import java.io.File;

import javax.swing.JFileChooser;

import oerich.nlputils.NLPInitializationException;
import oerich.nlputils.dataset.IDataSetDAO;
import secReq.controller.ICommand;
import secReq.controller.ICommandManager;



public class StoreDataSet implements ICommand {

	private ICommandManager manager;
	private JFileChooser fileChooser;
	private File file;

	@Override
	public void execute() {

		if (this.file == null) {
			if (getFileChooser().showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				this.file = getFileChooser().getSelectedFile();
			else
				return;
		}

		try {
			IDataSetDAO.NEW_XML.storeDataSet(this.file.getAbsolutePath(),
					this.manager.getDataSet());
		} catch (NLPInitializationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "Store DataSet";
	}

	@Override
	public void initCommand(ICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	private JFileChooser getFileChooser() {
		if (this.fileChooser == null) {
			this.fileChooser = new JFileChooser(new File("./testfiles"));

		}
		return this.fileChooser;
	}

	@Override
	public ICommand getClone() {
		StoreDataSet storeDataSet = new StoreDataSet();
		storeDataSet.fileChooser = getFileChooser();
		return storeDataSet;
	}

}
