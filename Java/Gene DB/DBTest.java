import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBTest {

	public static void main(String[] args) {
		Connection con;
		Statement stmt;
		ResultSet rs;
		ResultSetMetaData rsmd;

		/* Database credentials */
		String user = "cse4701";
		String password = "insert password here";
		String host = "query.engr.uconn.edu";
		String port = "1521";
		String sid = "BIBCI";
		String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();

			for (int j = 1; j <= 50; j++) {

				String sql1 = "SELECT * FROM ( SELECT SETID, C, GSETID, C1, OVERLAP FROM ( SELECT * FROM (Select query.setid, g1.setid as gsetid, count(g1.setid) as overlap From query JOIN G1 USING (geneid, geneid) WHERE query.Setid = "
						+ j
						+ " Group By G1.setid, query.setid)) NATURAL JOIN (select query.setid, COUNT(query.setid) as c FROM query GROUP BY query.setid) NATURAL JOIN ( select g1.setid as gsetid, COUNT(g1.setid) as c1 FROM g1 GROUP BY g1.setid) Order By Overlap Desc) WHERE ROWNUM < 6";

				String sql2 = "SELECT * FROM ( SELECT SETID, C, GSETID, C1, OVERLAP FROM ( SELECT * FROM (Select query.setid, g2.setid as gsetid, count(g2.setid) as overlap From query JOIN g2 USING (geneid, geneid) WHERE query.Setid = "
						+ j
						+ " Group By g2.setid, query.setid)) NATURAL JOIN (select query.setid, COUNT(query.setid) as c FROM query GROUP BY query.setid) NATURAL JOIN ( select g2.setid as gsetid, COUNT(g2.setid) as c1 FROM g2 GROUP BY g2.setid) Order By Overlap Desc) WHERE ROWNUM < 6";

				long startTime = System.currentTimeMillis();
				rs = stmt.executeQuery(sql1);
				long stopTime = System.currentTimeMillis();
				long elapsedTime = (stopTime - startTime);
				rsmd = rs.getMetaData();

				while (rs.next()) {
					ArrayList<Object> obArray = new ArrayList<Object>();
					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						obArray.add(rs.getObject(i + 1));
						System.out.print(obArray.toArray()[i] + "\t");
					}
					System.out.print(elapsedTime);
					System.out.println();
				}
			}
		} catch (SQLException ex) {
			Logger.getLogger(DBTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
