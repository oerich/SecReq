package secReq.controller;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;
import secReq.model.CategorizedRequirement;


public class ShowExplanation implements ICommand {

	private ICommandManager manager;
	private JFrame messageFrame;
	private JTable table;

	@Override
	public void execute() {
		IClassifier<IDataSet> classifier = this.manager.getLastClassifier();
		if ( classifier == null )
			return;
		CategorizedRequirement selectedRequirement = this.manager.getSelectedRequirement();
		String text = null;
		if (selectedRequirement != null)
			text = selectedRequirement.getRequirement();
		
		TableModel model = classifier.explainClassification(text); 
		if (model == null)
			return;
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				model);
		getTable().setRowSorter(sorter);
		getTable().setModel(model);
		
		getFrame().setVisible(true);
	}

	private JFrame getFrame() {
		if (this.messageFrame == null) {
			this.messageFrame = new JFrame("Explanation");
			this.messageFrame.add(new JScrollPane(getTable()));
			this.messageFrame.setSize(300, 300);
			this.messageFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.messageFrame.pack();
		}
		return this.messageFrame;
	}
	
	private JTable getTable() {
		if ( this.table == null ) {
			this.table = new JTable();
		}
		return this.table;
	}

	@Override
	public String getName() {
		return "explain selected";
	}

	@Override
	public void initCommand(ICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	@Override
	public ICommand getClone() {
		return new ShowExplanation();
	}

}
