package Servlets;

import DataTypes.Util.UBoat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EncryptStringServlet extends HttpServlet {
    @Override
    //gets the UBoat and encrypts word, the word is saved in the competition and sent back in response
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils servletUtils = new ServletUtils(getServletContext());

        String userName = req.getParameter("userName");//todo:: make sure it matches the html
        String unEncryptedWord = req.getParameter("decryptedWord");

        UBoat uboat = servletUtils.GetUboatByName(userName );

        String encryptedWord = uboat.EncryptWord(unEncryptedWord);

        servletUtils.GetCompetitionByUBoatUserName(userName).SetEncryptedWord(encryptedWord);

        resp.getWriter().print(encryptedWord);

    }
}
