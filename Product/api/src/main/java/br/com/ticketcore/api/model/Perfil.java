package br.com.ticketcore.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perfil {
    private Long idPerfil;
    private String nmPerfil;
}