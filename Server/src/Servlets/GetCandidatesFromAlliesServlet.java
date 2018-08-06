package Servlets;

import DataTypes.CandidateStringWithEncryptionInfo;
import DataTypes.Util.Ally;
import DataTypes.Util.Competition;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "GetCandidatesFromAlliesServlet", urlPatterns = "/GetCandidatesFromAllies")
public class GetCandidatesFromAlliesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        OptionalResults res = new OptionalResults();
        ArrayList<Result> results = new ArrayList<>();
        Set<String> winners = new HashSet<>();
        ServletUtils utils = new ServletUtils(getServletContext());

        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Competition competition = utils.GetCompetitionByUBoatUserName(userName);

        List<Ally> alies = competition.getAlies();
        boolean gameFinished = false;
        int countAllyFinished = 0;

        for (Ally ally : alies) {
            List<CandidateStringWithEncryptionInfo> candidates = ally.getCandidates();
            for(CandidateStringWithEncryptionInfo candidate: candidates){
                if(competition.getuBoat().isCandidateCurrect(candidate.getString())){
                    competition.addWinner(ally.getName());
                    winners.add(ally.getName());
                    gameFinished = true;
                }
                results.add(new Result(ally.getName(), candidate.getString()));
            }
            if(ally.getManager().isFinished()){
                countAllyFinished++;
            }
        }

        if(countAllyFinished == alies.size()){
            gameFinished = true;
        }

        if(gameFinished){
            res.setCompetitionDone(true);
            res.setWinnersName(winners);
        }
        res.setResults(results);

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }

    private class Result{
        private  String allyName;
        private  String result;

        public Result(String allyName, String result) {
            this.allyName = allyName;
            this.result = result;
        }
    }

    private class OptionalResults{
        private boolean competitionDone;
        private Set<String> winnersName ;
        private ArrayList<Result> results;

        public void setCompetitionDone(boolean competitionDone) {
            this.competitionDone = competitionDone;
        }

        public void setResults(ArrayList<Result> results) {
            this.results = results;
        }

        public void setWinnersName(Set<String> winnerName) {
            this.winnersName = winnerName;
        }
    }
}
