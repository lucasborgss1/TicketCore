package br.com.ticketcore.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "ticketcore-secret-key-minimo-32-caracteres-segura");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
        ReflectionTestUtils.invokeMethod(jwtUtil, "init");
    }

    @Test
    void tokenGeradoDeveSerValido() {
        String token = jwtUtil.gerarToken(1L, "user@email.com");
        assertTrue(jwtUtil.tokenValido(token));
    }

    @Test
    void deveExtrairIdUsuarioDoToken() {
        String token = jwtUtil.gerarToken(42L, "user@email.com");
        assertEquals(42L, jwtUtil.extrairIdUsuario(token));
    }

    @Test
    void tokenInvalidoDeveRetornarFalse() {
        assertFalse(jwtUtil.tokenValido("token.invalido.aqui"));
    }

    @Test
    void tokenVazioDeveRetornarFalse() {
        assertFalse(jwtUtil.tokenValido(""));
    }
}
