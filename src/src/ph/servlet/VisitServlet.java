package src.ph.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;


import src.ph.dao.PetDAO;
import src.ph.dao.VetDAO;
import src.ph.po.Pet;
import src.ph.dao.VisitDAO;
import src.ph.po.Vet;
import src.ph.po.Visit;

//@WebServlet(name = "VisitServlet")
@WebServlet("/VisitServlet")
public class VisitServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String mode = request.getParameter("mode");
        if("search".equals(mode))
        {
            searchPet(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String mode = request.getParameter("mode");
        if ("showCaseHistory".equals(mode))//浏览病历
        {
            showCaseHistory(request, response);
        }
    }

    private void searchPet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
    {
        String petName = request.getParameter("petName");
        String ownerName = request.getParameter("ownerName");
        try
        {
            List<Pet> pets = new PetDAO().search(petName, ownerName);
            if(0==pets.size())
            {
                request.setAttribute("msg", "没有查到宠物信息");
                request.getRequestDispatcher("/visitSearch.jsp").forward(request, response);
            }
            else
            {
                request.setAttribute("pets", pets);
                request.getRequestDispatcher("/visitSearchResult.jsp").forward(request, response);
            }
        }
        catch (Exception e)
        {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/visitSearch.jsp").forward(request, response);
        }
    }

    private void showCaseHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            VisitDAO visitDAO = new VisitDAO();
            List<Visit> visits = visitDAO.getVisitsByPetId(Integer.parseInt(request.getParameter("petId")));
            request.setAttribute("visits", visits);
            if (0 == visits.size())
            {
                request.setAttribute("msg", "没有找到历史病历");
            }
            request.getRequestDispatcher("/showCaseHistory.jsp").forward(request, response);
        }
        catch (Exception e)
        {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/showCaseHistory.jsp").forward(request, response);
        }
    }
}