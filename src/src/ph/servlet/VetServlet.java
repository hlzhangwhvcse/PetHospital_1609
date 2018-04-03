package src.ph.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;
//import ph.dao.SpecialityDAO;
import src.ph.dao.VetDAO;
import src.ph.po.Speciality;
import src.ph.po.Vet;

//@WebServlet(name = "VetServlet")
@WebServlet( "/VetServlet")
public class VetServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //doPost方法会被多个表单调用 ,查询医生,增加医生,增加专业  因此这里需要根据不同表单传递的标示参数调用不同的方法
        String m = request.getParameter("m");
        if("search".equals(m))
        {
            search(request, response);
        }
//        else if("addVet".equals(m))
//        {
//            addVet(request, response);
//        }
//        else if("addSpec".equals(m))
//        {
//            addSpec(request, response);
//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            String mode = request.getParameter("mode");//获得从vetsearch.jsp或vetsearch_result.jsp的超链接传递来的mode参数
            if ("deleteVet".equals(mode)) {
//          new VetDAO().delete (Integer.parseInt(request.getParameter("vetId")));
                String strVetID = request.getParameter("vetId");
                int vetID = Integer.parseInt(strVetID);
                VetDAO vetDAO = new VetDAO();
                vetDAO.delete(vetID);
                request.setAttribute("msg", "成功删除医生：" + request.getParameter("vetName"));
                request.getRequestDispatcher("/vetsearch.jsp").forward(request, response);
            }
//        else if("newVet".equals(mode))
//        {
//            request.setAttribute("specs", new SpecialityDAO().getAll());
//            request.getRequestDispatcher("/vetadd.jsp").forward(request, response);
//        }
//        else if("newSpec".equals(mode))
//        {
//            request.getRequestDispatcher("/specialityAdd.jsp").forward(request, response);
//        }
            else {
                request.getRequestDispatcher("/vetsearch.jsp").forward(request, response);
            }
        }
        catch(Exception e)
        {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/vetsearch.jsp").forward(request, response);
        }
    }

    private void search(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
    {
        String vname=request.getParameter("vname");
        String sname=request.getParameter("sname");
        try
        {
            List<Vet> vets=new VetDAO().search(vname, sname);
            if(0==vets.size())
            {
                request.setAttribute("msg", "没有相关医生信息");
                request.getRequestDispatcher("/vetsearch.jsp").forward(request, response);
            }
            else
            {
                request.setAttribute("vets", vets);
                request.getRequestDispatcher("/vetsearch_result.jsp").forward(request, response);
            }
        }
        catch (Exception e)
        {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/vetsearch.jsp").forward(request, response);
        }
    }
}
