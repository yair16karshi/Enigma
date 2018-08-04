package Servlets;

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

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "NumOfReadyAlliesServlet",urlPatterns="/NumOfReadyAlliesServlet")
public class NumOfReadyAlliesServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        boolean res;
        ServletUtils utils = new ServletUtils(getServletContext());
        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Competition competition = utils.GetCompetitionByUBoatUserName(userName);
        synchronized (competition){
            competition.getuBoat().setReady(true);
        }
        res = competition.isAllAlliesReady();

        if(res){
            competition.startCompetition();
        }

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }

    }
}
