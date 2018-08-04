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
import java.util.List;

@WebServlet(name = "UBoatCandidatesPollServlet" ,urlPatterns = "/uboatCandidatesPoll")
//returns nothing in the resp or an empty list or filled list, need to make sure it expects it on browser
public class UBoatCandidatesPollServlet extends HttpServlet  {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String userName = req.getParameter("userName");//todo:: make sure it matches the html
        List<Competition> competitionList = (List<Competition> )getServletContext().getAttribute("competitions");

        if(competitionList == null){
        //something maybe
            return;
        }

        List<String> candidatesList = FindCandidatesList(competitionList,userName);
        if(candidatesList != null){
            try(PrintWriter out = resp.getWriter()){
                Gson gson = new Gson();
                String json = gson.toJson(candidatesList);
                System.out.println(json);
                out.print(json);
            }
        }
    }

    private List<String> FindCandidatesList(List<Competition> competitionList,String userName) {
        for(Competition competition : competitionList){
            if(competition.getuBoat().getUserName().equals(userName)){
                return competition.getCandidatesList();
            }
        }
        return null;
    }
}
