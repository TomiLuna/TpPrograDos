package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Planta {
    private String nombre;
    private String direccion;
    private double capacidadMaxima;
    private LocalDateTime fechaPuestaEnFuncionamiento;
    private boolean activa;
    
    private List<PeriodoInactividad> periodosInactividad;
    private PeriodoInactividad periodoActual;
    private Map<LocalDateTime, RegistroMonitoreo> monitoreos;

    public Planta(String nombre, String direccion, double capacidadMaxima, LocalDateTime fechaPuesta) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la planta no puede estar vacio");
        }
        if (capacidadMaxima <= 0) {
            throw new RuntimeException("La capacidad maxima debe ser positiva");
        }
        if (fechaPuesta == null) {
            throw new RuntimeException("La fecha de puesta en funcionamiento no puede ser nula");
        }

        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaPuestaEnFuncionamiento = fechaPuesta;
        this.activa = true;
        this.periodosInactividad = new ArrayList<>();
        this.periodoActual = null;
        this.monitoreos = new HashMap<>();
    }

    // Métodos abstractos que debe implementar cada tipo de planta
    public abstract double capacidadMinimaCuota();
    public abstract boolean cumpleCuota(RegistroMonitoreo m);

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public double getCapacidadMaxima() { return capacidadMaxima; }
    public LocalDateTime getFechaPuestaEnFuncionamiento() { return fechaPuestaEnFuncionamiento; }
    public boolean isActiva() { return activa; }
    public Map<LocalDateTime, RegistroMonitoreo> getMonitoreos() { return monitoreos; }

    public void desactivar(LocalDateTime fechaHora) {
        if (!activa) return;
        if (fechaHora.isBefore(fechaPuestaEnFuncionamiento)) {
            throw new RuntimeException("La fecha de baja no puede ser anterior a la puesta en marcha");
        }
        activa = false;
        periodoActual = new PeriodoInactividad(fechaHora);
        periodosInactividad.add(periodoActual);
    }

    public void activar(LocalDateTime fechaHora) {
        if (activa) return;
        if (fechaHora.isBefore(fechaPuestaEnFuncionamiento)) {
            throw new RuntimeException("La fecha de alta no puede ser anterior a la puesta en marcha");
        }
        activa = true;
        if (periodoActual != null) {
            periodoActual.registrarReactivacion(fechaHora);
            periodoActual = null;
        }
    }

    public boolean estabaInactivaEn(LocalDateTime fechaHora) {
        for (PeriodoInactividad p : periodosInactividad) {
            if (p.estaInactivaEn(fechaHora)) {
                return true;
            }
        }
        return false;
    }

    public void agregarMonitoreo(RegistroMonitoreo m) {
        if (!activa) {
            throw new RuntimeException("No se puede registrar monitoreo en una planta inactiva");
        }
        if (m.getFechaHora().isBefore(fechaPuestaEnFuncionamiento)) {
            throw new RuntimeException("El monitoreo no puede ser anterior a la puesta en funcionamiento");
        }
        if (monitoreos.containsKey(m.getFechaHora())) {
            throw new RuntimeException("Ya existe un registro de monitoreo para esa fecha y hora");
        }
        monitoreos.put(m.getFechaHora(), m);
    }

    public double totalGenerado() {
        double total = 0;
        for (RegistroMonitoreo m : monitoreos.values()) {
            total += m.getEnergiaGenerada();
        }
        return total;
    }

    @Override
    public String toString() {
        String estado = activa ? "ACTIVA" : "INACTIVA";
        return nombre + " (" + estado + "): " + fechaPuestaEnFuncionamiento;
    }
}