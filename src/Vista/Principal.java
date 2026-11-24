package Vista;

import Estructura.Pila_historial;
import Servicio.Ticket_Servicio;
import Util.ContextoAplicacion_ArchivoTemporal;
import java.awt.Color;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author luisv
 */
public class Principal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Principal.class.getName());

    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();
        
        int radioEsquinas = 28; // Ajusta qué tan redondeadas quieres las esquinas (ej. 20 a 40)
        Color colorNormal = new Color(15, 15, 15);
        Color colorHover  = new Color(42, 38, 30);
        Color bordeNormal = new Color(188, 139, 42, 120);
        Color bordeHover  = new Color(188, 139, 42);

        estilizarTarjetaBoton(btn_crearTicket, radioEsquinas, colorNormal, colorHover, bordeNormal, bordeHover);
        estilizarTarjetaBoton(btn_colaAtencion, radioEsquinas, colorNormal, colorHover, bordeNormal, bordeHover);
        estilizarTarjetaBoton(btn_busquedaTicket, radioEsquinas, colorNormal, colorHover, bordeNormal, bordeHover);
        estilizarTarjetaBoton(btn_historialCambios, radioEsquinas, colorNormal, colorHover, bordeNormal, bordeHover);
        estilizarTarjetaBoton(btn_reportesEstadisticas, radioEsquinas, colorNormal, colorHover, bordeNormal, bordeHover);

        this.setExtendedState(MAXIMIZED_BOTH);
        this.getContentPane().setBackground(Color.black);
    }

    private Historial crearVentanaHistorial() {
        Pila_historial pilaCompartida = ContextoAplicacion_ArchivoTemporal.getPilaHistorial();
        if (pilaCompartida == null) {
            pilaCompartida = new Pila_historial();
        }
        return new Historial(pilaCompartida, new Ticket_Servicio());
    }

    private void abrirYcerrarPrincipal(Window ventanaDestino) {
        if (ventanaDestino == null) {
            return;
        }
        if (ventanaDestino instanceof JFrame destinoFrame) {
            destinoFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            destinoFrame.setExtendedState(MAXIMIZED_BOTH);
        }
        ventanaDestino.setLocationRelativeTo(null);
        ventanaDestino.setVisible(true);
        this.dispose();
    }
    
    private void abrirVentanaSecundaria(Window ventanaDestino) {
        if (ventanaDestino == null) {
            return;
        }
        if (ventanaDestino instanceof JFrame destinoFrame) {
            destinoFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            destinoFrame.setExtendedState(JFrame.NORMAL);
            destinoFrame.pack();
        }
        ventanaDestino.setLocationRelativeTo(null);
        ventanaDestino.setVisible(true);
    }

    private void mostrarHistorial() {
        Pila_historial pila = ContextoAplicacion_ArchivoTemporal.getPilaHistorial();
        if (pila == null || pila.estaVacia()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay movimientos registrados en el historial.",
                    "Historial de cambios",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Object[] elementos = pila.obtenerElementos();
        StringBuilder sb = new StringBuilder();
        int numero = 1;
        for (int i = elementos.length - 1; i >= 0; i--) {
            if (elementos[i] != null) {
                sb.append(numero++).append(". ").append(elementos[i]).append("\n");
            }
        }

        if (sb.length() == 0) {
            sb.append("No hay movimientos registrados en el historial.");
        }

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Historial de cambios",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void estilizarTarjetaBoton(
            javax.swing.JButton boton,
            int radioEsquinas,
            java.awt.Color colorNormal,
            java.awt.Color colorHover,
            java.awt.Color bordeNormal,
            java.awt.Color bordeHover) {

        final java.awt.Color[] fondoActual = { colorNormal };

        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setBorder(crearBordeRedondeado(bordeNormal, 1, radioEsquinas));
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondoActual[0]);
                g2.fill(new java.awt.geom.RoundRectangle2D.Float(
                    0, 0, c.getWidth(), c.getHeight(), radioEsquinas, radioEsquinas
                ));
                g2.dispose();
                super.paint(g, c);
            }
        });

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fondoActual[0] = colorHover;
                boton.setBorder(crearBordeRedondeado(bordeHover, 2, radioEsquinas));
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fondoActual[0] = colorNormal;
                boton.setBorder(crearBordeRedondeado(bordeNormal, 1, radioEsquinas));
                boton.repaint();
            }
        });
    }

    // Borde redondeado reutilizable con grosor y color configurables
    private javax.swing.border.Border crearBordeRedondeado(java.awt.Color color, int grosor, int radio) {
        return new javax.swing.border.AbstractBorder() {
            @Override
            public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new java.awt.BasicStroke(grosor));
                float offset = grosor / 2f;
                g2.draw(new java.awt.geom.RoundRectangle2D.Float(
                    x + offset, y + offset, width - grosor, height - grosor, radio, radio
                ));
                g2.dispose();
            }

            @Override
            public java.awt.Insets getBorderInsets(java.awt.Component c) {
                int pad = grosor + 4;
                return new java.awt.Insets(pad, pad, pad, pad);
            }

            @Override
            public java.awt.Insets getBorderInsets(java.awt.Component c, java.awt.Insets insets) {
                int pad = grosor + 4;
                insets.set(pad, pad, pad, pad);
                return insets;
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btn_inicio = new javax.swing.JButton();
        btn_servicios = new javax.swing.JButton();
        btn_reportes = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btn_imprimir = new javax.swing.JButton();
        btn_crear = new javax.swing.JButton();
        btn_cola = new javax.swing.JButton();
        btn_historial = new javax.swing.JButton();
        btn_trabajadores = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btn_crearTicket = new javax.swing.JButton();
        btn_colaAtencion = new javax.swing.JButton();
        btn_busquedaTicket = new javax.swing.JButton();
        btn_historialCambios = new javax.swing.JButton();
        btn_reportesEstadisticas = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel2.setPreferredSize(new java.awt.Dimension(656, 50));

        btn_inicio.setBackground(new java.awt.Color(0, 0, 0));
        btn_inicio.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_inicio.setForeground(new java.awt.Color(153, 102, 0));
        btn_inicio.setText("Inicio");
        btn_inicio.setBorderPainted(false);
        btn_inicio.setContentAreaFilled(false);
        btn_inicio.addActionListener(this::btn_inicioActionPerformed);

        btn_servicios.setBackground(new java.awt.Color(0, 0, 0));
        btn_servicios.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_servicios.setForeground(new java.awt.Color(153, 153, 153));
        btn_servicios.setText("Servicios");
        btn_servicios.setBorderPainted(false);
        btn_servicios.setContentAreaFilled(false);
        btn_servicios.addActionListener(this::btn_serviciosActionPerformed);

        btn_reportes.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_reportes.setForeground(new java.awt.Color(153, 153, 153));
        btn_reportes.setText("Reportes");
        btn_reportes.setBorderPainted(false);
        btn_reportes.setContentAreaFilled(false);
        btn_reportes.addActionListener(this::btn_reportesActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btn_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btn_servicios, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btn_reportes, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 977, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_inicio, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
            .addComponent(btn_servicios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_reportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(188, 139, 42));
        jPanel3.setMinimumSize(new java.awt.Dimension(200, 100));

        btn_imprimir.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_imprimir.setForeground(new java.awt.Color(255, 255, 255));
        btn_imprimir.setText("Imprimir excel");
        btn_imprimir.setContentAreaFilled(false);
        btn_imprimir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_imprimir.addActionListener(this::btn_imprimirActionPerformed);

        btn_crear.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_crear.setForeground(new java.awt.Color(255, 255, 255));
        btn_crear.setText("Crear ticket");
        btn_crear.setBorderPainted(false);
        btn_crear.setContentAreaFilled(false);
        btn_crear.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_crear.addActionListener(this::btn_crearActionPerformed);

        btn_cola.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_cola.setForeground(new java.awt.Color(255, 255, 255));
        btn_cola.setText("Cola de atencion");
        btn_cola.setBorderPainted(false);
        btn_cola.setContentAreaFilled(false);
        btn_cola.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_cola.addActionListener(this::btn_colaActionPerformed);

        btn_historial.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_historial.setForeground(new java.awt.Color(255, 255, 255));
        btn_historial.setText("Historial");
        btn_historial.setBorderPainted(false);
        btn_historial.setContentAreaFilled(false);
        btn_historial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_historial.addActionListener(this::btn_historialActionPerformed);

        btn_trabajadores.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btn_trabajadores.setForeground(new java.awt.Color(255, 255, 255));
        btn_trabajadores.setText("Trabajadores");
        btn_trabajadores.setBorderPainted(false);
        btn_trabajadores.setContentAreaFilled(false);
        btn_trabajadores.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_trabajadores.addActionListener(this::btn_trabajadoresActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_imprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_crear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_cola, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                    .addComponent(btn_historial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_trabajadores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btn_imprimir)
                .addGap(22, 22, 22)
                .addComponent(btn_crear)
                .addGap(22, 22, 22)
                .addComponent(btn_cola)
                .addGap(22, 22, 22)
                .addComponent(btn_historial)
                .addGap(22, 22, 22)
                .addComponent(btn_trabajadores)
                .addContainerGap(636, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        btn_crearTicket.setBackground(new java.awt.Color(16, 16, 16));
        btn_crearTicket.setForeground(new java.awt.Color(255, 255, 255));
        btn_crearTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/crearTicket.png"))); // NOI18N
        btn_crearTicket.setText("<html><div align='center'><b style='font-size:13px; color:#996600;'>CREAR TICKET</b><br><br><span style='font-size:10px; color:#CCCCCC;'>Permite ingresar nuevos vehículos<br>y generar órdenes de servicio.</span></div></html>");
        btn_crearTicket.setToolTipText("");
        btn_crearTicket.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));
        btn_crearTicket.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_crearTicket.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_crearTicket.addActionListener(this::btn_crearTicketActionPerformed);

        btn_colaAtencion.setBackground(new java.awt.Color(16, 16, 16));
        btn_colaAtencion.setForeground(new java.awt.Color(255, 255, 255));
        btn_colaAtencion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cola.png"))); // NOI18N
        btn_colaAtencion.setText("<html><div align='center'><b style='font-size:13px; color:#996600;'>COLA DE ATENCIÓN</b><br><br><span style='font-size:10px; color:#CCCCCC;'>Gestiona el orden de los vehículos y<br>asigna prioridades.</span></div></html>   ");
        btn_colaAtencion.setToolTipText("");
        btn_colaAtencion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));
        btn_colaAtencion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_colaAtencion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_colaAtencion.addActionListener(this::btn_colaAtencionActionPerformed);

        btn_busquedaTicket.setBackground(new java.awt.Color(16, 16, 16));
        btn_busquedaTicket.setForeground(new java.awt.Color(255, 255, 255));
        btn_busquedaTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/busqueda.png"))); // NOI18N
        btn_busquedaTicket.setText("<html><div align='center'><b style='font-size:13px; color:#996600;'>BÚSQUEDA DE TICKETS</b><br><br><span style='font-size:10px; color:#CCCCCC;'>Encuentra cualquier ticket al instante<br>por código.</span></div></html>\n");
        btn_busquedaTicket.setToolTipText("");
        btn_busquedaTicket.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));
        btn_busquedaTicket.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_busquedaTicket.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_busquedaTicket.addActionListener(this::btn_busquedaTicketActionPerformed);

        btn_historialCambios.setBackground(new java.awt.Color(16, 16, 16));
        btn_historialCambios.setForeground(new java.awt.Color(255, 255, 255));
        btn_historialCambios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/historial.png"))); // NOI18N
        btn_historialCambios.setText("<html><div align='center'><b style='font-size:13px; color:#996600;'>HISTORIAL DE CAMBIOS</b><br><br><span style='font-size:10px; color:#CCCCCC;'>Consulta la linea de  tiempo <br>completa de cada servicio y estado.</span></div></html> ");
        btn_historialCambios.setToolTipText("");
        btn_historialCambios.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));
        btn_historialCambios.setIconTextGap(20);
        btn_historialCambios.addActionListener(this::btn_historialCambiosActionPerformed);

        btn_reportesEstadisticas.setBackground(new java.awt.Color(16, 16, 16));
        btn_reportesEstadisticas.setForeground(new java.awt.Color(255, 255, 255));
        btn_reportesEstadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/reporte.png"))); // NOI18N
        btn_reportesEstadisticas.setText("<html><div align='center'><b style='font-size:13px; color:#996600;'>REPORTES Y <br>ESTADÍSTICAS</b><br><br><span style='font-size:10px; color:#CCCCCC;'>Visualiza tiempos de atención,<br>servicios populares y desempeño.</span></div></html>\n");
        btn_reportesEstadisticas.setToolTipText("");
        btn_reportesEstadisticas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));
        btn_reportesEstadisticas.setIconTextGap(20);
        btn_reportesEstadisticas.addActionListener(this::btn_reportesEstadisticasActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_historialCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_reportesEstadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_crearTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(btn_colaAtencion, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(btn_busquedaTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_busquedaTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_crearTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_colaAtencion, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_historialCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reportesEstadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(242, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_crearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crearActionPerformed
        abrirYcerrarPrincipal(new Registro_Tickets());
    }//GEN-LAST:event_btn_crearActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        abrirYcerrarPrincipal(new Reporte_Java());
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void btn_crearTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crearTicketActionPerformed
        abrirYcerrarPrincipal(new Registro_Tickets());
    }//GEN-LAST:event_btn_crearTicketActionPerformed

    private void btn_colaAtencionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_colaAtencionActionPerformed
        abrirVentanaSecundaria(new Cola_Atencion());
    }//GEN-LAST:event_btn_colaAtencionActionPerformed

    private void btn_busquedaTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_busquedaTicketActionPerformed
        abrirYcerrarPrincipal(new Busqueda_Ticket());
    }//GEN-LAST:event_btn_busquedaTicketActionPerformed

    private void btn_historialCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_historialCambiosActionPerformed
        abrirYcerrarPrincipal(crearVentanaHistorial());
    }//GEN-LAST:event_btn_historialCambiosActionPerformed

    private void btn_reportesEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reportesEstadisticasActionPerformed
        abrirYcerrarPrincipal(new Reporte_Java());
    }//GEN-LAST:event_btn_reportesEstadisticasActionPerformed

    private void btn_inicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_inicioActionPerformed
        this.toFront();
        this.requestFocus();
    }//GEN-LAST:event_btn_inicioActionPerformed

    private void btn_colaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_colaActionPerformed
        abrirVentanaSecundaria(new Cola_Atencion());
    }//GEN-LAST:event_btn_colaActionPerformed

    private void btn_historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_historialActionPerformed
        abrirYcerrarPrincipal(crearVentanaHistorial());
    }//GEN-LAST:event_btn_historialActionPerformed

    private void btn_trabajadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_trabajadoresActionPerformed
        abrirYcerrarPrincipal(new Gestion_Trabajadores());
    }//GEN-LAST:event_btn_trabajadoresActionPerformed

    private void btn_serviciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_serviciosActionPerformed
        abrirYcerrarPrincipal(new Servicio());
    }//GEN-LAST:event_btn_serviciosActionPerformed

    private void btn_reportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reportesActionPerformed
        abrirYcerrarPrincipal(new Reporte_Java());
    }//GEN-LAST:event_btn_reportesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Principal().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_busquedaTicket;
    private javax.swing.JButton btn_cola;
    private javax.swing.JButton btn_colaAtencion;
    private javax.swing.JButton btn_crear;
    private javax.swing.JButton btn_crearTicket;
    private javax.swing.JButton btn_historial;
    private javax.swing.JButton btn_historialCambios;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_inicio;
    private javax.swing.JButton btn_reportes;
    private javax.swing.JButton btn_reportesEstadisticas;
    private javax.swing.JButton btn_servicios;
    private javax.swing.JButton btn_trabajadores;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}
