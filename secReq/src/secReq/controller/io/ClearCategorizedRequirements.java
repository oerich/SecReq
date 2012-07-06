package secReq.controller.io;

import secReq.controller.ICommand;
import secReq.controller.ICommandManager;
import secReq.model.CategorizedRequirement;
import secReq.model.CategorizedRequirementsModel;

public class ClearCategorizedRequirements implements ICommand {

	private ICommandManager manager;

	private CategorizedRequirement[] oldData;

	@Override
	public void execute() {
		CategorizedRequirementsModel model = this.manager.getModel();
		this.oldData = model.getData().toArray(new CategorizedRequirement[0]);

		model.getData().clear();
		model.fireTableDataChanged();

		manager.addUndoableCommand(this);
	}

	@Override
	public String getName() {
		return "Clear categorized Requirements";
	}

	@Override
	public void undo() {
		this.manager.getModel().getData().clear();
		for (CategorizedRequirement cr : this.oldData) {
			this.manager.getModel().addCategorizedRequirement(cr);
		}
	}

	@Override
	public void initCommand(ICommandManager manager) {
		if (manager == null)
			throw new RuntimeException();
		this.manager = manager;
	}

	@Override
	public ICommand getClone() {
		return new ClearCategorizedRequirements();
	}
}
