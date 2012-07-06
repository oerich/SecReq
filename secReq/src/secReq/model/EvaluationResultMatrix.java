package secReq.model;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import secReq.controller.evaluation.EvaluationResult;


public class EvaluationResultMatrix implements TableModel {

	private List<TableModelListener> listeners = new LinkedList<TableModelListener>();
	private EvaluationResult[][] results;
	private int size;

	@Override
	public void addTableModelListener(TableModelListener l) {
		this.listeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return this.size-1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return String.valueOf(columnIndex + 1);
	}

	@Override
	public int getRowCount() {
		return this.size;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return getResultFor(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		this.listeners.remove(l);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		setResultFor(rowIndex, columnIndex, (EvaluationResult) aValue);
	}

	public EvaluationResult getResultFor(int bucketNo, int trainingSize) {
		return this.results[bucketNo][trainingSize];
	}

	public void setResultFor(int bucketNo, int trainingSize,
			EvaluationResult result) {
		this.results[bucketNo][trainingSize] = result;
	}

	public void initEvaluationMatrix(int n) {
		this.size = n;
		this.results = new EvaluationResult[n][n - 1];
	}

	public EvaluationResult[] computeAverageResults() {
		EvaluationResult[] results = new EvaluationResult[this.size - 1];
		for (int trainingSize = 0; trainingSize < results.length; trainingSize++) {
			results[trainingSize] = new EvaluationResult();
			// We cannot use NaN values in the average
			int precNaN = 0;
			int recNaN = 0;
			for (int evalBucketIndex = 0; evalBucketIndex < this.size; evalBucketIndex++) {
				EvaluationResult result = getResultFor(evalBucketIndex,
						trainingSize);
				if (Double.isNaN(result.getRecall()))
					recNaN++;
				else
					results[trainingSize].setRecall(results[trainingSize]
							.getRecall()
							+ result.getRecall());
				if (Double.isNaN(result.getPrecision()))
					precNaN++;
				else
					results[trainingSize].setPrecision(results[trainingSize]
							.getPrecision()
							+ result.getPrecision());
			}
			results[trainingSize].setRecall(results[trainingSize].getRecall()
					/ (this.size - recNaN));
			results[trainingSize].setPrecision(results[trainingSize]
					.getPrecision()
					/ (this.size - precNaN));
		}
		return results;
	}

}
