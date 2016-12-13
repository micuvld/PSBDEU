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
 * Servlet implementation class FisaLunara
 */
@WebServlet("/FisaLunara")
public class FisaLunara extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FisaLunara() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = DatabaseManager.getInstance().getConnection();
		CallableStatement statement = null;
		
		java.sql.Date luna = Utils.getDate(request.getParameter("luna")); 
		
		PrintWriter outWriter = response.getWriter();
		JSONArray listFacturi = new JSONArray();
		JSONArray listBonuri = new JSONArray();
		JSONObject json = new JSONObject();
		
	
		try {
			String sql = "{call get_facturi_si_bonuri (?, ?, ?)}";
			statement = connection.prepareCall(sql);

			statement.setDate(1, luna);
			statement.registerOutParameter(2, OracleTypes.CURSOR);
			statement.registerOutParameter(3, OracleTypes.CURSOR);

			statement.execute();
			
			ResultSet resultFacturi = ((OracleCallableStatement)statement).getCursor(2);
		    while (resultFacturi.next()) {
		        //System.out.println(result.getString("produse_id") + ":" + result.getString("denumire") + ":" + result.getString("pret"));
		    	json = new JSONObject();
		    	json.put("factura_id", resultFacturi.getString("facturi_id"));
		    	json.put("data", resultFacturi.getString("data"));
		    	json.put("cif", resultFacturi.getString("cif"));
		    	json.put("total", resultFacturi.getString("total"));
		    	json.put("numar_bucati", resultFacturi.getString("numar_bucati"));
		    	json.put("produs_id", resultFacturi.getString("produse_id"));
		    	json.put("denumire_produs", resultFacturi.getString("denumire"));
		    	
		    	listFacturi.add(json);
		      }
		    
			ResultSet resultBonuri = ((OracleCallableStatement)statement).getCursor(3);
		    while (resultBonuri.next()) {
		        //System.out.println(result.getString("produse_id") + ":" + result.getString("denumire") + ":" + result.getString("pret"));
		    	json = new JSONObject();
		    	json.put("bon_id", resultBonuri.getString("bonuri_id"));
		    	json.put("data", resultBonuri.getString("data"));
		    	json.put("cif", resultBonuri.getString("cif"));
		    	json.put("total", resultBonuri.getString("total"));
		    	json.put("numar_bucati", resultBonuri.getString("numar_bucati"));
		    	json.put("produs_id", resultBonuri.getString("produse_id"));
		    	json.put("denumire_produs", resultBonuri.getString("denumire"));
		    	
		    	listBonuri.add(json);
		      }
		    
		    json = new JSONObject();
		    json.put("facturi", listFacturi);
		    json.put("bonuri", listBonuri);
		    
		    outWriter.write(json.toJSONString());
		    resultFacturi.close();
		    resultFacturi = null;
		    resultBonuri.close();
		    resultBonuri = null;
			
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

