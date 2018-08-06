package Servlets;

import DataTypes.Util.Ally;
import DataTypes.Util.Competition;
import DataTypes.Util.UBoat;
import com.sun.prism.image.CompoundTexture;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class ServletUtils {
    private ServletContext m_servletContext;
    private final String COMPETITIONSLIST = "competitions";
    private final String PENDINGALLIES = "PendingAllies";
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
            if(competition.getuBoat().getUserName().equals(userName)){
                return competition;
            }
        }
        return null;
    }
    public Competition GetCompetitionByAllyUserName(String userName) {
        List<Competition> competitionList = GetCompetitionListFromContext();
        for(Competition competition : competitionList){
            for(Ally ally : competition.getAlies()){
                if(ally.getName().equals(userName))
                    return competition;
            }
        }
        return null;
    }

    public Competition GetCompetitionByCompetitionName(String compName){
        List<Competition> competitionList = GetCompetitionListFromContext();

        for(Competition competition : competitionList){
            if(competition.getBattlefield().getBattleName().equals(compName)){
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
                if(ally.getName().equals(userName))
                    return ally;
            }
        }
        return null;
    }

    public void SetCompetitionList(List<Competition> competitions){
        m_servletContext.setAttribute(COMPETITIONSLIST, competitions);
    }

    public Ally CreateNewAlly(String userName){
        List<Ally> allyList = (List<Ally> )m_servletContext.getAttribute(PENDINGALLIES);
        return new Ally(userName);
    }
    public List<Ally> GetPendingAllies (){
        return (List<Ally> )m_servletContext.getAttribute(PENDINGALLIES);
    }

    public List<Ally> GetAlliesCopyInCompetitionByAllyUserName(String userName) {
        Competition competition = GetCompetitionByAllyUserName(userName);
        List<Ally> res = new ArrayList<>();

        if(competition != null){
            for(Ally ally: competition.getAlies()){
                res.add(ally);
            }
            return res;
        }
        else{
            return  null;
        }
    }
}
