/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

/**
 *
 * @author Agustin
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
 
public class Lugares extends JFrame {
 
    private final Color DORADO       = new Color(212, 163, 63);
    private final Color DORADO_CLARO = new Color(230, 185, 85);
    private final Color NEGRO        = new Color(10, 10, 10);
    private final Color CARD_BG      = new Color(18, 15, 8);
    private final Color GRIS         = new Color(160, 160, 160);
    private final Color GRIS_CLARO   = new Color(200, 200, 200);
    private final Color VERDE        = new Color(60, 180, 80);
    private final Color ROJO         = new Color(180, 60, 60);
 
    // { nombre, zona, descripcion, imgPath, pts }
   private final String[][] lugaresGenerales = {
    {"Auditorio Mayor",      "Bloque principal",        "Sede de eventos académicos, conciertos y ceremonias de grado.",             "/img/auditorio_mayor.jpg",  "+100 pts"},
    {"Auditorio Menor",      "Bloque principal",        "Espacio para conferencias, talleres y presentaciones estudiantiles.",        "/img/auditorio_menor.jpg",  "+80 pts"},
    {"BANÚ",                 "Sector BANÚ",             "Zona de integración estudiantil, eventos y servicios universitarios.",       "/img/banu.jpg",             "+60 pts"},
    {"Biblioteca Central",   "Centro del campus",       "Recursos bibliográficos, salas de estudio y acceso a bases de datos.",       "/img/biblioteca.jpg",       "+200 pts"},
    {"Sector Bosque",        "Zona verde",              "Área natural rodeada de árboles, ideal para estudiar al aire libre.",        "/img/bosque.jpg",           "+70 pts"},
    {"Cafetería Bosque",     "Zona verde",              "Cafetería rodeada de naturaleza, ambiente tranquilo para descansar.",        "/img/cafeteria bosque.jpg", "+50 pts"},
    {"Cafetería Edificio L", "Bloque L",                "Cafetería principal con variedad de menús y zonas de descanso.",             "/img/cafeteria L.jpg",      "+40 pts"},
    {"La Casona",            "Sector histórico",        "Edificio patrimonial con arquitectura colonial, símbolo de la UNAB.",        "/img/casona.jpg",           "+90 pts"},
    {"Casona Café",          "Sector histórico",        "Café dentro de la Casona, espacio cultural y de encuentro.",                 "/img/casona_cafe.jpg",      "+55 pts"},
    {"CSU",                  "Bloque central",          "Centro de Servicios Universitarios: trámites, carnetización y más.",         "/img/csu.jpg",              "+60 pts"},
    {"Enfermería",           "Bienestar universitario", "Servicio de salud, primeros auxilios y atención médica estudiantil.",        "/img/enfermeria.jpg",       "+80 pts"},
    {"Jardín Botánico",      "Zona verde",              "Espacio de biodiversidad, relajación y clases al aire libre.",               "/img/jardin.jpg",           "+120 pts"},
    {"Plazoleta Central",    "Zona social",             "Punto de encuentro principal, eventos y actividades culturales.",            "/img/plazoleta.jpg",        "+80 pts"},
    {"Puente UNAB",          "Conexión campus",         "Pasarela icónica que conecta los bloques del campus universitario.",         "/img/puente.jpg",           "+50 pts"},
};
 
    // { lugar, descripcion, puntos_ejemplo, imgPath }
    private final String[][] lugaresApunab = {
    {"Cafetería Edificio L", "Escanea tu carnet al pagar y acumula APUNAB diariamente.",             "+15 pts/visita",  "/img/cafeteria L.jpg"},
    {"Cafetería Bosque",     "Check-in digital disponible en las mesas del fondo.",                  "+15 pts/visita",  "/img/cafeteria bosque.jpg"},
    {"CSU",                  "Realiza trámites oficiales y gana puntos por cada gestión completada.", "+30 pts/trámite", "/img/csu.jpg"},
    {"Biblioteca Central",   "Ingresa con tu carnet y acumula por horas de estudio registradas.",    "+20 pts/sesión",  "/img/biblioteca.jpg"},
    {"Casona Café",          "Registra tu compra con el código QR en caja.",                         "+15 pts/visita",  "/img/casona_cafe.jpg"},
    {"BANÚ",                 "App APUNAB disponible en este punto para escanear y ganar.",            "+20 pts/visita",  "/img/banu.jpg"},
    {"Plazoleta Central",    "Eventos en la plazoleta otorgan puntos automáticos al registrarte.",   "+25 pts/evento",  "/img/plazoleta.jpg"},
    {"Jardín Botánico",      "Visita registrada con QR en la entrada del jardín.",                   "+20 pts/visita",  "/img/jardin.jpg"},
};
 
    private String filtroActual = "Todos";
    private String usuario;
    private JPanel panelContenido;
 
    public Lugares(String usuario) {
        this.usuario = usuario;
        DatosUsuario.setUsuario(usuario);
        setTitle("APUNAB - Lugares del Campus");
        setSize(1100, 780);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(NEGRO);
        add(buildNavbar(), BorderLayout.NORTH);
        add(buildBody(),   BorderLayout.CENTER);
        setVisible(true);
    }
 
    // ══════════════════════════════════════════════
    // HELPER IMAGEN
    // ══════════════════════════════════════════════
    private JLabel loadImg(String path, int w, int h) {
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        try {
            java.net.URL url = getClass().getResource(path);
            if (url != null) {
                Image img = new ImageIcon(url).getImage();
                MediaTracker mt = new MediaTracker(lbl);
                mt.addImage(img, 0);
                mt.waitForAll();
                lbl.setIcon(new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
            }
        } catch (Exception ignored) {}
        return lbl;
    }
 
    // ── Crea un JLabel con imagen recortada en forma de rectángulo redondeado ──
    private JLabel loadImgRounded(String path, int w, int h) {
        JLabel lbl = new JLabel() {
            private Image imgCache;
            {
                try {
                    java.net.URL url = getClass().getResource(path);
                    if (url != null) {
                        Image raw = new ImageIcon(url).getImage();
                        MediaTracker mt = new MediaTracker(this);
                        mt.addImage(raw, 0);
                        mt.waitForAll();
                        imgCache = raw.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    }
                } catch (Exception ignored) {}
            }
            @Override protected void paintComponent(Graphics g) {
                if (imgCache == null) {
                    g.setColor(new Color(35, 28, 10));
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.drawImage(imgCache, 0, 0, this);
                g2.dispose();
            }
        };
        lbl.setPreferredSize(new Dimension(w, h));
        lbl.setOpaque(false);
        return lbl;
    }
 
    // ══════════════════════════════════════════════
    // NAVBAR
    // ══════════════════════════════════════════════
    private JPanel buildNavbar() {
        JPanel nav = new JPanel(null);
        nav.setBackground(new Color(15, 12, 8));
        nav.setPreferredSize(new Dimension(1100, 55));
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 45, 20)));
 
        JLabel logoTxt = new JLabel("unab");
        logoTxt.setForeground(DORADO);
        logoTxt.setFont(new Font("Dialog", Font.BOLD, 22));
        logoTxt.setBounds(20, 14, 80, 28);
        nav.add(logoTxt);
 
        JLabel titulo = new JLabel("📍  Lugares del Campus");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Dialog", Font.BOLD, 16));
        titulo.setBounds(330, 16, 300, 24);
        nav.add(titulo);
 
        JLabel lblPuntos = new JLabel("🪙 " + DatosUsuario.get().getPuntosTotal() + " pts");
        lblPuntos.setForeground(DORADO);
        lblPuntos.setFont(new Font("Dialog", Font.BOLD, 13));
        lblPuntos.setBounds(740, 16, 170, 24);
        nav.add(lblPuntos);
 
        JButton btnMenu = new JButton("⊞  Menú Principal");
        btnMenu.setBounds(920, 10, 160, 36);
        btnMenu.setBackground(new Color(35, 28, 15));
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setFont(new Font("Dialog", Font.BOLD, 12));
        btnMenu.setFocusPainted(false);
        btnMenu.setBorder(BorderFactory.createLineBorder(DORADO, 1));
        btnMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMenu.addActionListener(e -> dispose());
        btnMenu.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnMenu.setBackground(new Color(55, 42, 18)); }
            @Override public void mouseExited(MouseEvent e)  { btnMenu.setBackground(new Color(35, 28, 15)); }
        });
        nav.add(btnMenu);
        return nav;
    }
 
    // ══════════════════════════════════════════════
    // BODY
    // ══════════════════════════════════════════════
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(NEGRO);
        body.add(buildFiltros(), BorderLayout.NORTH);
        panelContenido = buildContenido();
        JScrollPane scroll = new JScrollPane(panelContenido);
        scroll.setBackground(NEGRO);
        scroll.getViewport().setBackground(NEGRO);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        body.add(scroll, BorderLayout.CENTER);
        return body;
    }
 
    // ══════════════════════════════════════════════
    // FILTROS
    // ══════════════════════════════════════════════
    private JPanel buildFiltros() {
        JPanel filtros = new JPanel(null);
        filtros.setBackground(new Color(12, 10, 5));
        filtros.setPreferredSize(new Dimension(1100, 50));
        filtros.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(55, 44, 20)));
 
        String[] cats = {"Todos", "Mis Lugares", "Dónde obtener APUNAB", "Registrarme", "Darme de baja"};
        int x = 16;
        for (String cat : cats) {
            JButton btn = makeFiltroBtn(cat);
            int w = btn.getPreferredSize().width + 24;
            btn.setBounds(x, 10, w, 30);
            final String cf = cat;
            btn.addActionListener(e -> { filtroActual = cf; refreshContenido(); });
            filtros.add(btn);
            x += w + 10;
        }
        return filtros;
    }
 
    private JButton makeFiltroBtn(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Dialog", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBackground(new Color(30, 24, 10));
        btn.setForeground(GRIS_CLARO);
        btn.setBorder(BorderFactory.createLineBorder(new Color(55, 44, 20), 1));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(50, 40, 15)); btn.setForeground(DORADO); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(30, 24, 10)); btn.setForeground(GRIS_CLARO); }
        });
        return btn;
    }
 
    private void refreshContenido() {
        JPanel body = (JPanel)((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        JScrollPane scroll = (JScrollPane)((BorderLayout) body.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panelContenido = buildContenido();
        scroll.setViewportView(panelContenido);
        scroll.revalidate();
        scroll.repaint();
    }
 
    // ══════════════════════════════════════════════
    // CONTENIDO
    // ══════════════════════════════════════════════
    private JPanel buildContenido() {
        JPanel panel = new JPanel(null);
        panel.setBackground(NEGRO);
        int y = 20;
 
        // ── TODOS ────────────────────────────────────────────────
        if (filtroActual.equals("Todos")) {
            y = addSeccionTitulo(panel, "🏛  Lugares del Campus UNAB", y);
            y = addBannerInfo(panel, "Explora y registra tu visita a cada lugar del campus para acumular APUNAB.", "🗺", y);
            for (int i = 0; i < lugaresGenerales.length; i++) {
                JPanel card = buildCardGeneral(i);
                card.setBounds(20, y, 1040, 88);
                panel.add(card);
                y += 96;
            }
            y += 10;
        }
 
        // ── MIS LUGARES ──────────────────────────────────────────
        if (filtroActual.equals("Mis Lugares")) {
            y = addSeccionTitulo(panel, "📌  Mis Lugares Registrados", y);
            List<String[]> datos = DatosUsuario.get().getLugares();
            boolean hay = datos.stream().anyMatch(l -> l[0].equals("done"));
            if (!hay) {
                y = addMensajeVacio(panel, "Aún no has registrado ningún lugar.",
                    "Ve a 'Registrarme' para marcar tus visitas y ganar APUNAB.", y);
            } else {
                for (int i = 0; i < datos.size(); i++) {
                    if (!datos.get(i)[0].equals("done")) continue;
                    JPanel card = buildCardMiLugar(i, datos.get(i));
                    card.setBounds(20, y, 1040, 88);
                    panel.add(card);
                    y += 96;
                }
            }
            y += 10;
        }
 
        // ── DÓNDE OBTENER APUNAB ─────────────────────────────────
        if (filtroActual.equals("Dónde obtener APUNAB")) {
            y = addSeccionTitulo(panel, "🪙  Dónde obtener APUNAB", y);
            y = addBannerInfo(panel,
                "En estos lugares puedes escanear tu carnet o hacer check-in para acumular puntos APUNAB automáticamente.",
                "💡", y);
            for (int i = 0; i < lugaresApunab.length; i++) {
                JPanel card = buildCardDondeApunab(i);
                card.setBounds(20, y, 1040, 88);
                panel.add(card);
                y += 96;
            }
            y += 10;
        }
 
        // ── REGISTRARME ──────────────────────────────────────────
        if (filtroActual.equals("Registrarme")) {
            y = addSeccionTitulo(panel, "✅  Registrarme en un Lugar", y);
            y = addBannerInfo(panel,
                "Selecciona un lugar para marcar tu visita y ganar los puntos APUNAB correspondientes.",
                "📍", y);
            List<String[]> datos = DatosUsuario.get().getLugares();
            boolean hayPend = false;
 
            // Lugares del sistema no visitados
            for (int i = 0; i < datos.size(); i++) {
                if (datos.get(i)[0].equals("done")) continue;
                hayPend = true;
                JPanel card = buildCardRegistrarme(i, datos.get(i));
                card.setBounds(20, y, 1040, 88);
                panel.add(card);
                y += 96;
            }
            // Lugares del catálogo que no están en DatosUsuario
            for (int i = datos.size(); i < lugaresGenerales.length; i++) {
                hayPend = true;
                JPanel card = buildCardRegistrarmeNuevo(i);
                card.setBounds(20, y, 1040, 88);
                panel.add(card);
                y += 96;
            }
            if (!hayPend) {
                y = addMensajeVacio(panel, "¡Has visitado todos los lugares del campus! 🎉",
                    "Eres todo un explorador UNAB.", y);
            }
            y += 10;
        }
 
        // ── DARME DE BAJA ────────────────────────────────────────
        if (filtroActual.equals("Darme de baja")) {
            y = addSeccionTitulo(panel, "🚫  Darme de baja de un Lugar", y);
            y = addBannerInfo(panel,
                "Si marcaste un lugar por error, puedes darte de baja. Los puntos asignados serán revertidos.",
                "⚠️", y);
            List<String[]> datos = DatosUsuario.get().getLugares();
            boolean hay = false;
            for (int i = 0; i < datos.size(); i++) {
                if (!datos.get(i)[0].equals("done")) continue;
                hay = true;
                JPanel card = buildCardDarseDeBaja(i, datos.get(i));
                card.setBounds(20, y, 1040, 88);
                panel.add(card);
                y += 96;
            }
            if (!hay) {
                y = addMensajeVacio(panel, "No tienes lugares registrados para darte de baja.",
                    "Visita la sección 'Registrarme' para marcar tus lugares.", y);
            }
            y += 10;
        }
 
        y += 20;
        panel.setPreferredSize(new Dimension(1080, y));
        return panel;
    }
 
    // ══════════════════════════════════════════════
    // HELPERS SECCIÓN
    // ══════════════════════════════════════════════
    private int addSeccionTitulo(JPanel panel, String texto, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(DORADO);
        lbl.setFont(new Font("Dialog", Font.BOLD, 18));
        lbl.setBounds(20, y, 700, 28);
        panel.add(lbl);
        y += 34;
        JPanel linea = new JPanel();
        linea.setBackground(DORADO);
        linea.setBounds(20, y, 100, 2);
        panel.add(linea);
        return y + 12;
    }
 
    private int addBannerInfo(JPanel panel, String texto, String emoji, int y) {
        JPanel banner = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(22, 18, 8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        banner.setBorder(BorderFactory.createLineBorder(new Color(60, 48, 22), 1));
        banner.setBounds(20, y, 1040, 52);
        JLabel ico = new JLabel(emoji);
        ico.setFont(new Font("Dialog", Font.PLAIN, 26));
        ico.setBounds(14, 12, 32, 32);
        banner.add(ico);
        JLabel lbl = new JLabel("<html>" + texto + "</html>");
        lbl.setForeground(GRIS_CLARO);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 13));
        lbl.setBounds(54, 10, 970, 32);
        banner.add(lbl);
        panel.add(banner);
        return y + 62;
    }
 
    private int addMensajeVacio(JPanel panel, String l1, String l2, int y) {
        JPanel v = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(16, 13, 6));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        v.setBorder(BorderFactory.createLineBorder(new Color(45, 36, 16), 1));
        v.setBounds(20, y, 1040, 70);
        JLabel la = new JLabel(l1);
        la.setForeground(GRIS_CLARO); la.setFont(new Font("Dialog", Font.BOLD, 14));
        la.setHorizontalAlignment(SwingConstants.CENTER); la.setBounds(0, 10, 1040, 22); v.add(la);
        JLabel lb = new JLabel(l2);
        lb.setForeground(GRIS); lb.setFont(new Font("Dialog", Font.PLAIN, 12));
        lb.setHorizontalAlignment(SwingConstants.CENTER); lb.setBounds(0, 36, 1040, 18); v.add(lb);
        panel.add(v);
        return y + 82;
    }
 
    // ══════════════════════════════════════════════
    // CARD BASE — construye el panel con imagen
    // ══════════════════════════════════════════════
    private JPanel makeCardBase(String imgPath, boolean visitado, boolean rojo) {
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = rojo ? new Color(20, 10, 10) : visitado ? new Color(10, 20, 10) : CARD_BG;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        Color borde = rojo ? new Color(80, 30, 30) : visitado ? new Color(40, 80, 40) : new Color(55, 44, 20);
        card.setBorder(BorderFactory.createLineBorder(borde, 1));
 
        // Imagen redondeada a la izquierda
        JLabel imgLbl = loadImgRounded(imgPath, 110, 70);
        imgLbl.setBounds(10, 9, 110, 70);
        card.add(imgLbl);
 
        return card;
    }
 
    // ══════════════════════════════════════════════
    // CARD — TODOS LOS LUGARES
    // ══════════════════════════════════════════════
    private JPanel buildCardGeneral(int idx) {
        List<String[]> datos = DatosUsuario.get().getLugares();
        boolean visitado = datos.stream()
            .anyMatch(l -> l[1].equalsIgnoreCase(lugaresGenerales[idx][0]) && l[0].equals("done"));
 
        JPanel card = makeCardBase(lugaresGenerales[idx][3], visitado, false);
 
        JLabel lNombre = new JLabel(lugaresGenerales[idx][0]);
        lNombre.setForeground(Color.WHITE);
        lNombre.setFont(new Font("Dialog", Font.BOLD, 14));
        lNombre.setBounds(130, 8, 400, 20);
        card.add(lNombre);
 
        JLabel lZona = new JLabel("📍 " + lugaresGenerales[idx][1]);
        lZona.setForeground(GRIS);
        lZona.setFont(new Font("Dialog", Font.PLAIN, 11));
        lZona.setBounds(130, 30, 320, 16);
        card.add(lZona);
 
        JLabel lDesc = new JLabel("<html>" + lugaresGenerales[idx][2] + "</html>");
        lDesc.setForeground(GRIS);
        lDesc.setFont(new Font("Dialog", Font.PLAIN, 11));
        lDesc.setBounds(130, 48, 500, 28);
        card.add(lDesc);
 
        JLabel lPts = new JLabel(lugaresGenerales[idx][4]);
        lPts.setForeground(VERDE);
        lPts.setFont(new Font("Dialog", Font.BOLD, 12));
        lPts.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VERDE, 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        lPts.setBounds(640, 30, 120, 22);
        card.add(lPts);
 
        if (visitado) {
            JLabel lV = new JLabel("✅  Visitado");
            lV.setForeground(VERDE);
            lV.setFont(new Font("Dialog", Font.BOLD, 12));
            lV.setBounds(820, 32, 120, 20);
            card.add(lV);
        } else {
            JLabel lP = new JLabel("⭕  Sin visitar");
            lP.setForeground(GRIS);
            lP.setFont(new Font("Dialog", Font.PLAIN, 12));
            lP.setBounds(820, 32, 130, 20);
            card.add(lP);
        }
        return card;
    }
 
    // ══════════════════════════════════════════════
    // CARD — MIS LUGARES
    // ══════════════════════════════════════════════
    private JPanel buildCardMiLugar(int idx, String[] lugar) {
        // Buscar imagen del catálogo por nombre
        String imgPath = "/img/csu.jpg";
        for (String[] lg : lugaresGenerales)
            if (lg[0].equalsIgnoreCase(lugar[1])) { imgPath = lg[3]; break; }
 
        JPanel card = makeCardBase(imgPath, true, false);
 
        JLabel lNombre = new JLabel(lugar[1]);
        lNombre.setForeground(Color.WHITE);
        lNombre.setFont(new Font("Dialog", Font.BOLD, 14));
        lNombre.setBounds(130, 8, 420, 20);
        card.add(lNombre);
 
        JLabel lFecha = new JLabel("🗓  " + lugar[2]);
        lFecha.setForeground(GRIS);
        lFecha.setFont(new Font("Dialog", Font.PLAIN, 11));
        lFecha.setBounds(130, 30, 350, 16);
        card.add(lFecha);
 
        JLabel lZona = new JLabel("📍 " + lugar[4]);
        lZona.setForeground(GRIS);
        lZona.setFont(new Font("Dialog", Font.PLAIN, 11));
        lZona.setBounds(130, 50, 300, 16);
        card.add(lZona);
 
        JLabel lPts = new JLabel(lugar[3]);
        lPts.setForeground(VERDE);
        lPts.setFont(new Font("Dialog", Font.BOLD, 13));
        lPts.setBounds(700, 32, 120, 22);
        card.add(lPts);
 
        JLabel lE = new JLabel("✅  Registrado");
        lE.setForeground(VERDE);
        lE.setFont(new Font("Dialog", Font.BOLD, 12));
        lE.setBounds(860, 32, 150, 22);
        card.add(lE);
 
        return card;
    }
 
    // ══════════════════════════════════════════════
    // CARD — DÓNDE OBTENER APUNAB
    // ══════════════════════════════════════════════
    private JPanel buildCardDondeApunab(int idx) {
        JPanel card = makeCardBase(lugaresApunab[idx][3], false, false);
 
        JLabel lNombre = new JLabel(lugaresApunab[idx][0]);
        lNombre.setForeground(DORADO);
        lNombre.setFont(new Font("Dialog", Font.BOLD, 15));
        lNombre.setBounds(130, 8, 380, 22);
        card.add(lNombre);
 
        JLabel lDesc = new JLabel("<html>" + lugaresApunab[idx][1] + "</html>");
        lDesc.setForeground(GRIS_CLARO);
        lDesc.setFont(new Font("Dialog", Font.PLAIN, 12));
        lDesc.setBounds(130, 32, 580, 40);
        card.add(lDesc);
 
        JLabel lPts = new JLabel("💰 " + lugaresApunab[idx][2]);
        lPts.setForeground(VERDE);
        lPts.setFont(new Font("Dialog", Font.BOLD, 13));
        lPts.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VERDE, 1),
            BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        lPts.setBounds(730, 30, 200, 26);
        card.add(lPts);
 
        JButton btnIr = new JButton("Ir al lugar →");
        btnIr.setBounds(920, 26, 110, 30);
        btnIr.setBackground(new Color(35, 28, 10));
        btnIr.setForeground(DORADO);
        btnIr.setFont(new Font("Dialog", Font.BOLD, 11));
        btnIr.setFocusPainted(false);
        btnIr.setBorder(BorderFactory.createLineBorder(DORADO, 1));
        btnIr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIr.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnIr.setBackground(new Color(55, 44, 15)); }
            @Override public void mouseExited(MouseEvent e)  { btnIr.setBackground(new Color(35, 28, 10)); }
        });
        btnIr.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Dirígete a:\n\n" + lugaresApunab[idx][0] +
            "\n\nRecuerda llevar tu carnet para hacer check-in y ganar " + lugaresApunab[idx][2] + ".",
            "¿Cómo llegar?", JOptionPane.INFORMATION_MESSAGE));
        card.add(btnIr);
 
        return card;
    }
 
    // ══════════════════════════════════════════════
    // CARD — REGISTRARME (desde DatosUsuario)
    // ══════════════════════════════════════════════
    private JPanel buildCardRegistrarme(int idx, String[] lugar) {
        String imgPath = "/img/csu.jpg";
        for (String[] lg : lugaresGenerales)
            if (lg[0].equalsIgnoreCase(lugar[1])) { imgPath = lg[3]; break; }
 
        JPanel card = makeCardBase(imgPath, false, false);
 
        JLabel lNombre = new JLabel(lugar[1]);
        lNombre.setForeground(Color.WHITE);
        lNombre.setFont(new Font("Dialog", Font.BOLD, 14));
        lNombre.setBounds(130, 8, 420, 20);
        card.add(lNombre);
 
        JLabel lZona = new JLabel("📍 " + lugar[4] + "  ·  Sin visitar");
        lZona.setForeground(GRIS);
        lZona.setFont(new Font("Dialog", Font.PLAIN, 11));
        lZona.setBounds(130, 30, 400, 16);
        card.add(lZona);
 
        JLabel lPts = new JLabel("Premio: " + lugar[3]);
        lPts.setForeground(VERDE);
        lPts.setFont(new Font("Dialog", Font.BOLD, 11));
        lPts.setBounds(130, 52, 250, 16);
        card.add(lPts);
 
        JButton btnReg = makeBotonRegistrar("Registrarme aquí");
        btnReg.setBounds(880, 26, 150, 34);
        btnReg.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this,
                "¿Confirmas que estás visitando:\n\n\"" + lugar[1] + "\"?\n\nSe añadirán " + lugar[3] + " a tu cuenta.",
                "Confirmar visita", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                DatosUsuario.get().visitarLugar(idx);
                JOptionPane.showMessageDialog(this,
                    "✅ ¡Visita registrada!\n" + lugar[1] + "\n" + lugar[3] + " añadidos.",
                    "¡Listo!", JOptionPane.INFORMATION_MESSAGE);
                dispose(); new Lugares(usuario);
            }
        });
        card.add(btnReg);
        return card;
    }
 
    // ══════════════════════════════════════════════
    // CARD — REGISTRARME (catálogo nuevo)
    // ══════════════════════════════════════════════
    private JPanel buildCardRegistrarmeNuevo(int catIdx) {
        JPanel card = makeCardBase(lugaresGenerales[catIdx][3], false, false);
 
        JLabel lNombre = new JLabel(lugaresGenerales[catIdx][0]);
        lNombre.setForeground(Color.WHITE);
        lNombre.setFont(new Font("Dialog", Font.BOLD, 14));
        lNombre.setBounds(130, 8, 420, 20);
        card.add(lNombre);
 
        JLabel lZona = new JLabel("📍 " + lugaresGenerales[catIdx][1]);
        lZona.setForeground(GRIS);
        lZona.setFont(new Font("Dialog", Font.PLAIN, 11));
        lZona.setBounds(130, 30, 400, 16);
        card.add(lZona);
 
        JLabel lPts = new JLabel("Premio: " + lugaresGenerales[catIdx][4]);
        lPts.setForeground(VERDE);
        lPts.setFont(new Font("Dialog", Font.BOLD, 11));
        lPts.setBounds(130, 52, 200, 16);
        card.add(lPts);
 
        JButton btnReg = makeBotonRegistrar("Registrarme aquí");
        btnReg.setBounds(880, 26, 150, 34);
        btnReg.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this,
                "¿Confirmas que estás visitando:\n\n\"" + lugaresGenerales[catIdx][0] + "\"?\n\nSe añadirán " + lugaresGenerales[catIdx][4] + " a tu cuenta.",
                "Confirmar visita", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String[] nuevo = new String[]{
                    "done",
                    lugaresGenerales[catIdx][0],
                    "Visitado el " + fecha,
                    lugaresGenerales[catIdx][4],
                    lugaresGenerales[catIdx][1],
                    lugaresGenerales[catIdx][3]
                };
                DatosUsuario.get().getLugares().add(nuevo);
                int pts = Integer.parseInt(lugaresGenerales[catIdx][4].replaceAll("[^0-9]", ""));
                DatosUsuario.get().agregarPuntosExterno(
                    "+" + pts + " pts",
                    "Visitó lugar: " + lugaresGenerales[catIdx][0], fecha);
                try {
                    java.io.PrintWriter pw = new java.io.PrintWriter(
                        new java.io.FileWriter("lugares_" + DatosUsuario.get().getUsuarioActual() + ".txt", true));
                    pw.println(String.join("|", nuevo));
                    pw.close();
                } catch (Exception ex) { ex.printStackTrace(); }
                JOptionPane.showMessageDialog(this,
                    "✅ ¡Visita registrada!\n" + lugaresGenerales[catIdx][0] + "\n" + lugaresGenerales[catIdx][4] + " añadidos.",
                    "¡Listo!", JOptionPane.INFORMATION_MESSAGE);
                dispose(); new Lugares(usuario);
            }
        });
        card.add(btnReg);
        return card;
    }
 
    // ══════════════════════════════════════════════
    // CARD — DARME DE BAJA
    // ══════════════════════════════════════════════
    private JPanel buildCardDarseDeBaja(int idx, String[] lugar) {
        String imgPath = "/img/csu.jpg";
        for (String[] lg : lugaresGenerales)
            if (lg[0].equalsIgnoreCase(lugar[1])) { imgPath = lg[3]; break; }
 
        JPanel card = makeCardBase(imgPath, false, true);
 
        JLabel lNombre = new JLabel(lugar[1]);
        lNombre.setForeground(Color.WHITE);
        lNombre.setFont(new Font("Dialog", Font.BOLD, 14));
        lNombre.setBounds(130, 8, 420, 20);
        card.add(lNombre);
 
        JLabel lFecha = new JLabel("🗓  " + lugar[2]);
        lFecha.setForeground(GRIS);
        lFecha.setFont(new Font("Dialog", Font.PLAIN, 11));
        lFecha.setBounds(130, 30, 350, 16);
        card.add(lFecha);
 
        JLabel lZona = new JLabel("📍 " + lugar[4]);
        lZona.setForeground(GRIS);
        lZona.setFont(new Font("Dialog", Font.PLAIN, 11));
        lZona.setBounds(130, 50, 300, 16);
        card.add(lZona);
 
        JLabel lPts = new JLabel(lugar[3] + "  (se revertirán)");
        lPts.setForeground(ROJO);
        lPts.setFont(new Font("Dialog", Font.BOLD, 11));
        lPts.setBounds(580, 32, 240, 22);
        card.add(lPts);
 
        JButton btnBaja = new JButton("Darme de baja");
        btnBaja.setBounds(880, 26, 150, 34);
        btnBaja.setBackground(new Color(80, 20, 20));
        btnBaja.setForeground(Color.WHITE);
        btnBaja.setFont(new Font("Dialog", Font.BOLD, 11));
        btnBaja.setFocusPainted(false);
        btnBaja.setBorder(BorderFactory.createLineBorder(ROJO, 1));
        btnBaja.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBaja.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnBaja.setBackground(new Color(120, 30, 30)); }
            @Override public void mouseExited(MouseEvent e)  { btnBaja.setBackground(new Color(80, 20, 20)); }
        });
        btnBaja.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this,
                "⚠️ ¿Confirmas que deseas darte de baja de:\n\n\"" + lugar[1] + "\"?\n\nSe revertirán: " + lugar[3],
                "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c == JOptionPane.YES_OPTION) {
                lugar[0] = "pend";
                lugar[2] = "No visitado";
                int pts = 0;
                try { pts = Integer.parseInt(lugar[3].replaceAll("[^0-9]", "")); } catch (Exception ignored) {}
                String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                DatosUsuario.get().agregarPuntosExterno(
                    "-" + pts + " pts", "Baja de lugar: " + lugar[1], fecha);
                try {
                    java.io.PrintWriter pw = new java.io.PrintWriter(
                        new java.io.FileWriter("lugares_" + DatosUsuario.get().getUsuarioActual() + ".txt", false));
                    for (String[] l : DatosUsuario.get().getLugares())
                        pw.println(String.join("|", l));
                    pw.close();
                } catch (Exception ex) { ex.printStackTrace(); }
                JOptionPane.showMessageDialog(this,
                    "Se ha dado de baja del lugar \"" + lugar[1] + "\".\n" + pts + " puntos revertidos.",
                    "Baja exitosa", JOptionPane.INFORMATION_MESSAGE);
                dispose(); new Lugares(usuario);
            }
        });
        card.add(btnBaja);
        return card;
    }
 
    // ══════════════════════════════════════════════
    // HELPER BOTÓN
    // ══════════════════════════════════════════════
    private JButton makeBotonRegistrar(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(DORADO);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Dialog", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(DORADO_CLARO); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(DORADO); }
        });
        return btn;
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Lugares("default"));
    }
}
