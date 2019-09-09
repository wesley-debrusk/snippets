import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	public static void main(String[] args) {
		Connection con;
		Statement stmt;
		ResultSet rs;
		ResultSetMetaData rsmd;

		// Database credentials
		String user = "cse4701";
		String password = "insert password here";
		String host = "query.engr.uconn.edu";
		String port = "1521";
		String sid = "BIBCI";
		String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;

		// Number of entries
		double result_entries = 0;
		double result_zeros = 0;
		double result_ones = 0;
		double info_d = 0;

		// Data storage
		int[][] counts = new int[10][2];
		int[][] zero = new int[10][2];
		int[][] one = new int[10][2];
		double[] info_storage = new double[10];
		double[] gain = new double[10];
		int[] overlap = new int[10];

		// Column Names
		String[] columns = { "APC", "TP53", "KRAS", "PIK3CA", "PTEN", "ATN", "MUC4", "SMAD4", "SYNE1", "FBXW7" };

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();

			String result_count = "SELECT COUNT(STATUS) FROM IG_READY";
			String result_count_0 = "SELECT COUNT(STATUS) FROM IG_READY WHERE STATUS = 0";
			String result_count_1 = "SELECT COUNT(STATUS) FROM IG_READY WHERE STATUS = 1";

			rs = stmt.executeQuery(result_count);
			rsmd = rs.getMetaData();
			if (rs.next()) {
				Object temp = rs.getObject(1);
				result_entries = ((Number) temp).intValue();
			}

			rs = stmt.executeQuery(result_count_0);
			rsmd = rs.getMetaData();
			if (rs.next()) {
				Object temp = rs.getObject(1);
				result_zeros = ((Number) temp).intValue();
			}

			rs = stmt.executeQuery(result_count_1);
			rsmd = rs.getMetaData();
			if (rs.next()) {
				Object temp = rs.getObject(1);
				result_ones = ((Number) temp).intValue();
			}

			info_d = information(result_zeros, result_ones, result_entries);

			for (int i = 0; i < 10; i++) {
				String sql0 = "SELECT COUNT(STATUS) FROM (SELECT " + columns[i] + ",STATUS FROM IG_READY WHERE "
						+ columns[i] + " = 0) WHERE STATUS = 0";
				String sql1 = "SELECT COUNT(STATUS) FROM (SELECT " + columns[i] + ",STATUS FROM IG_READY WHERE "
						+ columns[i] + " = 0) WHERE STATUS = 1";

				rs = stmt.executeQuery(sql0);
				rsmd = rs.getMetaData();
				if (rs.next()) {
					Object temp = rs.getObject(1);
					zero[i][0] = ((Number) temp).intValue();
				}

				rs = stmt.executeQuery(sql1);
				rsmd = rs.getMetaData();
				if (rs.next()) {
					Object temp = rs.getObject(1);
					zero[i][1] = ((Number) temp).intValue();
				}
			}

			for (int i = 0; i < 10; i++) {
				String sql0 = "SELECT COUNT(STATUS) FROM (SELECT " + columns[i] + ",STATUS FROM IG_READY WHERE "
						+ columns[i] + " = 1) WHERE STATUS = 0";
				String sql1 = "SELECT COUNT(STATUS) FROM (SELECT " + columns[i] + ",STATUS FROM IG_READY WHERE "
						+ columns[i] + " = 1) WHERE STATUS = 1";

				rs = stmt.executeQuery(sql0);
				rsmd = rs.getMetaData();
				if (rs.next()) {
					Object temp = rs.getObject(1);
					one[i][0] = ((Number) temp).intValue();
				}

				rs = stmt.executeQuery(sql1);
				rsmd = rs.getMetaData();
				if (rs.next()) {
					Object temp = rs.getObject(1);
					one[i][1] = ((Number) temp).intValue();
				}
			}

			for (int i = 0; i < 10; i++) {
				String sql0 = "SELECT COUNT(" + columns[i] + ") FROM IG_READY WHERE " + columns[i] + " = 0";
				String sql1 = "SELECT COUNT(" + columns[i] + ") FROM IG_READY WHERE " + columns[i] + " = 1";

				rs = stmt.executeQuery(sql0);
				rsmd = rs.getMetaData();
				if (rs.next()) {
					Object temp = rs.getObject(1);
					counts[i][0] = ((Number) temp).intValue();
				}

				rs = stmt.executeQuery(sql1);
				rsmd = rs.getMetaData();
				if (rs.next()) {
					Object temp = rs.getObject(1);
					counts[i][1] = ((Number) temp).intValue();
				}
			}

			for (int i = 0; i < 10; i++) {
				String sql = "SELECT COUNT(STATUS) FROM(SELECT * FROM IG_READY WHERE " + columns[i]
						+ " = 1 AND STATUS = 1)";

				rs = stmt.executeQuery(sql);
				rsmd = rs.getMetaData();
				if (rs.next()) {
					Object temp = rs.getObject(1);
					overlap[i] = ((Number) temp).intValue();
				}
			}

		} catch (SQLException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

		for (int i = 0; i < 10; i++) {
			double temp0 = information(zero[i][1], zero[i][0], zero[i][1] + zero[i][0])
					* ((zero[i][1] + zero[i][0]) / result_entries);
			double temp1 = information(one[i][1], one[i][0], one[i][1] + one[i][0])
					* ((one[i][1] + one[i][0]) / result_entries);
			info_storage[i] = temp0 + temp1;
		}

		for (int i = 0; i < 10; i++) {
			gain[i] = info_d - info_storage[i];
		}

		String[][] answer = new String[10][3];
		for (int i = 0; i < 10; i++) {
			answer[i][0] = columns[i];
			answer[i][1] = Double.toString(gain[i]);
			answer[i][2] = Integer.toString(overlap[i]);
		}
		printTable(10, 3, answer);

		String[][] to_print = new String[6][2];
		to_print[0][0] = "Gene ID";
		to_print[0][1] = "Information Gain";

	}

	// Additional methods
	public static double log2(double num) {
		return (Math.log(num) / Math.log(2));
	}

	public static double information(double one, double two, double base) {
		return ((-((one / base) * log2(one / base))) + (-((two / base) * log2(two / base))));
	}

	public static void printTable(int row, int column, String[][] data) {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				System.out.print(data[i][j] + "\t");
			}
			System.out.print("\n");
		}
	}
}
