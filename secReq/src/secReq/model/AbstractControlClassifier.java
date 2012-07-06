/**
 * 
 */
package secReq.model;

import javax.swing.table.TableModel;

import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;


abstract class AbstractControlClassifier implements
		IClassifier<IDataSet> {

	public abstract double classify(String text)
			throws IllegalArgumentException;

	@Override
	public double getMatchValue() {
		return 0;
	}

	@Override
	public void init(IDataSet initData) throws Exception {
	}

	@Override
	public boolean isMatch(String text) throws IllegalArgumentException {
		return classify(text) > getMatchValue();
	}

	@Override
	public void setMatchValue(double value) {
	}

	@Override
	public double getMinimalValue() {
		return 0;
	}

	@Override
	public double getMaximumValue() {
		return 1;
	}

	@Override
	public TableModel explainClassification(String text) {
		return null;
	}

}