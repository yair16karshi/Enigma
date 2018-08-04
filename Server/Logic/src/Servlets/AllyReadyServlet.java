package Servlets;

import DataTypes.Util.Ally;
import DataTypes.Util.Competition;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AllyReadyServlet extends HttpServlet {
    @Override
    //searches for the ally and sets that he is ready
    //then checks if all the allys are ready, and if so, the competition is active
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils servletUtils = new ServletUtils(getServletContext());

        String userName = req.getParameter("userName");
        Ally ally = servletUtils.GetAllyByUserName(userName);
        ally.setReady(true);
        //todo maybe send back the response(boolean)
        Competition competition = servletUtils.GetCompetitionByAllyUserName(userName);
        boolean everyoneReady = true;
        for(Ally allyfromList : competition.getAlies()){
            if(!allyfromList.isReady())
                everyoneReady = false;
        }
        competition.setActive(everyoneReady);
    }
}
