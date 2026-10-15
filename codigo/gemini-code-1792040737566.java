package ar.edu.ungs.prog2.smartgrids;

public class CodigoCliente {
    private static final String INICIO = "2026-10-08T00:00:00";
    private static final String HORA_MON_1 = "2026-10-08T10:00:00";
    private static final String HORA_MON_2 = "2026-10-08T11:00:00";
    private static final String HORA_BAJA = "2026-10-08T12:00:00";

    public static void main(String[] args) {
        ISmartGrids sistema = new SmartGrids();
        imprimirEstado("Inicio", sistema);

        System.out.println("\nRegistrando plantas...");
        sistema.registrarPlantaSolar("Solar Norte", "Ruta 1", 1000, INICIO,
                "Sol", 0.8, 1500, 12, 10);
        sistema.registrarPlantaEolica("Eolica Sur", "Ruta 2", 800, INICIO,
                "Viento", 0.75, 4, 80, 5);

        System.out.println("Plantas: " + sistema.consultarPlantas());

        System.out.println("\nRegistrando monitoreos...");
        sistema.registrarMonitoreo("Solar Norte", HORA_MON_1, 0, 0);
        sistema.registrarMonitoreo("Solar Norte", HORA_MON_2, 500, 1);
        sistema.registrarMonitoreo("Eolica Sur", HORA_MON_1, 200, 0, 3);

        imprimirEstado("A mitad de la simulación", sistema);

        System.out.println("\nLimpiando monitoreos sin datos...");
        System.out.println("Registros eliminados(1): "
                + sistema.limpiarMonitoreosSinDatos());

        System.out.println("Energia solar(500): " + sistema.totalGenerado("Solar Norte"));
        System.out.println("Orden de energía: "
                + sistema.plantasOrdenadasPorEnergiaGenerada());

        System.out.println("\nDesactivando Eólica Sur...");
        sistema.desactivarPlanta("Eolica Sur", HORA_BAJA);
        System.out.println("Plantas inactivas: "
                + sistema.plantasInactivasEn("2026-10-08T13:00:00"));

        System.out.println("\nIntentando monitorear planta desactivada...");
        try {
            sistema.registrarMonitoreo("Eolica Sur", "2026-10-08T14:00:00", 100, 0, 3);
            System.out.println("ERROR: se esperaba una excepción al monitorear una planta desactivada.");
        } catch (Exception e) {
            System.out.println("Fallo esperado: " + e.getMessage());
        }

        imprimirEstado("Final", sistema);
    }

    private static void imprimirEstado(String momento, ISmartGrids sistema) {
        System.out.println("\n--- " + momento + " INICIO ---");
        System.out.println(sistema.toString());
        System.out.println("\n--- " + momento + " Plantas ---");
        System.out.println("Plantas registradas: " + sistema.consultarPlantas());
        System.out.println("\n--- " + momento + " FIN ---");
    }
}