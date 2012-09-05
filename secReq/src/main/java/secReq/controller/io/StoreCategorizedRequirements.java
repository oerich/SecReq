package secReq.controller.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import secReq.controller.ICommand;
import secReq.controller.ICommandManager;
import secReq.model.CategorizedRequirement;



public class StoreCategorizedRequirements implements ICommand {

	private File file;
	private JFileChooser fileChooser;
	private ICommandManager manager;

	@Override
	public void execute() {
		if (this.file == null) {
			if (getFileChooser().showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				this.file = getFileChooser().getSelectedFile();
			else
				return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(this.file));
			for (CategorizedRequirement cr : this.manager.getModel().getData()) {
				writer.write(cr.getRequirement() + ";"
						+ cr.getUserClassification() + ";"
						+ cr.getHeuristicClassification() + ";"
						+ cr.isForTraining() + "\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private JFileChooser getFileChooser() {
		if (this.fileChooser == null) {
			this.fileChooser = new JFileChooser(new File("./testfiles"));

		}
		return this.fileChooser;
	}

	@Override
	public String getName() {
		return "Store categorized Requirements";
	}

	@Override
	public void undo() {
		// TODO
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
		StoreCategorizedRequirements storeCategorizedRequirements = new StoreCategorizedRequirements();
		storeCategorizedRequirements.fileChooser = fileChooser;
		return storeCategorizedRequirements;
	}
}
