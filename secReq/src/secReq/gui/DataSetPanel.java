package secReq.gui;

import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import oerich.nlputils.dataset.IDataSet;
import secReq.controller.ICommandManager;


public class DataSetPanel extends JPanel {

	private static final String TEXT = "DataSet (sec/nonsec): %s/%s";

	private static final long serialVersionUID = 1L;
	private ICommandManager manager;

	private JLabel label;

	public DataSetPanel(ICommandManager manager) {
		super(new FlowLayout());
		this.manager = manager;

		this.label = new JLabel();

		resetText();
		add(this.label);
	}

	@Override
	public void paint(Graphics g) {
		resetText();
		super.paint(g);
	}

	void resetText() {
		IDataSet dataSet = this.manager.getDataSet();
		this.label.setText(String.format(TEXT, ((Integer) dataSet
				.getCountSecReq()), ((Integer) dataSet.getCountNonSecReq())));
	}
}
