package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class RegistroMonitoreo {
    private final LocalDateTime fechaHora;
    private final double energiaGenerada;
    private final int incidencias;
    private Double velocidadViento;
    private Double co2Emitido;
    private Double combustibleUtilizado;

    // Constructor base para monitoreos estándar
    public RegistroMonitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
        if (fechaHora == null) {
            throw new RuntimeException("La fecha y hora no pueden ser nulas");
        }
        if (energiaGenerada < 0) {
            throw new RuntimeException("La energia generada no puede ser negativa");
        }
        if (incidencias < 0) {
            throw new RuntimeException("Las incidencias no pueden ser negativas");
        }
        this.fechaHora = fechaHora;
        this.energiaGenerada = energiaGenerada;
        this.incidencias = incidencias;
    }

    // Constructor para eolicas
    public RegistroMonitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
        this(fechaHora, energiaGenerada, incidencias);
        if (velocidadViento < 0) {
            throw new RuntimeException("La velocidad del viento no puede ser negativa");
        }
        this.velocidadViento = velocidadViento;
    }

    // Constructor para termicas
    public RegistroMonitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
        this(fechaHora, energiaGenerada, incidencias);
        if (co2Emitido < 0 || combustibleUtilizado < 0) {
            throw new RuntimeException("Los valores de CO2 o combustible no pueden ser negativos");
        }
        this.co2Emitido = co2Emitido;
        this.combustibleUtilizado = combustibleUtilizado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getEnergiaGenerada() {
        return energiaGenerada;
    }

    public int getIncidencias() {
        return incidencias;
    }

    public Double getVelocidadViento() {
        return velocidadViento;
    }

    public Double getCo2Emitido() {
        return co2Emitido;
    }

    public Double getCombustibleUtilizado() {
        return combustibleUtilizado;
    }

    @Override
    public String toString() {
        return fechaHora + " - Energia: " + energiaGenerada + " kWh, Incidencias: " + incidencias;
    }
}