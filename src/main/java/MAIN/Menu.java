package MAIN;

import DAO.ConexionDB;
import DAO.Empresa;
import MODELO.Departamento;
import MODELO.Empleado;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static DAO.Empresa.visualizarTablaDepartamento;
import static DAO.Empresa.visualizarTablaEmpleado;

public class Menu {
    public static void main(String[] args) {
        Connection connection = ConexionDB.getConnection();
        Empresa empresa = new Empresa(connection);
        Scanner scanner = new Scanner(System.in);

        // Crear las tablas al iniciar la aplicación
        empresa.crearTablaDepartamento();
        empresa.crearTablaEmpleado();

        int opcion;
        do {
            System.out.println("Menú de opciones:");
            System.out.println("1. Insertar departamento");
            System.out.println("2. Insertar empleado");
            System.out.println("3. Actualizar departamento");
            System.out.println("4. Actualizar empleado");
            System.out.println("5. Eliminar departamento");
            System.out.println("6. Eliminar empleado");
            System.out.println("7. Visualizar tabla departamento");
            System.out.println("8. Visualizar tabla empleado");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    //Insertar departamento
                    System.out.print("Nombre del departamento: ");
                    String nombreDepartamento = scanner.next();
                    Departamento departamento = new Departamento();
                    departamento.setNombre(nombreDepartamento);
                    empresa.insertarDepartamento(departamento);
                    System.out.println("Conoces el id del empleado?");
                    String Resultado = scanner.next();
                    if(Resultado.toLowerCase()=="si" || Resultado.toLowerCase()=="sí" ){
                        System.out.println("Dime el ID del empleado");
                        int idEmpleado = scanner.nextInt();
                        Empleado empleado = new Empleado();
                        empleado = Empresa.buscarEmpleadoPorId(idEmpleado);
                        empresa.insertarEmpleado(empleado);
                    }
                    break;
                case 2:
                    //Insertar empleado
                    System.out.print("Nombre del empleado: ");
                    String nombreEmpleado = scanner.next();
                    System.out.print("Salario del empleado: ");
                    double salarioEmpleado = scanner.nextDouble();
                    System.out.print("Fecha de nacimiento del empleado (AAAA-MM-DD): ");
                    String fechaNacimiento = scanner.next();
                    System.out.print("ID del departamento al que pertenece el empleado (si conoces el id del departamento): ");
                    int idDepartamento = scanner.nextInt();

                    Empleado empleado = new Empleado();
                    empleado.setNombre(nombreEmpleado);
                    empleado.setSalario(salarioEmpleado);
                    empleado.setNacido(LocalDate.parse(fechaNacimiento));
                    if (idDepartamento != -1) {
                        Departamento departamentoEmpleado = new Departamento();
                        departamentoEmpleado.setId(idDepartamento);
                        empleado.setDepartamento(departamentoEmpleado);
                    }
                    empresa.insertarEmpleado(empleado);
                    break;
                case 3:
                    // Actualizar Departamento
                    System.out.print("Ingrese el ID del departamento que desea actualizar: ");
                    int idDepartamentoActualizar = scanner.nextInt();
                    System.out.print("Ingrese el nuevo nombre del departamento: ");
                    scanner.nextLine();
                    String nuevoNombreDepartamento = scanner.nextLine();

                    System.out.print("¿Desea actualizar el jefe del departamento? (S/N): ");
                    String actualizarJefe = scanner.nextLine();
                    Empleado empleado1 =null;
                    int idJefeDepartamento = -1;

                    if (actualizarJefe.toLowerCase()=="si" || actualizarJefe.toLowerCase()=="sí" ) {
                        System.out.print("Ingrese el ID del nuevo jefe del departamento: ");
                        idJefeDepartamento = scanner.nextInt();
                        empleado1 = Empresa.buscarEmpleadoPorId(idJefeDepartamento);
                    }

                    Departamento departamentoActualizado = new Departamento(idDepartamentoActualizar, nuevoNombreDepartamento, empleado1);
                    empresa.actualizarDepartamento(departamentoActualizado);
                    break;

                case 4:
                    // Actualizar Empleado
                    System.out.print("Ingrese el ID del empleado que desea actualizar: ");
                    int idEmpleadoActualizar = scanner.nextInt();
                    System.out.print("Ingrese el nuevo nombre del empleado: ");
                    scanner.nextLine();
                    String nuevoNombreEmpleado = scanner.nextLine();
                    System.out.print("Ingrese el nuevo salario del empleado: ");
                    double nuevoSalarioEmpleado = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Ingrese la nueva fecha de nacimiento (AAAA-MM-DD): ");
                    String nuevaFechaNacimiento = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fechaLocalDate = LocalDate.parse(nuevaFechaNacimiento, formatter);

                    System.out.print("¿Desea actualizar el departamento del empleado? (S/N): ");
                    String actualizarDepartamento = scanner.nextLine();
                    Departamento departamento1 = null;
                    int idDepartamentoEmpleado = -1;

                    if (actualizarDepartamento.toLowerCase()=="si" || actualizarDepartamento.toLowerCase()=="sí" ) {
                        System.out.print("Ingrese el ID del nuevo departamento del empleado: ");
                        idDepartamentoEmpleado = scanner.nextInt();
                        departamento1 = Empresa.buscarDepartamentoPorId(idDepartamentoEmpleado);
                    }

                    Empleado empleadoActualizado = new Empleado(idEmpleadoActualizar, nuevoNombreEmpleado, nuevoSalarioEmpleado, fechaLocalDate, departamento1);
                    empresa.actualizarEmpleado(empleadoActualizado);
                    break;

                case 5:
                    // Eliminar Departamento
                    System.out.print("Ingrese el ID del departamento que desea eliminar: ");
                    int idDepartamentoEliminar = scanner.nextInt();
                    empresa.eliminarDepartamento(idDepartamentoEliminar);
                    break;
                case 6:
                    // Eliminar Empleado
                    System.out.print("Ingrese el ID del empleado que desea eliminar: ");
                    int idEmpleadoEliminar = scanner.nextInt();
                    empresa.eliminarEmpleado(idEmpleadoEliminar);
                    break;
                case 7:
                    // Visualizar tabla departamento
                    visualizarTablaDepartamento(connection);
                    break;
                case 8:
                    // Visualizar tabla empleado
                    visualizarTablaEmpleado(connection);
                    break;
                case 9:
                    System.out.println("Saliendo del programa.");
                    ConexionDB.close();
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (opcion != 9);
    }
}
