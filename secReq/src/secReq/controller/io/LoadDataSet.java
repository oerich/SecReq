package secReq.controller.io;

import java.io.File;

import javax.swing.JFileChooser;

import oerich.nlputils.NLPInitializationException;
import oerich.nlputils.dataset.IDataSet;
import oerich.nlputils.dataset.IDataSetDAO;
import secReq.controller.ICommand;
import secReq.controller.ICommandManager;



public class LoadDataSet implements ICommand {

	private ICommandManager manager;
	private IDataSet oldDataSet;
	private JFileChooser fileChooser;
	private File file;

	@SuppressWarnings("deprecation")
	@Override
	public void execute() {

		this.oldDataSet = this.manager.getDataSet();

		if (this.file == null) {
			if (getFileChooser().showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				this.file = getFileChooser().getSelectedFile();
			else
				return;
		}

		try {
			if (IDataSetDAO.NEW_XML
					.checkFileFormat(this.file.getAbsolutePath())) {
				this.manager.setDataSet(IDataSetDAO.NEW_XML
						.createDataSet(this.file.getAbsolutePath()));
			} else /*if (IDataSetDAO.STANDARD_XML.checkFileFormat(this.file
					.getAbsolutePath()))*/ {
				this.manager.setDataSet(IDataSetDAO.STANDARD_XML
						.createDataSet(this.file.getAbsolutePath()));
			}
		} catch (NLPInitializationException e) {
			e.printStackTrace();
		}

		this.manager.addUndoableCommand(this);
	}

	@Override
	public String getName() {
		return "Load DataSet";
	}

	@Override
	public void initCommand(ICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void undo() {
		this.manager.setDataSet(this.oldDataSet);
	}

	private JFileChooser getFileChooser() {
		if (this.fileChooser == null) {
			this.fileChooser = new JFileChooser(new File("./testfiles"));

		}
		return this.fileChooser;
	}

	@Override
	public ICommand getClone() {
		LoadDataSet loadDataSet = new LoadDataSet();
		loadDataSet.fileChooser = getFileChooser();
		return loadDataSet;
	}

}
