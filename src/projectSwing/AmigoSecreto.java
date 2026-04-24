package projectSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * ╔══════════════════════════════════════════╗
 * ║       🎁 AMIGO SECRETO - SWING 🎁        ║
 * ║   Dinâmica: cada participante vê o seu   ║
 * ║   resultado individualmente (passando    ║
 * ║   o celular de mão em mão).              ║
 * ╚══════════════════════════════════════════╝
 *
 * Funcionalidades:
 *  - Cadastro de participantes (mínimo 3)
 *  - Sorteio sem auto-sorteio e sem pares invertidos
 *  - Revelação individual: cada um vê só o SEU amigo secreto
 *  - Interface Swing moderna com tema neutro
 *
 * @version 2.0 - Swing Edition
 */
public class AmigoSecreto {

    // ─────────────────────────────────────────────────────────
    //  PALETA DE CORES
    // ─────────────────────────────────────────────────────────
    private static final Color VERDE_ESCURO   = new Color(0x1A2744);
    private static final Color VERDE_MEDIO    = new Color(0x2C4A7C);
    private static final Color VERDE_CLARO    = new Color(0x4F80C0);
    private static final Color DOURADO        = new Color(0xE8A020);
    private static final Color DOURADO_ESCURO = new Color(0xC07010);
    private static final Color VERMELHO       = new Color(0x2563A8);
    private static final Color BRANCO         = new Color(0xFAFAFA);
    private static final Color CINZA_CARD     = new Color(0xEEF2F7);
    private static final Color SOMBRA         = new Color(0x00000030, true);

    // ─────────────────────────────────────────────────────────
    //  FONTES
    // ─────────────────────────────────────────────────────────
    private static final Font FONTE_TITULO   = new Font("Georgia", Font.BOLD, 28);
    private static final Font FONTE_SUBTITULO= new Font("Georgia", Font.ITALIC, 16);
    private static final Font FONTE_LABEL    = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FONTE_PEQUENA  = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONTE_GRANDE   = new Font("Georgia", Font.BOLD, 36);
    private static final Font FONTE_NOME     = new Font("Georgia", Font.BOLD | Font.ITALIC, 24);
    private static final Font FONTE_BTN      = new Font("SansSerif", Font.BOLD, 14);

    // ─────────────────────────────────────────────────────────
    //  ESTADO DA APLICAÇÃO
    // ─────────────────────────────────────────────────────────
    private static final List<Participante> participantes = new ArrayList<>();
    private static final List<Map.Entry<Participante, Participante>> ordemRevelacao = new ArrayList<>();
    private static int indiceAtual = 0;   // qual participante está vendo agora
    private static boolean sorteioFeito  = false;

    // ─────────────────────────────────────────────────────────
    //  COMPONENTES DA JANELA
    // ─────────────────────────────────────────────────────────
    private static JFrame janela;
    private static CardLayout cartoes;
    private static JPanel painelPrincipal;

    // IDs dos painéis no CardLayout
    private static final String TELA_CADASTRO  = "CADASTRO";
    private static final String TELA_PASSAGEM  = "PASSAGEM";
    private static final String TELA_REVELACAO = "REVELACAO";
    private static final String TELA_FIM       = "FIM";

    // Componentes dinâmicos
    private static JList<String> listaParticipantesUI;
    private static DefaultListModel<String> modeloLista;
    private static JLabel lblNomeVez;
    private static JLabel lblNomeRevelado;
    private static JLabel lblContadorRevelacao;
    private static JButton btnVerMeu;

    // ─────────────────────────────────────────────────────────
    //  CLASSE INTERNA: PARTICIPANTE
    // ─────────────────────────────────────────────────────────
    static class Participante {
        final String nome;
        Participante amigoSecreto;

        Participante(String nome) { this.nome = nome; }

        @Override public String toString() { return nome; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Participante)) return false;
            return nome.equalsIgnoreCase(((Participante) o).nome);
        }
        @Override public int hashCode() { return nome.toLowerCase().hashCode(); }
    }

    // ─────────────────────────────────────────────────────────
    //  PONTO DE ENTRADA
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
            construirJanela();
        });
    }

    // ─────────────────────────────────────────────────────────
    //  CONSTRUÇÃO DA JANELA PRINCIPAL
    // ─────────────────────────────────────────────────────────
    private static void construirJanela() {
        janela = new JFrame("🎁 Amigo Secreto");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(new Dimension(480, 600));
        janela.setResizable(true);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);

        cartoes = new CardLayout();
        painelPrincipal = new JPanel(cartoes);
        painelPrincipal.setBackground(VERDE_ESCURO);

        painelPrincipal.add(construirTelaCadastro(),  TELA_CADASTRO);
        painelPrincipal.add(construirTelaPassagem(),  TELA_PASSAGEM);
        painelPrincipal.add(construirTelaRevelacao(), TELA_REVELACAO);
        painelPrincipal.add(construirTelaFim(),       TELA_FIM);

        janela.setContentPane(painelPrincipal);
        janela.setVisible(true);

        cartoes.show(painelPrincipal, TELA_CADASTRO);
    }

    // ═════════════════════════════════════════════════════════
    //  TELA 1 — CADASTRO DE PARTICIPANTES
    // ═════════════════════════════════════════════════════════
    private static JPanel construirTelaCadastro() {
        JPanel raiz = criarPainelFundo();
        raiz.setLayout(new BorderLayout(0, 0));

        // ── CABEÇALHO ──────────────────────────────────────
        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        JLabel emoji = criarLabel("🎊", new Font("SansSerif", Font.PLAIN, 48), DOURADO);
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = criarLabel("AMIGO SECRETO", FONTE_TITULO, DOURADO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = criarLabel("Cadastre os participantes", FONTE_SUBTITULO, BRANCO);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        cabecalho.add(emoji);
        cabecalho.add(Box.createVerticalStrut(8));
        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(4));
        cabecalho.add(sub);

        // ── FORMULÁRIO ─────────────────────────────────────
        JPanel formulario = new JPanel();
        formulario.setOpaque(false);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        // Campo de nome
        JLabel lblNome = criarLabel("Nome do participante:", FONTE_LABEL, DOURADO);
        lblNome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField campoNome = new JTextField();
        campoNome.setFont(FONTE_LABEL);
        campoNome.setBackground(BRANCO);
        campoNome.setForeground(VERDE_ESCURO);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DOURADO_ESCURO, 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campoNome.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botão adicionar
        JButton btnAdicionar = criarBotao("➕  Adicionar", DOURADO, VERDE_ESCURO);
        btnAdicionar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAdicionar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        // Lista de participantes
        JLabel lblLista = criarLabel("Participantes adicionados:", FONTE_LABEL, DOURADO);
        lblLista.setAlignmentX(Component.LEFT_ALIGNMENT);

        modeloLista = new DefaultListModel<>();
        listaParticipantesUI = new JList<>(modeloLista);
        listaParticipantesUI.setFont(FONTE_LABEL);
        listaParticipantesUI.setBackground(new Color(0x163523));
        listaParticipantesUI.setForeground(BRANCO);
        listaParticipantesUI.setSelectionBackground(VERDE_CLARO);
        listaParticipantesUI.setSelectionForeground(VERDE_ESCURO);
        listaParticipantesUI.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        listaParticipantesUI.setFixedCellHeight(32);

        JScrollPane scroll = new JScrollPane(listaParticipantesUI);
        scroll.setPreferredSize(new Dimension(0, 140));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        scroll.setBorder(BorderFactory.createLineBorder(VERDE_CLARO, 1));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botão remover selecionado
        JButton btnRemover = criarBotao("🗑  Remover selecionado", VERMELHO, BRANCO);
        btnRemover.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRemover.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnRemover.setFont(new Font("SansSerif", Font.BOLD, 12));

        formulario.add(lblNome);
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(campoNome);
        formulario.add(Box.createVerticalStrut(10));
        formulario.add(btnAdicionar);
        formulario.add(Box.createVerticalStrut(18));
        formulario.add(lblLista);
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(scroll);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnRemover);

        // ── RODAPÉ COM BOTÃO SORTEAR ────────────────────────
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setBorder(BorderFactory.createEmptyBorder(16, 30, 30, 30));

        JButton btnSortear = criarBotao("🎲  REALIZAR SORTEIO", VERMELHO, BRANCO);
        btnSortear.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSortear.setPreferredSize(new Dimension(0, 52));

        rodape.add(btnSortear, BorderLayout.CENTER);

        raiz.add(cabecalho, BorderLayout.NORTH);
        raiz.add(formulario, BorderLayout.CENTER);
        raiz.add(rodape, BorderLayout.SOUTH);

        // ── AÇÕES ───────────────────────────────────────────
        ActionListener acaoAdicionar = e -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty()) {
                mostrarAviso("Nome não pode estar vazio!", raiz);
                return;
            }
            if (nome.length() < 2) {
                mostrarAviso("Nome muito curto!", raiz);
                return;
            }
            for (Participante p : participantes) {
                if (p.nome.equalsIgnoreCase(nome)) {
                    mostrarAviso("\"" + nome + "\" já foi adicionado!", raiz);
                    return;
                }
            }
            participantes.add(new Participante(nome));
            modeloLista.addElement("  👤  " + nome);
            campoNome.setText("");
            campoNome.requestFocus();
            sorteioFeito = false;
        };

        btnAdicionar.addActionListener(acaoAdicionar);
        campoNome.addActionListener(acaoAdicionar); // Enter também adiciona

        btnRemover.addActionListener(e -> {
            int idx = listaParticipantesUI.getSelectedIndex();
            if (idx < 0) {
                mostrarAviso("Selecione um participante para remover.", raiz);
                return;
            }
            String nome = participantes.get(idx).nome;
            participantes.remove(idx);
            modeloLista.remove(idx);
            sorteioFeito = false;
        });

        btnSortear.addActionListener(e -> {
            if (participantes.size() < 3) {
                mostrarAviso("É necessário no mínimo 3 participantes!\nAtual: " + participantes.size(), raiz);
                return;
            }
            if (realizarSorteio()) {
                indiceAtual = 0;
                atualizarTelaPassagem();
                cartoes.show(painelPrincipal, TELA_PASSAGEM);
            } else {
                mostrarAviso("Não foi possível sortear. Tente novamente.", raiz);
            }
        });

        return raiz;
    }

    // ═════════════════════════════════════════════════════════
    //  TELA 2 — "PASSE PARA..." (antes de revelar)
    // ═════════════════════════════════════════════════════════
    private static JPanel construirTelaPassagem() {
        JPanel raiz = criarPainelFundo();
        raiz.setLayout(new GridBagLayout());

        JPanel card = criarCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel emojiCelular = criarLabel("👥", new Font("SansSerif", Font.PLAIN, 64), VERDE_ESCURO);
        emojiCelular.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instrucao = criarLabel("PASSE PARA O PARTICIPANTE:", new Font("SansSerif", Font.BOLD, 13), VERDE_MEDIO);
        instrucao.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNomeVez = criarLabel("—", FONTE_GRANDE, VERDE_ESCURO);
        lblNomeVez.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dica = criarLabel("(somente essa pessoa deve olhar a tela)", FONTE_PEQUENA, new Color(0x777777));
        dica.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEstouPronto = criarBotao("🔍  Estou pronto — revelar!", VERDE_ESCURO, BRANCO);
        btnEstouPronto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEstouPronto.setMaximumSize(new Dimension(320, 50));
        btnEstouPronto.setFont(new Font("SansSerif", Font.BOLD, 15));

        card.add(emojiCelular);
        card.add(Box.createVerticalStrut(20));
        card.add(instrucao);
        card.add(Box.createVerticalStrut(8));
        card.add(lblNomeVez);
        card.add(Box.createVerticalStrut(16));
        card.add(dica);
        card.add(Box.createVerticalStrut(30));
        card.add(btnEstouPronto);

        raiz.add(card);

        btnEstouPronto.addActionListener(e -> {
            atualizarTelaRevelacao();
            cartoes.show(painelPrincipal, TELA_REVELACAO);
        });

        return raiz;
    }

    // ═════════════════════════════════════════════════════════
    //  TELA 3 — REVELAÇÃO (o amigo secreto)
    // ═════════════════════════════════════════════════════════
    private static JPanel construirTelaRevelacao() {
        JPanel raiz = criarPainelFundo();
        raiz.setLayout(new BorderLayout());

        // Cabeçalho
        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));

        JLabel emojiPresente = criarLabel("🎊", new Font("SansSerif", Font.PLAIN, 52), DOURADO);
        emojiPresente.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = criarLabel("SEU AMIGO SECRETO É...", new Font("SansSerif", Font.BOLD, 16), DOURADO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        topo.add(emojiPresente);
        topo.add(Box.createVerticalStrut(10));
        topo.add(lblTitulo);

        // Card central com o nome revelado
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        JPanel card = criarCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DOURADO, 3, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel estrelas = criarLabel("✦  ✦  ✦", new Font("SansSerif", Font.PLAIN, 16), DOURADO_ESCURO);
        estrelas.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNomeRevelado = criarLabel("—", FONTE_NOME, VERDE_ESCURO);
        lblNomeRevelado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel estrelasBaixo = criarLabel("✦  ✦  ✦", new Font("SansSerif", Font.PLAIN, 16), DOURADO_ESCURO);
        estrelasBaixo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mensagem = criarLabel("Presenteie com muito carinho! 💝", FONTE_PEQUENA, new Color(0x555555));
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(estrelas);
        card.add(Box.createVerticalStrut(14));
        card.add(lblNomeRevelado);
        card.add(Box.createVerticalStrut(14));
        card.add(estrelasBaixo);
        card.add(Box.createVerticalStrut(16));
        card.add(mensagem);

        centro.add(card);

        // Rodapé
        JPanel rodape = new JPanel();
        rodape.setOpaque(false);
        rodape.setLayout(new BoxLayout(rodape, BoxLayout.Y_AXIS));
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        lblContadorRevelacao = criarLabel("", FONTE_PEQUENA, BRANCO);
        lblContadorRevelacao.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnVerMeu = criarBotao("➡  Próximo participante", DOURADO, VERDE_ESCURO);
        btnVerMeu.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerMeu.setMaximumSize(new Dimension(320, 50));
        btnVerMeu.setFont(new Font("SansSerif", Font.BOLD, 15));

        rodape.add(lblContadorRevelacao);
        rodape.add(Box.createVerticalStrut(12));
        rodape.add(btnVerMeu);

        raiz.add(topo, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(rodape, BorderLayout.SOUTH);

        btnVerMeu.addActionListener(e -> {
            indiceAtual++;
            if (indiceAtual >= ordemRevelacao.size()) {
                cartoes.show(painelPrincipal, TELA_FIM);
            } else {
                atualizarTelaPassagem();
                cartoes.show(painelPrincipal, TELA_PASSAGEM);
            }
        });

        return raiz;
    }

    // ═════════════════════════════════════════════════════════
    //  TELA 4 — FIM DO SORTEIO
    // ═════════════════════════════════════════════════════════
    private static JPanel construirTelaFim() {
        JPanel raiz = criarPainelFundo();
        raiz.setLayout(new GridBagLayout());

        JPanel card = criarCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel emojiNoel = criarLabel("🎉", new Font("SansSerif", Font.PLAIN, 72), VERDE_ESCURO);
        emojiNoel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = criarLabel("SORTEIO CONCLUÍDO!", new Font("SansSerif", Font.BOLD, 20), VERDE_ESCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = criarLabel("Todos já sabem seu amigo secreto!", FONTE_SUBTITULO, VERDE_MEDIO);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel divider = criarLabel("────────────────", FONTE_PEQUENA, new Color(0xBBBBBB));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel aviso = criarLabel("Bom sorteio e boas compras! ✨", FONTE_LABEL, VERDE_ESCURO);
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botão para novo sorteio
        JButton btnNovoSorteio = criarBotao("🔄  Novo Sorteio", VERDE_ESCURO, BRANCO);
        btnNovoSorteio.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNovoSorteio.setMaximumSize(new Dimension(280, 48));
        btnNovoSorteio.setFont(new Font("SansSerif", Font.BOLD, 14));

        card.add(emojiNoel);
        card.add(Box.createVerticalStrut(16));
        card.add(titulo);
        card.add(Box.createVerticalStrut(8));
        card.add(sub);
        card.add(Box.createVerticalStrut(24));
        card.add(divider);
        card.add(Box.createVerticalStrut(24));
        card.add(aviso);
        card.add(Box.createVerticalStrut(32));
        card.add(btnNovoSorteio);

        raiz.add(card);

        btnNovoSorteio.addActionListener(e -> {
            // Resetar estado
            participantes.clear();
            ordemRevelacao.clear();
            modeloLista.clear();
            sorteioFeito = false;
            indiceAtual = 0;
            cartoes.show(painelPrincipal, TELA_CADASTRO);
        });

        return raiz;
    }

    // ─────────────────────────────────────────────────────────
    //  LÓGICA DE SORTEIO
    // ─────────────────────────────────────────────────────────
    private static boolean realizarSorteio() {
        List<Participante> doadores  = new ArrayList<>(participantes);
        List<Participante> receptores = new ArrayList<>(participantes);

        Random rng = new Random();
        boolean valido = false;
        int tentativas = 0;

        while (!valido && tentativas < 2000) {
            Collections.shuffle(receptores, rng);
            valido = true;

            // Verifica auto-sorteio
            for (int i = 0; i < doadores.size(); i++) {
                if (doadores.get(i).equals(receptores.get(i))) {
                    valido = false;
                    break;
                }
            }

            // Verifica pares invertidos (A→B e B→A)
            if (valido) {
                for (int i = 0; i < doadores.size(); i++) {
                    for (int j = i + 1; j < doadores.size(); j++) {
                        if (doadores.get(i).equals(receptores.get(j)) &&
                                doadores.get(j).equals(receptores.get(i))) {
                            valido = false;
                            break;
                        }
                    }
                    if (!valido) break;
                }
            }

            tentativas++;
        }

        if (!valido) return false;

        // Atribui amigos secretos
        for (int i = 0; i < doadores.size(); i++) {
            doadores.get(i).amigoSecreto = receptores.get(i);
        }

        // Monta ordem de revelação (embaralhada para não revelar por ordem de cadastro)
        ordemRevelacao.clear();
        List<Participante> ordemEmbaralhada = new ArrayList<>(participantes);
        Collections.shuffle(ordemEmbaralhada, rng);
        for (Participante p : ordemEmbaralhada) {
            ordemRevelacao.add(Map.entry(p, p.amigoSecreto));
        }

        sorteioFeito = true;
        return true;
    }

    // ─────────────────────────────────────────────────────────
    //  ATUALIZAÇÃO DINÂMICA DAS TELAS
    // ─────────────────────────────────────────────────────────
    private static void atualizarTelaPassagem() {
        if (indiceAtual < ordemRevelacao.size()) {
            String nome = ordemRevelacao.get(indiceAtual).getKey().nome;
            lblNomeVez.setText(nome);
        }
    }

    private static void atualizarTelaRevelacao() {
        if (indiceAtual < ordemRevelacao.size()) {
            String amigo = ordemRevelacao.get(indiceAtual).getValue().nome;
            lblNomeRevelado.setText(amigo);
            lblContadorRevelacao.setText(
                    "Participante " + (indiceAtual + 1) + " de " + ordemRevelacao.size()
            );

            boolean isUltimo = (indiceAtual == ordemRevelacao.size() - 1);
            btnVerMeu.setText(isUltimo ? "✅  Concluir sorteio" : "➡  Próximo participante");
        }
    }

    // ─────────────────────────────────────────────────────────
    //  UTILITÁRIOS DE UI
    // ─────────────────────────────────────────────────────────
    private static JPanel criarPainelFundo() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradiente de fundo
                GradientPaint grad = new GradientPaint(
                        0, 0, VERDE_ESCURO,
                        0, getHeight(), new Color(0x0D2B1E)
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Estrelinhas decorativas
                g2.setColor(new Color(0xFFFFFF20, true));
                for (int i = 0; i < 30; i++) {
                    int x = (i * 137 + 23) % getWidth();
                    int y = (i * 89 + 41) % getHeight();
                    int r = (i % 3) + 1;
                    g2.fillOval(x, y, r * 2, r * 2);
                }
                g2.dispose();
            }
        };
    }

    private static JPanel criarCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(SOMBRA);
                g2.fillRoundRect(6, 6, getWidth() - 6, getHeight() - 6, 24, 24);
                // Card
                g2.setColor(CINZA_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 24, 24);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        return card;
    }

    private static JLabel criarLabel(String texto, Font fonte, Color cor) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setFont(fonte);
        l.setForeground(cor);
        l.setOpaque(false);
        return l;
    }

    private static JButton criarBotao(String texto, Color fundo, Color textoColor) {
        JButton btn = new JButton(texto) {
            private boolean hover = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            }); }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hover ? fundo.brighter() : fundo;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(textoColor);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
            @Override public boolean isFocusPainted() { return false; }
            @Override public boolean isBorderPainted() { return false; }
            @Override public boolean isContentAreaFilled() { return false; }
        };
        btn.setFont(FONTE_BTN);
        btn.setForeground(textoColor);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 42));
        return btn;
    }

    private static void mostrarAviso(String mensagem, Component pai) {
        JOptionPane.showMessageDialog(
                pai, mensagem, "Atenção",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
