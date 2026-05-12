package variosSorteioSwing;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Revela extends JFrame {

    // ==============================
    // COMPONENTES DA INTERFACE
    // ==============================

    private JTextField campoNome;

    private DefaultListModel<String> modeloLista;

    private JList<String> listaParticipantes;

    private JTextArea areaResultado;

    // ==============================
    // DADOS
    // ==============================

    private final List <String> participantes = new ArrayList<>();

    // ==============================
    // CONSTRUTOR
    // ==============================

    public Revela() {

        configurarJanela();

        criarComponentes();

        setVisible(true);
    }

    // ==============================
    // MAIN
    // ==============================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(Revela::new);
    }

    // ==============================
    // CONFIGURAÇÕES DA JANELA
    // ==============================

    private void configurarJanela() {

        setTitle("REVELA — Sistema de Sorteios");

        setSize(1000, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout(15, 15));

        getContentPane().setBackground(new Color(25, 25, 35));
    }

    // ==============================
    // CRIAR COMPONENTES
    // ==============================

    private void criarComponentes() {

        // ==============================
        // TOPO
        // ==============================

        JPanel painelTopo = new JPanel();

        painelTopo.setBackground(new Color(35, 35, 50));

        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("REVELA — SISTEMA DE SORTEIOS");

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        titulo.setForeground(Color.WHITE);

        painelTopo.add(titulo);

        add(painelTopo, BorderLayout.NORTH);

        // ==============================
        // LADO ESQUERDO
        // ==============================

        JPanel painelEsquerdo = new JPanel();

        painelEsquerdo.setLayout(new BorderLayout(10, 10));

        painelEsquerdo.setPreferredSize(new Dimension(300, 0));

        painelEsquerdo.setBackground(new Color(40, 40, 55));

        painelEsquerdo.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // CAMPO + BOTÃO

        JPanel painelAdicionar = new JPanel(new BorderLayout(5, 5));

        painelAdicionar.setBackground(new Color(40, 40, 55));

        campoNome = new JTextField();

        campoNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton btnAdicionar = criarBotao("Adicionar");

        painelAdicionar.add(campoNome, BorderLayout.CENTER);

        painelAdicionar.add(btnAdicionar, BorderLayout.EAST);

        // LISTA

        modeloLista = new DefaultListModel<>();

        listaParticipantes = new JList<>(modeloLista);

        listaParticipantes.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JScrollPane scrollLista = new JScrollPane(listaParticipantes);

        // BOTÃO REMOVER

        JButton btnRemover = criarBotao("Remover");

        // ADICIONAR AO PAINEL

        painelEsquerdo.add(painelAdicionar, BorderLayout.NORTH);

        painelEsquerdo.add(scrollLista, BorderLayout.CENTER);

        painelEsquerdo.add(btnRemover, BorderLayout.SOUTH);

        add(painelEsquerdo, BorderLayout.WEST);

        // ==============================
        // CENTRO (RESULTADOS)
        // ==============================

        JPanel painelCentro = new JPanel(new BorderLayout());

        painelCentro.setBackground(new Color(30, 30, 45));

        painelCentro.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JLabel tituloResultado = new JLabel("RESULTADOS");

        tituloResultado.setForeground(Color.WHITE);

        tituloResultado.setFont(new Font("Segoe UI", Font.BOLD, 20));

        areaResultado = new JTextArea();

        areaResultado.setEditable(false);

        areaResultado.setFont(new Font("Consolas", Font.PLAIN, 16));

        areaResultado.setBackground(new Color(20, 20, 30));

        areaResultado.setForeground(Color.GREEN);

        JScrollPane scrollResultado = new JScrollPane(areaResultado);

        painelCentro.add(tituloResultado, BorderLayout.NORTH);

        painelCentro.add(scrollResultado, BorderLayout.CENTER);

        add(painelCentro, BorderLayout.CENTER);

        // ==============================
        // LADO DIREITO (BOTÕES)
        // ==============================

        JPanel painelDireito = new JPanel();

        painelDireito.setLayout(new GridLayout(5, 1, 10, 10));

        painelDireito.setPreferredSize(new Dimension(220, 0));

        painelDireito.setBackground(new Color(40, 40, 55));

        painelDireito.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JButton btnAmigo = criarBotao("Amigo Secreto");

        JButton btnSorteio = criarBotao("Sorteio Simples");

        JButton btnTimes = criarBotao("Formar Times");

        JButton btnOrdem = criarBotao("Ordem Apresentação");

        JButton btnLimpar = criarBotao("Limpar Resultados");

        painelDireito.add(btnAmigo);

        painelDireito.add(btnSorteio);

        painelDireito.add(btnTimes);

        painelDireito.add(btnOrdem);

        painelDireito.add(btnLimpar);

        add(painelDireito, BorderLayout.EAST);

        // ==============================
        // EVENTOS
        // ==============================

        btnAdicionar.addActionListener(e -> adicionarParticipante());

        btnRemover.addActionListener(e -> removerParticipante());

        btnOrdem.addActionListener(e -> gerarOrdem());

        btnSorteio.addActionListener(e -> sorteioSimples());

        btnTimes.addActionListener(e -> formarTimes());

        btnAmigo.addActionListener(e -> amigoSecreto());

        btnLimpar.addActionListener(e -> areaResultado.setText(""));
    }

    // ==============================
    // CRIAR BOTÕES
    // ==============================

    private JButton criarBotao(String texto) {

        JButton botao = new JButton(texto);

        botao.setFocusPainted(false);

        botao.setFont(new Font("Segoe UI", Font.BOLD, 15));

        botao.setBackground(new Color(70, 130, 180));

        botao.setForeground(Color.WHITE);

        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return botao;
    }

    // ==============================
    // ADICIONAR PARTICIPANTE
    // ==============================

    private void adicionarParticipante() {

        String nome = campoNome.getText().trim();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite um nome."
            );

            return;
        }

        for (String p : participantes) {

            if (p.equalsIgnoreCase(nome)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Participante já existe."
                );

                return;
            }
        }

        participantes.add(nome);

        modeloLista.addElement(nome);

        campoNome.setText("");

        campoNome.requestFocus();
    }

    // ==============================
    // REMOVER PARTICIPANTE
    // ==============================

    private void removerParticipante() {

        int indice = listaParticipantes.getSelectedIndex();

        if (indice == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um participante."
            );

            return;
        }

        participantes.remove(indice);

        modeloLista.remove(indice);
    }

    // ==============================
    // ORDEM DE APRESENTAÇÃO
    // ==============================

    private void gerarOrdem() {

        if (participantes.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum participante."
            );

            return;
        }

        List<String> ordem = new ArrayList<>(participantes);

        Collections.shuffle(ordem);

        areaResultado.setText("");

        areaResultado.append("===== ORDEM DE APRESENTAÇÃO =====\n\n");

        for (int i = 0; i < ordem.size(); i++) {

            areaResultado.append(
                    (i + 1)
                            + ". "
                            + ordem.get(i)
                            + "\n"
            );
        }
    }

    // ==============================
    // SORTEIO SIMPLES
    // ==============================

    private void sorteioSimples() {

        if (participantes.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum participante."
            );

            return;
        }

        List<String> copia = new ArrayList<>(participantes);

        Collections.shuffle(copia);

        String vencedor = copia.get(0);

        areaResultado.setText("");

        areaResultado.append("===== SORTEIO SIMPLES =====\n\n");

        areaResultado.append("🎉 GANHADOR:\n\n");

        areaResultado.append(vencedor);
    }

    // ==============================
    // FORMAÇÃO DE TIMES
    // ==============================

    private void formarTimes() {

        if (participantes.size() < 2) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mínimo 2 participantes."
            );

            return;
        }

        String entrada = JOptionPane.showInputDialog(
                this,
                "Número de times:"
        );

        if (entrada == null) return;

        int numTimes;

        try {

            numTimes = Integer.parseInt(entrada);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Número inválido."
            );

            return;
        }

        if (numTimes < 2) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mínimo 2 times."
            );

            return;
        }

        List<String> copia = new ArrayList<>(participantes);

        Collections.shuffle(copia);

        List<List<String>> times = new ArrayList<>();

        for (int i = 0; i < numTimes; i++) {

            times.add(new ArrayList<>());
        }

        for (int i = 0; i < copia.size(); i++) {

            times.get(i % numTimes)
                    .add(copia.get(i));
        }

        areaResultado.setText("");

        areaResultado.append("===== TIMES =====\n\n");

        for (int i = 0; i < times.size(); i++) {

            areaResultado.append(
                    "TIME "
                            + (i + 1)
                            + "\n"
            );

            for (String nome : times.get(i)) {

                areaResultado.append(
                        "• "
                                + nome
                                + "\n"
                );
            }

            areaResultado.append("\n");
        }
    }

    // ==============================
    // AMIGO SECRETO
    // ==============================

    private void amigoSecreto() {
            if (participantes.size() < 3) {

                JOptionPane.showMessageDialog(
                        this,
                        "Mínimo 3 participantes."
                );

                return;
            }

            // ==============================
            // LISTAS DE SORTEIO
            // ==============================

            List<String> doadores =
                    new ArrayList<>(participantes);

            List<String> receptores =
                    new ArrayList<>(participantes);

            Random random = new Random();

            boolean valido = false;

            int tentativas = 0;

            // ==============================
            // GERAR SORTEIO VÁLIDO
            // ==============================

            while (!valido && tentativas < 2000) {

                Collections.shuffle(receptores);

                valido = true;

                // Evitar auto-sorteio

                for (int i = 0; i < doadores.size(); i++) {

                    if (doadores.get(i)
                            .equals(receptores.get(i))) {

                        valido = false;

                        break;
                    }
                }

                // Evitar pares invertidos

                if (valido) {

                    for (int i = 0; i < doadores.size(); i++) {

                        for (int j = i + 1; j < doadores.size(); j++) {

                            boolean invertido =
                                    doadores.get(i).equals(receptores.get(j))
                                            &&
                                            doadores.get(j).equals(receptores.get(i));

                            if (invertido) {

                                valido = false;

                                break;
                            }
                        }

                        if (!valido) break;
                    }
                }

                tentativas++;
            }

            if (!valido) {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível gerar um sorteio válido."
                );

                return;
            }

            // ==============================
            // MAPA FINAL
            // ==============================

            Map<String, String> resultado =
                    new HashMap<>();

            for (int i = 0; i < doadores.size(); i++) {

                resultado.put(
                        doadores.get(i),
                        receptores.get(i)
                );
            }

            // ==============================
            // ORDEM DE REVELAÇÃO
            // ==============================

            List<String> ordem =
                    new ArrayList<>(participantes);

            Collections.shuffle(ordem);

            // ==============================
            // REVELAÇÃO INDIVIDUAL
            // ==============================

            for (int i = 0; i < ordem.size(); i++) {

                String participante = ordem.get(i);

                // PASSAR O CELULAR

                JOptionPane.showMessageDialog(
                        this,

                        "📱 PASSE O CELULAR PARA:\n\n"
                                + participante
                                + "\n\n"
                                + "Somente essa pessoa deve olhar.",

                        "Próximo Participante",

                        JOptionPane.INFORMATION_MESSAGE
                );

                // CONFIRMAR

                int confirmar = JOptionPane.showConfirmDialog(

                        this,

                        participante
                                + ", deseja ver seu amigo secreto?",

                        "Revelar",

                        JOptionPane.YES_NO_OPTION
                );

                if (confirmar != JOptionPane.YES_OPTION) {

                    i--;

                    continue;
                }

                // MOSTRAR RESULTADO

                JOptionPane.showMessageDialog(

                        this,

                        "🎁 OLÁ, "
                                + participante
                                + "!\n\n"
                                + "Seu amigo secreto é:\n\n"
                                + "👉 "
                                + resultado.get(participante)
                                + "\n\n"
                                + "🎉 Boa sorte!",

                        "Amigo Secreto",

                        JOptionPane.PLAIN_MESSAGE
                );

                // ESCONDER PARA O PRÓXIMO

                if (i < ordem.size() - 1) {

                    JOptionPane.showMessageDialog(

                            this,

                            "✅ Resultado visualizado.\n\n"
                                    + "Passe o celular para o próximo participante.",

                            "Continuar",

                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }

            // ==============================
            // FINALIZAÇÃO
            // ==============================

            JOptionPane.showMessageDialog(

                    this,

                    "🎉 Todos já visualizaram seus amigos secretos!\n\n"
                            + "Bom sorteio e boas compras!",

                    "Fim do Sorteio",

                    JOptionPane.INFORMATION_MESSAGE
            );
    }
}