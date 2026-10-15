package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaEolica extends Planta {
    private String fuentePrimaria;
    private double eficienciaPromedio;
    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double velocidadMinimaViento;

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima, LocalDateTime fechaPuesta,
            String fuentePrimaria, double eficienciaPromedio, int cantidadAerogeneradores, double alturaTorres,
            double velocidadMinimaViento) {
        super(nombre, direccion, capacidadMaxima, fechaPuesta);
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
        this.cantidadAerogeneradores = cantidadAerogeneradores;
        this.alturaTorres = alturaTorres;
        this.velocidadMinimaViento = velocidadMinimaViento;
    }

    public String getFuentePrimaria() { return fuentePrimaria; }
    public double getVelocidadMinimaViento() { return velocidadMinimaViento; }

    @Override
    public double capacidadMinimaCuota() {
        return getCapacidadMaxima() * eficienciaPromedio;
    }

    @Override
    public boolean cumpleCuota(RegistroMonitoreo m) {
        return m.getEnergiaGenerada() >= capacidadMinimaCuota();
    }

    @Override
    public void agregarMonitoreo(RegistroMonitoreo m) {
        if (m.getVelocidadViento() == null) {
            throw new RuntimeException("El monitoreo eolico debe incluir la velocidad del viento");
        }
        super.agregarMonitoreo(m);
    }
}
