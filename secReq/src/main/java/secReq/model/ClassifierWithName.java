package secReq.model;

import java.util.Random;

import javax.swing.table.TableModel;

import oerich.nlputils.NLPInitializationException;
import oerich.nlputils.classifier.BayesianFilter;
import oerich.nlputils.classifier.GroundedBayesianFilter;
import oerich.nlputils.classifier.IBayesianFilter;
import oerich.nlputils.classifier.IClassifier;
import oerich.nlputils.classifier.TFxIDF;
import oerich.nlputils.dataset.IDataSet;

public class ClassifierWithName implements IClassifier<IDataSet> {

	private static IClassifier<IDataSet> random;
	private static IClassifier<IDataSet> aggressiveBayes;
	private static IClassifier<IDataSet> classicBayes;
	private static IClassifier<IDataSet> precisionBayes;
	private static IClassifier<IDataSet> all;
	private static IClassifier<IDataSet> alternately;
	private static IClassifier<IDataSet> groundedBayes;
	private static IClassifier<IDataSet> tf_idf;
	private static IClassifier<IDataSet> neutralBayes;
	private static IClassifier<IDataSet> recallBayes;
	private String name;
	private IClassifier<IDataSet> delegate;

	public static IClassifier<IDataSet> getTFxIDFClassifier() {
		if (tf_idf == null) {
			tf_idf = new ClassifierWithName("Classify Requirements (TFxIDF)",
					new TFxIDF());
		}
		return tf_idf;
	}

	public static IClassifier<IDataSet> getGroundedBayesClassifier() {
		if (groundedBayes == null) {
			GroundedBayesianFilter filter = new GroundedBayesianFilter();
			filter.setUnknownWordValue(0.5);
			groundedBayes = new ClassifierWithName(
					"Classify Requirements (Bayes; sound)", filter);
		}
		return groundedBayes;
	}

	public static IClassifier<IDataSet> getPrecisionBayesClassifier() {
		if (precisionBayes == null) {
			BayesianFilter filter = new BayesianFilter();
			filter.setProSecBias(0.5);
			filter.setUnknownWordValue(0.4);
			filter.setComparator(IBayesianFilter.BEST_VALUES);
			precisionBayes = new ClassifierWithName(
					"Classify Requirements (Bayes; high precision)", filter);
		}
		return precisionBayes;
	}

	public static IClassifier<IDataSet> getClassicBayesClassifier() {
		if (classicBayes == null) {
			BayesianFilter filter = new BayesianFilter();
			filter.setProSecBias(1.0);
			filter.setUnknownWordValue(0.4);
			filter.setComparator(IBayesianFilter.BEST_VALUES);
			classicBayes = new ClassifierWithName(
					"Classify Requirements (Bayes; classic)", filter);
		}
		return classicBayes;
	}

	public static IClassifier<IDataSet> getNeutralBayesClassifier() {
		if (neutralBayes == null) {
			BayesianFilter filter = new BayesianFilter();
			filter.setProSecBias(1.0);
			filter.setUnknownWordValue(0.4);
			filter.setComparator(IBayesianFilter.BEST_VALUES);
			neutralBayes = new ClassifierWithName(
					"Classify Requirements (Bayes; neutral)", filter);
		}
		return neutralBayes;
	}

	public static IClassifier<IDataSet> getRecallBayesClassifier() {
		if (recallBayes == null) {
			BayesianFilter filter = new BayesianFilter();
			filter.setProSecBias(2.0);
			filter.setUnknownWordValue(0.6);
			filter.setComparator(IBayesianFilter.BEST_VALUES);
			recallBayes = new ClassifierWithName(
					"Classify Requirements (Bayes; high recall)", filter);
		}
		return recallBayes;
	}

	public static IClassifier<IDataSet> getAlternatelyClassifier() {
		if (alternately == null) {
			alternately = new ClassifierWithName(
					"Classify Requirements (control; alternately)",
					new AbstractControlClassifier() {

						boolean secSwitch;

						@Override
						public double classify(String text)
								throws IllegalArgumentException {
							if (secSwitch) {
								secSwitch = false;
								return 1;
							}
							secSwitch = true;
							return 0;
						}
					});
		}

		return alternately;

	}

	public static IClassifier<IDataSet> getRandomClassifier() {
		if (random == null) {
			random = new ClassifierWithName(
					"Classify Requirements (control; random)",
					new AbstractControlClassifier() {
						private Random r = new Random();

						@Override
						public double classify(String text)
								throws IllegalArgumentException {
							return r.nextInt(2);
						}
					});
		}

		return random;

	}

	public static IClassifier<IDataSet> getAllClassifier() {
		if (all == null) {
			all = new ClassifierWithName(
					"Classify Requirements (control; all)",
					new AbstractControlClassifier() {

						@Override
						public double classify(String text)
								throws IllegalArgumentException {
							return 1;
						}

					});
		}

		return all;

	}

	public static IClassifier<IDataSet> getAggressiveBayesClassifier() {
		if (aggressiveBayes == null) {
			BayesianFilter filter = new BayesianFilter();
			filter.setProSecBias(2.0);
			filter.setUnknownWordValue(0.5);
			filter.setComparator(IBayesianFilter.HIGHEST_VALUES);
			aggressiveBayes = new ClassifierWithName(
					"Classify Requirements (Bayes; aggressive)", filter);
		}
		return aggressiveBayes;
	}

	private ClassifierWithName(String name, IClassifier<IDataSet> delegate) {
		this.name = name;
		this.delegate = delegate;
	}

	@Override
	public double classify(String text) throws NLPInitializationException {
		return this.delegate.classify(text);
	}

	@Override
	public double getMatchValue() {
		return this.delegate.getMatchValue();
	}

	@Override
	public boolean isMatch(String text) throws NLPInitializationException {
		return this.delegate.isMatch(text);
	}

	@Override
	public void setMatchValue(double value) {
		this.delegate.setMatchValue(value);
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public void init(IDataSet initData) throws Exception {
		this.delegate.init(initData);
	}

	@Override
	public TableModel explainClassification(String text) {
		return this.delegate.explainClassification(text);
	}

	@Override
	public double getMaximumValue() {
		return this.delegate.getMaximumValue();
	}

	@Override
	public double getMinimalValue() {
		return this.delegate.getMinimalValue();
	}
}
