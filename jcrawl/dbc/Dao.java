/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcrawl.dbc;

/**
 *
 * @author miguel
 */
import java.math.BigDecimal;
import java.io.Serializable;
import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;



public class Dao implements Serializable {

    private String host;
    private String usuario;
    private String password;
    private Integer puerto;
    private String baseDatos;
    private String archivo = "configDao.properties";
    private String coneccion;
    private boolean autocommit = true;
    private Properties props;
    private Connection conn;
    private Statement stm;
    private PreparedStatement pstm;
    private ResultSet rst;
    private String sparam;
    private String sql;
    private List<SQLParams> lParams;
    private List<SQLResult> lResult;

    
    
    public Dao() throws IOException, SQLException {
        this.configurarConeccion();
        conectar();
    }

    public Dao(boolean autocommit) throws IOException, SQLException {
        this.autocommit = autocommit;
        this.configurarConeccion();
        conectar();
    }

    public Dao(String archivo) throws IOException, SQLException {
        this.archivo = archivo;
        this.configurarConeccion();
        conectar();
    }

    public Dao(String archivo, boolean autocommit) throws IOException, SQLException {
        this.archivo = archivo;
        this.autocommit = autocommit;
        this.configurarConeccion();
        conectar();
    }

    private void configurarConeccion() throws IOException {
        props = ConfigPropiedades.getProperties(this.archivo);
        host = props.getProperty("host");
        usuario = props.getProperty("usuario");
        password = props.getProperty("password");
        baseDatos = props.getProperty("baseDatos");
        puerto = Integer.valueOf(props.getProperty("puerto"));
        coneccion = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos + "?user=" + usuario + "&password=" + password + "";
    }

    private void conectar() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        conn = DriverManager.getConnection(coneccion);
        conn.setAutoCommit(autocommit);
    }

    public ResultSet consultar(String sql) throws SQLException {
        stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rst = stm.executeQuery(sql);

        return rst;
    }

    public ResultSet consultar(String sql, List<SQLParams> lParams) throws SQLException {
        
            this.lParams = lParams;
            this.sql = sql;
            pstm = this.conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            this.armaDatos();
            this.rst = pstm.executeQuery();
            //this.consultarMetadata();
        
        return this.rst;
    }
    
    public int actualizar(String sql, List<SQLParams> lParams) throws SQLException {
       return this.modificar(sql, lParams);        
    }
    
    public int insertar(String sql, List<SQLParams> lParams) throws SQLException {
       return this.modificar(sql, lParams);        
    }
    
    public int borrar(String sql, List<SQLParams> lParams) throws SQLException {
       return this.modificar(sql, lParams);        
    }
    
    private int modificar(String sql, List<SQLParams> lParams) throws SQLException {
        int ret = 0;
        
        this.lParams = lParams;
        this.sql = sql;
        pstm = this.conn.prepareStatement(sql);
        this.armaDatos();
        ret = pstm.executeUpdate();
                
      return ret;  
    }
    
    public void consultarMetadata() throws SQLException {
         lResult = new ArrayList();                         
         
         int i = 1;
         while (rst != null && rst.next()) {
             SQLResult res = new SQLResult();           
             res.setColumna(rst.getMetaData().getColumnName(i));
             res.setDato(rst.getObject(i));
             res.setTipoDato(rst.getMetaData().getColumnTypeName(i));
             res.setTamanio(rst.getMetaData().getColumnDisplaySize(i));
             res.setSchema(rst.getMetaData().getSchemaName(i));
             res.setTabla(rst.getMetaData().getTableName(i));             
             lResult.add(res);
           i++;  
         }                      
         rst.beforeFirst();
    }
    
    public List<SQLParams> getSQLParams() {
        return lParams;
    }
    
    public List<SQLResult> getSQLResult() {
        return lResult;
    }

    private void armaDatos() throws SQLException {
        int i = 1;
        for (SQLParams pl : lParams) {
            switch (pl.getTipoDato()) {
                case SQLParams.CADENA_CARACTERES:
                    pstm.setString(i, String.valueOf(pl.getDato()));
                    break;
                case SQLParams.FECHA:
                    pstm.setDate(i, Date.valueOf(String.valueOf(pl.getDato())));
                    break;
                case SQLParams.NUMERO_DOUBLE:
                    pstm.setDouble(i, (Double)pl.getDato());
                    break;
                case SQLParams.NUMERO_ENTERO:
                    pstm.setInt(i, (Integer)pl.getDato());
                    break;
                case SQLParams.NUMERO_FLOAT:
                    pstm.setFloat(i, (Float)(pl.getDato()));
                    break;
                case SQLParams.NUMERO_LONG:
                    pstm.setLong(i, (Long)pl.getDato());
                    break;
                case SQLParams.NUMERO_BIGDECIMAL:
                    pstm.setBigDecimal(i, BigDecimal.valueOf((Double)pl.getDato()));
                    break;
            }

            sparam += "[" + i + "," + pl.getDato() + "," + pl.getTipoDato() + "] ";
            i++;
        }
    }

    private int modificar(String sql) throws SQLException {
        int reng = 0;
        this.sql = sql;

        stm = conn.createStatement();
        reng = stm.executeUpdate(sql);

        return reng;
    }

    public int actualizar(String sql) throws SQLException {
        return modificar(sql);
    }

    public int borrar(String sql) throws SQLException {
        return modificar(sql);
    }
    public int insertar(String sql) throws SQLException {
        return modificar(sql);
    }

    public void setCommit() throws SQLException {
        conn.commit();
    }

    public void setRollBack() throws SQLException {
        conn.rollback();
    }
    
    public String getSQL() {
        return sql;
    }
    
    public String getParams() {
        return sparam;
    }
    
    public void desconectar() {
        try {
            if (rst instanceof ResultSet) {
                rst.close();
            }

            if (stm instanceof Statement) {
                stm.close();
            }

            if (pstm instanceof PreparedStatement) {
                pstm.close();
            }

            if (conn instanceof Connection) {
                conn.close();
            }
        } catch (Exception e) {
            try {
                rst.close();
                stm.close();
                pstm.close();
                conn.close();
            } catch (SQLException f) {
            }
        } finally {
            rst = null;
            stm = null;
            pstm = null;
            conn = null;
        }
    }

}

