package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.ReporteBalanceDTO;
import restaurante_api.service.ReporteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // GET: /api/reportes/balance/hoy
    @GetMapping("/balance/hoy")
    public ResponseEntity<ReporteBalanceDTO> obtenerBalanceHoy() {
        return ResponseEntity.ok(reporteService.obtenerBalanceDelDia());
    }

    // GET: /api/reportes/balance?inicio=2026-07-01&fin=2026-07-31
    // Sirve para semanal, mensual o rangos personalizados
    @GetMapping("/balance")
    public ResponseEntity<ReporteBalanceDTO> obtenerBalancePorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        LocalDateTime fechaInicio = inicio.atStartOfDay();
        LocalDateTime fechaFin = fin.atTime(LocalTime.MAX);

        return ResponseEntity.ok(reporteService.obtenerBalancePorRango(fechaInicio, fechaFin));
    }
}