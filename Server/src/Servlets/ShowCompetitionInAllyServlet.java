package Servlets;

import DataTypes.CandidateStringWithEncryptionInfo;
import DataTypes.Util.Ally;
import DataTypes.Util.Competition;
import Producer.AgentResults;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "ShowCompetitionInAllyServlet", urlPatterns = "/ShowCompetitionInAlly")
public class ShowCompetitionInAllyServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        List<String> winners = null;
        ArrayList<String> results = new ArrayList<>();
        boolean competitionFinished = false;

        ServletUtils utils = new ServletUtils(getServletContext());

        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Ally ally = utils.GetAllyByUserName(userName);

        for(CandidateStringWithEncryptionInfo candidate: ally.getCandidates())
            results.add(candidate.getString());

        Competition competition = utils.GetCompetitionByAllyUserName(userName);

        winners = competition.getWinners();
        competitionFinished = competition.isCompetitionFinish();
        CompetitionDetailsForAlly res = new CompetitionDetailsForAlly(competitionFinished, winners, results);

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }

    private class CompetitionDetailsForAlly{
        boolean competitionFinished;
        List<String> winners;
        ArrayList<String> results;

        public CompetitionDetailsForAlly(boolean competitionFinished, List<String> winners, ArrayList<String> results) {
            this.competitionFinished = competitionFinished;
            this.winners = winners;
            this.results = results;
        }
    }
}
