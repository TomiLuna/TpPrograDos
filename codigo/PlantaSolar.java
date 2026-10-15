package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaSolar extends Planta {
    private String fuentePrimaria;
    private double eficienciaPromedio;
    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacionDiaria;

    public PlantaSolar(String nombre, String direccion, double capacidadMaxima, LocalDateTime fechaPuesta,
            String fuentePrimaria, double eficienciaPromedio, double superficieCubierta, int cantidadPaneles,
            double horasRadiacionDiaria) {
        super(nombre, direccion, capacidadMaxima, fechaPuesta);
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
        this.superficieCubierta = superficieCubierta;
        this.cantidadPaneles = cantidadPaneles;
        this.horasRadiacionDiaria = horasRadiacionDiaria;
    }

    public String getFuentePrimaria() { return fuentePrimaria; }

    @Override
    public double capacidadMinimaCuota() {
        return getCapacidadMaxima() * eficienciaPromedio * (horasRadiacionDiaria / 24.0);
    }

    @Override
    public boolean cumpleCuota(RegistroMonitoreo m) {
        return m.getEnergiaGenerada() >= capacidadMinimaCuota();
    }
}