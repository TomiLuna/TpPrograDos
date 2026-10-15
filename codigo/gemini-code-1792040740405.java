package ar.edu.ungs.prog2.smartgrids;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SmartGridsTest {
    private static final String PUESTA = "2020-01-01T00:00:00";
    private static final String DIA1 = "2026-03-01";
    private static final String DIA2 = "2026-03-02";

    private ISmartGrids sistema;

    @Before
    public void setUp() {
        sistema = new SmartGrids();
        // cuota = 1000 * 0.5 * 12 / 24 = 250
        sistema.registrarPlantaSolar("Solar1", "Ruta 1", 1000, PUESTA, "Sol", 0.5, 5000, 2000, 12);
        // cuota = 400 * 0.5 = 200
        sistema.registrarPlantaEolica("Eolica1", "Ruta 2", 400, PUESTA, "Viento", 0.5, 10, 80, 5);
        // cuota = 800 * 0.25 = 200
        sistema.registrarPlantaHidraulica("Hidro1", "Ruta 3", 800, PUESTA, "Agua", 0.25, 4, 1000);
        // cuota = 600 / 2 = 300 y CO2 < 50
        sistema.registrarPlantaTermica("Termica1", "Ruta 4", 600, PUESTA, "Gas", 50, 900);
    }

    @Test
    public void registrarYConsultaPlantas() {
        assertEquals(Arrays.asList(
            "Solar1 (ACTIVA): " + PUESTA,
            "Eolica1 (ACTIVA): " + PUESTA,
            "Hidro1 (ACTIVA): " + PUESTA,
            "Termica1 (ACTIVA): " + PUESTA), sistema.consultarPlantas());
        assertTrue(sistema.consultarPlantas().get(0).startsWith("Solar1"));
    }

    @Test
    public void noPermiteNombresDuplicadosNiPlantasInexistentes() {
        // debe fallar porque ya existe una planta con ese nombre.
        assertNotNull(assertThrows(RuntimeException.class,
            () -> sistema.registrarPlantaTermica("Solar1", "x", 10, PUESTA, "Gas", 1, 1)));
        // debe fallar porque no existe ninguna planta con ese nombre.
        assertNotNull(assertThrows(RuntimeException.class,
            () -> sistema.totalGenerado("Nada")));
    }

    @Test
    public void capacidadMinimaParaCumplirCuota() {
        assertEquals(250, sistema.capacidadMinimaCuota("Solar1"), 0.1);
        assertEquals(200, sistema.capacidadMinimaCuota("Eolica1"), 0.1);
        assertEquals(200, sistema.capacidadMinimaCuota("Hidro1"), 0.1);
        assertEquals(300, sistema.capacidadMinimaCuota("Termica1"), 0.1);
    }

    @Test
    public void actualizaEstadoYBloqueaMonitoreoEnInactiva() {
        String d1_09 = DIA1 + "T09:00:00";
        String d1_10 = DIA1 + "T10:00:00";
        String d1_11 = DIA1 + "T11:00:00";
        String d1_12 = DIA1 + "T12:00:00";

        sistema.desactivarPlanta("Solar1", d1_09);
        assertFalse(sistema.estaActiva("Solar1"));
        // debe fallar porque la planta está desactivada
        assertNotNull(assertThrows(RuntimeException.class,
            () -> sistema.registrarMonitoreo("Solar1", d1_10, 100, 0)));

        sistema.activarPlanta("Solar1", d1_11);
        sistema.registrarMonitoreo("Solar1", d1_12, 100, 0);
        assertEquals(100, sistema.totalGenerado("Solar1"), 0.1);
    }

    @Test
    public void rechazaMonitoreoDeTipoIncorrectoDuplicadoOAnteriorAPuesta() {
        String d1_01 = DIA1 + "T01:00:00";

        // llama al método para registrar un monitoreo de planta eólica para una planta solar.
        assertNotNull(assertThrows(RuntimeException.class,
            () -> sistema.registrarMonitoreo("Solar1", d1_01, 1, 0, 1));

        // el primero registra ok. el 2do lanza error por registro duplicado.
        sistema.registrarMonitoreo("Hidro1", d1_01, 1, 0);
        assertNotNull(assertThrows(RuntimeException.class,
            () -> sistema.registrarMonitoreo("Hidro1", d1_01, 1, 0));

        // debe lanzar excepción por fecha anterior a puesta en marcha.
        assertNotNull(assertThrows(RuntimeException.class,
            () -> sistema.registrarMonitoreo("Hidro1", "2019-01-01T01:00:00", 1, 0));
    }

    @Test
    public void totalGeneradoEIncidenciasPorHora() {
        String d1_03 = DIA1 + "T03:00:00";
        String d2_03 = DIA2 + "T03:00:00";
        String d2_04 = DIA2 + "T04:00:00";
        sistema.registrarMonitoreo("Hidro1", d1_03, 100, 2);
        sistema.registrarMonitoreo("Hidro1", d2_03, 50, 1);
        sistema.registrarMonitoreo("Hidro1", d2_04, 25, 4);
        assertEquals(175, sistema.totalGenerado("Hidro1"), 0.1);
        int[] esperado = new int[24];
        esperado[3] = 3;
        esperado[4] = 4;
        assertArrayEquals(esperado, sistema.incidenciasPorHora("Hidro1"));
    }

    @Test
    public void histogramaDeCumplimientoPorHora() {
        String d1_12 = DIA1 + "T12:00:00";
        String d1_13 = DIA1 + "T13:00:00";
        String d2_12 = DIA2 + "T12:00:00";
        String d2_13 = DIA2 + "T13:00:00";

        sistema.registrarMonitoreo("Solar1", d1_12, 300, 0); // cumple
        sistema.registrarMonitoreo("Solar1", d2_12, 250, 0); // cumple
        sistema.registrarMonitoreo("Solar1", d1_13, 300, 0