package ar.edu.ungs.prog2.smartgrids;

import java.util.List;
import java.util.Map;

/**
 * Interfaz pública del sistema SmartGrids.
 */
public interface ISmartGrids {

    void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria);

    void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento);

    void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadTurbinas, double capacidadEmbalse);

    void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
            double capacidadAlmacenamientoCombustible);

    List<String> consultarPlantas();

    double capacidadMinimaCuota(String nombre);

    void activarPlanta(String nombre, String fechaHora);

    void desactivarPlanta(String nombre, String fechaHora);

    boolean estaActiva(String nombre);

    void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias);

    void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double velocidadViento);

    void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double co2Emitido, double combustibleUtilizado);

    double totalGenerado(String nombre);

    int[] incidenciasPorHora(String nombre);

    int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta);

    Map<String, Integer> plantasRenovablesPorFuente();

    List<String> plantasQueNuncaCumplieronCuota();

    String horasSinVelocidadMinimaPorPlantaEolica();

    int limpiarMonitoreosSinDatos();

    List<String> plantasOrdenadasPorEnergiaGenerada();

    List<String> plantasInactivasEn(String fechaHora);
}