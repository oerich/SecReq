package secReq.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import secReq.controller.ICommandManager;
import secReq.controller.evaluation.Statistics;


public class StatisticsPanel extends JPanel implements TableModelListener {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private Statistics statistics;
	private JCheckBox checkbox;

	public StatisticsPanel(ICommandManager manager) {
		super(new FlowLayout());

		
		this.statistics = new Statistics(manager.getModel().getData());
		
		this.checkbox = new JCheckBox();
		this.checkbox.setToolTipText("Consider training requirements in statistics");
		this.checkbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				statistics.setIncludesTrainingSet(checkbox.isSelected());
				tableChanged(null);
			}
		});
		this.add(this.checkbox);
		
		this.label = new JLabel();
		this.add(this.label);
		
		resetText();
	}

	void resetText() {
		this.label.setText(this.statistics.toString());
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		resetText();
		repaint();
	}
}
