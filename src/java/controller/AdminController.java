package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Admin;

public class AdminController extends HttpServlet
{

	private final String loginSessionKey = "admin";

//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException
	{
		HttpSession session = request.getSession();
		Object sessionObject = session.getAttribute(loginSessionKey);

		Admin adminUser = (Admin) sessionObject;
		if (adminUser == null)
		{
			System.out.println("no admin user object");
			adminUser = new Admin();
			session.setAttribute(loginSessionKey, adminUser);
		}

		String menu = "home";
		menu = request.getParameter("menu");

		if (menu == null)
		{
			System.out.println("menu is null");
			menu = "home";
		}
		switch (menu)
		{
			case "Login":
				gotoPage("/adminlogin.jsp", request, response);
				break;
			case "SignUp":
				gotoPage("/adminSignup.jsp", request, response);
				break;
			case "Save":
				ProcessSave(request, session);
				gotoPage("/AdminHomepage.jsp", request, response);
				break;
			case "Logout":
				session.invalidate();
				System.out.println("logout");
				gotoPage("/home.jsp", request, response);
				break;
			case "Update":
				ProcessUpdate(request, session, adminUser);
				gotoPage("/AdminHomepage.jsp", request, response);
				break;
			case "updateUser":
				gotoPage("/detailedUserView.jsp", request, response);
				break;
			case "DeleteUser":
				String snid = request.getParameter("user_id");
				int nid = Integer.parseInt(snid);
				Admin user2 = new Admin();
				boolean worked = user2.deleteUser(nid);

				ArrayList<Admin> allusers2 = new ArrayList<>();
				allusers2 = user2.getAllUsers();

				session.setAttribute("allusers", allusers2);
				gotoPage("/manageUsers2.jsp", request, response);
				break;
//            case "AddNotice":
//                gotoPage("/AddNotice.jsp", request, response);
//                break;
//            case "delete":
//                gotoPage("/login.jsp", request, response);
//                break; 
			case "Process Login":
				boolean validLogin = ProcessLogin(request, session);
				if (!validLogin)
				{
					String message = "invalid logon details.. try again";
					session.setAttribute("message", message);
					gotoPage("/adminlogin.jsp", request, response);
				} else
				{

					gotoPage("/adminHome.jsp", request, response);
				}
				break;

			case "getUserView":

				String userid = request.getParameter("user_id");
				int user_id = Integer.parseInt(userid);
				System.out.println("user_id" + user_id);

				Admin s = new Admin();
				s = s.getUserDetails(user_id);

				if (s != null)
				{

					session.setAttribute("user", s);
					System.out.println("sesion contents" + session.getAttribute("user"));
					Admin u = new Admin();
					System.out.println("get user details " + s.getUser_id());
					u = u.getUserDetails(s.getUser_id());
					if (u != null)
					{
						System.out.println("user" + u.getUsername());
						session.setAttribute("user", u);
					} else
					{
						System.out.println("user details null");
					}

				}
				gotoPage("/detailedUserView.jsp", request, response);
				break;

			case "home":

				Admin users = new Admin();
				ArrayList<Admin> allusers = new ArrayList<>();
				allusers = users.getAllUsers();
				session.setAttribute("allusers", allusers);
				gotoPage("/manageusers2.jsp", request, response);
				System.out.println("in switch");
				break;

			case "homepage":
				gotoPage("/homepage.jsp", request, response);

				break;

			case "profile":
				gotoPage("/profilepage.jsp", request, response);

				break;

			case "gallery":
				gotoPage("/gallery.jsp", request, response);

				break;

			case "showspage":
				gotoPage("/userShowPage.jsp", request, response);

				break;

			case "Save User Details":
				boolean worked1 = ProcessUserUpdate(request, adminUser, session);
				gotoPage("/profile_Admin.jsp", request, response);
				break;

			default:
				gotoPage("/invalid.jsp", request, response);
				break;

//            default:
//                gotoPage("/invalid.jsp", request, response);
//                break;
		}
	}

	private boolean ProcessLogin(HttpServletRequest request, HttpSession session) throws SQLException
	{

		String username = request.getParameter("username");
		String password = request.getParameter("password");
//        String AccountType = request.getParameter("AccountType");
		Admin us = new Admin(username, password);
		us.Login(username, password);
		session.setAttribute(loginSessionKey, us);

		if (us.getUser_id() != 0)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private void ProcessSave(HttpServletRequest request, HttpSession session) throws SQLException
	{

		String f_name = request.getParameter("f_name");
		String l_name = request.getParameter("l_name");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String profile_pic = request.getParameter("profile_pic");
		String password = request.getParameter("password");

		String bio = request.getParameter("bio");

//        String AccountType=request.getParameter("AccountType");
		System.out.println(f_name);
		Admin us = new Admin(f_name, l_name, email, username, profile_pic, password, bio);
		us.saveToDatabase();

		session.setAttribute("user", us);
		System.out.println("userid" + us.getUser_id());
	}

	private void gotoPage(String url,
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		RequestDispatcher dispatcher
				= getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{
			processRequest(request, response);
		} catch (SQLException ex)
		{
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{
			processRequest(request, response);
		} catch (SQLException ex)
		{
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo()
	{
		return "Short description";
	}// </editor-fold>

	public void ProcessUpdate(HttpServletRequest request, HttpSession session, Admin user)
	{
		String f_name = request.getParameter("f_name");
		String l_name = request.getParameter("l_name");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String profile_pic = request.getParameter("profile_pic");
		String password = request.getParameter("password");

		String bio = request.getParameter("bio");

//         String AccountType=request.getParameter("AccountType");
		System.out.println("f_name");
		Admin us = new Admin(user.getUser_id(), f_name, l_name, email, username, profile_pic, password, bio);

		us.updateUser();
		session.setAttribute("user", user);
		System.out.println("userid" + user.getUser_id());
	}

	private void ProcessDelete(HttpServletRequest request, HttpSession session, Admin user)
	{
		Admin us = new Admin(user.getUser_id());
		us.delete(user.getUser_id());
		session.setAttribute("user", user);
		System.out.println("userid" + user.getUser_id());
	}

	private boolean ProcessUserUpdate(HttpServletRequest request, Admin user, HttpSession session)
	{
		String f_name = request.getParameter("f_name");
		String l_name = request.getParameter("l_name");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String profile_pic = request.getParameter("profile_pic");
		String password = request.getParameter("password");
		String bio = request.getParameter("bio");

		int UserID = user.getUser_id();
		System.out.println("in process update");

		Admin u = user.updateDatabase(UserID, f_name, l_name, email, username, profile_pic, password, bio);
		// put it back in the sesssion
		System.out.println("after update");
		session.setAttribute("user", u);
		return true;
	}

}
