/**
 * 
 */
package secReq.controller.evaluation;

public class EvaluationResult {
	private double recall;
	private double precision;

	
	public double getRecall() {
		return recall;
	}


	public void setRecall(double recall) {
		this.recall = recall;
	}


	public double getPrecision() {
		return precision;
	}


	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getFMeasure() {
		return (2 * recall * precision) / (recall + precision);
	}

	@Override
	public String toString() {
		return "(r:" + getRecall() + "/p:" + getPrecision() + "/f:"
				+ getFMeasure() + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		return toString().equals(other.toString());
	}
}