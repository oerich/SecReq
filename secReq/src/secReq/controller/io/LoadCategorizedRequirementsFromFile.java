package secReq.controller.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;

import secReq.controller.ICommand;
import secReq.controller.ICommandManager;
import secReq.model.CategorizedRequirement;
import secReq.model.CategorizedRequirementsModel;



public class LoadCategorizedRequirementsFromFile implements ICommand {

	private File file;
	private JFileChooser fileChooser;
	private ICommandManager manager;

	private CategorizedRequirement[] oldData;

	@Override
	public void execute() {
		CategorizedRequirementsModel model = this.manager.getModel();
		this.oldData = model.getData().toArray(new CategorizedRequirement[0]);

		// model.getData().clear();

		if (this.file == null) {
			if (getFileChooser().showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				this.file = getFileChooser().getSelectedFile();
			else
				return;
		}

		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(this.file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] reqCatType = line.split(";");
				if (reqCatType.length > 1) {
					CategorizedRequirement r = new CategorizedRequirement();
					r.setRequirement(reqCatType[0].trim());
					r.setUserClassification(reqCatType[1].trim());
					if (reqCatType.length > 3) {
						r.setHeuristicClassification(reqCatType[2].trim());
						r.setForTraining(Boolean.parseBoolean(reqCatType[3]
								.trim()));
					}
					model.addCategorizedRequirement(r);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		manager.addUndoableCommand(this);
	}

	private JFileChooser getFileChooser() {
		if (this.fileChooser == null) {
			this.fileChooser = new JFileChooser(new File("./testfiles"));

		}
		return this.fileChooser;
	}

	@Override
	public String getName() {
		return "Add categorized Requirements from file (csv)";
	}

	@Override
	public void undo() {
		this.manager.getModel().getData().clear();
		for (CategorizedRequirement cr : this.oldData) {
			this.manager.getModel().addCategorizedRequirement(cr);
		}
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public void initCommand(ICommandManager manager) {
		if (manager == null)
			throw new RuntimeException();
		this.manager = manager;
	}

	@Override
	public ICommand getClone() {
		LoadCategorizedRequirementsFromFile loadCategorizedRequirementsFromFile = new LoadCategorizedRequirementsFromFile();
		loadCategorizedRequirementsFromFile.fileChooser = getFileChooser();
		return loadCategorizedRequirementsFromFile;
	}
}
