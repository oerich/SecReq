package secReq.controller;

import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;
import secReq.model.CategorizedRequirement;
import secReq.model.CategorizedRequirementsModel;

public interface ICommandManager {

	public abstract void addUndoableCommand(ICommand command);

	public abstract void undoLastCommand();
	
	public abstract String getNameOfLastUnduableCommand();

	public abstract void setModel(CategorizedRequirementsModel dataModel);
	
	public abstract CategorizedRequirementsModel getModel();

	public abstract IDataSet getDataSet();
	
	public abstract void setDataSet(IDataSet  dataSet);

	public abstract CategorizedRequirement getSelectedRequirement();
	
	public abstract void setSelectedRequirement(CategorizedRequirement r);

	public abstract void setLastClassifier(IClassifier<IDataSet> classifier);

	public abstract IClassifier<IDataSet> getLastClassifier();
}
