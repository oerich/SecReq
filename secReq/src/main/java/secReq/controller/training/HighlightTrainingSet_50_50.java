package secReq.controller.training;

import secReq.model.CategorizedRequirement;

public class HighlightTrainingSet_50_50 extends AbstractHighlightCommand {

	@Override
	public void execute() {
		// TODO undo
		int sec = 0;
		int nonsec = 0;

		for (CategorizedRequirement cr : this.manager.getModel().getData()) {
			if ("sec".equals(cr.getUserClassification()) && sec < 50) {
				cr.setForTraining(true);
				sec++;
			} else if ("nonsec".equals(cr.getUserClassification())
					&& nonsec < 50) {
				cr.setForTraining(true);
				nonsec++;
			}
		}
		this.manager.getModel().fireTableDataChanged();
	}

	@Override
	public String getName() {
		return "Select for training (first 50 %)";
	}

}
