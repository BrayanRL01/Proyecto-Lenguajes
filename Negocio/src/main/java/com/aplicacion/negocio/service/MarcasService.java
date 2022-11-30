package com.aplicacion.negocio.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aplicacion.negocio.controller.JDBCconnection;
import com.aplicacion.negocio.entity.Marcas;

import oracle.jdbc.OracleTypes;

@Service
public class MarcasService {

    JDBCconnection JDBC = new JDBCconnection();

    public List<Marcas> ObtenerMarcas() throws SQLException {
        List<Marcas> ArrayMarcas = new ArrayList<>();

        JDBC.init();

        JDBC.prepareCall("BEGIN NEGOCIO.SP_OBTENER_MARCAS (?,?,?); END;");

        JDBC.call.registerOutParameter(1, OracleTypes.REF_CURSOR);
        JDBC.call.registerOutParameter(2, OracleTypes.NUMBER);
        JDBC.call.registerOutParameter(3, OracleTypes.VARCHAR);

        JDBC.call.execute();

        ResultSet RS = (ResultSet) JDBC.call.getObject(1);

        while (RS.next()) {
            Marcas M = new Marcas(
                    RS.getLong(1),
                    RS.getString(2));
            ArrayMarcas.add(M);
        }
        RS.close();
        JDBC.call.close();
        JDBC.close();

        return ArrayMarcas;
    }

    public Marcas ObtenerMarcaPorID(Long id) throws SQLException {
        Marcas M;
        JDBC.init();

        JDBC.prepareCall("BEGIN NEGOCIO.SP_OBTENER_UNA_MARCA (?,?,?,?); END;");

        JDBC.call.setLong(1, id);
        JDBC.call.registerOutParameter(2, OracleTypes.VARCHAR);
        JDBC.call.registerOutParameter(3, OracleTypes.NUMBER);
        JDBC.call.registerOutParameter(4, OracleTypes.VARCHAR);

        JDBC.call.execute();

        String N = (String) JDBC.call.getObject(2);

        M = new Marcas(id, N);

        JDBC.call.close();
        JDBC.close();

        return M;
    }

    public void InsertarMarcas(Marcas M) throws SQLException {

        JDBC.init();

        JDBC.prepareCall("BEGIN NEGOCIO.SP_INSERTAR_MARCA (?,?,?); END;");

        JDBC.call.setString(1, M.getNombre_Marca());
        JDBC.call.registerOutParameter(2, OracleTypes.NUMBER);
        JDBC.call.registerOutParameter(3, OracleTypes.VARCHAR);

        JDBC.call.execute();

        JDBC.call.close();
        JDBC.close();
    }

    public void ModificarMarca(Marcas M) throws SQLException {
        JDBC.init();

        JDBC.prepareCall("BEGIN NEGOCIO.SP_MODIFICAR_MARCA (?,?,?,?); END;");

        JDBC.call.setLong(1, M.getId_Marca());
        JDBC.call.setString(2, M.getNombre_Marca());
        JDBC.call.registerOutParameter(3, OracleTypes.NUMBER);
        JDBC.call.registerOutParameter(4, OracleTypes.VARCHAR); 

        JDBC.call.execute();

    }

    public void EliminarMarca(Long Id_Marca) throws SQLException {

        JDBC.init();

        JDBC.prepareCall("BEGIN NEGOCIO.SP_ELIMINAR_MARCA (?,?,?); END;");

        JDBC.call.setLong(1, Id_Marca);
        JDBC.call.registerOutParameter(2, OracleTypes.NUMBER);
        JDBC.call.registerOutParameter(3, OracleTypes.VARCHAR);

        JDBC.call.execute();

        // String Mensaje = (String) JDBC.call.getObject(3);

        JDBC.call.close();
        JDBC.close();
    }

}
