package secReq.controller.classification;

import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;
import secReq.controller.ICommand;
import secReq.controller.ICommandManager;
import secReq.model.CategorizedRequirement;

public abstract class AbstractClassificationCommand implements ICommand {

	private ICommandManager manager;

	protected abstract IClassifier<IDataSet> getClassifier(IDataSet dataSet);

	@Override
	public void initCommand(ICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute() {
		IClassifier<IDataSet> classifier = getClassifier(getDataSet());
		this.manager.setLastClassifier(classifier);
		for (CategorizedRequirement cr : this.manager.getModel().getData()) {
			double val = classifier.classify(cr.getRequirement());
			if (val > classifier.getMatchValue())
				cr.setHeuristicClassification("sec (" + val + ")");
			else
				cr.setHeuristicClassification("nonsec (" + val + ")");
		}
		this.manager.getModel().fireTableDataChanged();
	}

	protected IDataSet getDataSet() {
		return this.manager.getDataSet();
	}
}
