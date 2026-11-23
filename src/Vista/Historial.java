package Vista;

import Estructura.Pila_historial;
import Modelo.Ticket;
import Servicio.Ticket_Servicio;
import Util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luisv
 */
public class Historial extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Historial.class.getName());
    private static final DateTimeFormatter FMT_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Pila_historial pilaHistorial;
    private final Ticket_Servicio ticketServicio;

    /** Creates new form Historial */
    public Historial() {
        this(new Pila_historial(), new Ticket_Servicio());
    }

    public Historial(Pila_historial pilaHistorial, Ticket_Servicio ticketServicio) {
        this.pilaHistorial = pilaHistorial != null ? pilaHistorial : new Pila_historial();
        this.ticketServicio = ticketServicio != null ? ticketServicio : new Ticket_Servicio();
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        cargarHistorial();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbb_filtro = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txt_ticket = new javax.swing.JTextField();
        btn_limpiar = new javax.swing.JButton();
        btn_filtrar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lbl_ultimoEvento = new javax.swing.JLabel();
        lbl_total = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_historial = new javax.swing.JTable();
        btn_volver = new javax.swing.JButton();
        btn_deshacer = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setMinimumSize(new java.awt.Dimension(1280, 960));
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 960));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HISTORIAL DEL SISTEMA");
        jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        jLabel1.setPreferredSize(new java.awt.Dimension(37, 80));
        jPanel1.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setMaximumSize(new java.awt.Dimension(1357, 656));
        jPanel2.setMinimumSize(new java.awt.Dimension(1357, 656));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 102, 0));
        jLabel2.setText("Filtro:");

        cbb_filtro.setBackground(new java.awt.Color(18, 17, 17));
        cbb_filtro.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbb_filtro.setForeground(new java.awt.Color(153, 102, 0));
        cbb_filtro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pila", "Ordenes" }));
        cbb_filtro.setBorder(null);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 102, 0));
        jLabel3.setText("Ticket:");

        txt_ticket.setBackground(new java.awt.Color(18, 17, 17));
        txt_ticket.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_ticket.setForeground(new java.awt.Color(153, 102, 0));
        txt_ticket.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        btn_limpiar.setBackground(new java.awt.Color(153, 102, 0));
        btn_limpiar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_limpiar.setText("Limpiar");
        btn_limpiar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_limpiar.setBorderPainted(false);
        btn_limpiar.addActionListener(this::btn_limpiarActionPerformed);

        btn_filtrar.setBackground(new java.awt.Color(153, 102, 0));
        btn_filtrar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_filtrar.setText("Filtrar");
        btn_filtrar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_filtrar.setBorderPainted(false);
        btn_filtrar.addActionListener(this::btn_filtrarActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel2)
                .addGap(29, 29, 29)
                .addComponent(cbb_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txt_ticket, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_filtrar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_limpiar)
                    .addComponent(btn_filtrar)
                    .addComponent(jLabel2)
                    .addComponent(cbb_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txt_ticket, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        jPanel4.setForeground(new java.awt.Color(153, 102, 0));

        lbl_ultimoEvento.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_ultimoEvento.setForeground(new java.awt.Color(153, 102, 0));
        lbl_ultimoEvento.setText("Ultimo evento (Pila):   ");

        lbl_total.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_total.setForeground(new java.awt.Color(153, 102, 0));
        lbl_total.setText("Total: ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(lbl_ultimoEvento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_total)
                .addGap(146, 146, 146))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ultimoEvento)
                    .addComponent(lbl_total))
                .addGap(18, 18, 18))
        );

        tbl_historial.setBackground(new java.awt.Color(18, 17, 17));
        tbl_historial.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 102, 0)));
        tbl_historial.setForeground(new java.awt.Color(153, 102, 0));
        tbl_historial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Origen / Tipo", "Nro. Ticket", "Fecha / Hora", "Estado / Detalle"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_historial.setGridColor(new java.awt.Color(153, 102, 0));
        tbl_historial.setOpaque(false);
        tbl_historial.setSelectionBackground(new java.awt.Color(153, 102, 0));
        jScrollPane1.setViewportView(tbl_historial);

        btn_volver.setBackground(new java.awt.Color(153, 102, 0));
        btn_volver.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_volver.setText("Volver");
        btn_volver.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_volver.setBorderPainted(false);
        btn_volver.addActionListener(this::btn_volverActionPerformed);

        btn_deshacer.setBackground(new java.awt.Color(153, 102, 0));
        btn_deshacer.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_deshacer.setText("Deshacer pila (Pop)");
        btn_deshacer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_deshacer.setBorderPainted(false);
        btn_deshacer.addActionListener(this::btn_deshacerActionPerformed);

        btn_actualizar.setBackground(new java.awt.Color(153, 102, 0));
        btn_actualizar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_actualizar.setText("Actualizar");
        btn_actualizar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_actualizar.setBorderPainted(false);
        btn_actualizar.addActionListener(this::btn_actualizarActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_deshacer, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_actualizar)
                    .addComponent(btn_deshacer)
                    .addComponent(btn_volver))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiarActionPerformed
        cbb_filtro.setSelectedIndex(0);
        txt_ticket.setText("");
        cargarHistorial();
    }//GEN-LAST:event_btn_limpiarActionPerformed

    private void btn_filtrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_filtrarActionPerformed
        int totalFilas = cargarHistorial();
        String ticketBuscado = txt_ticket.getText().trim();

        if (!ticketBuscado.isEmpty() && totalFilas == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron registros para el ticket: " + ticketBuscado,
                    "Sin coincidencias",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }//GEN-LAST:event_btn_filtrarActionPerformed

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        boolean hayPrincipalVisible = false;
        for (java.awt.Window w : java.awt.Window.getWindows()) {
            if (w instanceof Principal p && p.isDisplayable() && p.isVisible()) {
                p.setExtendedState(MAXIMIZED_BOTH);
                p.toFront();
                hayPrincipalVisible = true;
                break;
            }
        }
        if (!hayPrincipalVisible) {
            Principal principal = new Principal();
            principal.setExtendedState(MAXIMIZED_BOTH);
            principal.setLocationRelativeTo(null);
            principal.setVisible(true);
        }
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

    private void btn_deshacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deshacerActionPerformed
        String retirado = pilaHistorial != null ? pilaHistorial.desapilar() : null;
        if (retirado == null) {
            JOptionPane.showMessageDialog(this, "La pila de historial está vacía.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Evento retirado de la pila:\n" + retirado, "Pila actualizada", JOptionPane.INFORMATION_MESSAGE);
        }
        cargarHistorial();
    }//GEN-LAST:event_btn_deshacerActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        cargarHistorial();
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private int cargarHistorial() {
        DefaultTableModel modelo = (DefaultTableModel) tbl_historial.getModel();
        modelo.setRowCount(0);

        String filtro = cbb_filtro.getSelectedItem() != null ? cbb_filtro.getSelectedItem().toString() : "Todos";
        String ticketBuscado = txt_ticket.getText().trim().toLowerCase();

        String ultimo = pilaHistorial != null ? pilaHistorial.verUltimo() : null;
        lbl_ultimoEvento.setText("Ultimo evento (Pila): " + (ultimo != null ? ultimo : "Ninguno"));

        int totalFilas = 0;

        if (pilaHistorial != null && ("Todos".equalsIgnoreCase(filtro) || "Pila".equalsIgnoreCase(filtro))) {
            String[] eventos = pilaHistorial.obtenerElementos();
            if (eventos != null) {
                for (int i = eventos.length - 1; i >= 0; i--) {
                    String ev = eventos[i];
                    if (ev != null && (ticketBuscado.isEmpty() || ev.toLowerCase().contains(ticketBuscado))) {
                        modelo.addRow(new Object[]{"Pila", "-", "-", ev});
                        totalFilas++;
                    }
                }
            }
        }

        if ("Todos".equalsIgnoreCase(filtro) || "Ordenes".equalsIgnoreCase(filtro)) {
            List<Object[]> filasBD = consultarOrdenesBD(ticketBuscado);
            for (Object[] fila : filasBD) {
                modelo.addRow(fila);
                totalFilas++;
            }
        }

        lbl_total.setText("Total: " + totalFilas);
        return totalFilas;
    }

    private List<Object[]> consultarOrdenesBD(String ticketBuscado) {
        List<Object[]> filas = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT NRO_TICKET, FECHA_INGRESO, FECHA_TERMINO, ESTADO, "
                + "MOTIVO_RETRASO, NOTA_CORRECCION, MOTIVO_CANCELACION, MOTIVO_DESCUENTO "
                + "FROM ORDEN_SERVICIO"
        );
        List<Object> params = new ArrayList<>();

        if (ticketBuscado != null && !ticketBuscado.isBlank()) {
            sql.append(" WHERE LOWER(NRO_TICKET) LIKE ?");
            params.add("%" + ticketBuscado + "%");
        }
        sql.append(" ORDER BY FECHA_INGRESO DESC, ID_ORDEN DESC");

        try (Connection con = ConexionBD.conn();
             PreparedStatement ps = con != null ? con.prepareStatement(sql.toString()) : null) {

            if (ps == null) {
                agregarTicketsMemoria(filas, ticketBuscado);
                return filas;
            }

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp fTer = rs.getTimestamp("FECHA_TERMINO");
                    Timestamp fIng = rs.getTimestamp("FECHA_INGRESO");
                    String fechaStr = fTer != null
                            ? fTer.toLocalDateTime().format(FMT_FECHA_HORA)
                            : (fIng != null ? fIng.toLocalDateTime().format(FMT_FECHA_HORA) : "-");

                    String detalle = construirDetalle(
                            rs.getString("ESTADO"),
                            rs.getString("MOTIVO_CANCELACION"),
                            rs.getString("MOTIVO_RETRASO"),
                            rs.getString("NOTA_CORRECCION"),
                            rs.getString("MOTIVO_DESCUENTO")
                    );

                    filas.add(new Object[]{"Orden BD", rs.getString("NRO_TICKET"), fechaStr, detalle});
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar ORDEN_SERVICIO en Historial: " + ex.getMessage());
            agregarTicketsMemoria(filas, ticketBuscado);
        }

        return filas;
    }

    private void agregarTicketsMemoria(List<Object[]> destino, String ticketBuscado) {
        if (ticketServicio == null) {
            return;
        }
        Ticket[] enMemoria = ticketServicio.listarTodosLosTickets();
        if (enMemoria == null) {
            return;
        }
        for (Ticket t : enMemoria) {
            if (t == null) {
                continue;
            }
            String nro = t.getNroTicket() != null ? t.getNroTicket() : "";
            if (ticketBuscado == null || ticketBuscado.isBlank() || nro.toLowerCase().contains(ticketBuscado)) {
                String fechaStr = t.getFechaIngreso() != null ? t.getFechaIngreso().format(FMT_FECHA_HORA) : "-";
                destino.add(new Object[]{"Memoria", nro, fechaStr, t.getEstado()});
            }
        }
    }

    private String construirDetalle(String estado, String cancelacion, String retraso, String correccion, String descuento) {
        StringBuilder sb = new StringBuilder(estado != null ? estado : "SIN ESTADO");
        if (cancelacion != null && !cancelacion.isBlank()) {
            sb.append(" | Cancelación: ").append(cancelacion.trim());
        }
        if (retraso != null && !retraso.isBlank()) {
            sb.append(" | Retraso: ").append(retraso.trim());
        }
        if (correccion != null && !correccion.isBlank()) {
            sb.append(" | Corrección: ").append(correccion.trim());
        }
        if (descuento != null && !descuento.isBlank()) {
            sb.append(" | Descuento: ").append(descuento.trim());
        }
        return sb.toString();
    }
    
    public static void main(String args[]) {
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

        java.awt.EventQueue.invokeLater(() -> new Historial().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_deshacer;
    private javax.swing.JButton btn_filtrar;
    private javax.swing.JButton btn_limpiar;
    private javax.swing.JButton btn_volver;
    private javax.swing.JComboBox<String> cbb_filtro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_ultimoEvento;
    private javax.swing.JTable tbl_historial;
    private javax.swing.JTextField txt_ticket;
    // End of variables declaration//GEN-END:variables

}
