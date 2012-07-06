package secReq.controller.training;

import secReq.model.CategorizedRequirement;

public class HighlightTrainingSet_EverySecond extends AbstractHighlightCommand {

	@Override
	public void execute() {
		// TODO undo
		boolean secSwitch = false;
		for (CategorizedRequirement cr : this.manager.getModel().getData()) {
			secSwitch = !secSwitch;
			cr.setForTraining(secSwitch);
		}
		this.manager.getModel().fireTableDataChanged();
	}

	@Override
	public String getName() {
		return "Select for training (alternately)";
	}

}