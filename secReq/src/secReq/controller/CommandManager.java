package secReq.controller;

import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;
import oerich.nlputils.dataset.IDataSetDAO;
import secReq.model.CategorizedRequirement;
import secReq.model.CategorizedRequirementsModel;

public class CommandManager implements ICommandManager {

	private CategorizedRequirementsModel dataModel;
	private IDataSet dataSet;
	private CategorizedRequirement selectedRequirement;
	private IClassifier<IDataSet> lastClassifier;

	@Override
	public void addUndoableCommand(ICommand command) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNameOfLastUnduableCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undoLastCommand() {
		// TODO Auto-generated method stub

	}

	@Override
	public CategorizedRequirementsModel getModel() {
		if  (this.dataModel == null)
			this.dataModel = new CategorizedRequirementsModel();
		return this.dataModel;
	}

	@Override
	public void setModel(CategorizedRequirementsModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public IDataSet getDataSet() {
		if (this.dataSet == null)
			this.dataSet = IDataSetDAO.NEW_XML.createDataSet();
		return this.dataSet;
	}

	@Override
	public void setDataSet(IDataSet dataSet) {
		this.dataSet = dataSet;
	}

	@Override
	public CategorizedRequirement getSelectedRequirement() {
		return this.selectedRequirement;
	}

	@Override
	public void setSelectedRequirement(CategorizedRequirement r) {
		this.selectedRequirement = r;
	}

	@Override
	public void setLastClassifier(IClassifier<IDataSet> classifier) {
		this.lastClassifier = classifier;
	}

	@Override
	public IClassifier<IDataSet> getLastClassifier() {
		return this.lastClassifier;
	}

}
