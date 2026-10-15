package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaHidraulica extends Planta {
    private String fuentePrimaria;
    private double eficienciaPromedio;
    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima, LocalDateTime fechaPuesta,
            String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaPuesta);
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    public String getFuentePrimaria() { return fuentePrimaria; }

    @Override
    public double capacidadMinimaCuota() {
        return getCapacidadMaxima() * eficienciaPromedio;
    }

    @Override
    public boolean cumpleCuota(RegistroMonitoreo m) {
        return m.getEnergiaGenerada() >= capacidadMinimaCuota();
    }
}