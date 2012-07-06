package secReq.controller.evaluation;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;


import org.junit.Test;

import secReq.model.CategorizedRequirement;

public class StatisticsTest {

	private Statistics stat;

	@Test
	public void testStatistic() {
		List<CategorizedRequirement> data = createTestData();
		this.stat = new Statistics(data);

		assertEquals(2, this.stat.getFound());
		assertEquals(1, this.stat.getRelevantFound());
		assertEquals(2, this.stat.getRelevant());

		assertEquals(0.5, this.stat.getRecall(), 0.01d);
		assertEquals(0.5, this.stat.getPrecision(), 0.01d);
		assertEquals(0.5, this.stat.getFMeasure(), 0.01d);

		assertEquals(
				"Recall: 0,500000 | Precision: 0,500000 | F-Val: 0,500000 | Rel: 2 | Found: 2 | Relevant-Found: 1",
				this.stat.toString());

		// defaults to not including training set:
		data.get(0).setForTraining(true);
		assertEquals(1, this.stat.getFound());
		assertEquals(0, this.stat.getRelevantFound());
		assertEquals(1, this.stat.getRelevant());

		assertEquals(0.0, this.stat.getRecall(), 0.01d);
		assertEquals(0.0, this.stat.getPrecision(), 0.01d);
		assertEquals(Double.NaN, this.stat.getFMeasure(), 0.01d);
		this.stat.setIncludesTrainingSet(true);
	}

	private List<CategorizedRequirement> createTestData() {
		List<CategorizedRequirement> data = new LinkedList<CategorizedRequirement>();

		CategorizedRequirement r1 = new CategorizedRequirement();
		CategorizedRequirement r2 = new CategorizedRequirement();
		CategorizedRequirement r3 = new CategorizedRequirement();
		CategorizedRequirement r4 = new CategorizedRequirement();
		r1.setUserClassification("sec");
		r1.setHeuristicClassification("sec");
		r2.setUserClassification("sec");
		r2.setHeuristicClassification("nonsec");
		r3.setUserClassification("nonsec");
		r3.setHeuristicClassification("sec");
		r4.setUserClassification("nonsec");
		r4.setHeuristicClassification("nonsec");

		data.add(r1);
		data.add(r2);
		data.add(r3);
		data.add(r4);
		return data;
	}

	@Test
	public void testUninitialized() {
		this.stat = new Statistics(new LinkedList<CategorizedRequirement>());

		assertEquals(0, this.stat.getFound());
		assertEquals(0, this.stat.getRelevantFound());
		assertEquals(0, this.stat.getRelevant());

		assertEquals(Double.NaN, this.stat.getRecall(), 0.01d);
		assertEquals(Double.NaN, this.stat.getPrecision(), 0.01d);
		assertEquals(Double.NaN, this.stat.getFMeasure(), 0.01d);

		assertEquals(
				"Recall: NaN | Precision: NaN | F-Val: NaN | Rel: 0 | Found: 0 | Relevant-Found: 0",
				this.stat.toString());
	}

	@Test
	public void testInvalidPrecsision() {
		List<CategorizedRequirement> data = createTestData();
		// No relevant found data:
		for (CategorizedRequirement r : data)
			r.setHeuristicClassification("nonsec");
		this.stat = new Statistics(data);
		assertEquals(0.0, this.stat.getRecall(), 0.01d);
		assertEquals(Double.NaN, this.stat.getPrecision(), 0.01d);
		assertEquals(Double.NaN, this.stat.getFMeasure(), 0.01d);
	}

	@Test
	public void testInvalidRecall() {
		List<CategorizedRequirement> data = createTestData();
		// No relevant data:
		for (CategorizedRequirement r : data)
			r.setUserClassification("nonsec");
		this.stat = new Statistics(data);
		assertEquals(Double.NaN, this.stat.getRecall(), 0.01d);
		assertEquals(0.0, this.stat.getPrecision(), 0.01d);
		assertEquals(Double.NaN, this.stat.getFMeasure(), 0.01d);
	}
	
	@Test
	public void testAlternativeModeOfOperation() {
		this.stat = new Statistics();
		assertEquals(Double.NaN, this.stat.getRecall(), 0.01d);
		assertEquals(Double.NaN, this.stat.getPrecision(), 0.01d);
		assertEquals(Double.NaN, this.stat.getFMeasure(), 0.01d);
		
		List<CategorizedRequirement> data = createTestData();
		data.get(0).setForTraining(true);

		EvaluationResult result = this.stat.evaluate(data);
		assertEquals(0.0, result.getRecall(), 0.01d);
		assertEquals(0.0, result.getPrecision(), 0.01d);
		assertEquals(Double.NaN, result.getFMeasure(), 0.01d);

		this.stat.setIncludesTrainingSet(true);
		result = this.stat.evaluate(data);
		assertEquals(0.5, result.getRecall(), 0.01d);
		assertEquals(0.5, result.getPrecision(), 0.01d);
		assertEquals(0.5, result.getFMeasure(), 0.01d);
	}
}
