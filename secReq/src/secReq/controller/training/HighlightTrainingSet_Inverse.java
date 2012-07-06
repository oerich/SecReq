package secReq.controller.training;

import secReq.model.CategorizedRequirement;

public class HighlightTrainingSet_Inverse extends AbstractHighlightCommand {

	@Override
	public void execute() {
		// TODO undo
		for (CategorizedRequirement cr : this.manager.getModel().getData()) {
			cr.setForTraining(!cr.isForTraining());
		}
		this.manager.getModel().fireTableDataChanged();
	}

	@Override
	public String getName() {
		return "Select for training (inverse selection)";
	}

}