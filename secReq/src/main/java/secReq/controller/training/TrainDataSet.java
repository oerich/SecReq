package secReq.controller.training;

import java.io.File;

import oerich.nlputils.NLPInitializationException;
import oerich.nlputils.dataset.IDataSet;
import oerich.nlputils.dataset.IDataSetDAO;
import oerich.nlputils.text.IStopWordFilter;
import oerich.nlputils.text.StopWordFilterFactory;
import oerich.nlputils.tokenize.ITokenizer;
import secReq.controller.ICommand;
import secReq.controller.ICommandManager;
import secReq.model.CategorizedRequirement;




public class TrainDataSet implements ICommand {

	private ICommandManager manager;
	private String tmpFileName;
	private ITokenizer tokenizer = StopWordFilterFactory
			.createTokenizer("./stopsigns.txt");
	// private IStopWordFilter filter = StopWordFilterFactory
	// .createStopWordFilter("./stopwords.txt");
	private IStopWordFilter filter = StopWordFilterFactory.NULL_FILTER;

	@Override
	public void execute() {
		IDataSet dataSet = manager.getDataSet();
		try {
			// Try to make a backup.
			tmpFileName = getClass().getSimpleName() + "_tmp_"
					+ System.currentTimeMillis() + ".bak";
			File f = new File(tmpFileName);
			if (!f.createNewFile()) {
				System.err.println(getClass().getSimpleName()
						+ ": Could not create temporal data set file <"
						+ f.getAbsolutePath() + ">.");
			}
			f.deleteOnExit();

			IDataSetDAO.NEW_XML.storeDataSet(f.getAbsolutePath(), dataSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		dataSet.clear();
		for (CategorizedRequirement cr : this.manager.getModel().getData()) {
			if (cr.isForTraining()) {
				String[] sentence = this.filter.filterStopWords(tokenizer
						.tokenize(cr.getRequirement()));
				dataSet.learn(sentence, cr.getUserClassification());
			}
		}

		this.manager.getModel().fireTableDataChanged();
		this.manager.addUndoableCommand(this);
	}

	@Override
	public String getName() {
		return "Train Dataset";
	}

	@Override
	public void initCommand(ICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void undo() {
		IDataSet dataSet;
		try {
			dataSet = IDataSetDAO.NEW_XML.createDataSet(this.tmpFileName);
			this.manager.setDataSet(dataSet);
		} catch (NLPInitializationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ICommand getClone() {
		return new TrainDataSet();
	}

}
