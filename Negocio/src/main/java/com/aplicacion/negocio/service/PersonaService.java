package com.aplicacion.negocio.service;

import com.aplicacion.negocio.controller.JDBCconnection;
import com.aplicacion.negocio.entity.Personas;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Service;

/**
 *
 * @author XPC
 */
@Service
public class PersonaService {

    // instancia para la conexion a la BD
    JDBCconnection jdbc = new JDBCconnection();

    public List<Personas> obtenerPersonas() throws SQLException {

        // crear lista que el metodo va devolver
        List<Personas> contenedor = new ArrayList<>();

        // Connect to the database
        jdbc.init();

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_OBTENER_PERSONAS (?,?,?); END;");

        // se le indica la posicion del parametro y el tipo
        jdbc.call.registerOutParameter(1, OracleTypes.REF_CURSOR);
        jdbc.call.registerOutParameter(2, OracleTypes.NUMBER);
        jdbc.call.registerOutParameter(3, OracleTypes.VARCHAR);
        // se ejecuta el query
        jdbc.call.execute();
        // rset guarda el resultado del llamado
        ResultSet rset = (ResultSet) jdbc.call.getObject(1);
  
        while (rset.next()) {
   
            Personas per = new Personas(
                    rset.getLong(1),
                    rset.getLong(2),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6),
                    rset.getString(7),
                    rset.getString(8),
                    1,
                    rset.getString(9));
            contenedor.add(per);
        }
        // Close all the resources
        rset.close();
        jdbc.call.close();
        jdbc.close();
        return contenedor;
    }

    public void savePersonas(Personas per) throws SQLException {
        /*
         * IN_CEDULA IN NUMBER,
         * IN_NOMBRE IN VARCHAR2,
         * IN_PRIMER_APELLIDO IN VARCHAR2,
         * IN_SEGUNDO_APELLIDO IN VARCHAR2,
         * IN_DIRECCION IN VARCHAR2,
         * IN_EMAIL VARCHAR2,
         * IN_TELEFONO IN VARCHAR2,
         * IN_TIPO_PERSONA_ID IN NUMBER
         */
        // Connect to the database
        jdbc.init();

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_INSERTAR_PERSONA (?,?,?,?,?,?,?,?); END;");

        /*
         * String name = "1-" + per.getCedula();
         * name += " 2-" + per.getNombre();
         * name += " 3-" + per.getPrimerAp();
         * name += " 4-" + per.getSegundoAp();
         * name += " 5-" + per.getDireccion();
         * name += " 6-" + per.getEmail();
         * name += " 7-" + per.getTelefono();
         * name += " 8-" + per.getTipoPersonaId();
         * name += " 9-" + per.getTipoPersonaDesc();
         * System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOO"+name);
         * 
         * jdbc.call.setLong(1,per.getCedula());
         * jdbc.call.setString(2,per.getNombre());
         * jdbc.call.setString(3,per.getPrimerAp());
         * jdbc.call.setString(4,per.getSegundoAp());
         * jdbc.call.setString(5,per.getDireccion());
         * jdbc.call.setString(6,per.getEmail());
         * jdbc.call.setString(7,per.getTelefono());
         * jdbc.call.setInt(8,per.getTipoPersonaId());
         */
        // se ejecuta el query
        jdbc.call.execute();

        jdbc.call.close();
        jdbc.close();
    }

    public Personas getPersonaPorID(Long id) throws SQLException {
        Personas per = new Personas();

        jdbc.init();

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_OBTENER_UNA_PERSONA (?,?); END;");

        jdbc.call.setLong(1, id);
        jdbc.call.registerOutParameter(2, OracleTypes.REF_CURSOR);

        // se ejecuta el query
        jdbc.call.execute();

        // se almacena el resultado del query en rset
        ResultSet rset = (ResultSet) jdbc.call.getObject(2);

        while (rset.next()) {
            String nombre = rset.getString("NOMBRE");
            Long cedula = rset.getLong("CEDULA");
            String apellido1 = rset.getString("PRIMER_APELLIDO");
            String apellido2 = rset.getString("SEGUNDO_APELLIDO");
            String direccion = rset.getString("DIRECCION");
            String mail = rset.getString("EMAIL");
            String telefono = rset.getString("TELEFONO");
            String tipoP = rset.getString("TIPO_PERSONA");

            String var = ("Cedula: " + cedula + ", "
                    + "Nombre: " + nombre + ", "
                    + "apellido1: " + apellido1 + ", "
                    + "apellido2: " + apellido2 + ", "
                    + "direccion: " + direccion + ", "
                    + "Email: " + mail + ", "
                    + "telefono: " + telefono + ", "
                    + "tipoP: " + tipoP + "\n");

            per = new Personas(
                    rset.getLong(1),
                    rset.getLong(2),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6),
                    rset.getString(7),
                    rset.getString(8),
                    1,
                    rset.getString(9));

        }
        jdbc.call.close();
        jdbc.close();

        return per;
    }

    public void actualizarPersona(Personas per) throws SQLException {
        jdbc.init();

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_MODIFICAR_PERSONA (?,?,?,?,?,?,?,?,?,?); END;");

        jdbc.call.setLong(1, per.getId_persona());
        jdbc.call.setLong(2, per.getCedula());
        jdbc.call.setString(3, per.getNombre());
        jdbc.call.setString(4, per.getPrimerAp());
        jdbc.call.setString(5, per.getSegundoAp());
        jdbc.call.setString(6, per.getDireccion());
        jdbc.call.setString(7, per.getEmail());
        jdbc.call.setString(8, per.getTelefono());
        jdbc.call.setInt(9, per.getTipoPersonaId());
        jdbc.call.registerOutParameter(10, OracleTypes.NUMBER);

        jdbc.call.execute();
        jdbc.call.close();
        jdbc.close();
    }

    public void eliminarPersona(Long per) throws SQLException {

        jdbc.init();

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_ELIMINAR_PERSONA (?,?); END;");

        jdbc.call.setLong(1, per);
        jdbc.call.registerOutParameter(2, OracleTypes.NUMBER);

        // se ejecuta el query
        jdbc.call.execute();

        // System.out.println("+++++++++++++++++ Resultado de SP_ELIMINAR_PERSONA: " +
        // rset);
        jdbc.call.close();
        jdbc.close();
    }
}
