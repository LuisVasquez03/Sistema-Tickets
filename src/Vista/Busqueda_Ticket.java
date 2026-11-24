package Vista;

import Estructura.Arbol_AVL;
import Estructura.Pila_historial;
import Modelo.Ticket;
import Servicio.Ticket_Servicio;
import Util.ContextoAplicacion_ArchivoTemporal;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luisv
 */
public class Busqueda_Ticket extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Busqueda_Ticket.class.getName());

    private final Ticket_Servicio ticketServicio = new Ticket_Servicio();
    private final Arbol_AVL arbolAVL = new Arbol_AVL();
    private List<Ticket> ticketsCargados = new ArrayList<>();

    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter[] FORMATOS_FILTRO_FECHA = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    };

    /**
     * Creates new form Busqueda_Ticket
     */
    public Busqueda_Ticket() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        configurarVista();
        cargarTicketsDesdeBD();
    }

    private void configurarVista() {
        txt_detalles.setEditable(false);
        tbl_busqueda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbl_busqueda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleFilaSeleccionada();
            }
        });
    }

    private int extraerNumeroTicket(String nroTicket) {
        if (nroTicket == null || nroTicket.isBlank()) {
            return 0;
        }
        String digitos = nroTicket.replaceAll("\\D+", "");
        if (digitos.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(digitos);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private int obtenerSiguienteNumeroTicket() {
        int maxNumero = 0;
        for (Ticket t : ticketsCargados) {
            int num = extraerNumeroTicket(t.getNroTicket());
            if (num > maxNumero) {
                maxNumero = num;
            }
        }
        return maxNumero + 1;
    }

    private String generarSiguienteCodigoTicket() {
        return formatearTicketVisual(obtenerSiguienteNumeroTicket());
    }

    private String formatearTicketVisual(int numero) {
        int seguro = Math.max(0, numero);
        return String.format("TCK - %03d", seguro);
    }

    private String formatearTicketVisual(Ticket ticket) {
        if (ticket == null) {
            return "TCK - 000";
        }
        int numero = extraerNumeroTicket(ticket.getNroTicket());
        if (numero > 0) {
            return formatearTicketVisual(numero);
        }
        if (ticket.getIdOrden() > 0) {
            return formatearTicketVisual(ticket.getIdOrden());
        }
        return ticket.getNroTicket() != null && !ticket.getNroTicket().isBlank()
                ? ticket.getNroTicket()
                : "TCK - 000";
    }

    private void cargarTicketsDesdeBD() {
        ticketsCargados = ticketServicio.listarTodosDesdeBD();
        arbolAVL.limpiar();

        for (Ticket t : ticketsCargados) {
            if (t.getNroTicket() != null && !t.getNroTicket().isBlank()) {
                arbolAVL.insertar(t);
            }
        }

        llenarTabla(ticketsCargados);
        limpiarDetalles();
    }

    private void llenarTabla(List<Ticket> lista) {
        DefaultTableModel modelo = (DefaultTableModel) tbl_busqueda.getModel();
        modelo.setRowCount(0);

        for (Ticket t : lista) {
            String nroVisual = formatearTicketVisual(t);
            String fechaIngreso = t.getFechaIngreso() != null
                    ? t.getFechaIngreso().format(FORMATO_FECHA_HORA)
                    : "Sin fecha";
            String estado = (t.getEstado() != null && !t.getEstado().isBlank())
                    ? t.getEstado()
                    : "REGISTRADO";
            double total = t.getTotal() != null ? t.getTotal().doubleValue() : 0.0;

            modelo.addRow(new Object[]{
                nroVisual,
                String.valueOf(t.getIdCliente()),
                String.valueOf(t.getIdVehiculo()),
                String.valueOf(t.getIdTrabajador()),
                fechaIngreso,
                estado,
                total
            });
        }
    }

    private LocalDate parsearFechaFiltro(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        for (DateTimeFormatter fmt : FORMATOS_FILTRO_FECHA) {
            try {
                return LocalDate.parse(texto.trim(), fmt);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private Ticket obtenerTicketSeleccionado() {
        int filaVista = tbl_busqueda.getSelectedRow();
        if (filaVista < 0) {
            return null;
        }
        int filaModelo = tbl_busqueda.convertRowIndexToModel(filaVista);
        Object valorCelda = tbl_busqueda.getModel().getValueAt(filaModelo, 0);
        int numeroBuscado = extraerNumeroTicket(valorCelda != null ? valorCelda.toString() : "");

        for (Ticket t : ticketsCargados) {
            if (extraerNumeroTicket(t.getNroTicket()) == numeroBuscado && numeroBuscado > 0) {
                return t;
            }
        }
        if (filaModelo >= 0 && filaModelo < ticketsCargados.size()) {
            return ticketsCargados.get(filaModelo);
        }
        return null;
    }

    private void mostrarDetalleFilaSeleccionada() {
        Ticket t = obtenerTicketSeleccionado();
        if (t == null) {
            return;
        }

        String fechaTermino = t.getFechaTermino() != null
                ? t.getFechaTermino().format(FORMATO_FECHA_HORA)
                : "Pendiente";
        String subtotal = t.getSubtotal() != null ? String.format("%.2f", t.getSubtotal()) : "0.00";
        String descuento = t.getDescuentoMonto() != null ? String.format("%.2f", t.getDescuentoMonto()) : "0.00";
        String igv = t.getMontoIgv() != null ? String.format("%.2f", t.getMontoIgv()) : "0.00";
        String total = t.getTotal() != null ? String.format("%.2f", t.getTotal()) : "0.00";

        txt_detalles.setText(
                "\n   DETALLES DEL TICKET\n\n"
                + "   Nro. ticket: " + formatearTicketVisual(t) + "\n\n"
                + "   Fecha termino: " + fechaTermino + "\n\n"
                + "   Subtotal: S/. " + subtotal + "\n\n"
                + "   Descuento: S/. " + descuento + "\n\n"
                + "   IGV: S/. " + igv + "\n\n"
                + "   Total: S/. " + total
        );
    }

    private void limpiarDetalles() {
        String proximoCodigo = generarSiguienteCodigoTicket();
        txt_detalles.setText(
                "\n   DETALLES DEL TICKET\n  \n"
                + "   Nro. ticket: (Próximo: " + proximoCodigo + ")\n\n\n"
                + "   Fecha termino: -\n\n\n"
                + "   Subtotal: S/. 0.00\n\n\n"
                + "   Descuento: S/. 0.00\n\n\n"
                + "   IGV: S/. 0.00\n\n\n"
                + "   Total: S/. 0.00"
        );
    }

    private void mostrarVentanaRecorrido(String tipoRecorrido, Ticket[] tickets) {
        StringBuilder sb = new StringBuilder();
        if (tickets == null || tickets.length == 0) {
            sb.append("No hay tickets registrados en el árbol AVL.");
        } else {
            for (int i = 0; i < tickets.length; i++) {
                Ticket t = tickets[i];
                sb.append(formatearTicketVisual(t));
                if (t.getEstado() != null && !t.getEstado().isBlank()) {
                    sb.append(" [").append(t.getEstado()).append("]");
                }
                if (i < tickets.length - 1) {
                    sb.append("  ->  ");
                }
            }
        }

        JTextArea area = new JTextArea(sb.toString(), 6, 44);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(18, 17, 17));
        area.setForeground(new Color(218, 165, 32));

        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Recorrido AVL - " + tipoRecorrido,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_criterio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_desde = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_hasta = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbb_estado = new javax.swing.JComboBox<>();
        btn_buscar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_busqueda = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btn_inorder = new javax.swing.JButton();
        btn_preorder = new javax.swing.JButton();
        btn_postorder = new javax.swing.JButton();
        btn_historial = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        btn_volver = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_detalles = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 800));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1390, 966));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/lupa.png"))); // NOI18N
        jLabel1.setText("BÚSQUEDA TICKET");
        jLabel1.setPreferredSize(new java.awt.Dimension(37, 80));
        jPanel1.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jPanel3.setBackground(new java.awt.Color(18, 17, 17));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 102, 0));
        jLabel2.setText("CRITERIO DE BÚSQUEDA");

        txt_criterio.setBackground(new java.awt.Color(18, 17, 17));
        txt_criterio.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_criterio.setForeground(new java.awt.Color(153, 102, 0));
        txt_criterio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 102, 0));
        jLabel3.setText("Filtros avanzados");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 102, 0));
        jLabel4.setText("Desde:");

        txt_desde.setBackground(new java.awt.Color(18, 17, 17));
        txt_desde.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_desde.setForeground(new java.awt.Color(153, 102, 0));
        txt_desde.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 102, 0));
        jLabel5.setText("Hasta:");

        txt_hasta.setBackground(new java.awt.Color(18, 17, 17));
        txt_hasta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_hasta.setForeground(new java.awt.Color(153, 102, 0));
        txt_hasta.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 102, 0));
        jLabel6.setText("Estado");

        cbb_estado.setBackground(new java.awt.Color(18, 17, 17));
        cbb_estado.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbb_estado.setForeground(new java.awt.Color(153, 102, 0));
        cbb_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TODOS", "REGISTRADO", "EN PROCESO", "TERMINADO", "COBRADO" }));
        cbb_estado.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        btn_buscar.setBackground(new java.awt.Color(153, 102, 0));
        btn_buscar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_buscar.setText("BUSCAR");
        btn_buscar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_buscar.setBorderPainted(false);
        btn_buscar.addActionListener(this::btn_buscarActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txt_desde, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txt_hasta, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cbb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel2)
                    .addComponent(txt_criterio, javax.swing.GroupLayout.PREFERRED_SIZE, 948, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addGap(25, 25, 25)
                .addComponent(txt_criterio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txt_desde, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txt_hasta, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))))
        );

        jPanel4.setBackground(new java.awt.Color(18, 17, 17));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        jPanel4.setPreferredSize(new java.awt.Dimension(229, 375));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 102, 0));
        jLabel7.setText("Resultado de búsqueda");

        tbl_busqueda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nro. ticket", "ID cliente", "ID vehículo", "ID trabajador", "Fecha de ingreso", "Estado", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl_busqueda);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 983, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel7)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(18, 17, 17));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 102, 0));
        jLabel8.setText("Recorrido AVL/ acciones");

        btn_inorder.setBackground(new java.awt.Color(153, 102, 0));
        btn_inorder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_inorder.setText("INORDER");
        btn_inorder.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_inorder.setBorderPainted(false);
        btn_inorder.addActionListener(this::btn_inorderActionPerformed);

        btn_preorder.setBackground(new java.awt.Color(153, 102, 0));
        btn_preorder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_preorder.setText("PREORDER");
        btn_preorder.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_preorder.setBorderPainted(false);
        btn_preorder.addActionListener(this::btn_preorderActionPerformed);

        btn_postorder.setBackground(new java.awt.Color(153, 102, 0));
        btn_postorder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_postorder.setText("POSTORDER");
        btn_postorder.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_postorder.setBorderPainted(false);
        btn_postorder.addActionListener(this::btn_postorderActionPerformed);

        btn_historial.setBackground(new java.awt.Color(153, 102, 0));
        btn_historial.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_historial.setText("VER HISTORIAL");
        btn_historial.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_historial.setBorderPainted(false);
        btn_historial.addActionListener(this::btn_historialActionPerformed);

        btn_eliminar.setBackground(new java.awt.Color(153, 102, 0));
        btn_eliminar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_eliminar.setText("ELIMINAR");
        btn_eliminar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_eliminar.setBorderPainted(false);
        btn_eliminar.addActionListener(this::btn_eliminarActionPerformed);

        btn_volver.setBackground(new java.awt.Color(153, 102, 0));
        btn_volver.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_volver.setText("VOLVER");
        btn_volver.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btn_volver.setBorderPainted(false);
        btn_volver.addActionListener(this::btn_volverActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(btn_inorder, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btn_preorder, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btn_postorder, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btn_historial, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(46, 46, 46))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_inorder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_postorder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_preorder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btn_historial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        txt_detalles.setBackground(new java.awt.Color(18, 17, 17));
        txt_detalles.setColumns(20);
        txt_detalles.setForeground(new java.awt.Color(153, 102, 0));
        txt_detalles.setRows(5);
        txt_detalles.setText("\n   DETALLES DEL TICKET\n  \n   Nro. ticket:\n\n\n   Fecha termino:\n\n\n   Subtotal:\n\n\n   Descuento:\n\n\n   IGV:\n\n\n   Total:");
        txt_detalles.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        txt_detalles.setRequestFocusEnabled(false);
        txt_detalles.setSelectedTextColor(new java.awt.Color(153, 102, 0));
        txt_detalles.setSelectionColor(new java.awt.Color(153, 102, 0));
        jScrollPane2.setViewportView(txt_detalles);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(249, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        String criterio = txt_criterio.getText().trim().toLowerCase();
        String estadoSel = cbb_estado.getSelectedItem() != null
                ? cbb_estado.getSelectedItem().toString().trim()
                : "TODOS";
        LocalDate desde = parsearFechaFiltro(txt_desde.getText());
        LocalDate hasta = parsearFechaFiltro(txt_hasta.getText());

        List<Ticket> filtrados = new ArrayList<>();
        for (Ticket t : ticketsCargados) {
            String visual = formatearTicketVisual(t).toLowerCase();
            int num = extraerNumeroTicket(t.getNroTicket());
            String numStr = String.valueOf(num);
            String rawNro = t.getNroTicket() != null ? t.getNroTicket().toLowerCase() : "";

            boolean coincideTexto = criterio.isEmpty()
                    || visual.contains(criterio)
                    || rawNro.contains(criterio)
                    || (num > 0 && numStr.equals(criterio));

            String estadoTicket = t.getEstado() != null
                    ? t.getEstado().replace("_", " ").trim()
                    : "";
            String estadoSelNorm = estadoSel.replace("_", " ").trim();
            boolean coincideEstado = "TODOS".equalsIgnoreCase(estadoSelNorm)
                    || estadoTicket.equalsIgnoreCase(estadoSelNorm);

            LocalDate fechaIng = t.getFechaIngreso() != null ? t.getFechaIngreso().toLocalDate() : null;
            boolean coincideDesde = (desde == null) || (fechaIng != null && !fechaIng.isBefore(desde));
            boolean coincideHasta = (hasta == null) || (fechaIng != null && !fechaIng.isAfter(hasta));

            if (coincideTexto && coincideEstado && coincideDesde && coincideHasta) {
                filtrados.add(t);
            }
        }

        llenarTabla(filtrados);
        limpiarDetalles();
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void btn_inorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_inorderActionPerformed
        mostrarVentanaRecorrido("INORDER", arbolAVL.obtenerTodosEnOrden());
    }//GEN-LAST:event_btn_inorderActionPerformed

    private void btn_preorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_preorderActionPerformed
        mostrarVentanaRecorrido("PREORDER", arbolAVL.obtenerPreOrden());
    }//GEN-LAST:event_btn_preorderActionPerformed

    private void btn_postorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_postorderActionPerformed
        mostrarVentanaRecorrido("POSTORDER", arbolAVL.obtenerPostOrden());
    }//GEN-LAST:event_btn_postorderActionPerformed

    private void btn_historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_historialActionPerformed
        Pila_historial pilaCompartida = ContextoAplicacion_ArchivoTemporal.getPilaHistorial();
        if (pilaCompartida == null) {
            pilaCompartida = new Pila_historial();
        }
        Historial ventanaHistorial = new Historial(pilaCompartida, new Ticket_Servicio());
        ventanaHistorial.setExtendedState(MAXIMIZED_BOTH);
        ventanaHistorial.setLocationRelativeTo(null);
        ventanaHistorial.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_historialActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        Ticket seleccionado = obtenerTicketSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un ticket de la tabla para eliminar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String codigoVisual = formatearTicketVisual(seleccionado);
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar el ticket " + codigoVisual + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = ticketServicio.eliminarTicket(seleccionado.getNroTicket());
            if (eliminado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ticket " + codigoVisual + " eliminado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
                cargarTicketsDesdeBD();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar el ticket en la base de datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        Principal ventanaPrincipal = new Principal();
        ventanaPrincipal.setExtendedState(MAXIMIZED_BOTH);
        ventanaPrincipal.setLocationRelativeTo(null);
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Busqueda_Ticket().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_historial;
    private javax.swing.JButton btn_inorder;
    private javax.swing.JButton btn_postorder;
    private javax.swing.JButton btn_preorder;
    private javax.swing.JButton btn_volver;
    private javax.swing.JComboBox<String> cbb_estado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbl_busqueda;
    private javax.swing.JTextField txt_criterio;
    private javax.swing.JTextField txt_desde;
    private javax.swing.JTextArea txt_detalles;
    private javax.swing.JTextField txt_hasta;
    // End of variables declaration//GEN-END:variables
}
