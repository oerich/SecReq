package secReq.model;

public class CategorizedRequirement {

	private boolean forTraining;
	private String requirement;
	private String userClassification;
	private String heuristicClassification;

	public boolean isForTraining() {
		return forTraining;
	}

	public void setForTraining(boolean forTraining) {
		this.forTraining = forTraining;
	}

	public String getRequirement() {
		if (this.requirement == null)
			this.requirement = "";
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public String getUserClassification() {
		if (this.userClassification == null)
			this.userClassification = "";
		return userClassification;
	}

	public void setUserClassification(String userClassification) {
		this.userClassification = userClassification;
	}

	public String getHeuristicClassification() {
		if (this.heuristicClassification == null)
			this.heuristicClassification = "";
		return heuristicClassification;
	}

	public void setHeuristicClassification(String heuristicClassification) {
		this.heuristicClassification = heuristicClassification;
	}

}
