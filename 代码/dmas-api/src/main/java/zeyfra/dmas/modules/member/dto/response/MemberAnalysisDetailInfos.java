package zeyfra.dmas.modules.member.dto.response;

import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/24 20:22
 */
public class MemberAnalysisDetailInfos {

    private List<Integer> generalCourseScores;
    private List<Integer> basicCourseScores;
    private List<Integer> professionalCourseScores;

    public MemberAnalysisDetailInfos(List<Integer> generalCourseScores, List<Integer> basicCourseScores, List<Integer> professionalCourseScores) {
        this.generalCourseScores = generalCourseScores;
        this.basicCourseScores = basicCourseScores;
        this.professionalCourseScores = professionalCourseScores;
    }

    public List<Integer> getGeneralCourseScores() {
        return generalCourseScores;
    }

    public void setGeneralCourseScores(List<Integer> generalCourseScores) {
        this.generalCourseScores = generalCourseScores;
    }

    public List<Integer> getBasicCourseScores() {
        return basicCourseScores;
    }

    public void setBasicCourseScores(List<Integer> basicCourseScores) {
        this.basicCourseScores = basicCourseScores;
    }

    public List<Integer> getProfessionalCourseScores() {
        return professionalCourseScores;
    }

    public void setProfessionalCourseScores(List<Integer> professionalCourseScores) {
        this.professionalCourseScores = professionalCourseScores;
    }
}
