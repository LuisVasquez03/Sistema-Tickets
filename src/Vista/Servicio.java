package Vista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luisv
 */
public class Servicio extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Servicio.class.getName());

    /**
     * Creates new form Servicio
     */
    public Servicio() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        configurarEventosTabla();
        limpiarFormulario();
        cargarTablaServicios(listarServiciosDesdeBD(""));
    }

    private void configurarEventosTabla() {
        tbl_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tbl_detalle.getSelectedRow();
                if (fila >= 0) {
                    Object valId = tbl_detalle.getValueAt(fila, 0);
                    Object valNombre = tbl_detalle.getValueAt(fila, 1);
                    Object valPrecio = tbl_detalle.getValueAt(fila, 2);
                    Object valEstado = tbl_detalle.getValueAt(fila, 3);
                    Object valDesc = tbl_detalle.getValueAt(fila, 4);

                    if (valId != null) {
                        txt_id.setText(valId.toString());
                        txt_id.setEditable(false);
                    }
                    txt_nombre.setText(valNombre != null ? valNombre.toString() : "");
                    txt_precio.setText(valPrecio != null ? valPrecio.toString() : "0.00");
                    if (valEstado != null) {
                        cbb_estado.setSelectedItem(valEstado.toString());
                    }
                    txt_descipcion.setText(valDesc != null ? valDesc.toString() : "");
                }
            }
        });
    }

    private List<Object[]> listarServiciosDesdeBD(String filtroSql) {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT ID_SERVICIO, NOMBRE, DESCRIPCION, ACTIVO "
                + "FROM servicio " + filtroSql + " ORDER BY ID_SERVICIO ASC";

        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                boolean activo = rs.getBoolean("ACTIVO");
                filas.add(new Object[]{
                    rs.getInt("ID_SERVICIO"),
                    rs.getString("NOMBRE"),
                    "0.00",
                    activo ? "Activo" : "Inactivo",
                    rs.getString("DESCRIPCION")
                });
            }
        } catch (SQLException ex) {
            logger.severe("Error al listar servicios: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los servicios desde la BD.\nDetalle: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return filas;
    }

    private void cargarTablaServicios(List<Object[]> filas) {
        DefaultTableModel modelo = (DefaultTableModel) tbl_detalle.getModel();
        modelo.setRowCount(0);

        if (filas == null) {
            return;
        }
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
    }

    private void limpiarFormulario() {
        txt_id.setText("AUTOMATICO");
        txt_id.setEditable(false);
        txt_nombre.setText("");
        txt_precio.setText("");
        txt_descipcion.setText("");
        if (cbb_estado.getItemCount() > 0) {
            cbb_estado.setSelectedIndex(0);
        }
        tbl_detalle.clearSelection();
        txt_nombre.requestFocus();
    }

    private void seleccionarFilaPorId(int idBuscado) {
        for (int fila = 0; fila < tbl_detalle.getRowCount(); fila++) {
            Object valId = tbl_detalle.getValueAt(fila, 0);
            if (valId != null && Integer.parseInt(valId.toString()) == idBuscado) {
                tbl_detalle.setRowSelectionInterval(fila, fila);
                tbl_detalle.scrollRectToVisible(tbl_detalle.getCellRect(fila, 0, true));
                return;
            }
        }
    }

    private boolean esIdAutomatico() {
        String textoId = txt_id.getText().trim();
        return textoId.isEmpty() || textoId.equalsIgnoreCase("AUTOMATICO");
    }

    private Connection obtenerConexion() throws SQLException {
        Connection cn = Util.ConexionBD.conn();
        if (cn == null) {
            throw new SQLException("No se pudo establecer conexión con la base de datos.");
        }
        return cn;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        txt_nombre = new javax.swing.JTextField();
        txt_descipcion = new javax.swing.JTextField();
        cbb_estado = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_detalle = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btn_buscar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        btn_nuevo = new javax.swing.JButton();
        btn_volver = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setMinimumSize(new java.awt.Dimension(1280, 720));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 720));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GESTIÓN DE SERVICIOS");
        jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        jLabel1.setPreferredSize(new java.awt.Dimension(37, 80));
        jLabel1.setRequestFocusEnabled(false);
        jPanel1.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setMinimumSize(new java.awt.Dimension(1400, 640));
        jPanel2.setPreferredSize(new java.awt.Dimension(1400, 640));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 102, 0));
        jLabel3.setText("ID SERVICIO:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 102, 0));
        jLabel4.setText("Precio base (S/.) :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 102, 0));
        jLabel5.setText("Nombre del servicio:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 102, 0));
        jLabel11.setText("Estado:");

        txt_id.setEditable(false);
        txt_id.setBackground(new java.awt.Color(18, 17, 17));
        txt_id.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txt_id.setForeground(new java.awt.Color(153, 102, 0));
        txt_id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_id.setText("AUTOMATICO");
        txt_id.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        txt_nombre.setBackground(new java.awt.Color(18, 17, 17));
        txt_nombre.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_nombre.setForeground(new java.awt.Color(153, 102, 0));
        txt_nombre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        txt_descipcion.setBackground(new java.awt.Color(18, 17, 17));
        txt_descipcion.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_descipcion.setForeground(new java.awt.Color(153, 102, 0));
        txt_descipcion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        cbb_estado.setBackground(new java.awt.Color(18, 17, 17));
        cbb_estado.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbb_estado.setForeground(new java.awt.Color(153, 102, 0));
        cbb_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));
        cbb_estado.setBorder(null);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 102, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("DETALLE DEL SERVICIO");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 102, 0));
        jLabel13.setText("Descripción:");

        txt_precio.setBackground(new java.awt.Color(18, 17, 17));
        txt_precio.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_precio.setForeground(new java.awt.Color(153, 102, 0));
        txt_precio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel12)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_descipcion, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel12)
                .addGap(55, 55, 55)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(54, 54, 54)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(53, 53, 53)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(55, 55, 55)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_descipcion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        jPanel4.setPreferredSize(new java.awt.Dimension(700, 4));

        tbl_detalle.setBackground(new java.awt.Color(18, 17, 17));
        tbl_detalle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 102, 0)));
        tbl_detalle.setForeground(new java.awt.Color(153, 102, 0));
        tbl_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Precio base", "Estado", "Descripción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_detalle.setGridColor(new java.awt.Color(153, 102, 0));
        tbl_detalle.setOpaque(false);
        tbl_detalle.setSelectionBackground(new java.awt.Color(153, 102, 0));
        jScrollPane1.setViewportView(tbl_detalle);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 102, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LISTADO DE SERVICIOS");
        jLabel2.setToolTipText("");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(242, 242, 242)
                        .addComponent(jLabel2)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        btn_buscar.setBackground(new java.awt.Color(153, 102, 0));
        btn_buscar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_buscar.setText("Buscar");
        btn_buscar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_buscar.setBorderPainted(false);
        btn_buscar.addActionListener(this::btn_buscarActionPerformed);

        btn_eliminar.setBackground(new java.awt.Color(153, 102, 0));
        btn_eliminar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_eliminar.setText("Eliminar");
        btn_eliminar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_eliminar.setBorderPainted(false);
        btn_eliminar.addActionListener(this::btn_eliminarActionPerformed);

        btn_nuevo.setBackground(new java.awt.Color(153, 102, 0));
        btn_nuevo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_nuevo.setText("Nuevo");
        btn_nuevo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_nuevo.setBorderPainted(false);
        btn_nuevo.addActionListener(this::btn_nuevoActionPerformed);

        btn_volver.setBackground(new java.awt.Color(153, 102, 0));
        btn_volver.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_volver.setForeground(new java.awt.Color(153, 102, 0));
        btn_volver.setText("Volver");
        btn_volver.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_volver.setContentAreaFilled(false);
        btn_volver.addActionListener(this::btn_volverActionPerformed);

        btn_guardar.setBackground(new java.awt.Color(153, 102, 0));
        btn_guardar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_guardar.setBorderPainted(false);
        btn_guardar.addActionListener(this::btn_guardarActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133)
                        .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133)
                        .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        Principal ventanaPrincipal = new Principal();
        ventanaPrincipal.setExtendedState(MAXIMIZED_BOTH);
        ventanaPrincipal.setLocationRelativeTo(null);
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        limpiarFormulario();
        cargarTablaServicios(listarServiciosDesdeBD(""));
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        String nombre = txt_nombre.getText().trim();
        String precioStr = txt_precio.getText().trim().replace(",", ".");
        String descripcion = txt_descipcion.getText().trim();
        String estadoTexto = cbb_estado.getSelectedItem() != null ? cbb_estado.getSelectedItem().toString().trim() : "";

        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Nombre del servicio no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_nombre.requestFocus();
            return;
        }
        if (precioStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Precio base no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_precio.requestFocus();
            return;
        }
        if (descripcion.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Descripción no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_descipcion.requestFocus();
            return;
        }
        if (estadoTexto.isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecciona un Estado válido.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            cbb_estado.requestFocus();
            return;
        }

        double precioBase;
        try {
            precioBase = Double.parseDouble(precioStr);
            if (precioBase < 0) {
                throw new NumberFormatException("Precio negativo no permitido");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un precio base numérico válido (0 o mayor).", "Precio inválido", JOptionPane.WARNING_MESSAGE);
            txt_precio.requestFocus();
            return;
        }

        boolean activo = "Activo".equalsIgnoreCase(estadoTexto) || "ACTIVO".equalsIgnoreCase(estadoTexto);
        String precioFormateado = String.format(java.util.Locale.US, "%.2f", precioBase);

        if (esIdAutomatico()) {
            String sqlInsert = "INSERT INTO servicio (NOMBRE, DESCRIPCION, ACTIVO) VALUES (?, ?, ?)";
            try (Connection cn = obtenerConexion();
                 PreparedStatement ps = cn.prepareStatement(sqlInsert)) {
                ps.setString(1, nombre);
                ps.setString(2, descripcion);
                ps.setBoolean(3, activo);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Servicio registrado correctamente.");
                limpiarFormulario();
                cargarTablaServicios(listarServiciosDesdeBD(""));
                if (tbl_detalle.getRowCount() > 0) {
                    int ultimaFila = tbl_detalle.getRowCount() - 1;
                    tbl_detalle.setValueAt(precioFormateado, ultimaFila, 2);
                }
            } catch (SQLException ex) {
                logger.severe("Error al registrar servicio: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el servicio.\nDetalle: " + ex.getMessage(),
                        "Error al guardar", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                int idActual = Integer.parseInt(txt_id.getText().trim());
                String sqlUpdate = "UPDATE servicio SET NOMBRE = ?, DESCRIPCION = ?, ACTIVO = ? WHERE ID_SERVICIO = ?";
                try (Connection cn = obtenerConexion();
                     PreparedStatement ps = cn.prepareStatement(sqlUpdate)) {
                    ps.setString(1, nombre);
                    ps.setString(2, descripcion);
                    ps.setBoolean(3, activo);
                    ps.setInt(4, idActual);

                    int filas = ps.executeUpdate();
                    if (filas > 0) {
                        JOptionPane.showMessageDialog(this, "Servicio actualizado correctamente.");
                        limpiarFormulario();
                        cargarTablaServicios(listarServiciosDesdeBD(""));
                        seleccionarFilaPorId(idActual);
                        int filaSel = tbl_detalle.getSelectedRow();
                        if (filaSel >= 0) {
                            tbl_detalle.setValueAt(precioFormateado, filaSel, 2);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se encontró el servicio con ID: " + idActual,
                                "No encontrado", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de servicio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                logger.severe("Error al actualizar servicio: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el servicio.\nDetalle: " + ex.getMessage(),
                        "Error al actualizar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        Integer idAEliminar = null;

        if (!esIdAutomatico()) {
            try {
                idAEliminar = Integer.parseInt(txt_id.getText().trim());
            } catch (NumberFormatException ignored) {
            }
        } else {
            int fila = tbl_detalle.getSelectedRow();
            if (fila >= 0) {
                Object valId = tbl_detalle.getValueAt(fila, 0);
                if (valId != null) {
                    idAEliminar = Integer.parseInt(valId.toString());
                }
            }
        }

        if (idAEliminar == null) {
            String entrada = JOptionPane.showInputDialog(
                    this,
                    "Ingresa el ID del servicio a eliminar:",
                    "Eliminar Servicio",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (entrada == null || entrada.trim().isEmpty()) {
                return;
            }
            try {
                idAEliminar = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar el servicio con ID " + idAEliminar + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM servicio WHERE ID_SERVICIO = ?";
            try (Connection cn = obtenerConexion();
                 PreparedStatement ps = cn.prepareStatement(sqlDelete)) {
                ps.setInt(1, idAEliminar);
                int filas = ps.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(this, "Servicio eliminado correctamente.");
                    limpiarFormulario();
                    cargarTablaServicios(listarServiciosDesdeBD(""));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No existe un servicio con el ID: " + idAEliminar,
                            "No encontrado", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                logger.severe("Error al eliminar servicio: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el servicio (puede estar asociado a una orden/detalle).\nDetalle: " + ex.getMessage(),
                        "Error al eliminar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        String[] opciones = {"ID", "Nombre"};
        int seleccion = JOptionPane.showOptionDialog(
                this,
                "¿Por qué campo deseas buscar el servicio?",
                "Buscar Servicio",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion < 0) {
            return;
        }

        String criterio = opciones[seleccion];
        String valor = JOptionPane.showInputDialog(
                this,
                "Ingresa el " + criterio.toLowerCase() + " a buscar:",
                "Buscar por " + criterio,
                JOptionPane.QUESTION_MESSAGE
        );

        if (valor == null || valor.trim().isEmpty()) {
            return;
        }

        String termino = valor.trim();
        String sql = "ID".equals(criterio)
                ? "SELECT ID_SERVICIO, NOMBRE, DESCRIPCION, ACTIVO FROM servicio WHERE ID_SERVICIO = ?"
                : "SELECT ID_SERVICIO, NOMBRE, DESCRIPCION, ACTIVO FROM servicio WHERE NOMBRE LIKE ? ORDER BY ID_SERVICIO ASC";

        List<Object[]> resultados = new ArrayList<>();
        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            if ("ID".equals(criterio)) {
                ps.setInt(1, Integer.parseInt(termino));
            } else {
                ps.setString(1, "%" + termino + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    boolean activo = rs.getBoolean("ACTIVO");
                    resultados.add(new Object[]{
                        rs.getInt("ID_SERVICIO"),
                        rs.getString("NOMBRE"),
                        "0.00",
                        activo ? "Activo" : "Inactivo",
                        rs.getString("DESCRIPCION")
                    });
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (SQLException ex) {
            logger.severe("Error al buscar servicio: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al buscar servicio: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron servicios con ese criterio.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        } else {
            cargarTablaServicios(resultados);
            Object[] primero = resultados.get(0);
            txt_id.setText(String.valueOf(primero[0]));
            txt_nombre.setText(String.valueOf(primero[1]));
            txt_precio.setText(String.valueOf(primero[2]));
            cbb_estado.setSelectedItem(String.valueOf(primero[3]));
            txt_descipcion.setText(String.valueOf(primero[4]));
            seleccionarFilaPorId(Integer.parseInt(primero[0].toString()));
        }
    }//GEN-LAST:event_btn_buscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        java.awt.EventQueue.invokeLater(() -> new Servicio().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_volver;
    private javax.swing.JComboBox<String> cbb_estado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_detalle;
    private javax.swing.JTextField txt_descipcion;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_precio;
    // End of variables declaration//GEN-END:variables
}
