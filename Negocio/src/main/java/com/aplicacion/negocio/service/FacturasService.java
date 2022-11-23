/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacion.negocio.service;

import com.aplicacion.negocio.controller.JDBCconnection;
import com.aplicacion.negocio.entity.DetalleObj;
import com.aplicacion.negocio.entity.DetalleVista;
import com.aplicacion.negocio.entity.FacturaObj;
import com.aplicacion.negocio.entity.FacturaVista;
import com.aplicacion.negocio.entity.FacturasConDetalles;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.springframework.stereotype.Service;

/**
 *
 * @author XPC
 */
@Service
public class FacturasService {

    JDBCconnection jdbc = new JDBCconnection();

    public List<FacturaVista> obtenerFacturasSinDetalle() throws SQLException {
        // crear lista que el metodo va devolver
        List<FacturaVista> contenedor = new ArrayList<>();

        // Connect to the database
        jdbc.init();

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_OBTENER_FACTURAS (?); END;");

        // se le indica la posicion del parametro y el tipo
        jdbc.call.registerOutParameter(1, OracleTypes.REF_CURSOR);
        // se ejecuta el query
        jdbc.call.execute();
        // rset guarda el resultado del llamado
        ResultSet rset = (ResultSet) jdbc.call.getObject(1);
        // como ver el nombre de las columnas

        //ResultSetMetaData rsmd = rset.getMetaData();
        // Dump the cursor
        while (rset.next()) {

            String name = "1-" + rset.getLong(1);
            name += " 2-" + rset.getString(2);
            name += " 3-" + rset.getString(3);
            name += " 4-" + rset.getString(4);
            name += " 5-" + rset.getLong(5);
            name += " 6-" + rset.getLong(6);
            name += " 7-" + rset.getString(7);
            name += " 8-" + rset.getString(8);
            System.out.println(name);

            FacturaVista per = new FacturaVista(
                    rset.getLong(1),
                    rset.getString(2),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getLong(5),
                    rset.getLong(6),
                    rset.getString(7),
                    rset.getString(8));
            contenedor.add(per);
        }
        // Close all the resources
        rset.close();
        jdbc.call.close();
        jdbc.close();
        return contenedor;
    }

    public FacturasConDetalles obtenerFactconDetalles(Long id_factura) throws SQLException, ClassNotFoundException {
        // crear lista que el metodo va devolver
        FacturasConDetalles factura = new FacturasConDetalles(); // factura full
        List<DetalleVista> listaDetalles = new ArrayList<>(); //lista de detalles a agregar

        // Connect to the database
        jdbc.init();

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_OBTENER_FACTURA_CON_DETALLES (?,?,?,?,?,?,?,?,?,?,?); END;");

        // se le indica la posicion del parametro y el tipo
        jdbc.call.setLong(1, id_factura); //id factura
        jdbc.call.registerOutParameter(2, OracleTypes.NUMBER); //out id factura
        jdbc.call.registerOutParameter(3, OracleTypes.VARCHAR); //vendedor
        jdbc.call.registerOutParameter(4, OracleTypes.VARCHAR); //cliente
        jdbc.call.registerOutParameter(5, OracleTypes.VARCHAR); //tipo venta
        jdbc.call.registerOutParameter(6, OracleTypes.DECIMAL); //total entrega
        jdbc.call.registerOutParameter(7, OracleTypes.DECIMAL); //total
        jdbc.call.registerOutParameter(8, OracleTypes.VARCHAR); //medio de pago
        jdbc.call.registerOutParameter(9, OracleTypes.TIMESTAMP); //fecha 
        jdbc.call.registerOutParameter(10, OracleTypes.REF_CURSOR); //detalles
        jdbc.call.registerOutParameter(11, OracleTypes.NUMBER); //resultado

        jdbc.call.registerOutParameter(10, OracleTypes.REF_CURSOR);

        /*
        java.util.Map map = jdbc.getConn().getTypeMap();
        map.put("OBJ_DETALLE_FACTURA",Class.forName("FacturaObj"));
        map.put("OBJ_DETALLES_FACTURA",Class.forName("FacturaObj"));
         */
        // se ejecuta el query
        jdbc.call.execute();
        // rset guarda el resultado del llamado
        Long id = (Long) jdbc.call.getLong(2);
        String vendedor = (String) jdbc.call.getObject(3);
        String cliente = (String) jdbc.call.getObject(4);
        String tipoVenta = (String) jdbc.call.getObject(5);
        Long totalEntrega = (Long) jdbc.call.getLong(6);
        Long total = (Long) jdbc.call.getLong(7);
        String medioPago = (String) jdbc.call.getObject(8);
        String fecha = (String) jdbc.call.getString(9);
        ResultSet detalles = (ResultSet) jdbc.call.getObject(10);
        Long resultado = (Long) jdbc.call.getLong(11);
        System.out.println("Resultado de SP_OBTENER_FACTURA_CON_DETALLES: " + resultado);

        while (detalles.next()) {
            DetalleVista detalle = new DetalleVista(
                    detalles.getLong(1),
                    detalles.getLong(2),
                    detalles.getString(3),
                    detalles.getString(4),
                    detalles.getLong(5),
                    detalles.getLong(6)
            );
            listaDetalles.add(detalle);
        }
        factura.setId_factura(id);
        factura.setVendedor(vendedor);
        factura.setCliente(cliente);
        factura.setTipoVenta(tipoVenta);
        factura.setTotalEntrega(totalEntrega);
        factura.setTotal(total);
        factura.setMedioPago(medioPago);
        factura.setFechaHoraVenta(fecha);
        factura.setListaDetalles(listaDetalles);

        // Close all the resources
        detalles.close();
        jdbc.call.close();
        jdbc.close();
        return factura;
    }

    public void crearFactura() throws SQLException, ClassNotFoundException {
        jdbc.init();

        //create obj bs for testing
        //FacturaObj facturaObj = new FacturaObj("NEGOCIO.OBJ_DETALLES_FACTURA");

        //String sql_type, Long productoID, Long cantidad, Float precio, Float IVA
        //DetalleObj detalle1 = new DetalleObj("NEGOCIO.OBJ_DETALLE_FACTURA", 1L, 2L, 12323.5F, 1231321.4F);
        Object[] project1 = new Object[] {"NEGOCIO.OBJ_DETALLE_FACTURA", 1L, 2L, 12323.5F, 1231321.4F};
        //DetalleObj detalle2 = new DetalleObj("NEGOCIO.OBJ_DETALLE_FACTURA", 2L, 5L, 4512.5F, 123.4F);
        Object[] project2 = new Object[] {"NEGOCIO.OBJ_DETALLE_FACTURA", 2L, 5L, 4512.5F, 123.4F};
        //facturaObj.add(detalle1);
        //facturaObj.add(detalle2);
        // ------------------------------


        // descriptor for OBJECT type defined in database
        StructDescriptor projectTypeDesc = StructDescriptor.createDescriptor("NEGOCIO.OBJ_DETALLE_FACTURA", jdbc.getConn());
        //StructDescriptor projectTypeDesc2 = StructDescriptor.createDescriptor("NEGOCIO.OBJ_DETALLES_FACTURA", jdbc.getConn());

        // each struct is one ProjectType object
        STRUCT structProject1 = new STRUCT(projectTypeDesc, jdbc.getConn(),project1);
        STRUCT structProject2 = new STRUCT(projectTypeDesc, jdbc.getConn(), project2);

        STRUCT[] structArrayOfProjects = {structProject1, structProject2};

        // descriptor of TABLE type defined in database
        ArrayDescriptor projectTypeArrayDesc = ArrayDescriptor.createDescriptor("NEGOCIO.OBJ_DETALLES_FACTURA", jdbc.getConn());

        // array holding two ProjectType objects
        ARRAY arrayOfProjects = new ARRAY(projectTypeArrayDesc, jdbc.getConn(), structArrayOfProjects);

        // ------------------
        
        //ARRAY a = new ARRAY(des, jdbc.getConn(), detalle1);

        // Prepare a PL/SQL call
        jdbc.prepareCall("BEGIN NEGOCIO.SP_INSERTAR_FACTURA (?,?,?,?,?,?,?,?); END;");

        // se le indica la posicion del parametro y el tipo
        jdbc.call.setLong(1, 1);
        jdbc.call.setLong(2, 1);
        jdbc.call.setLong(3, 1);
        jdbc.call.setLong(4, 1);
        jdbc.call.setLong(5, 1);
        jdbc.call.setArray(6, arrayOfProjects);
        jdbc.call.registerOutParameter(7, OracleTypes.NUMBER);
        jdbc.call.registerOutParameter(8, OracleTypes.VARCHAR);

        /*
                    IN_ID_VENDEDOR IN NUMBER, 
                    IN_ID_CLIENTE IN NUMBER, 
                    IN_TIPO_VENTA_ID IN NUMBER, 
                    IN_TOTAL_ENTREGA IN NUMBER, 
                    IN_MEDIO_PAGO_ID IN NUMBER, 
                    IN_OBJ_DETALLES_FACTURA IN OBJ_DETALLES_FACTURA, 

                    RESULTADO OUT NUMBER, 
                    MENSAJE OUT VARCHAR2
         */
        // se ejecuta el query
        jdbc.call.execute();

        jdbc.call.close();
        jdbc.close();
    }
}
