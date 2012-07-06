package secReq.controller.training;

import secReq.model.CategorizedRequirement;

public class HighlightTrainingSet_All extends AbstractHighlightCommand {

	@Override
	public void execute() {
		// TODO undo
		for (CategorizedRequirement cr : this.manager.getModel().getData()) {
			cr.setForTraining(true);
		}
		this.manager.getModel().fireTableDataChanged();
	}

	@Override
	public String getName() {
		return "Select for training (all)";
	}

}