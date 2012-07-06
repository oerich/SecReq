package secReq.controller.training;

import java.util.Random;

import secReq.model.CategorizedRequirement;


public class HighlightTrainingSet_10random extends AbstractHighlightCommand {


	@Override
	public void execute() {
		// TODO undo
		int sec = 0;
		int nonsec = 0;

		int tries = 0;
		Random r = new Random();
		int size = this.manager.getModel().getRowCount();
		while (sec <10 && tries < 1000 ) {
			tries++;
			int guess = r.nextInt(size);
			CategorizedRequirement req = this.manager.getModel().getData().get(guess);	
			if ("sec".equals(req.getUserClassification()) && !req.isForTraining()) {
				req.setForTraining(true);
				sec++;
			}
		}
		tries = 0;
		while (nonsec <10 && tries < 1000 ) {
			tries++;
			int guess = r.nextInt(size);
			CategorizedRequirement req = this.manager.getModel().getData().get(guess);	
			if ("nonsec".equals(req.getUserClassification()) && !req.isForTraining()) {
				req.setForTraining(true);
				nonsec++;
			}
		}
		this.manager.getModel().fireTableDataChanged();
	}

	@Override
	public String getName() {
		return "Select for training (+10 random of each class)";
	}


}
