import java.util.List;

public class MatchesSuccessResponse {
    private List<String> matchList;

    public MatchesSuccessResponse (List<String> matchList) {
        this.matchList = matchList;
    }

    public List<String> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<String> matchList) {
        this.matchList = matchList;
    }
}
