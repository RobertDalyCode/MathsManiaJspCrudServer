package controller;

import web.session.management.SessionManager;
import web.session.management.SessionKeys;
import web.controller.support.AdminControllerCommands;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entities.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import repository.mysql.UserRepository;

public class AdminController extends HttpServlet
{
	private final UserRepository userRepository = new UserRepository();

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException
	{
		HttpSession session = request.getSession();
		User user = SessionManager.getSessionUserValue(session);
		User activeUser = SessionManager.getSessionActiveUserValue(session);
		if (activeUser == null);
		{

		}
		String menu = getMenuSelction(request);
		switch (menu)
		{
			case AdminControllerCommands.SignUp:
				processSignUp(request, response);
				break;
			case AdminControllerCommands.InsertRequest:
				processInsertRequest(request, session, response);
				break;
			case AdminControllerCommands.LogoutRequest:
				processLogoutRequest(session, request, response);
				break;
			case AdminControllerCommands.Update:
				ProcessUpdate(request, session, user);

				break;
			case AdminControllerCommands.UpdateUserRequest:
				gotoPage("/detailedUserView.jsp", request, response);
				break;
			case AdminControllerCommands.DeleteUser:
				processDeleteUserRequest(request, session, response);
				break;
			case AdminControllerCommands.Login:
				processLogin(request, response);
				break;
			case AdminControllerCommands.LoginRequest:
				processLoginRequest(request, session, response);
				break;

			case AdminControllerCommands._ViewUser:

				processViewUser(request, session, response);
				break;

			case AdminControllerCommands.ViewAll:
				processViewAll(session, request, response);
				break;

			case AdminControllerCommands.UpdateRequest:
				processUpdateUserRequest(request, user, session, response);
				break;

			default:
				gotoPage("/invalid.jsp", request, response);
				break;

		}
	}

	private void processUpdateUserRequest(HttpServletRequest request, User user, HttpSession session, HttpServletResponse response) throws IOException, SQLException, ServletException
	{
		mapRequestParametersIntoUser(request, user);

		try
		{
			userRepository.updateUser(user);
			SessionManager.setSessionUserValue(session, user);
			//TODO : consider active user
			gotoPage("/profile_Admin.jsp", request, response);
		}
		catch (SQLException | ServletException | IOException ex)
		{
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
		};

	}

	private void processViewUser(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws SQLException, NumberFormatException, IOException, ServletException
	{
		String userid = request.getParameter("user_id");
		int user_id = Integer.parseInt(userid);
		System.out.println("user_id" + user_id);
		User s = userRepository.getUserById(user_id);

		if (s != null)
		{
			session.setAttribute("user", s);
			User u = userRepository.getUserById(s.getUserid());
			if (u != null)
			{
				session.setAttribute("user", u);
				gotoPage("/detailedUserView.jsp", request, response);
			}
			else
			{
			}
		}
	}

	private void processViewAll(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException
	{
		try
		{
			ArrayList<User> users = userRepository.getUsers();
			SessionManager.setSessionUsersValue(session, users);
			gotoPage("/manageUsers.jsp", request, response);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void processLoginRequest(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws SQLException, ServletException, IOException
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try
		{
			User activeUser = userRepository.getUserByCredentials(username, password);
			if (activeUser == null)
			{
				String message = "Invalid logon details. Please try again.";
				session.setAttribute("message", message);
				gotoPage("/login.jsp", request, response);
			}
			else
			{
				SessionManager.setSessionActiveUserValue(session, activeUser);

				directUserToUserTypeArea(activeUser, session, request, response);
			}
		}
		catch (SQLException ex)
		{
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);

			//gotoPage("/adminHome.jsp", request, response);
		}
	}

	private void processDeleteUserRequest(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, ServletException, SQLException, NumberFormatException
	{
		int userId = Integer.parseInt(request.getParameter("user_id"));
		try
		{
			userRepository.deleteUserById(userId);
			gotoPage("/manageUsers.jsp", request, response);
		}
		catch (SQLException | IOException | ServletException ex)
		{
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
		}
		gotoPage("/manageUsers.jsp", request, response);
	}

	private void processInsertRequest(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws ServletException, IOException
	{
		User user = new User();
		mapRequestParametersIntoUser(request, user);
		try
		{
			userRepository.insertUser(user);
			processLogin(request, response);
			gotoPage("/AdminHomepage.jsp", request, response);
		}
		catch (Exception ex)
		{
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
		}
		SessionManager.setSessionUserValue(session, user);

	}

	private void processLogoutRequest(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		session.invalidate();
		gotoPage("/home.jsp", request, response);
	}

	private void processSignUp(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		gotoPage("/adminSignup.jsp", request, response);
	}

	private void processLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		gotoPage("/adminlogin.jsp", request, response);
	}

	private String getMenuSelction(HttpServletRequest request)
	{
		String menu = request.getParameter("menu");

		return menu;
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
		}
		catch (SQLException ex)
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
		}
		catch (SQLException ex)
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

	public void ProcessUpdate(HttpServletRequest request, HttpSession session, User user) throws SQLException
	{
		mapRequestParametersIntoUser(request, user);

		userRepository.updateUser(user);
		SessionManager.setSessionUserValue(session, user);
		//gotoPage("/manageUsers.jsp", request, response);
	}

	private void mapRequestParametersIntoUser(HttpServletRequest request, User user)
	{
		user.setBio(request.getParameter("bio"));
		user.setEmail(request.getParameter("email"));
		user.setfName(request.getParameter("f_name"));
		user.setlName(request.getParameter("l_name"));
		user.setProfilePic(request.getParameter("profile_pic"));
		user.setPassword(request.getParameter("password"));
		user.setUsername(request.getParameter("username"));
	}

	private void directUserToUserTypeArea(User activeUser, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException
	{
		String userType = activeUser.getAccountType();

		if ("Admin".equals(userType))
		{
//TODO Add ADmin code to navigate site
		}
		else
		{
			processViewAll(session, request, response);//TODO: change to go to only one user
		}
	}

	//	private boolean ProcessLogin(HttpServletRequest request, HttpSession session) throws SQLException
//	{
//
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		User us = new User(username, password);
//		userRepository.getUserByCredentials(username, password);
//		session.setAttribute("user", us);
//
//		if (us.getUserid() != 0)
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
	//	private void ProcessDelete(HttpServletRequest request, HttpSession session, User user) throws SQLException
//	{
//		userRepository.deleteUser(user);
//		session.setAttribute("user", user);
//		System.out.println("userid" + user.getUserid());
//	}
//
//	private boolean ProcessUserUpdate(HttpServletRequest request, User user, HttpSession session) throws SQLException
//	{
//		String fName = request.getParameter("f_name");
//		String lName = request.getParameter("l_name");
//		String email = request.getParameter("email");
//		String username = request.getParameter("username");
//		String profilePic = request.getParameter("profile_pic");
//		String password = request.getParameter("password");
//		String bio = request.getParameter("bio");
//
//		int UserID = user.getUserid();
//
//		User user1 = new User(fName, lName, email, username, profilePic, password, bio);
//		System.out.println("in process update");
//
//		userRepository.updateUser(user1);
//
//		System.out.println("after update");
//		session.setAttribute("user", user1);
//		return true;
//	}
}
