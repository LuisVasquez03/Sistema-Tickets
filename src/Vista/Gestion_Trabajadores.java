package Vista;

import Modelo.Trabajador;
import Servicio.Trabajador_servicio;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luisv
 */
public class Gestion_Trabajadores extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Gestion_Trabajadores.class.getName());
    private final Trabajador_servicio trabajadorServicio = new Trabajador_servicio();

    /**
     * Creates new form Gestion_Trabajadores
     */
    public Gestion_Trabajadores() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        configurarEventosTabla();
        limpiarFormulario();
        cargarTablaTrabajadores(trabajadorServicio.listarTodos());
    }

    private void configurarEventosTabla() {
        tbl_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tbl_detalle.getSelectedRow();
                if (fila >= 0) {
                    Object valId = tbl_detalle.getValueAt(fila, 0);
                    if (valId != null) {
                        try {
                            int id = Integer.parseInt(valId.toString());
                            Trabajador t = trabajadorServicio.buscarPorId(id);
                            if (t != null) {
                                cargarEnFormulario(t);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        });
    }

    private void cargarTablaTrabajadores(List<Trabajador> lista) {
        DefaultTableModel modelo = (DefaultTableModel) tbl_detalle.getModel();
        modelo.setRowCount(0);

        if (lista == null) {
            return;
        }

        for (Trabajador t : lista) {
            modelo.addRow(new Object[]{
                t.getIdTrabajador(),
                t.getPrimerNombre(),
                t.getPrimerApellido(),
                t.getCodigoPais(),
                t.getCelular(),
                t.getCargo(),
                t.getUsuario(),
                t.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void limpiarFormulario() {
        txt_id.setText("AUTOMATICO");
        txt_id.setEditable(false);
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_pais.setText("+51");
        txt_celular.setText("");
        txt_usuario.setText("");
        txt_clave.setText("");
        if (cbb_cargo.getItemCount() > 0) {
            cbb_cargo.setSelectedIndex(0);
        }
        cbb_estado.setSelectedItem("Activo");
        tbl_detalle.clearSelection();
        txt_nombre.requestFocus();
    }

    private void cargarEnFormulario(Trabajador t) {
        txt_id.setText(String.valueOf(t.getIdTrabajador()));
        txt_id.setEditable(false);
        txt_nombre.setText(t.getPrimerNombre() != null ? t.getPrimerNombre() : "");
        txt_apellido.setText(t.getPrimerApellido() != null ? t.getPrimerApellido() : "");
        txt_pais.setText(t.getCodigoPais() != null ? t.getCodigoPais() : "+51");
        txt_celular.setText(t.getCelular() != null ? t.getCelular() : "");
        txt_usuario.setText(t.getUsuario() != null ? t.getUsuario() : "");
        txt_clave.setText("");
        if (t.getCargo() != null) {
            cbb_cargo.setSelectedItem(t.getCargo());
        }
        cbb_estado.setSelectedItem(t.isActivo() ? "Activo" : "Inactivo");
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        txt_nombre = new javax.swing.JTextField();
        txt_apellido = new javax.swing.JTextField();
        txt_pais = new javax.swing.JTextField();
        txt_celular = new javax.swing.JTextField();
        txt_usuario = new javax.swing.JTextField();
        txt_clave = new javax.swing.JTextField();
        cbb_estado = new javax.swing.JComboBox<>();
        cbb_cargo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
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
        jLabel1.setText("GESTIÓN DE TRABAJADORES");
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
        jLabel3.setText("ID TRABAJADOR:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 102, 0));
        jLabel4.setText("Apellido:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 102, 0));
        jLabel5.setText("Nombre:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 102, 0));
        jLabel6.setText("Cod. país:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 102, 0));
        jLabel7.setText("Celular:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 102, 0));
        jLabel8.setText("Cargo:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 102, 0));
        jLabel9.setText("Usuario:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 102, 0));
        jLabel10.setText("Clave:");

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

        txt_apellido.setBackground(new java.awt.Color(18, 17, 17));
        txt_apellido.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_apellido.setForeground(new java.awt.Color(153, 102, 0));
        txt_apellido.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        txt_pais.setBackground(new java.awt.Color(18, 17, 17));
        txt_pais.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_pais.setForeground(new java.awt.Color(153, 102, 0));
        txt_pais.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_pais.setText("+51");
        txt_pais.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        txt_celular.setBackground(new java.awt.Color(18, 17, 17));
        txt_celular.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_celular.setForeground(new java.awt.Color(153, 102, 0));
        txt_celular.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        txt_usuario.setBackground(new java.awt.Color(18, 17, 17));
        txt_usuario.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_usuario.setForeground(new java.awt.Color(153, 102, 0));
        txt_usuario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        txt_clave.setBackground(new java.awt.Color(18, 17, 17));
        txt_clave.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_clave.setForeground(new java.awt.Color(153, 102, 0));
        txt_clave.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 1, true));

        cbb_estado.setBackground(new java.awt.Color(18, 17, 17));
        cbb_estado.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbb_estado.setForeground(new java.awt.Color(153, 102, 0));
        cbb_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));
        cbb_estado.setBorder(null);

        cbb_cargo.setBackground(new java.awt.Color(18, 17, 17));
        cbb_cargo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbb_cargo.setForeground(new java.awt.Color(153, 102, 0));
        cbb_cargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Recepcionista", "Lavador", "Detailer", "Supervisor" }));
        cbb_cargo.setBorder(null);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 102, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("DETALLE DEL TRABAJADOR");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_clave, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbb_cargo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_pais, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 17, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(67, Short.MAX_VALUE)
                    .addComponent(jLabel12)
                    .addGap(47, 47, 47)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_pais, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbb_cargo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txt_clave, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel12)
                    .addContainerGap(458, Short.MAX_VALUE)))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        jPanel4.setPreferredSize(new java.awt.Dimension(700, 4));

        tbl_detalle.setBackground(new java.awt.Color(18, 17, 17));
        tbl_detalle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 102, 0)));
        tbl_detalle.setForeground(new java.awt.Color(153, 102, 0));
        tbl_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Apellido", "País", "Celular", "Cargo", "Usuario", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
        jLabel2.setText("LISTADO DE TRABAJADORES");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        Principal principal = new Principal();
        principal.setExtendedState(MAXIMIZED_BOTH);
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        limpiarFormulario();
        cargarTablaTrabajadores(trabajadorServicio.listarTodos());
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        String nombre = txt_nombre.getText().trim();
        String apellido = txt_apellido.getText().trim();
        String pais = txt_pais.getText().trim();
        String celular = txt_celular.getText().trim();
        String cargo = cbb_cargo.getSelectedItem() != null ? cbb_cargo.getSelectedItem().toString().trim() : "";
        String estadoTexto = cbb_estado.getSelectedItem() != null ? cbb_estado.getSelectedItem().toString().trim() : "";
        String usuario = txt_usuario.getText().trim();
        String clave = txt_clave.getText().trim();

        boolean esNuevo = esIdAutomatico();

        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Nombre no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_nombre.requestFocus();
            return;
        }
        if (apellido.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Apellido no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_apellido.requestFocus();
            return;
        }
        if (pais.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Cód. país no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_pais.requestFocus();
            return;
        }
        if (celular.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Celular no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_celular.requestFocus();
            return;
        }
        if (cargo.isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecciona un Cargo válido.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            cbb_cargo.requestFocus();
            return;
        }
        if (usuario.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Usuario no puede estar vacío.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_usuario.requestFocus();
            return;
        }
        if (esNuevo && clave.isBlank()) {
            JOptionPane.showMessageDialog(this, "El campo Clave no puede estar vacío para un nuevo trabajador.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txt_clave.requestFocus();
            return;
        }
        if (estadoTexto.isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecciona un Estado válido.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            cbb_estado.requestFocus();
            return;
        }

        boolean activo = "Activo".equalsIgnoreCase(estadoTexto);

        Trabajador t = new Trabajador();
        t.setPrimerNombre(nombre);
        t.setPrimerApellido(apellido);
        t.setCodigoPais(pais);
        t.setCelular(celular);
        t.setCargo(cargo);
        t.setUsuario(usuario);
        t.setActivo(activo);

        if (esNuevo) {
            t.setClaveHash(clave);
            if (trabajadorServicio.insertar(t)) {
                JOptionPane.showMessageDialog(this, "Trabajador registrado correctamente.");
                limpiarFormulario();
                cargarTablaTrabajadores(trabajadorServicio.listarTodos());
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el trabajador. Verifica los datos o si el usuario ya existe.",
                        "Error al guardar",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            try {
                int idActual = Integer.parseInt(txt_id.getText().trim());
                Trabajador actual = trabajadorServicio.buscarPorId(idActual);
                if (actual == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró el trabajador con ID: " + idActual, "No encontrado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                t.setIdTrabajador(idActual);
                t.setClaveHash(!clave.isBlank() ? clave : actual.getClaveHash());

                if (trabajadorServicio.actualizar(t)) {
                    JOptionPane.showMessageDialog(this, "Trabajador actualizado correctamente.");
                    limpiarFormulario();
                    cargarTablaTrabajadores(trabajadorServicio.listarTodos());
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo actualizar el trabajador con ID: " + idActual,
                            "Error al actualizar",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de trabajador inválido.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    "Ingresa el ID del trabajador a eliminar:",
                    "Eliminar Trabajador",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (entrada == null || entrada.trim().isEmpty()) {
                return;
            }
            try {
                idAEliminar = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser un número entero válido.",
                        "Dato inválido",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        Trabajador existente = trabajadorServicio.buscarPorId(idAEliminar);
        if (existente == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No existe un trabajador con el ID: " + idAEliminar,
                    "No encontrado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar a " + existente.getPrimerNombre() + " " + existente.getPrimerApellido() + " (ID " + idAEliminar + ")?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (trabajadorServicio.eliminar(idAEliminar)) {
                JOptionPane.showMessageDialog(this, "Trabajador eliminado correctamente.");
                limpiarFormulario();
                cargarTablaTrabajadores(trabajadorServicio.listarTodos());
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar el trabajador. Es posible que tenga registros asociados.",
                        "Error al eliminar",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        String[] opciones = {"ID", "Nombre", "Apellido"};
        int seleccion = JOptionPane.showOptionDialog(
                this,
                "¿Por qué campo deseas buscar al trabajador?",
                "Buscar Trabajador",
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

        if ("ID".equals(criterio)) {
            try {
                int idBuscado = Integer.parseInt(termino);
                Trabajador encontrado = trabajadorServicio.buscarPorId(idBuscado);
                if (encontrado != null) {
                    cargarTablaTrabajadores(trabajadorServicio.listarTodos());
                    cargarEnFormulario(encontrado);
                    seleccionarFilaPorId(encontrado.getIdTrabajador());
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No existe un trabajador con el ID: " + idBuscado,
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser un número entero válido.",
                        "Dato inválido",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } else if ("Nombre".equals(criterio)) {
            List<Trabajador> resultados = trabajadorServicio.buscarPorNombre(termino);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No existe un trabajador con el nombre: " + termino,
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                cargarTablaTrabajadores(resultados);
                cargarEnFormulario(resultados.get(0));
                seleccionarFilaPorId(resultados.get(0).getIdTrabajador());
            }
        } else {
            List<Trabajador> resultados = trabajadorServicio.buscarPorApellido(termino);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No existe un trabajador con el apellido: " + termino,
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                cargarTablaTrabajadores(resultados);
                cargarEnFormulario(resultados.get(0));
                seleccionarFilaPorId(resultados.get(0).getIdTrabajador());
            }
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
        java.awt.EventQueue.invokeLater(() -> new Gestion_Trabajadores().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_volver;
    private javax.swing.JComboBox<String> cbb_cargo;
    private javax.swing.JComboBox<String> cbb_estado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_detalle;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_celular;
    private javax.swing.JTextField txt_clave;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_pais;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
