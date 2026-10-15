package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PeriodoInactividad {
    private final LocalDateTime inicio;
    private LocalDateTime fin;

    public PeriodoInactividad(LocalDateTime inicio) {
        if (inicio == null) {
            throw new RuntimeException("La fecha de inicio no puede ser nula");
        }
        this.inicio = inicio;
    }

    public void registrarReactivacion(LocalDateTime fin) {
        if (fin != null && fin.isBefore(inicio)) {
            throw new RuntimeException("La fecha de reactivacion no puede ser anterior al inicio de la baja");
        }
        this.fin = fin;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public boolean estaInactivaEn(LocalDateTime momento) {
        if (momento.isBefore(inicio)) {
            return false;
        }
        return fin == null || !momento.isAfter(fin);
    }

    @Override
    public String toString() {
        return "Inactiva desde " + inicio + (fin != null ? " hasta " + fin : " (actualmente inactiva)");
    }
}