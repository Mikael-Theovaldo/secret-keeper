package test.java;

import main.java.com.secretKeeper.model.Participante;
import main.java.com.secretKeeper.repository.ParticipanteRepository;

import main.java.com.secretKeeper.service.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class Teste {

    // ---------- SorteioSimplesService ----------
    @Test
    public void testSortearSimplesValido() {
        List<String> participantes = Arrays.asList("Ana", "Bruno", "Carlos");
        List<String> sorteados = SorteioSimplesService.sortear(participantes, 2);
        assertEquals(2, sorteados.size());
        assertTrue(participantes.containsAll(sorteados));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSortearSimplesInvalido() {
        List<String> participantes = Arrays.asList("Ana");
        SorteioSimplesService.sortear(participantes, 1);
    }

    // ---------- FormacaoTimesServices ----------
    @Test
    public void testFormarTimesDistribuicao() {
        FormacaoTimesService service = new FormacaoTimesService();
        List<String> participantes = Arrays.asList("Ana", "Bruno", "Carlos", "Daniel");
        List<List<String>> times = service.formarTimes(participantes, 2);

        assertEquals(2, times.size());
        assertEquals(4, times.get(0).size() + times.get(1).size());
    }

    // ---------- OrdemApresentacaoService ----------
    @Test
    public void testGerarOrdemNaoVazio() {
        OrdemApresentacaoService service = new OrdemApresentacaoService();
        List<String> participantes = Arrays.asList("Ana", "Bruno", "Carlos");
        List<String> ordem = service.gerarOrdem(participantes);

        assertEquals(3, ordem.size());
        assertTrue(ordem.containsAll(participantes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGerarOrdemInvalido() {
        OrdemApresentacaoService service = new OrdemApresentacaoService();
        service.gerarOrdem(Collections.emptyList());
    }

    // ---------- SorteioNumeroService ----------
    @Test
    public void testSortearComRepeticao() {
        SorteioNumeroService service = new SorteioNumeroService();
        List<Integer> numeros = service.sortearComRepeticao(1, 10, 5, SorteioNumeroService.OrdemResultado.CRESCENTE);

        assertEquals(5, numeros.size());
        assertTrue(numeros.get(0) <= numeros.get(numeros.size()-1)); // crescente
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSortearSemRepeticaoErro() {
        SorteioNumeroService service = new SorteioNumeroService();
        service.sortearSemRepeticao(1, 5, 10, SorteioNumeroService.OrdemResultado.ALEATORIO);
    }

    // ---------- AmigoSecretoService ----------
    @Test
    public void testAmigoSecretoValido() {
        List<String> nomes = Arrays.asList("Ana", "Bruno", "Carlos");
        List<Participante> resultado = AmigoSecretoService.sortear(nomes);

        assertEquals(3, resultado.size());
        for (Participante p : resultado) {
            assertNotNull(p.getAmigoSecreto());
            assertNotEquals(p, p.getAmigoSecreto());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAmigoSecretoMinimoParticipantes() {
        List<String> nomes = Arrays.asList("Ana", "Bruno");
        AmigoSecretoService.sortear(nomes);
    }

    // ---------- ParticipanteRepository ----------
    @Test
    public void testAdicionarRemoverParticipante() {
        ParticipanteRepository.adicionar("Ana");
        ParticipanteRepository.adicionar("Bruno");

        assertEquals(2, ParticipanteRepository.totalParticipante());

        ParticipanteRepository.remover(1); // remove "Ana" (1-based)
        assertEquals(1, ParticipanteRepository.totalParticipante());
    }
}
