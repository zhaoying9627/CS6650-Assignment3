import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class StatsServlet extends HttpServlet {
    private final static int SWIPER_BOUND = 5000;
    private StatsDao statsDao;

    public void init() {
        statsDao = new StatsDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String urlPath = request.getPathInfo();
        System.out.println(urlPath);

        Gson gson = new Gson();
        ErrorResponse invalidInputsMessage = new ErrorResponse("Invalid inputs");
        String invalidInputsMessageJson = gson.toJson(invalidInputsMessage);
        // Check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print(invalidInputsMessageJson);
            out.flush();
            out.close();
            return;
        }

        String[] urlParts = urlPath.split("/");

        // Validate url and return the response
        if (!isUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print(invalidInputsMessageJson);
            out.flush();
            out.close();
            return;
        }

        // Get matchList from MatchesDao
        try {
            Stats stats = statsDao.getStats(urlParts[1]);
            // User Not Found
            if (stats == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                ErrorResponse userNotFoundMessage = new ErrorResponse("User not found");
                String userNotFoundMessageJson = gson.toJson(userNotFoundMessage);
                PrintWriter out = response.getWriter();
                out.print(userNotFoundMessageJson);
                out.flush();
                out.close();
                return;
            }
            String statsJson = gson.toJson(stats);
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(statsJson);
            out.flush();
            out.close();
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private boolean isUrlValid (String[] urlParts) {
        // check whether length equals two
        if(urlParts.length < 2) {
            return false;
        }
        // check whether equals left or right
        if(!(1 <= Integer.parseInt(urlParts[1]) && Integer.parseInt(urlParts[1]) <= SWIPER_BOUND)) {
            return false;
        }
        return true;
    }
}
