package com.hotel.system.domain.entity;

import com.hotel.system.domain.enums.EstadoEstadia;
import com.hotel.system.domain.enums.ModoUso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estadias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "modo_uso", nullable = false)
    private ModoUso modoUso;

    @Column(name = "tarifa_aplicada", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaAplicada;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "turno_id", nullable = false)
    private TurnoEntidad turno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEstadia estado = EstadoEstadia.ACTIVA;

    @PrePersist
    public void prePersist() {
        fechaHoraInicio = LocalDateTime.now();
    }
}