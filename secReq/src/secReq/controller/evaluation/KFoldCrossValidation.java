package secReq.controller.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.dataset.IDataSet;
import oerich.nlputils.dataset.impl.NewXMLDataSetDAO;
import oerich.nlputils.text.IStopWordFilter;
import oerich.nlputils.text.StopWordFilterFactory;
import oerich.nlputils.tokenize.ITokenizer;
import secReq.model.CategorizedRequirement;
import secReq.model.ClassifierWithName;
import secReq.model.EvaluationResultMatrix;


public class KFoldCrossValidation {

	private IStopWordFilter stopwordfilter = StopWordFilterFactory.NULL_FILTER;
	private ITokenizer tokenizer = StopWordFilterFactory
			.createTokenizer("./stopsign2.txt");
	private int k;
	private List<CategorizedRequirement>[] buckets;
	private Statistics statistics;
	private EvaluationResultMatrix results = new EvaluationResultMatrix();
	private EvaluationResult[] learningCurve;
	private List<CategorizedRequirement>[] otherData;
	private EvaluationResultMatrix[] otherResults;
	private IClassifier<IDataSet> classifier = ClassifierWithName
			.getClassicBayesClassifier();

	public KFoldCrossValidation() {
		this.statistics  = new Statistics();
		this.statistics.setIncludesTrainingSet(true);
	}
	public int getK() {
		if (this.k <= 1)
			return 10;
		return this.k;
	}

	public void setK(int k) {
		this.k = k;
	}

	@SuppressWarnings("unchecked")
	public EvaluationResult evaluate(List<CategorizedRequirement> evalData,
			List<CategorizedRequirement>... otherData) {
		// Does this data make sense?
		if (evalData.size() < getK())
			throw new IllegalArgumentException(String.format(
					"Cannot distribute %d samples over %d buckets.", evalData
							.size(), getK()));

		// 1. Init
		results.initEvaluationMatrix(getK());
		// Create the buckets
		this.buckets = new List[getK()];
		for (int i = 0; i < getK(); i++) {
			this.buckets[i] = new LinkedList<CategorizedRequirement>();
		}
		// Get the categorized requirements
		List<CategorizedRequirement> data = new LinkedList<CategorizedRequirement>();
		data.addAll(evalData);

		// Put them randomly in n buckets
		Random r = new Random();
		while (!data.isEmpty()) {
			for (List<CategorizedRequirement> bucket : this.buckets) {
				if (!data.isEmpty()) {
					bucket.add(data.remove(r.nextInt(data.size())));
				}
			}
		}

		// 2. Deal with optional additional evalSets
		this.otherData = otherData;
		this.otherResults = new EvaluationResultMatrix[otherData.length];
		for (int i = 0; i < otherData.length; i++) {
			this.otherResults[i] = new EvaluationResultMatrix();
			this.otherResults[i].initEvaluationMatrix(getK());
		}

		// 3. 10 runs, use each bucket for testing, the other ones for training.
		evaluateEachBucket();

		// 4. Compute the average
		this.learningCurve = this.results.computeAverageResults();

		// The last value contains the values for training with k-1 buckets.
		return this.learningCurve[this.learningCurve.length - 1];
	}

	private void evaluateEachBucket() {
		// 3. For i = 0; i < n-1:
		// 3.a Use bucket i for evaluation, the other buckets for training
		// 3.b Use 1 to n-1 training buckets to compute recall and precision for
		// training bucket
		List<CategorizedRequirement> evalBucket;
		List<CategorizedRequirement> trainingSet = new LinkedList<CategorizedRequirement>();
		for (int evalBucketIndex = 0; evalBucketIndex < getK(); evalBucketIndex++) {
			evalBucket = this.buckets[evalBucketIndex];
			trainingSet.clear();
			int trainingSize = 0;
			for (List<CategorizedRequirement> bucket : buckets) {
				if (!evalBucket.equals(bucket)) {
					trainingSet.addAll(bucket);
					this.results.setResultFor(evalBucketIndex, trainingSize,
							runClassifierOn(evalBucket, trainingSet));
					// Evaluate optional other data:
					for (int i = 0; i < this.otherData.length; i++) {
						this.otherResults[i].setResultFor(evalBucketIndex,
								trainingSize, runClassifierOn(evalBucket,
										this.otherData[i]));
					}
					trainingSize++;
				}
			}
		}
	}

	private EvaluationResult runClassifierOn(
			List<CategorizedRequirement> testSet,
			List<CategorizedRequirement> trainingSet) {

		// Train the set:
		IDataSet dataSet = NewXMLDataSetDAO.NEW_XML.createDataSet();
		if (trainingSet.isEmpty())
			System.err.println("No trainingdata given.");
		for (CategorizedRequirement cr : trainingSet) {
			String[] sentence = this.stopwordfilter.filterStopWords(tokenizer
					.tokenize(cr.getRequirement()));
			dataSet.learn(sentence, cr.getUserClassification());
		}

		try {
			this.classifier.init(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Classify the evaluation set
		for (CategorizedRequirement cr : testSet) {
			double bayes = this.classifier.classify(cr.getRequirement());
			if (0.9 < bayes)
				cr.setHeuristicClassification("sec (" + bayes + ")");
			else
				cr.setHeuristicClassification("nonsec (" + bayes + ")");
		}

		// Compute recall and precision:
		EvaluationResult evaluate = this.statistics.evaluate(testSet);
		// System.out.println("#sec: " + dataSet.getCountSecReq() +
		// ", #nonsec: "
		// + dataSet.getCountNonSecReq() + ", result: " + evaluate);
		return evaluate;
	}

	public IClassifier<IDataSet> getClassifier() {
		return classifier;
	}

	public void setClassifier(IClassifier<IDataSet> classifier) {
		this.classifier = classifier;
	}

	public EvaluationResult[] getLearningCurve() {
		return this.learningCurve;
	}

	public EvaluationResult[] getLearningCurve(int i) {
		return this.otherResults[i].computeAverageResults();
	}

}
