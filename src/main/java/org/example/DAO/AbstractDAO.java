package org.example.DAO;

import org.example.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.ConnectionFactory.getConnection;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName().toLowerCase());
        return sb.toString();
    }

    private String createInsertQuery() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = type.getDeclaredFields();
        sb.append("INSERT ");
        sb.append(" INTO ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" (");
        for (int i = 1; i < fields.length; i++) {
            if (i == fields.length - 1) {
                sb.append(fields[i].getName()).append(") ");
            } else {
                sb.append(fields[i].getName()).append(",");
            }
        }
        sb.append(" VALUES");
        sb.append("(");
        for (int i = 1; i < fields.length; i++) {
            if (i == fields.length - 1) {
                sb.append("?");
            } else {
                sb.append("?,");
            }
        }
        sb.append(")");
        return sb.toString();
    }


    private String createDeleteQuery(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        sb.append("id= ");
        sb.append(id);
        return sb.toString();
    }


    private String createaUpdateQuery(int id) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = type.getDeclaredFields();
        sb.append("Update ");
        sb.append(type.getSimpleName()).append(" ");
        sb.append("SET ");
        for (int i = 1; i < fields.length; i++) {
            if (i == fields.length - 1) {
                sb.append(fields[i].getName()).append("=").append("?");
            } else {
                sb.append(fields[i].getName()).append("=").append("?,");
            }
        }
        sb.append(" WHERE ");
        sb.append("id = ");
        sb.append(id);
        return sb.toString();
    }


    /**
     * Function to extract all the data in the table
     * @return list of objects of what type of data is in the database table
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Function similar to findall that returns the data from the specified id
     * @param id the id searched for
     * @return the data at the specified ID
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> temp = createObjects(resultSet);
            if (temp.size() == 0)
                return null;

            return temp.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (Constructor constructor : ctors) {
            ctor = constructor;
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException |
                 InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Function to insert data in the database
     * @param dates the string of data that is to be inserted
     */
    public void insert(String[] dates) {
        for (String str : dates) {
            if (str.equals(""))
                return;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            uptdateVariable(statement, dates);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void uptdateVariable(PreparedStatement statement, String[] dates) throws SQLException {
        Field[] fields = type.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            String aux;
            aux = fields[i].getType().getSimpleName();
            switch (aux) {
                case "String" -> statement.setString(i, dates[i - 1]);
                case "int" -> statement.setInt(i, Integer.parseInt(dates[i - 1]));
            }
        }
    }

    /**
     * Function to update the data already existent in the database
     * @param id the specified id
     * @param dates the specified data you wish to update in order
     */
    public void update(int id, String[] dates) {
        String[] original = extractLine(findById(id));
        String[] finalDates = dates.clone();
        for (int i = 1; i < original.length; i++) {
            if (finalDates[i - 1].equals("")) {
                finalDates[i - 1] = original[i];
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        String query = createaUpdateQuery(id);
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            uptdateVariable(statement, finalDates);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Function to delete a row in the table at a specified id
     * @param id the specified id
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery(id);
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Function for the simple use of the data extracted by the findall function
     * @param t generic param the type of current working table
     * @return the array of Strings extracted from the generic param
     */
    public String[] extractLine(T t) {
        if (t == null)
            return null;
        String[] data = new String[type.getDeclaredFields().length];
        Field[] fields = t.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fields[i].getName(), t.getClass());
                data[i] = String.valueOf(propertyDescriptor.getReadMethod().invoke(t));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * Function to return the header of the current working table
     * @return the header of the table as an array of Strings
     */
    public String[] getHeader() {
        Field[] fields = type.getDeclaredFields();
        String[] header = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            header[i] = fields[i].getName();
        }
        return header;
    }


}

