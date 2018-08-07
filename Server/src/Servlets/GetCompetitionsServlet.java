package Servlets;

import DataTypes.Util.Competition;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/GetCompetitions")
public class GetCompetitionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils utils = new ServletUtils(req.getServletContext());
        resp.setContentType("application/json;charset = UTF-8");
        List<CompetitionToDisplay> res = new ArrayList<>();
        for (Competition competition : utils.GetCompetitionListFromContext()) {
            if (!competition.isActive()) {
                res.add(new CompetitionToDisplay(
                        competition.getuBoat().getUserName(),
                        competition.getBattlefield().getBattleName(),
                        competition.getBattlefield().getLevel(),
                        competition.getBattlefield().getAllies(),
                        competition.getAlies().size(),
                        competition.isActive()));
            }
        }

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }

    private class CompetitionToDisplay {
        String ownerName;
        String compName;
        String level;
        int maxAllowed;
        int numOfAlliesInComp;
        boolean active;


        public CompetitionToDisplay(String ownerName, String compName, String level, int maxAllowed, int numOfAlliesInComp, boolean active) {
            this.ownerName = ownerName;
            this.compName = compName;
            this.level = level;
            this.maxAllowed = maxAllowed;
            this.active = active;
            this.numOfAlliesInComp = numOfAlliesInComp;
        }
    }
}