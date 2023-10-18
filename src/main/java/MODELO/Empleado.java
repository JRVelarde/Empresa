package MODELO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
    private int id;
    private String nombre;
    private double salario;
    private LocalDate nacido;
    private Departamento departamento;
    
}
