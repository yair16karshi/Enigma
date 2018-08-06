package Servlets;

import DataTypes.Util.Ally;
import DataTypes.Util.Competition;
import pukteam.enigma.component.machine.api.Secret;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(name = "AllyReadyServlet", urlPatterns = "/allyReadyToStart")
public class AllyReadyServlet extends HttpServlet {
    @Override
    //searches for the ally and sets that he is ready
    //then checks if all the allys are ready, and if so, the competition is active
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils servletUtils = new ServletUtils(getServletContext());

        String userName = null;
        Cookie cookies [] = req.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Ally ally = servletUtils.GetAllyByUserName(userName);
        synchronized (ally){
            ally.setReady(true);
        }

        Competition competition = servletUtils.GetCompetitionByAllyUserName(userName);
        boolean everyoneReady = true;
        for(Ally allyfromList : competition.getAlies()){
            if(!allyfromList.isReady())
                everyoneReady = false;
        }

        synchronized (competition){
            competition.setActive(everyoneReady);
        }
        int difficulty = parseToDifficulty(competition.getBattlefield().getLevel());
        synchronized (ally){
            ally.setDM(competition.getuBoat().getEncryptedMsg(),competition.getuBoat().getMachineWrapper().getSecret(),difficulty, ally.getManager().getMissionSize(),0);
        }
    }

    private int parseToDifficulty(String level) {
        switch (level){
            case "EASY":
                return 1;
            case "MEDIUM":
                return 2;
            case "HARD":
                return 3;
            case "UNPOSSIBLE":
                return 4;
            default:
                return 1;
        }
    }
}
