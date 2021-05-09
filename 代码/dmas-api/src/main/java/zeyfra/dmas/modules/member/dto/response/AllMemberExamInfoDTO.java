package zeyfra.dmas.modules.member.dto.response;

import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/27 10:25
 */
public class AllMemberExamInfoDTO {

    private List<Integer> generalCourseScores;
    private List<Integer> basicCourseScores;
    private List<Integer> professionalCourseScores;
    private int max;

    public AllMemberExamInfoDTO(List<Integer> generalCourseScores,
                                List<Integer> basicCourseScores,
                                List<Integer> professionalCourseScores,
                                int max) {
        this.generalCourseScores = generalCourseScores;
        this.basicCourseScores = basicCourseScores;
        this.professionalCourseScores = professionalCourseScores;
        this.max = max;
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
