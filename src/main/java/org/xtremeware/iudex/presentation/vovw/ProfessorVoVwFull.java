package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.ProfessorVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

public class ProfessorVoVwFull {

	private ProfessorVo vo;
	private RatingSummaryVo ratingSummary;
	//TODO: Get this from a centralized place
	private static final String defaultImageUrl = "../resources/professor.png";

	public ProfessorVoVwFull(ProfessorVo vo, RatingSummaryVo ratingSummary) {
		this.vo = vo;
		this.ratingSummary = ratingSummary;
	}

	@Override
	public String toString() {
		return "ProfessorVoVwFill{" + vo.toString() + "ratingSummary=" + ratingSummary + '}';
	}

	public RatingSummaryVo getRatingSummary() {
		return ratingSummary;
	}

	public String getDescription() {
		return vo.getDescription();
	}

	public String getEmail() {
		return vo.getEmail();
	}

	public String getFirstName() {
		return vo.getFirstName();
	}

	public Long getId() {
		return vo.getId();
	}

	public String getImageUrl() {
		String url = vo.getImageUrl();
		if ( url == null){
			return defaultImageUrl;
		}
		return vo.getImageUrl();
	}

	public String getLastName() {
		return vo.getLastName();
	}

	public String getWebsite() {
		return vo.getWebsite();
	}
}
