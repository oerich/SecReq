package secReq.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CategorizedRequirementsModel implements TableModel {

	private static final String[] columns = { "Training", "Requirement",
			"User Classification", "Heuristic Classification" };
	private List<TableModelListener> tml = new Vector<TableModelListener>();
	private List<CategorizedRequirement> data = new Vector<CategorizedRequirement>();

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		this.tml.add(arg0);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		switch (arg0) {
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		}
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int col) {
		return columns[col];
	}

	@Override
	public int getRowCount() {
		return this.data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		CategorizedRequirement r = this.data.get(row);
		switch (col) {
		case 0:
			return r.isForTraining();
		case 1:
			return r.getRequirement();
		case 2:
			return r.getUserClassification();
		case 3:
			return r.getHeuristicClassification();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		switch (col) {
		case 0:
			return true;
		case 1:
			return false;
		case 2:
			return true;
		case 3:
			return false;
		}
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		this.tml.add(arg0);
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		CategorizedRequirement r = this.data.get(row);
		switch (col) {
		case 0:
			r.setForTraining((Boolean) value);
			break;
		case 1:
			r.setRequirement((String) value);
			break;
		case 2:
			r.setUserClassification((String) value);
			break;
		case 3:
			r.setHeuristicClassification((String) value);
			break;
		}
	}

	public void addCategorizedRequirement(CategorizedRequirement r) {
		this.data.add(r);
		fireTableDataChanged();
	}

	public List<CategorizedRequirement> getData() {
		if ( this.data == null )
			this.data = new LinkedList<CategorizedRequirement>();
		return this.data;
	}

	public void setData(List<CategorizedRequirement> data) {
		this.data = data;
		fireTableDataChanged();
	}

	public void fireTableDataChanged(){
		TableModelEvent e = new TableModelEvent(this);
		for(TableModelListener l : this.tml)
			l.tableChanged(e);
	}
}
