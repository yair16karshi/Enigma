package Servlets;

import DataTypes.Util.Competition;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "SigningServlet" ,urlPatterns = "/signing")
public class SigninServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String userName = request.getParameter("username");
        String userType = request.getParameter("userType");
        SigningResponse res;
        System.out.println(userType);

        List<Competition> game = getGame();

        if(userName == null || userName.isEmpty()){
            res = new SigningResponse(false, "Please insert user name.");
        }
        else if(userIsExist(userName)){
            String alternativeName = findOptionalName(game, userName);
            res = new SigningResponse(false, "Unavailable name.\n You can use: "+optionalName );
        }
        else {
            if(userType.equals("Uboat")){
                Cookie cookie = new Cookie("userName",userName);
                response.addCookie(cookie);
                addUser(gM,userName);
                gM.createNewUboat(userName);
                res = new SigningResponse(true, "uboatRegister.html");
            }
            else if(userType.equals("Ally")){
                Cookie cookie = new Cookie("userName",userName);
                response.addCookie(cookie);
                addUser(gM,userName);
                gM.createNewAlly(userName);
                res = new SigningResponse(true, "aliesRegister.html");
            }
            else
            {
                res = new SigningResponse(false, "You Must Choose User Type.");
            }
    }

    private String findOptionalName(List<Competition> game, String userName) {
    }

    private boolean userIsExist(String userName) {
    }

    private List<Competition> getGame() {
        List<Competition> res;
        Object game = getServletContext().getAttribute("game");

        if(game == null){
            getServletContext().setAttribute("game", new LinkedList<Competition>());
            res = (LinkedList<Competition>)getServletContext().getAttribute("game");
        }
        else
            res = (LinkedList<Competition>)game;
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
