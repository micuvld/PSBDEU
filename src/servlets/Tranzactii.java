package servlets;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 * Servlet implementation class Tranzactii
 */
@WebServlet("/Tranzactie")
public class Tranzactii extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tranzactii() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection connection = DatabaseManager.getInstance().getConnection();
		CallableStatement statement = null;
		
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = new JSONObject();
		
		try {
			jsonReceived = (JSONObject)parser.parse(request.getReader());
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println((String)jsonReceived.get("data")); 

		java.sql.Date dataFormular = Utils.getDate(jsonReceived.get("data")); 

		
		String cifFormular = (String)jsonReceived.get("cif");
		String tipFormular = (String)jsonReceived.get("tip");
		
		JSONArray numarBucatiJsonArray = (JSONArray)jsonReceived.get("numarBucati");
		JSONArray idProduseJsonArray = (JSONArray)jsonReceived.get("idProduse");
		
		long[] numarBucati = new long[numarBucatiJsonArray.size()];
		long[] idProduse = new long[idProduseJsonArray.size()];

		// Extract numbers from JSON array.
		for (int i = 0; i < numarBucatiJsonArray.size(); ++i) {
			numarBucati[i] = (long)numarBucatiJsonArray.get(i);
			idProduse[i] = (long)idProduseJsonArray.get(i);
		}
		
		System.out.println(numarBucati[0]);
		String returning = "base";
		
		ArrayDescriptor userDescriptor;
		ARRAY numarBucatiList = null;
		ARRAY idProduseList = null;
		try {
			userDescriptor = ArrayDescriptor.createDescriptor("INT_ARRAY",
					connection.unwrap(oracle.jdbc.OracleConnection.class));
			numarBucatiList = new ARRAY(userDescriptor, 
					connection.unwrap(oracle.jdbc.OracleConnection.class),
					numarBucati);
			
			userDescriptor = ArrayDescriptor.createDescriptor("INT_ARRAY",
					connection.unwrap(oracle.jdbc.OracleConnection.class));
			idProduseList = new ARRAY(userDescriptor, 
					connection.unwrap(oracle.jdbc.OracleConnection.class),
					idProduse);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			String sql = "{call inserare_" + tipFormular + " (?, ?, ?, ?)}";
			statement = connection.prepareCall(sql);

			statement.setDate(1, dataFormular);
			statement.setString(2, cifFormular);
			statement.setArray(3, numarBucatiList);
			statement.setArray(4, idProduseList);
			//statement.registerOutParameter(2, OracleTypes.VARCHAR);
			

			statement.execute();
			
			//returning = ((OracleCallableStatement)statement).getString(2);
			//System.out.print(returning);
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

}
