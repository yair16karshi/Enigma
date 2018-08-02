package Servlets;

import DataTypes.Util.Ally;
import DataTypes.Util.Competition;
import DataTypes.Util.UBoat;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "SigningServlet" ,urlPatterns = "/signing")
public class SigninServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String userName = request.getParameter("username");
        String userType = request.getParameter("userType");
        SigningResponse res;
        System.out.println(userType);

        List<Competition> competitions = getCompetitions();
        List<Ally> pendingAllies = getPendingAllies();

        if(userName == null || userName.isEmpty()){
            res = new SigningResponse(false, "Please insert user name.");
        }
        else if(isUserExist(userName)){
            String alternativeName = findAlternativeName(competitions, userName);
            res = new SigningResponse(false, "Name already taken.\n but: "+alternativeName+" is also cool" );
        }
        else {
            if(userType.equals("Uboat")){
                Cookie cookie = new Cookie("userName",userName);
                response.addCookie(cookie);
                Competition competition = createCompetition(userName);
                competitions.add(competition);
                res = new SigningResponse(true, "uboatRegister.html");
            }
            else if(userType.equals("Ally")){
                Cookie cookie = new Cookie("userName",userName);
                response.addCookie(cookie);
                Ally ally = new Ally(userName);
                pendingAllies.add(ally);
                res = new SigningResponse(true, "aliesRegister.html");
            }
            else
            {
                res = new SigningResponse(false, "You Must Choose User Type.");
            }
        }

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }

    private List<Ally> getPendingAllies() {
        List<Ally> res;
        Object pendingAllies = getServletContext().getAttribute("pendingAllies");

        if(pendingAllies == null){
            getServletContext().setAttribute("pendingAllies", new LinkedList<Competition>());
            res = (LinkedList<Ally>)getServletContext().getAttribute("pendingAllies");
        }
        else
            res = (LinkedList<Ally>)pendingAllies;
        return res;
    }

    private Competition createCompetition(String userName) {
        UBoat uBoat = new UBoat();
        uBoat.setUserName(userName);
        return new Competition(uBoat);
    }

    private String findAlternativeName(List<Competition> game, String userName) {
        StringBuilder name = new StringBuilder(userName);
        Random rnd = new Random();
        do{
            int num = rnd.nextInt(1000);
            name.append(String.valueOf(num));

        }while(isUserExist(name.toString()));

        return name.toString();
    }

    private boolean isUserExist(String userName) {
        List<Competition> competitions = getCompetitions();
        List<Ally> pendingAllies = getPendingAllies();

        for(Competition comp: competitions){
            for(Ally ally: comp.getAlies()){
                if(ally.getName().equals(userName)){
                    return true;
                }
            }
        }

        for (Ally ally: pendingAllies){
            if(ally.getName().equals(userName)){
                return true;
            }
        }

        return false;
    }

    private List<Competition> getCompetitions() {
        List<Competition> res;
        Object competitions = getServletContext().getAttribute("competitions");

        if(competitions == null){
            getServletContext().setAttribute("competitions", new LinkedList<Competition>());
            res = (LinkedList<Competition>)getServletContext().getAttribute("competitions");
        }
        else
            res = (LinkedList<Competition>)competitions;
        return res;
    }

    private class SigningResponse{
        private boolean successSign;
        private String msg;

        public SigningResponse(boolean successSign, String msg) {
            this.successSign = successSign;
            this.msg = msg;
        }
    }
}
