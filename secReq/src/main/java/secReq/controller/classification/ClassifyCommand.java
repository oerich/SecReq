package secReq.controller.classification;

import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;
import secReq.controller.ICommand;

public class ClassifyCommand extends AbstractClassificationCommand {

	private IClassifier<IDataSet> classifier;

	public ClassifyCommand(IClassifier<IDataSet> classifier) {
		this.classifier = classifier;
	}
	
	@Override
	protected IClassifier<IDataSet> getClassifier(IDataSet dataSet) {
		try {
			this.classifier.init(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.classifier;
	}

	@Override
	public String getName() {
		return this.classifier.toString();
	}

	@Override
	public ICommand getClone() {
		return new ClassifyCommand(classifier);
	}

	
}
