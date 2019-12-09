/*
 * DriverWrapper.java
 * 
 * PostGIS extension for PostgreSQL JDBC driver - Wrapper utility class
 * 
 * (C) 2005 Markus Schaber, markus.schaber@logix-tt.com
 * 
 * (C) 2015 Phillip Ross, phillip.w.g.ross@gmail.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */

package com.github.lonelyleaf.gis.db;

import org.postgis.DriverWrapperAutoprobe;
import org.postgis.DriverWrapperLW;
import org.postgresql.Driver;
import org.postgresql.PGConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 修改自{@link org.postgis.DriverWrapper},但添加了geography类型
 */
public class DriverWrapper extends Driver {

    protected static final Logger logger = Logger.getLogger("org.postgis.DriverWrapper");
    
    public static final String POSTGRES_PROTOCOL = "jdbc:postgresql:";
    public static final String POSTGIS_PROTOCOL = "jdbc:postgresql_postGIS:";
    public static final String REVISION = "$Revision$";
    protected static TypesAdder ta72 = null;
    protected static TypesAdder ta74 = null;
    protected static TypesAdder ta80 = null;

    protected TypesAdder typesAdder;

    /**
     * Default constructor.
     * 
     * This also loads the appropriate TypesAdder for our SQL Driver instance.
     * 
     * @throws SQLException when a SQLException occurs
     */
    public DriverWrapper() throws SQLException {
        super();
        typesAdder = getTypesAdder(this);
        // The debug method is @since 7.2
        if (super.getMajorVersion() > 8 || super.getMinorVersion() > 1) {
            logger.fine(this.getClass().getName() + " loaded TypesAdder: "
                    + typesAdder.getClass().getName());
        }
    }

    protected static TypesAdder getTypesAdder(Driver d) throws SQLException {
        if (d.getMajorVersion() == 7) {
            if (d.getMinorVersion() >= 3) {
                if (ta74 == null) {
                    ta74 = loadTypesAdder("74");
                }
                return ta74;
            } else {
                if (ta72 == null) {
                    ta72 = loadTypesAdder("72");
                }
                return ta72;
            }
        } else {
            if (ta80 == null) {
                ta80 = loadTypesAdder("80");
            }
            return ta80;
        }
    }

    private static TypesAdder loadTypesAdder(String version) throws SQLException {
        try {
            Class klass = Class.forName("com.github.lonelyleaf.gis.db.DriverWrapper$TypesAdder" + version);
            return (TypesAdder) klass.newInstance();
        } catch (Exception e) {
            throw new SQLException("Cannot create TypesAdder instance! " + e.getMessage());
        }
    }

    static {
        try {
            // Try to register ourself to the DriverManager
            java.sql.DriverManager.registerDriver(new DriverWrapper());
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error registering PostGIS Wrapper Driver", e);
        }
    }

    /**
     * Creates a postgresql connection, and then adds the PostGIS data types to
     * it calling addpgtypes()
     * 
     * @param url the URL of the database to connect to
     * @param info a list of arbitrary tag/value pairs as connection arguments
     * @return a connection to the URL or null if it isnt us
     * @exception SQLException if a database access error occurs
     * 
     * @see java.sql.Driver#connect
     * @see Driver
     */
    public Connection connect(String url, Properties info) throws SQLException {
        url = mangleURL(url);
        Connection result = super.connect(url, info);
        typesAdder.addGT(result, useLW(result));
        return result;
    }

    /**
     * Do we have HexWKB as well known text representation - to be overridden by
     * subclasses.
     *
     * @param result Connection to check
     * @return true if using EWKB, false otherwise
     */
    protected boolean useLW(Connection result) {
        if (result == null) {
            throw new IllegalArgumentException("null is no valid parameter");
        }
        return false;
    }

    /**
     * Check whether the driver thinks he can handle the given URL.
     *
     * @see java.sql.Driver#acceptsURL
     * @param url the URL of the driver
     * @return true if this driver accepts the given URL
     */
    public boolean acceptsURL(String url) {
        try {
            url = mangleURL(url);
        } catch (SQLException e) {
            return false;
        }
        return super.acceptsURL(url);
    }

    /**
     * Returns our own CVS version plus postgres Version
     *
     * @return String value reprenstation of the version
     */
    public static String getVersion() {
        return "PostGisWrapper " + REVISION + ", wrapping " + Driver.getVersion();
    }

    /*
     * Here follows the addGISTypes() stuff. This is a little tricky because the
     * pgjdbc people had several, partially incompatible API changes during 7.2
     * and 8.0. We still want to support all those releases, however.
     *
     */
    /**
     * adds the JTS/PostGIS Data types to a PG 7.3+ Connection. If you use
     * PostgreSQL jdbc drivers V8.0 or newer, those methods are deprecated due
     * to some class loader problems (but still work for now), and you may want
     * to use the method below instead.
     *
     * @param pgconn The PGConnection object to add the types to
     * @throws SQLException when a SQLException occurs
     *
     */
    public static void addGISTypes(PGConnection pgconn) throws SQLException {
        loadTypesAdder("74").addGT((Connection) pgconn, false);
    }

    /**
     * adds the JTS/PostGIS Data types to a PG 8.0+ Connection.
     *
     * @param pgconn The PGConnection object to add the types to
     * @throws SQLException when a SQLException occurs
     */
    public static void addGISTypes80(PGConnection pgconn) throws SQLException {
        loadTypesAdder("80").addGT((Connection) pgconn, false);
    }

    /**
     * adds the JTS/PostGIS Data types to a PG 7.2 Connection.
     *
     * @param pgconn The PGConnection object to add the types to
     * @throws SQLException when a SQLException occurs
     */
    public static void addGISTypes72(PGConnection pgconn) throws SQLException {
        loadTypesAdder("72").addGT((Connection) pgconn, false);
    }

    /**
     * Mangles the PostGIS URL to return the original PostGreSQL URL
     *
     * @param url String containing the url to be "mangled"
     * @return "mangled" string
     * @throws SQLException when a SQLException occurs
     */
    protected String mangleURL(String url) throws SQLException {
        String myProgo = getProtoString();
        if (url.startsWith(myProgo)) {
            return POSTGRES_PROTOCOL + url.substring(myProgo.length());
        } else {
            throw new SQLException("Unknown protocol or subprotocol in url " + url);
        }
    }

    protected String getProtoString() {
        return POSTGIS_PROTOCOL;
    }

    /** Base class for the three typewrapper implementations */
    public abstract static class TypesAdder {
        public final void addGT(Connection conn, boolean lw) throws SQLException {
            if (lw) {
                addBinaryGeometries(conn);
            } else {
                addGeometries(conn);
            }
            addBoxen(conn);
        }

        public abstract void addGeometries(Connection conn) throws SQLException;

        public abstract void addBoxen(Connection conn) throws SQLException;

        public abstract void addBinaryGeometries(Connection conn) throws SQLException;
    }

    /** addGISTypes for V7.3 and V7.4 pgjdbc */
    @Deprecated
    public static final class TypesAdder74 extends TypesAdder {
        public void addGeometries(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("geometry", org.postgis.PGgeometry.class);
            pgconn.addDataType("public.geometry", org.postgis.PGgeometry.class);
            pgconn.addDataType("\"public\".\"geometry\"", org.postgis.PGgeometry.class);
        }

        public void addBoxen(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("box3d", org.postgis.PGbox3d.class);
            pgconn.addDataType("box2d", org.postgis.PGbox2d.class);
        }

        public void addBinaryGeometries(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("geometry", org.postgis.PGgeometryLW.class);
        }
    }

    /** addGISTypes for V7.2 pgjdbc */
    @Deprecated
    public static class TypesAdder72 extends TypesAdder {
        public void addGeometries(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("geometry", org.postgis.PGgeometry.class);
            pgconn.addDataType("public.geometry", org.postgis.PGgeometry.class);
            pgconn.addDataType("\"public\".\"geometry\"", org.postgis.PGgeometry.class);
        }

		public void addBoxen(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("box3d", org.postgis.PGbox3d.class);
            pgconn.addDataType("box2d", org.postgis.PGbox2d.class);
        }

        public void addBinaryGeometries(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("geometry", org.postgis.PGgeometryLW.class);
        }
    }

    /** addGISTypes for V8.0 (and hopefully newer) pgjdbc */
    public static class TypesAdder80 extends TypesAdder {
        public void addGeometries(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("geometry", org.postgis.PGgeometry.class);
            pgconn.addDataType("public.geometry", org.postgis.PGgeometry.class);
            pgconn.addDataType("\"public\".\"geometry\"", org.postgis.PGgeometry.class);
//            老的代码没有添加
            pgconn.addDataType("geography", org.postgis.PGgeometry.class);
            pgconn.addDataType("public.geography", org.postgis.PGgeometry.class);
            pgconn.addDataType("\"public\".\"geography\"", org.postgis.PGgeometry.class);
        }

        public void addBoxen(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("box3d", org.postgis.PGbox3d.class);
            pgconn.addDataType("box2d", org.postgis.PGbox2d.class);
        }

        public void addBinaryGeometries(Connection conn) throws SQLException {
            PGConnection pgconn = (PGConnection) conn;
            pgconn.addDataType("geometry", org.postgis.PGgeometryLW.class);
            pgconn.addDataType("geography", org.postgis.PGgeometryLW.class);
        }
    }

    public Logger getParentLogger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
