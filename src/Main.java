import db.DB;
import db.DbException;
import db.DbIntegrityException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Connection conn = null;
        //recuperar dados
        Statement st = null;
        //em forma de tabela
        ResultSet rs = null;

        //montar consulta sql deixando os parametros para depois e atualizar dados
        PreparedStatement pt = null;


        try{
            conn = DB.getConnection();

            //As alterações precisam de uma confirmação explicita do programador para serem salvas no banco de dados
            conn.setAutoCommit(false);

//            pt = conn.prepareStatement(
//                    "INSERT INTO seller " +
//                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
//                    + "VALUES "
//                    + "(?, ?, ?, ?, ?)",
//                    Statement.RETURN_GENERATED_KEYS
//            );
//
//            //Substituindo cada interrogação por uma info
//            pt.setString(1, "Carl Purple");
//            pt.setString(2, "carl@email.com");
//            pt.setDate(3, new java.sql.Date(sdf.parse("22/04/1995").getTime()));
//            pt.setDouble(4, 3000.0);
//            pt.setInt(5, 4);

            pt = conn.prepareStatement(
                    "INSERT INTO department " +
                        "(Name) "
                        + "VALUES "
                        + "(?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pt.setString(1, "D1");
            pt.setString(1, "D2");

            pt = conn.prepareStatement(
                    "UPDATE seller "
                            + "SET BaseSalary = BaseSalary + ? "
                            + "WHERE "
                            + "(DepartmentId = ?)");

            pt.setDouble(1, 200.0);
            pt.setInt(2, 2);

            pt = conn.prepareStatement(
                    "DELETE FROM department "
                    + "WHERE "
                    + "Id = ?"
            );

            pt.setInt(1, 5);
            int rowsAffected = pt.executeUpdate();

            System.out.println("Done! Rows affected: " + rowsAffected);

            st = conn.createStatement();
            rs = st.executeQuery("select * from department");

            int rowa1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2900 WHERE DepartmentId = 1");

            //erro no meio do caminho
//            int x = 1;
//            if( x < 2){
//                throw new SQLException("Fake error");
//            }
            int rowa2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");

            //confirmação explicita
            conn.commit();

            System.out.println("rows1 " + rowa1);
            System.out.println("rows2 " + rowa2);

            while (rs.next()){
                System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
            }
        }
        catch (SQLException e){
            try {
                //se ocorrer erro no meio do caminho então será feito rollback
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                throw new DbException("Eroor tryong to rollback! Caused by: " + ex.getMessage());
            }
        }
//        catch (ParseException e){
//         e.printStackTrace();
//        }
        finally {
            DB.closeStatement(pt);
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }
}