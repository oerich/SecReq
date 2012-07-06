package secReq.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import oerich.nlputils.tokenize.AbstractTokenizerAdapter;
import oerich.nlputils.tokenize.ITokenizer;
import oerich.nlputils.tokenize.sentenceboundarydetection.SimpleSentenceBoundaryDetector;
import secReq.controller.CommandManager;
import secReq.controller.ICommandManager;
import secReq.controller.io.StoreCategorizedRequirements;
import secReq.model.CategorizedRequirement;
import secReq.model.CategorizedRequirementsModel;



public class TextConverterDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final static String[] signs = new String[] { ";", "", "", "\t", "\n" };
	private CategorizedRequirementsModel tableModel;
	private JTable table;
	private JTextArea textArea;
	private JComboBox classifyBox;
	private JComboBox chunkBox;
	private ICommandManager manager;
	public TextConverterDialog(ICommandManager manager) {
		super("SecReq - Text Chunking");

		this.manager = manager;
		setLayout(new BorderLayout());

		JTextArea infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea
				.setText("Converts running text into tokens. Paste text in the text fied on the left side. Choose chunking settings in the middle and press chunk Button.");

		infoTextArea.setWrapStyleWord(true);
		add(infoTextArea, BorderLayout.NORTH);

		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(200, 400));
		add(new JScrollPane(textArea), BorderLayout.WEST);
		add(new JScrollPane(getTable()), BorderLayout.EAST);

		JPanel settingsPanel = new JPanel(new GridLayout(4, 1));
		JPanel controlPanel = new JPanel(new GridLayout(1, 4));

		JButton chunkButton = new JButton("convert");
		chunkButton.addActionListener(this);

		JButton saveButton = new JButton("save");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ICommandManager m = new CommandManager();
				m.setModel(tableModel);
				StoreCategorizedRequirements store = new StoreCategorizedRequirements();
				store.initCommand(m);
				store.execute();
			}
		});

		JButton clearButton = new JButton("clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.setData(new LinkedList<CategorizedRequirement>());
			}
		});
		JButton okayButton = new JButton("use this data");
		okayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (CategorizedRequirement cr : tableModel.getData()) {
					TextConverterDialog.this.manager.getModel().addCategorizedRequirement(cr);
				}
				setVisible(false);
			}
		});
		JButton cancelButton = new JButton("cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		JPanel classifyPanel = new JPanel(new GridLayout(1, 2));
		classifyPanel.add(new JLabel("classify as:"));
		classifyBox = new JComboBox(new String[] { "sec", "nonsec" });
		classifyPanel.add(classifyBox);

		JPanel layoutPanel1 = new JPanel();
		layoutPanel1.add(classifyPanel);
		settingsPanel.add(layoutPanel1);

		JPanel chunkPanel = new JPanel(new GridLayout(1, 2));
		chunkPanel.add(new JLabel("Chunk by:"));
		this.chunkBox = new JComboBox(new TokenizerWrapper[] {
				new TokenizerWrapper("new line", new NewLineTokenizer()),
				new TokenizerWrapper("sentence (Java, en)",
						new SimpleSentenceBoundaryDetector(Locale.ENGLISH)),
				new TokenizerWrapper("sentence (Java, de)",
						new SimpleSentenceBoundaryDetector(Locale.GERMAN)) });
		chunkPanel.add(this.chunkBox);

		JPanel layoutPanel2 = new JPanel();
		layoutPanel2.add(chunkPanel);
		settingsPanel.add(layoutPanel2);

		JPanel layoutPanel3 = new JPanel();
		layoutPanel3.add(chunkButton);
		settingsPanel.add(layoutPanel3);

		controlPanel.add(saveButton);
		controlPanel.add(clearButton);
		controlPanel.add(cancelButton);
		controlPanel.add(okayButton);

		add(settingsPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
		pack();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TextConverterDialog f = new TextConverterDialog(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	private JTable getTable() {
		if (this.table == null) {
			this.table = new JTable(getTableModel());
		}
		return this.table;
	}

	private CategorizedRequirementsModel getTableModel() {
		if (this.tableModel == null) {
			this.tableModel = new CategorizedRequirementsModel();
		}
		return this.tableModel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String text = this.textArea.getText();
		String[] tokens = ((ITokenizer) this.chunkBox.getSelectedItem())
				.tokenize(text);

		for (String token : tokens) {
			CategorizedRequirement r = new CategorizedRequirement();
			
			// Clean up 
			for (String sign : signs) {
				while (token.indexOf(sign) >= 0)
					token = token.replace(sign, " ");
			}

			
			r.setRequirement(token.trim());
			r.setUserClassification(this.classifyBox.getSelectedItem()
					.toString());
			this.tableModel.addCategorizedRequirement(r);
		}

		this.tableModel.fireTableDataChanged();
	}

	private class TokenizerWrapper implements ITokenizer {
		String name;
		ITokenizer delegate;

		public TokenizerWrapper(String name, ITokenizer tokenizer) {
			this.name = name;
			this.delegate = tokenizer;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public String nextToken() {
			return this.delegate.nextToken();
		}

		@Override
		public void setText(String text) {
			this.delegate.setText(text);
		}

		@Override
		public String[] tokenize(String text) {
			return this.delegate.tokenize(text);
		}
	}

	private class NewLineTokenizer extends AbstractTokenizerAdapter {

		@Override
		public String[] tokenize(String text) {
			List<String> ret = new LinkedList<String>();
			String[] chunks = text.split("\n");
			
			for (String chunk : chunks) {
				for (String sign : signs) {
					while (chunk.indexOf(sign) >= 0)
						chunk = chunk.replace(sign, " ");
				}
				chunk.trim();
				if (chunk.length() > 8) {
					ret.add(chunk);
				}
			}
			return ret.toArray(new String[] {});
		}

	}
}
