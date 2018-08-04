package Servlets;

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

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "GetSignedAlliesServlet", urlPatterns = "/GetSignedAllies")
public class GetSignAlliesServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletUtils utils = new ServletUtils(getServletContext());
        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Competition competition = utils.GetCompetitionByUBoatUserName(userName);
        ArrayList<AllyDetails> allyDetails;
        GetAlliesResponse res;

        ArrayList<Ally> signedAllies = new ArrayList<>(competition.getAlies());
        allyDetails = new ArrayList<>();

        for (Ally signedAlly : signedAllies) {
            allyDetails.add(new AllyDetails(signedAlly.getName(), signedAlly.getNumOfAgents()));
        }

        res = new GetAlliesResponse(competition.isCompetitionFull() , allyDetails);

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }

    private class GetAlliesResponse{
        private boolean isFull;
        private ArrayList<AllyDetails> allyDetails;

        public GetAlliesResponse(boolean isFull, ArrayList<AllyDetails> allyDetails) {
            this.isFull = isFull;
            this.allyDetails = allyDetails;
        }
    }

    private class AllyDetails{
        private String allyName;
        private int numOfAgents;

        public AllyDetails(String name, int numOfAgents) {
            this.allyName = name;
            this.numOfAgents = numOfAgents;
        }
    }
}
