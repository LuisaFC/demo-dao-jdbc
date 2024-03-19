import db.DB;

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

            pt = conn.prepareStatement(
                    "INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                    + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            //Substituindo cada interrogação por uma info
            pt.setString(1, "Carl Purple");
            pt.setString(2, "carl@email.com");
            pt.setDate(3, new java.sql.Date(sdf.parse("22/04/1995").getTime()));
            pt.setDouble(4, 3000.0);
            pt.setInt(5, 4);

            pt = conn.prepareStatement(
                    "UPDATE seller "
                            + "SET BaseSalary = BaseSalary + ? "
                            + "WHERE "
                            + "(DepartmentId = ?)");

            pt.setDouble(1, 200.0);
            pt.setInt(2, 2);


            int rowsAffected = pt.executeUpdate();

            System.out.println("Done! Rows affected: " + rowsAffected);

            st = conn.createStatement();
            rs = st.executeQuery("select * from department");

            while (rs.next()){
                System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (ParseException e){
         e.printStackTrace();
        }
        finally {
            DB.closeStatement(pt);
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }
}