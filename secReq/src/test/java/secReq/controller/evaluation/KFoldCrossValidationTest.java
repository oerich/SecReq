package secReq.controller.evaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import secReq.model.CategorizedRequirement;

public class KFoldCrossValidationTest {

	private KFoldCrossValidation eval;

	@Before
	public void setUp() throws Exception {
		this.eval = new KFoldCrossValidation();
	}

	@Test
	public void testDefaults() {
		assertEquals(10, this.eval.getK());
		this.eval.setK(40);
		assertEquals(40, this.eval.getK());
	}

	private List<CategorizedRequirement> createTestData() {
		List<CategorizedRequirement> data = new LinkedList<CategorizedRequirement>();

		CategorizedRequirement r1 = new CategorizedRequirement();
		CategorizedRequirement r2 = new CategorizedRequirement();
		CategorizedRequirement r3 = new CategorizedRequirement();
		CategorizedRequirement r4 = new CategorizedRequirement();
		r1.setRequirement("This is a security requirement");
		r1.setUserClassification("sec");
		r2.setRequirement("This is not a security requirement");
		r2.setUserClassification("nonsec");
		r3.setRequirement("This is a nonsecurity requirement");
		r3.setUserClassification("nonsec");
		r4.setRequirement("This is not a nonsecurity requirement");
		r4.setUserClassification("sec");

		data.add(r1);
		data.add(r2);
		data.add(r3);
		data.add(r4);
		return data;
	}

	@Test
	public void testEvaluation() {
		List<CategorizedRequirement> data = createTestData();
		this.eval.setK(4);
		@SuppressWarnings("unchecked")
		EvaluationResult testresult = this.eval.evaluate(data);

		EvaluationResult expected = new EvaluationResult();
		expected.setPrecision(0.0);
		expected.setRecall(0.0);

		assertEquals(expected, testresult);

		// Now see the intermediate results.
		EvaluationResult[] learningCurve = this.eval.getLearningCurve();
		assertEquals(3, learningCurve.length);
		assertEquals(testresult, learningCurve[2]);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWrongInitialisation() {
		try {
			List<CategorizedRequirement> data = createTestData();
			this.eval.evaluate(data);
			fail("Not allowed to run with empty buckets!");
		} catch (IllegalArgumentException iae) {
			assertEquals("Cannot distribute 4 samples over 10 buckets.", iae
					.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testChangeK() {
		List<CategorizedRequirement> data = createTestData();
		this.eval.setK(2);
		assertNotNull(this.eval.evaluate(data));
		this.eval.setK(3);
		assertNotNull(this.eval.evaluate(data));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWithAdditionalData() {
		List<CategorizedRequirement> data = createTestData();
		List<CategorizedRequirement> data1 = createTestData();
		List<CategorizedRequirement> data2 = createTestData();

		data1.get(0).setRequirement("Another security requirement.");
		data1.get(1).setRequirement("Another nonsecurity requirement.");
		data1.get(2).setRequirement("Not a requirement at all.");
		data1.get(3).setRequirement("The security requirement.");
		this.eval.setK(3);
		assertNotNull(this.eval.evaluate(data, data1, data2));
		assertNotNull(this.eval.getLearningCurve(0));
		assertNotNull(this.eval.getLearningCurve(1));
		assertEquals("(r:0.0/p:NaN/f:NaN)", this.eval.getLearningCurve(0)[1]
				.toString());
		// assertEquals("(r:1.0/p:0.5/f:0.6666666666666666)",this.eval.getLearningCurve(1)[1].toString());

		// System.out.println(Arrays.toString(this.eval.getLearningCurve(0)));
		// System.out.println(Arrays.toString(this.eval.getLearningCurve(1)));
	}
}
