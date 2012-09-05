package secReq.model;

import java.util.LinkedList;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;

import secReq.controller.evaluation.EvaluationResult;

public class KFoldCrossValidationModel implements CategoryDataset {

	private static final String F_MEASURE = "f-measure";
	private static final String PRECISION = "precision";
	private static final String RECALL = "recall";
	private List<Comparable<Integer>> columnKeys;
	private List<Comparable<String>> rowKeys;
	private List<DatasetChangeListener> dataChangeListeners = new LinkedList<DatasetChangeListener>();
	private DatasetGroup group;
	private EvaluationResult[] result;

	public KFoldCrossValidationModel() {
		setResults(new EvaluationResult[0]);
	}

	public void setResults(EvaluationResult[] result) {
		this.result = result;
		if (this.columnKeys == null)
			this.columnKeys = new LinkedList<Comparable<Integer>>();
		else
			this.columnKeys.clear();
		for (int i = 0; i < result.length; i++) {
			this.columnKeys.add(new Category(i));
		}

		if (this.rowKeys == null) {
			this.rowKeys = new LinkedList<Comparable<String>>();
			this.rowKeys.add(RECALL);
			this.rowKeys.add(PRECISION);
			this.rowKeys.add(F_MEASURE);
		}

		for (DatasetChangeListener l : this.dataChangeListeners)
			l.datasetChanged(new DatasetChangeEvent(this, this));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int getColumnIndex(Comparable c) {
		return this.columnKeys.indexOf(c);
	}

	@Override
	public Comparable<Integer> getColumnKey(int col) {
		return this.columnKeys.get(col);
	}

	@Override
	public List<Comparable<Integer>> getColumnKeys() {
		return this.columnKeys;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int getRowIndex(Comparable c) {
		return this.rowKeys.indexOf(c);
	}

	@Override
	public Comparable<String> getRowKey(int row) {
		return this.rowKeys.get(row);
	}

	@Override
	public List<Comparable<String>> getRowKeys() {
		return this.rowKeys;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Number getValue(Comparable rowKey, Comparable columnKey) {
		EvaluationResult v = this.result[((Category) columnKey).i];
		if (RECALL.equals(rowKey))
			return v.getRecall();
		if (PRECISION.equals(rowKey))
			return v.getPrecision();
		if (F_MEASURE.equals(rowKey))
			return v.getFMeasure();
		return null;
	}

	@Override
	public int getColumnCount() {
		return this.columnKeys.size();
	}

	@Override
	public int getRowCount() {
		return this.rowKeys.size();
	}

	@Override
	public Number getValue(int row, int column) {
		return getValue(this.rowKeys.get(row), this.columnKeys.get(column));
	}

	@Override
	public void addChangeListener(DatasetChangeListener arg0) {
		this.dataChangeListeners.add(arg0);
	}

	@Override
	public DatasetGroup getGroup() {
		return this.group;
	}

	@Override
	public void removeChangeListener(DatasetChangeListener arg0) {
		this.dataChangeListeners.remove(arg0);
	}

	@Override
	public void setGroup(DatasetGroup arg0) {
		this.group = arg0;
	}

	private class Category implements Comparable<Integer> {

		int i;

		public Category(int i) {
			this.i = i;
		}

		@Override
		public int compareTo(Integer o) {
			return this.i - o.intValue();
		}

		@Override
		public String toString() {
			return String.valueOf(this.i);
		}
	}
}
