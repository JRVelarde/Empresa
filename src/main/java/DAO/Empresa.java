package DAO;

import MODELO.Departamento;
import MODELO.Empleado;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;

public class Empresa {
    private static Connection connection;

    public Empresa(Connection connection) {
        this.connection = connection;
    }

    public void crearTablaDepartamento() {
        String sql = "CREATE TABLE IF NOT EXISTS departamento (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, jefe INTEGER)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("Tabla creada correctamente");
        } catch (SQLException e) {
            System.out.println("ERROR al crear la tabla departamento" + e.getMessage());
        }
    }

    public void crearTablaEmpleado() {
        String sql = "CREATE TABLE IF NOT EXISTS empleado (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, salario DOUBLE, nacido LOCALDATE, departamento INTEGER)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("Tabla empleado creada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla empleado: " + e.getMessage());
        }
    }

    public void insertarDepartamento(Departamento departamento) {
        String sql = "INSERT INTO departamento (nombre, jefe) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, departamento.getNombre());
            if (departamento.getJefe() != null) {
                preparedStatement.setInt(2, departamento.getJefe().getId());
            } else {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            }

            preparedStatement.executeUpdate();
            System.out.println("Departamento insertado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar departamento: " + e.getMessage());
        }
    }


    public void insertarEmpleado(Empleado empleado) {
        String sql = "INSERT INTO empleado (nombre, salario, nacido, departamento) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, empleado.getNombre());
            preparedStatement.setDouble(2, empleado.getSalario());
            preparedStatement.setObject(3, empleado.getNacido());

            if (empleado.getDepartamento() != null) {
                preparedStatement.setInt(4, empleado.getDepartamento().getId());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }

            preparedStatement.executeUpdate();


            System.out.println("Empleado insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar empleado: " + e.getMessage());
        }
    }

    public void actualizarDepartamento(Departamento departamento) {
        String sql = "UPDATE departamento SET nombre = ?, jefe = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, departamento.getNombre());

            if (departamento.getJefe() != null) {
                preparedStatement.setInt(2, departamento.getJefe().getId());
            } else {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            }

            preparedStatement.setInt(3, departamento.getId());
            preparedStatement.executeUpdate();

            System.out.println("Departamento actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar departamento: " + e.getMessage());
        }
    }

    public void actualizarEmpleado(Empleado empleado) {
        String sql = "UPDATE empleado SET nombre = ?, salario = ?, nacido = ?, departamento = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, empleado.getNombre());
            preparedStatement.setDouble(2, empleado.getSalario());
            preparedStatement.setObject(3, empleado.getNacido());

            if (empleado.getDepartamento() != null) {
                preparedStatement.setInt(4, empleado.getDepartamento().getId());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }

            preparedStatement.setInt(5, empleado.getId());
            preparedStatement.executeUpdate();

            System.out.println("Empleado actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
        }
    }


    public void eliminarEmpleado(int idEmpleado) {
        if (esJefeDepartamento(idEmpleado)) {
            actualizarJefeDepartamentoANull(idEmpleado);
        }

        String sql = "DELETE FROM empleado WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idEmpleado);
            preparedStatement.executeUpdate();
            System.out.println("Empleado eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
        }
    }

    private boolean esJefeDepartamento(int idEmpleado) {
        String sql = "SELECT id FROM departamento WHERE jefe = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idEmpleado);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Error al verificar si es jefe de departamento: " + e.getMessage());
            return false;
        }
    }

    private void actualizarJefeDepartamentoANull(int idEmpleado) {
        String sql = "UPDATE departamento SET jefe = NULL WHERE jefe = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idEmpleado);
            preparedStatement.executeUpdate();
            System.out.println("Se ha actualizado el jefe del departamento a NULL.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar jefe de departamento a NULL: " + e.getMessage());
        }
    }

    public void eliminarDepartamento(int idDepartamento) {
        actualizarEmpleadosDepartamentoANull(idDepartamento);

        String sql = "DELETE FROM departamento WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idDepartamento);
            preparedStatement.executeUpdate();
            System.out.println("Departamento eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar departamento: " + e.getMessage());
        }
    }

    private void actualizarEmpleadosDepartamentoANull(int idDepartamento) {
        String sql = "UPDATE empleado SET departamento = NULL WHERE departamento = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idDepartamento);
            preparedStatement.executeUpdate();
            System.out.println("Se han actualizado los empleados del departamento a NULL.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar empleados del departamento a NULL: " + e.getMessage());
        }
    }

    public static void visualizarTablaDepartamento(Connection connection) {
        String sql = "SELECT * FROM departamento";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("ID\tNombre\tJefe");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                int jefe = resultSet.getInt("jefe");
                System.out.println(id + "\t" + nombre + "\t" + jefe);
            }
        } catch (SQLException e) {
            System.err.println("Error al visualizar tabla departamento: " + e.getMessage());
        }
    }

    public static void visualizarTablaEmpleado(Connection connection) {
        String sql = "SELECT * FROM empleado";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("ID\tNombre\tSalario\tNacido\tDepartamento");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                double salario = resultSet.getDouble("salario");
                String nacido = resultSet.getString("nacido");
                int departamento = resultSet.getInt("departamento");
                System.out.println(id + "\t" + nombre + "\t" + salario + "\t" + nacido + "\t" + departamento);
            }
        } catch (SQLException e) {
            System.err.println("Error al visualizar tabla empleado: " + e.getMessage());
        }
    }

    public static Empleado buscarEmpleadoPorId(int idEmpleado) {
        String sql = "SELECT * FROM empleado WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idEmpleado);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Empleado empleado = new Empleado();
                    empleado.setId(resultSet.getInt("id"));
                    empleado.setNombre(resultSet.getString("nombre"));
                    empleado.setSalario(resultSet.getDouble("salario"));
                    empleado.setNacido(resultSet.getObject("nacido", LocalDate.class));
                    return empleado;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado por ID: " + e.getMessage());
        }

        return null;
    }
    public static Departamento buscarDepartamentoPorId(int idDepartamento) {
        String sql = "SELECT id, nombre, jefe FROM departamento WHERE id = ?";
        Departamento departamento = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idDepartamento);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nombre = resultSet.getString("nombre");
                    int jefeId = resultSet.getInt("jefe");

                    Empleado jefeDepartamento = buscarEmpleadoPorId(jefeId);

                    departamento = new Departamento(id, nombre, jefeDepartamento);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar departamento por ID: " + e.getMessage());
        }

        return departamento;
    }

}
