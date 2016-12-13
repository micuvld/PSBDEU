package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

/**
 * Servlet implementation class InsertProduct
 */
@WebServlet("/Produs")
public class Produs extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Produs() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = DatabaseManager.getInstance().getConnection();
		CallableStatement statement = null;
		
		int idProdus = Integer.parseInt(request.getParameter("idProdus"));
		String denumireProdus = request.getParameter("denumireProdus");
		double pretProdus = Double.parseDouble(request.getParameter("pretProdus"));

		try {
			String sql = "{call actualizare_produs (?, ?, ?)}";
			statement = connection.prepareCall(sql);

			statement.setInt(1, idProdus);
			statement.setString(2, denumireProdus);
			statement.setDouble(3, pretProdus);

			statement.execute();
			statement.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se2) {
			}
		} // end tr
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = DatabaseManager.getInstance().getConnection();
		CallableStatement statement = null;
		
		int idProdus = Integer.parseInt(request.getParameter("idProdus"));

		try {
			String sql = "{call sterge_produs (?)}";
			statement = connection.prepareCall(sql);

			statement.setInt(1, idProdus);

			statement.execute();
			statement.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se2) {
			}
		} // end tr
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = DatabaseManager.getInstance().getConnection();
		CallableStatement statement = null;

		String denumireProdus = request.getParameter("denumireProdus");
		double pretProdus = Double.parseDouble(request.getParameter("pretProdus"));

		try {
			String sql = "{call inserare_produs (?, ?)}";
			statement = connection.prepareCall(sql);

			statement.setString(1, denumireProdus);
			statement.setDouble(2, pretProdus);

			statement.execute();
			statement.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se2) {
			}
		} // end try
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = DatabaseManager.getInstance().getConnection();
		CallableStatement statement = null;
		
		PrintWriter outWriter = response.getWriter();
		JSONArray list = new JSONArray();
		JSONObject json = new JSONObject();
		
	
		try {
			String sql = "{call get_produse (?)}";
			statement = connection.prepareCall(sql);

			statement.registerOutParameter(1, OracleTypes.CURSOR);

			statement.execute();
			
			ResultSet result = ((OracleCallableStatement)statement).getCursor(1);
		    while (result.next()) {
		        //System.out.println(result.getString("produse_id") + ":" + result.getString("denumire") + ":" + result.getString("pret"));
		    	json = new JSONObject();
		    	json.put("id", result.getString("produse_id"));
		    	json.put("denumire", result.getString("denumire"));
		    	json.put("pret", result.getString("pret"));
		    	
		    	list.add(json);
		      }
		    
		    outWriter.write(list.toJSONString());
		    result.close();
		    result = null;
			
			statement.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se2) {
			}
		}
	}

}
