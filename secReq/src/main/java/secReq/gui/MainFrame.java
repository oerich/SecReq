package secReq.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import secReq.controller.CommandManager;
import secReq.controller.ICommand;
import secReq.controller.ICommandManager;
import secReq.controller.ShowExplanation;
import secReq.controller.classification.ClassifyCommand;
import secReq.controller.io.ClearCategorizedRequirements;
import secReq.controller.io.LoadCategorizedRequirementsFromFile;
import secReq.controller.io.LoadDataSet;
import secReq.controller.io.StoreCategorizedRequirements;
import secReq.controller.io.StoreDataSet;
import secReq.controller.training.HighlightTrainingSet_10random;
import secReq.controller.training.HighlightTrainingSet_50_50;
import secReq.controller.training.HighlightTrainingSet_90Percent;
import secReq.controller.training.HighlightTrainingSet_All;
import secReq.controller.training.HighlightTrainingSet_EverySecond;
import secReq.controller.training.HighlightTrainingSet_Inverse;
import secReq.controller.training.HighlightTrainingSet_None;
import secReq.controller.training.TrainDataSet;
import secReq.model.CategorizedRequirementsModel;
import secReq.model.ClassifierWithName;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CategorizedRequirementsModel tableModel;
	private JTable table;
	private JMenu actionMenu;
	private JMenuBar menubar;
	private transient ICommandManager commandManager;
	private JMenu editMenu;
	private JMenu fileMenu;
	private JMenu toolsMenu;
	private TextConverterDialog textConverterDialog;
	private KFoldCrossValidationView crossValidationView;

	public MainFrame() {
		super("SecReq");

		setJMenuBar(getMainMenuBar());
		setLayout(new BorderLayout());

		getContentPane().add(new JScrollPane(getTable()), BorderLayout.CENTER);

		JPanel infoPanel = new JPanel(new FlowLayout());
		getContentPane().add(infoPanel, BorderLayout.SOUTH);
		infoPanel.add(new DataSetPanel(getCommandManager()));
		StatisticsPanel sp = new StatisticsPanel(getCommandManager());
		getTableModel().addTableModelListener(sp);
		infoPanel.add(sp);
		pack();
	}

	private JTable getTable() {
		if (this.table == null) {
			this.table = new JTable(getTableModel());
		}
		return this.table;
	}

	private CategorizedRequirementsModel getTableModel() {
		if (this.tableModel == null) {
			this.tableModel = getCommandManager().getModel();
		}
		return this.tableModel;
	}

	private JMenu getActionMenu() {
		if (this.actionMenu == null) {
			this.actionMenu = new JMenu("Actions");
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new TrainDataSet())));
			this.actionMenu.addSeparator();
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getClassicBayesClassifier()))));
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getPrecisionBayesClassifier()))));
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getRecallBayesClassifier()))));
			this.actionMenu.addSeparator();
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getGroundedBayesClassifier()))));
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getAggressiveBayesClassifier()))));
			this.actionMenu.addSeparator();
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getTFxIDFClassifier()))));
			this.actionMenu.addSeparator();
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getAlternatelyClassifier()))));
			this.actionMenu.add(new JMenuItem(new CommandWrapper(
					new ClassifyCommand(ClassifierWithName
							.getRandomClassifier()))));
			this.actionMenu
					.add(new JMenuItem(new CommandWrapper(new ClassifyCommand(
							ClassifierWithName.getAllClassifier()))));
			this.actionMenu.addSeparator();
			// this.actionMenu.add(new JMenuItem(new CommandWrapper(
			// new DoNFoldCrossEvaluation())));
			this.actionMenu.add(new JMenuItem(new AbstractAction(
					"K-Fold Cross Validation") {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					getCrossValidationView().validate();
					getCrossValidationView().setVisible(true);
				}
			}));
			// this.actionMenu.add(new JMenuItem(new CommandWrapper(
			// new InterSpecCrossEvaluation())));
		}
		return this.actionMenu;
	}

	private KFoldCrossValidationView getCrossValidationView() {
		if (this.crossValidationView == null) {
			this.crossValidationView = new KFoldCrossValidationView(
					getCommandManager());
			this.crossValidationView
					.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.crossValidationView.pack();
		}
		return this.crossValidationView;
	}

	private JMenu getFileMenu() {
		if (this.fileMenu == null) {
			this.fileMenu = new JMenu("File");
			this.fileMenu.add(new JMenuItem(new CommandWrapper(
					new LoadCategorizedRequirementsFromFile())));
			this.fileMenu.add(new JMenuItem(new CommandWrapper(
					new StoreCategorizedRequirements())));
			this.fileMenu.addSeparator();
			this.fileMenu.add(new JMenuItem(new CommandWrapper(
					new LoadDataSet())));
			this.fileMenu.add(new JMenuItem(new CommandWrapper(
					new StoreDataSet())));
			this.fileMenu.addSeparator();
			this.fileMenu.add(new JMenuItem(new CommandWrapper(
					new ClearCategorizedRequirements())));

		}
		return this.fileMenu;
	}

	private JMenu getEditMenu() {
		if (this.editMenu == null) {
			this.editMenu = new JMenu("Edit");
			this.editMenu.add(new JMenuItem(new CommandWrapper(
					new HighlightTrainingSet_10random())));
			this.editMenu.add(new JMenuItem(new CommandWrapper(
					new HighlightTrainingSet_50_50())));
			this.editMenu.add(new JMenuItem(new CommandWrapper(
					new HighlightTrainingSet_EverySecond())));
			this.editMenu.add(new JMenuItem(new CommandWrapper(
					new HighlightTrainingSet_90Percent())));
			this.editMenu.addSeparator();
			this.editMenu.add(new JMenuItem(new CommandWrapper(
					new HighlightTrainingSet_All())));
			this.editMenu.add(new JMenuItem(new CommandWrapper(
					new HighlightTrainingSet_None())));
			this.editMenu.add(new JMenuItem(new CommandWrapper(
					new HighlightTrainingSet_Inverse())));
		}
		return this.editMenu;

	}

	private JMenu getToolsMenu() {
		if (this.toolsMenu == null) {
			this.toolsMenu = new JMenu("Tools");
			this.toolsMenu.add(new AbstractAction("Import requirements") {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					getTextConverterDialog().setVisible(true);
				}
			});
			this.toolsMenu.add(new JMenuItem(new CommandWrapper(
					new ShowExplanation())));
		}
		return this.toolsMenu;
	}

	private TextConverterDialog getTextConverterDialog() {
		if (this.textConverterDialog == null) {
			this.textConverterDialog = new TextConverterDialog(
					getCommandManager());
			this.textConverterDialog
					.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		return this.textConverterDialog;
	}

	private JMenuBar getMainMenuBar() {
		if (this.menubar == null) {
			this.menubar = new JMenuBar();
			this.menubar.add(getFileMenu());
			this.menubar.add(getEditMenu());
			this.menubar.add(getActionMenu());
			this.menubar.add(getToolsMenu());
		}
		return this.menubar;
	}

	private synchronized ICommandManager getCommandManager() {
		if (this.commandManager == null) {
			this.commandManager = new CommandManager();
		}
		return this.commandManager;
	}

	public static void main(String[] args) {
		MainFrame f = new MainFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	private class CommandWrapper extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private ICommand command;

		CommandWrapper(ICommand c) {
			super(c.getName());
			this.command = c;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int i = getTable().getSelectedRow();

			if (i >= 0)
				getCommandManager().setSelectedRequirement(
						getTableModel().getData().get(i));
			ICommand copy = this.command.getClone();
			copy.initCommand(getCommandManager());
			copy.execute();
		}
	}
}
