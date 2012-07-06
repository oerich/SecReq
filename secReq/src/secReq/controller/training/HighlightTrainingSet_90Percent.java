package secReq.controller.training;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import secReq.model.CategorizedRequirement;


public class HighlightTrainingSet_90Percent extends AbstractHighlightCommand {

	@Override
	public void execute() {
		int reqTotal = this.manager.getModel().getRowCount();
		int amount = reqTotal * 9 / 10;

		Random r = new Random();
		List<CategorizedRequirement> data = new LinkedList<CategorizedRequirement>();
		data.addAll(this.manager.getModel().getData());
		while (data.size() > (reqTotal - amount)) {
			data.remove(r.nextInt(data.size())).setForTraining(true);
		}
		this.manager.getModel().fireTableDataChanged();
	}

	@Override
	public String getName() {
		return "Select for training (90%)";
	}

}
