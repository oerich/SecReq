package secReq.controller.training;

import secReq.model.CategorizedRequirement;

public class HighlightTrainingSet_None extends AbstractHighlightCommand {

	@Override
	public void execute() {
		// TODO undo
		for (CategorizedRequirement cr : this.manager.getModel().getData()) {
			cr.setForTraining(false);
		}
		this.manager.getModel().fireTableDataChanged();
	}

	@Override
	public String getName() {
		return "Select for training (none)";
	}

}