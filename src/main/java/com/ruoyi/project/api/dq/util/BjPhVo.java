package com.ruoyi.project.api.dq.util;

public class BjPhVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1642008893532990600L;
	
	private String warePlanId;
	private String planFee;
	private String planId;
	private String planName;
	private String submitId;
	private String userName;
	
	public BjPhVo(){}

	public String getWarePlanId() {
		return warePlanId;
	}

	public void setWarePlanId(String warePlanId) {
		this.warePlanId = warePlanId;
	}

	public String getPlanFee() {
		return planFee;
	}

	public void setPlanFee(String planFee) {
		this.planFee = planFee;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getSubmitId() {
		return submitId;
	}

	public void setSubmitId(String submitId) {
		this.submitId = submitId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
