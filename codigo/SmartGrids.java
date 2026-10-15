package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SmartGrids implements ISmartGrids {

    private final Map<String, Planta> plantas;

    public SmartGrids() {
        this.plantas = new HashMap<>();
    }

    private LocalDateTime parsearFechaHora(String fechaHora) {
        if (fechaHora == null) {
            throw new RuntimeException("La fechaHora no puede ser nula");
        }
        try {
            return LocalDateTime.parse(fechaHora);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de fechaHora invalido: " + fechaHora);
        }
    }

    private LocalDate parsearFecha(String fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha no puede ser nula");
        }
        try {
            return LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de fecha invalido: " + fecha);
        }
    }

    private Planta obtenerPlantaExistente(String nombre) {
        Planta p = plantas.get(nombre);
        if (p == null) {
            throw new RuntimeException("La planta " + nombre + " no existe");
        }
        return p;
    }

    private void validarNombreNuevo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la planta es invalido");
        }
        if (plantas.containsKey(nombre)) {
            throw new RuntimeException("Ya existe una planta registrada con el nombre: " + nombre);
        }
    }

    @Override
    public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaHora(fechaHoraPuestaEnFuncionamiento);
        Planta p = new PlantaSolar(nombre, direccion, capacidadMaxima, fechaPuesta, fuentePrimaria,
                eficienciaPromedio, superficieCubierta, cantidadPaneles, horasRadiacionDiaria);
        plantas.put(nombre, p);
    }

    @Override
    public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaHora(fechaHoraPuestaEnFuncionamiento);
        Planta p = new PlantaEolica(nombre, direccion, capacidadMaxima, fechaPuesta, fuentePrimaria,
                eficienciaPromedio, cantidadAerogeneradores, alturaTorres, velocidadMinimaViento);
        plantas.put(nombre, p);
    }

    @Override
    public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadTurbinas, double capacidadEmbalse) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaHora(fechaHoraPuestaEnFuncionamiento);
        Planta p = new PlantaHidraulica(nombre, direccion, capacidadMaxima, fechaPuesta, fuentePrimaria,
                eficienciaPromedio, cantidadTurbinas, capacidadEmbalse);
        plantas.put(nombre, p);
    }

    @Override
    public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
            double capacidadAlmacenamientoCombustible) {
        validarNombreNuevo(nombre);
        LocalDateTime fechaPuesta = parsearFechaHora(fechaHoraPuestaEnFuncionamiento);
        Planta p = new PlantaTermica(nombre, direccion, capacidadMaxima, fechaPuesta, tipoCombustible,
                objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
        plantas.put(nombre, p);
    }

    @Override
    public List<String> consultarPlantas() {
        List<String> res = new ArrayList<>();
        for (Planta p : plantas.values()) {
            res.add(p.toString());
        }
        return res;
    }

    @Override
    public double capacidadMinimaCuota(String nombre) {
        Planta p = obtenerPlantaExistente(nombre);
        return p.capacidadMinimaCuota();
    }

    @Override
    public void activarPlanta(String nombre, String fechaHora) {
        Planta p = obtenerPlantaExistente(nombre);
        LocalDateTime dt = parsearFechaHora(fechaHora);
        p.activar(dt);
    }

    @Override
    public void desactivarPlanta(String nombre, String fechaHora) {
        Planta p = obtenerPlantaExistente(nombre);
        LocalDateTime dt = parsearFechaHora(fechaHora);
        p.desactivar(dt);
    }

    @Override
    public boolean estaActiva(String nombre) {
        Planta p = obtenerPlantaExistente(nombre);
        return p.isActiva();
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
        Planta p = obtenerPlantaExistente(nombre);
        LocalDateTime dt = parsearFechaHora(fechaHora);
        RegistroMonitoreo m = new RegistroMonitoreo(dt, energiaGenerada, incidencias);
        p.agregarMonitoreo(m);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double velocidadViento) {
        Planta p = obtenerPlantaExistente(nombre);
        LocalDateTime dt = parsearFechaHora(fechaHora);
        RegistroMonitoreo m = new RegistroMonitoreo(dt, energiaGenerada, incidencias, velocidadViento);
        p.agregarMonitoreo(m);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double co2Emitido, double combustibleUtilizado) {
        Planta p = obtenerPlantaExistente(nombre);
        LocalDateTime dt = parsearFechaHora(fechaHora);
        RegistroMonitoreo m = new RegistroMonitoreo(dt, energiaGenerada, incidencias, co2Emitido, combustibleUtilizado);
        p.agregarMonitoreo(m);
    }

    @Override
    public double totalGenerado(String nombre) {
        Planta p = obtenerPlantaExistente(nombre);
        return p.totalGenerado();
    }

    @Override
    public int[] incidenciasPorHora(String nombre) {
        Planta p = obtenerPlantaExistente(nombre);
        int[] res = new int[24];
        for (RegistroMonitoreo m : p.getMonitoreos().values()) {
            int hora = m.getFechaHora().getHour();
            res[hora] += m.getIncidencias();
        }
        return res;
    }

    @Override
    public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
        Planta p = obtenerPlantaExistente(nombre);
        LocalDate desde = parsearFecha(fechaDesde);
        LocalDate hasta = parsearFecha(fechaHasta);

        if (desde.isAfter(hasta)) {
            throw new RuntimeException("La fecha desde no puede ser posterior a la fecha hasta");
        }

        int[] histograma = new int[24];

        for (RegistroMonitoreo m : p.getMonitoreos().values()) {
            LocalDate fechaMonitoreo = m.getFechaHora().toLocalDate();
            if ((fechaMonitoreo.isEqual(desde) || fechaMonitoreo.isAfter(desde)) &&
                (fechaMonitoreo.isEqual(hasta) || fechaMonitoreo.isBefore(hasta))) {
                if (p.cumpleCuota(m)) {
                    int hora = m.getFechaHora().getHour();
                    histograma[hora]++;
                }
            }
        }
        return histograma;
    }

    @Override
    public Map<String, Integer> plantasRenovablesPorFuente() {
        Map<String, Integer> resultado = new TreeMap<>();
        for (Planta p : plantas.values()) {
            String fuente = null;
            if (p instanceof PlantaSolar) {
                fuente = ((PlantaSolar) p).getFuentePrimaria();
            } else if (p instanceof PlantaEolica) {
                fuente = ((PlantaEolica) p).getFuentePrimaria();
            } else if (p instanceof PlantaHidraulica) {
                fuente = ((PlantaHidraulica) p).getFuentePrimaria();
            }

            if (fuente != null) {
                resultado.put(fuente, resultado.getOrDefault(fuente, 0) + 1);
            }
        }
        return resultado;
    }

    @Override
    public List<String> plantasQueNuncaCumplieronCuota() {
        List<String> res = new ArrayList<>();
        for (Planta p : plantas.values()) {
            boolean cumplioAlgunaVez = false;
            for (RegistroMonitoreo m : p.getMonitoreos().values()) {
                if (p.cumpleCuota(m)) {
                    cumplioAlgunaVez = true;
                    break;
                }
            }
            if (!cumplioAlgunaVez) {
                res.add(p.getNombre());
            }
        }
        Collections.sort(res);
        return res;
    }

    @Override
    public String horasSinVelocidadMinimaPorPlantaEolica() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plantas Eolicas: Horas con poco viento\n");

        for (Planta p : plantas.values()) {
            if (p instanceof PlantaEolica) {
                PlantaEolica eolica = (PlantaEolica) p;
                
                // Horas con registros que superan el viento mínimo
                Set<Integer> horasConVientoSuficiente = new HashSet<>();
                // Registros agrupados por hora
                Map<Integer, List<RegistroMonitoreo>> monitoreosPorHora = new HashMap<>();

                for (RegistroMonitoreo m : eolica.getMonitoreos().values()) {
                    int hora = m.getFechaHora().getHour();
                    monitoreosPorHora.putIfAbsent(hora, new ArrayList<>());
                    monitoreosPorHora.get(hora).add(m);

                    if (m.getVelocidadViento() != null && m.getVelocidadViento() >= eolica.getVelocidadMinimaViento()) {
                        horasConVientoSuficiente.add(hora);
                    }
                }

                List<Integer> horasSinVientoMinimo = new ArrayList<>();
                for (Integer hora : monitoreosPorHora.keySet()) {
                    if (!horasConVientoSuficiente.contains(hora)) {
                        horasSinVientoMinimo.add(hora);
                    }
                }

                if (!horasSinVientoMinimo.isEmpty()) {
                    Collections.sort(horasSinVientoMinimo);
                    sb.append(eolica.getNombre()).append(": ")
                      .append((int) eolica.getVelocidadMinimaViento()).append(" m/s\n");

                    for (int hora : horasSinVientoMinimo) {
                        List<RegistroMonitoreo> lista = monitoreosPorHora.get(hora);
                        double vientoRegistrado = lista.get(0).getVelocidadViento();
                        String horaFormateada = String.format("%02d", hora);
                        sb.append("       ").append(horaFormateada).append(": ")
                          .append((int) vientoRegistrado).append(" m/s\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    public int limpiarMonitoreosSinDatos() {
        int eliminados = 0;
        for (Planta p : plantas.values()) {
            List<LocalDateTime> aBorrar = new ArrayList<>();
            for (RegistroMonitoreo m : p.getMonitoreos().values()) {
                if (m.getEnergiaGenerada() == 0 && m.getIncidencias() == 0) {
                    aBorrar.add(m.getFechaHora());
                }
            }
            for (LocalDateTime dt : aBorrar) {
                p.getMonitoreos().remove(dt);
                eliminados++;
            }
        }
        return eliminados;
    }

    @Override
    public List<String> plantasOrdenadasPorEnergiaGenerada() {
        List<Planta> lista = new ArrayList<>(plantas.values());
        lista.sort(new Comparator<Planta>() {
            @Override
            public int compare(Planta p1, Planta p2) {
                return Double.compare(p2.totalGenerado(), p1.totalGenerado());
            }
        });

        List<String> res = new ArrayList<>();
        for (Planta p : lista) {
            res.add(p.getNombre() + ": " + p.totalGenerado());
        }
        return res;
    }

    @Override
    public List<String> plantasInactivasEn(String fechaHora) {
        LocalDateTime dt = parsearFechaHora(fechaHora);
        List<String> res = new ArrayList<>();
        for (Planta p : plantas.values()) {
            if (p.estabaInactivaEn(dt)) {
                res.add(p.getNombre());
            }
        }
        Collections.sort(res);
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sistema de Monitoreo Energetico:\n");
        sb.append("Total de plantas registradas: ").append(plantas.size()).append("\n");
        for (Planta p : plantas.values()) {
            sb.append(" - ").append(p.toString()).append("\n");
        }
        return sb.toString();
    }
}
