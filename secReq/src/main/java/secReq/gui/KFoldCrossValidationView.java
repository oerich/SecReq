package secReq.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;


import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import secReq.controller.CommandManager;
import secReq.controller.ICommandManager;
import secReq.controller.evaluation.KFoldCrossValidation;
import secReq.model.ClassifierWithName;
import secReq.model.KFoldCrossValidationModel;


public class KFoldCrossValidationView extends JFrame {

	private static final long serialVersionUID = 1L;
	private ICommandManager manager;
	private KFoldCrossValidation crossValidator;
	private JSpinner kSpinner;
	private KFoldCrossValidationModel dataSet;
	private JComboBox classifierBox;

	public KFoldCrossValidationView(ICommandManager manager) {
		super("K-Fold Cross Validation");

		this.manager = manager;
		setLayout(new BorderLayout());

		add(getInfoPanel(), BorderLayout.NORTH);
		add(getConfigValidationPanel(), BorderLayout.WEST);
		add(getResultPanel(), BorderLayout.CENTER);
		// add(getConfigViewPanel(), BorderLayout.EAST);
	}

	private Component getResultPanel() {
		JPanel ret = new JPanel();
		ret.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Results:"));

		JFreeChart chart = ChartFactory.createLineChart("Learning Curve",
				"No. of training buckets", "Performance [%]",
				getCategoryDataset(), PlotOrientation.VERTICAL, true, true,
				false);
		((LineAndShapeRenderer) chart.getCategoryPlot().getRenderer())
				.setBaseShapesVisible(true);
		chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
		chart.getCategoryPlot().setRangeGridlinePaint(Color.LIGHT_GRAY);
		ChartPanel chartPanel = new ChartPanel(chart);
		ret.add(chartPanel);
		return ret;
	}

	private KFoldCrossValidationModel getCategoryDataset() {
		if (this.dataSet == null)
			this.dataSet = new KFoldCrossValidationModel();

		return this.dataSet;
	}

	private Component getConfigValidationPanel() {
		Box ret = new Box(BoxLayout.Y_AXIS);

		ret.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Configure:"));

		JLabel kLabel = new JLabel("No. of buckets (k):");
		SpinnerModel kModel = new SpinnerNumberModel(10, 2, 1000, 1);
		kSpinner = new JSpinner(kModel);

		JPanel kPanel = new JPanel(new GridLayout(1, 2));
		kPanel.add(kLabel);
		kPanel.add(kSpinner);
		ret.add(kPanel);

		JButton runButton = new JButton("run validation");
		runButton.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent event) {
				getCrossValidator().setK(
						Integer.parseInt(kSpinner.getValue().toString()));
				try {
					getCrossValidator().setClassifier(
							(IClassifier<IDataSet>) classifierBox
									.getSelectedItem());
					getCrossValidator().evaluate(manager.getModel().getData());
					getCategoryDataset().setResults(
							getCrossValidator().getLearningCurve());
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(
							KFoldCrossValidationView.this, iae.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		classifierBox = new JComboBox(new IClassifier<?>[] {
				ClassifierWithName.getClassicBayesClassifier(),
				ClassifierWithName.getNeutralBayesClassifier(),
				ClassifierWithName.getPrecisionBayesClassifier(),
				ClassifierWithName.getRecallBayesClassifier(),
				ClassifierWithName.getGroundedBayesClassifier(),
				ClassifierWithName.getAggressiveBayesClassifier(),
				ClassifierWithName.getTFxIDFClassifier(),
				ClassifierWithName.getRandomClassifier(),
				ClassifierWithName.getAlternatelyClassifier(),
				ClassifierWithName.getAllClassifier() });

		ret.add(classifierBox);

		// ret.add(Box.createVerticalGlue());
		// ret.add(Box.createVerticalGlue());
		// ret.add(Box.createVerticalGlue());
		// ret.add(Box.createVerticalGlue());
		// ret.add(Box.createVerticalGlue());
		// ret.add(Box.createVerticalGlue());
		ret.add(new Box.Filler(new Dimension(100, 100),
				new Dimension(100, 300), new Dimension(100, Short.MAX_VALUE)));

		ret.add(runButton);
		// JList list = new JList(new Object[] {kPanel, runButton,
		// classifierBox});
		// ret.add(new JScrollPane(list));
		return ret;
	}

	private Component getInfoPanel() {
		JLabel ret = new JLabel();
		ret.setBackground(Color.WHITE);
		// ret.setForeground(Color.WHITE);

		ret.setBorder(BorderFactory.createEtchedBorder());
		ret
				.setText("This window allows to configure, run, and  analyse the results of the k-fold cross validation.");
		return ret;
	}

	private KFoldCrossValidation getCrossValidator() {
		if (this.crossValidator == null) {
			this.crossValidator = new KFoldCrossValidation();
		}
		return this.crossValidator;
	}

	public static void main(String[] args) {
		KFoldCrossValidationView view = new KFoldCrossValidationView(
				new CommandManager());
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.pack();
		view.setVisible(true);

	}
}
