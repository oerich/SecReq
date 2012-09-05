package secReq.controller.evaluation;

import java.util.LinkedList;
import java.util.List;

import secReq.model.CategorizedRequirement;


public class Statistics {

	private static final String TEXT = "Recall: %f | Precision: %f | F-Val: %f | Rel: %d | Found: %d | Relevant-Found: %d";
	private boolean includeTrainingSet;
	private List<CategorizedRequirement> data;

	public Statistics(List<CategorizedRequirement> data) {
		this.data = data;
	}

	public Statistics() {
		this.data = new LinkedList<CategorizedRequirement>();
	}

	public double getRecall() {
		return (double) getRelevantFound() / (double) getRelevant();
	}

	public double getPrecision() {
		return (double) getRelevantFound() / (double) getFound();
	}

	public double getFMeasure() {
		double prec = getPrecision();
		double rec = getRecall();
		return (2.0 * prec * rec) / (prec + rec);
	}

	public int getFound() {
		int ret = 0;
		for (CategorizedRequirement cr : this.data) {
			if (valid(cr) && cr.getHeuristicClassification().startsWith("sec"))
				ret++;
		}
		return ret;
	}

	public int getRelevantFound() {
		int ret = 0;
		for (CategorizedRequirement cr : this.data) {
			if (valid(cr) && cr.getHeuristicClassification().startsWith("sec")
					&& cr.getUserClassification().startsWith("sec"))
				ret++;
		}
		return ret;
	}

	public int getRelevant() {
		int ret = 0;
		for (CategorizedRequirement cr : this.data) {
			if (valid(cr) && cr.getUserClassification().startsWith("sec"))
				ret++;
		}
		return ret;
	}

	@Override
	public String toString() {
		return String.format(TEXT, getRecall(), getPrecision(), getFMeasure(),
				getRelevant(), getFound(), getRelevantFound());
	}

	public boolean includesTrainingSet() {
		return this.includeTrainingSet;
	}

	public void setIncludesTrainingSet(boolean val) {
		this.includeTrainingSet = val;
	}

	private boolean valid(CategorizedRequirement cr) {
		if (includesTrainingSet())
			return true;

		if (cr.isForTraining())
			return false;

		return true;
	}

	public EvaluationResult evaluate(List<CategorizedRequirement> data) {
		this.data = data;

		EvaluationResult ret = new EvaluationResult();
		ret.setPrecision(getPrecision());
		ret.setRecall(getRecall());
		return ret;
	}
}
