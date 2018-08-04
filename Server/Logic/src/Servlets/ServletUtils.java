package Servlets;

import DataTypes.Util.Ally;
import DataTypes.Util.Competition;
import DataTypes.Util.UBoat;

import javax.servlet.ServletContext;
import java.util.List;

public class ServletUtils {
    private ServletContext m_servletContext;
    private final String COMPETITIONSLIST = "competitions";
    private final String USERNAME = "userName";

    public ServletUtils(ServletContext context){
        m_servletContext = context;
    }

    public List<Competition> GetCompetitionListFromContext(){
        return (List<Competition>)m_servletContext.getAttribute(COMPETITIONSLIST);
    }

    public Competition GetCompetitionByUBoatUserName(String userName){
        List<Competition> competitionList = GetCompetitionListFromContext();

        for(Competition competition : competitionList){
            if(competition.getuBoat().getUserName() == userName){
                return competition;
            }
        }
        return null;
    }
    public Competition GetCompetitionByAllyUserName(String userName) {
        List<Competition> competitionList = GetCompetitionListFromContext();
        for(Competition competition : competitionList){
            for(Ally ally : competition.getAlies()){
                if(ally.getName() == userName)
                    return competition;
            }
        }
        return null;
    }

    public Competition GetCompetitionByCompetitionName(String compName){
        List<Competition> competitionList = GetCompetitionListFromContext();

        for(Competition competition : competitionList){
            if(competition.getCompName() == compName){
                return competition;
            }
        }
        return null;
    }

    public UBoat GetUboatByName(String userName){
        Competition competition = GetCompetitionByUBoatUserName(userName);
        if (competition != null) {
            return competition.getuBoat();
        }
        return null;
    }

    public Ally GetAllyByUserName(String userName) {
        List<Competition> competitionList = GetCompetitionListFromContext();
        for(Competition competition : competitionList){
            for(Ally ally : competition.getAlies()){
                if(ally.getName() == userName)
                    return ally;
            }
        }
        return null;
    }
}
