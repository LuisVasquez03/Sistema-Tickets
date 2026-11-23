package Vista;

import Estructura.Cola_Prioridad;
import Estructura.Pila_historial;
import Modelo.Ticket;
import Servicio.Ticket_Servicio;
import Util.ContextoAplicacion_ArchivoTemporal;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Cola_Atencion extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Cola_Atencion.class.getName());

    private final Cola_Prioridad colaAtencion;
    private final Ticket_Servicio ticketServicio;
    private final Pila_historial pilaHistorial;
    private DefaultTableModel modeloTabla;

    public Cola_Atencion() {
        initComponents();

        getContentPane().setBackground(new java.awt.Color(153, 102, 0));
        lblCodigoBuscar.setText("Código del Ticket: ");

        colaAtencion = ContextoAplicacion_ArchivoTemporal.getColaAtencion();
        ticketServicio = ContextoAplicacion_ArchivoTemporal.getTicketServicio();
        pilaHistorial = ContextoAplicacion_ArchivoTemporal.getPilaHistorial();

        configurarVentana();
        configurarTabla();
        sincronizarDesdeBD();
        actualizarTabla();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblSiguiente = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCola = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblCodigoBuscar = new javax.swing.JLabel();
        txtCodigoBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnIniciarAtencion = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnVerSiguiente = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btn_volver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(153, 102, 0));
        lblTitulo.setText("   CAR WASH - COLA DE ATENCIÓN FIFO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(lblTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTitulo)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        lblSiguiente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSiguiente.setForeground(new java.awt.Color(153, 102, 0));
        lblSiguiente.setText("Siguiente: No hay Tickets");

        lblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCantidad.setForeground(new java.awt.Color(153, 102, 0));
        lblCantidad.setText(" Tickets pendientes: 0    ");

        tblCola.setBackground(new java.awt.Color(153, 102, 0));
        tblCola.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Posición", "Nro Ticket", "ID Cliente", "ID Vehículo", "ID Trabajador", "Estado"
            }
        ));
        jScrollPane1.setViewportView(tblCola);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblCantidad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSiguiente)
                .addGap(88, 88, 88))
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCantidad)
                    .addComponent(lblSiguiente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 102, 0));
        jLabel4.setText("Buscar dentro de la cola:");

        lblCodigoBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCodigoBuscar.setForeground(new java.awt.Color(153, 102, 0));
        lblCodigoBuscar.setText("Código del Tiket: ");

        txtCodigoBuscar.addActionListener(this::txtCodigoBuscarActionPerformed);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblCodigoBuscar)
                        .addGap(38, 38, 38)
                        .addComponent(txtCodigoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigoBuscar)
                    .addComponent(txtCodigoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        btnIniciarAtencion.setText("Iniciar Atención");
        btnIniciarAtencion.addActionListener(this::btnIniciarAtencionActionPerformed);

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        btnVerSiguiente.setText("ver siguiente");
        btnVerSiguiente.addActionListener(this::btnVerSiguienteActionPerformed);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(this::btnCerrarActionPerformed);

        btn_volver.setText("Volver");
        btn_volver.addActionListener(this::btn_volverActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnVerSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnIniciarAtencion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciarAtencion)
                    .addComponent(btnVerSiguiente)
                    .addComponent(btnActualizar)
                    .addComponent(btnCerrar)
                    .addComponent(btn_volver))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerSiguienteActionPerformed
        mostrarSiguiente();        
    }//GEN-LAST:event_btnVerSiguienteActionPerformed

    private void btnIniciarAtencionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarAtencionActionPerformed
        iniciarAtencion();        
    }//GEN-LAST:event_btnIniciarAtencionActionPerformed

    private void txtCodigoBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBuscarActionPerformed
        buscarTicket();
    }//GEN-LAST:event_txtCodigoBuscarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        sincronizarDesdeBD();
        actualizarTabla();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarTicket();        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        cerrarVentana();        
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        volverAPrincipal();
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed
   
public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(() -> new Cola_Atencion().setVisible(true));
    }

    private void configurarVentana() {
        setTitle("Car Wash - Cola de atención FIFO");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "Posición",
                    "Nro Ticket",
                    "ID Cliente",
                    "ID Vehículo",
                    "ID Trabajador",
                    "Estado"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tblCola.setModel(modeloTabla);
        tblCola.setRowHeight(27);
        tblCola.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCola.getTableHeader().setReorderingAllowed(false);
    }

    private void sincronizarDesdeBD() {
        if (ticketServicio == null || colaAtencion == null) {
            return;
        }
        List<Ticket> pendientesBD = ticketServicio.obtenerTicketsPorEstadoDesdeBD("REGISTRADO");
        if (pendientesBD != null) {
            for (Ticket t : pendientesBD) {
                if (t != null
                        && t.getNroTicket() != null
                        && !t.getNroTicket().isBlank()
                        && "REGISTRADO".equalsIgnoreCase(t.getEstado())
                        && !colaAtencion.existeCodigo(t.getNroTicket())) {
                    colaAtencion.encolar(t);
                }
            }
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);

        if (colaAtencion == null) {
            actualizarResumen();
            return;
        }

        Ticket[] tickets = colaAtencion.obtenerTodos();
        if (tickets != null) {
            for (int posicion = 0; posicion < tickets.length; posicion++) {
                Ticket ticket = tickets[posicion];
                if (ticket == null) {
                    continue;
                }

                modeloTabla.addRow(new Object[]{
                    posicion + 1,
                    mostrarTexto(ticket.getNroTicket()),
                    ticket.getIdCliente(),
                    ticket.getIdVehiculo(),
                    ticket.getIdTrabajador(),
                    mostrarTexto(ticket.getEstado())
                });
            }
        }

        actualizarResumen();
    }

    private void actualizarResumen() {
        int cantidad = (colaAtencion != null) ? colaAtencion.getTamano() : 0;
        lblCantidad.setText("Tickets pendientes: " + cantidad);

        Ticket siguiente = (colaAtencion != null) ? colaAtencion.verSiguiente() : null;

        if (siguiente == null || siguiente.getNroTicket() == null) {
            lblSiguiente.setText("Siguiente: no hay tickets");
        } else {
            lblSiguiente.setText("Siguiente: " + siguiente.getNroTicket());
        }

        boolean existenTickets = colaAtencion != null && !colaAtencion.estaVacia();

        btnVerSiguiente.setEnabled(existenTickets);
        btnIniciarAtencion.setEnabled(existenTickets);
    }

    private void mostrarSiguiente() {
        if (colaAtencion == null) {
            return;
        }
        Ticket siguiente = colaAtencion.verSiguiente();

        if (siguiente == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No existen tickets pendientes.",
                    "Cola vacía",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String detalle = "Número de ticket: " + mostrarTexto(siguiente.getNroTicket())
                + "\nID Cliente: " + siguiente.getIdCliente()
                + "\nID Vehículo: " + siguiente.getIdVehiculo()
                + "\nID Trabajador: " + siguiente.getIdTrabajador()
                + "\nEstado: " + mostrarTexto(siguiente.getEstado());

        JOptionPane.showMessageDialog(
                this,
                detalle,
                "Siguiente ticket",
                JOptionPane.INFORMATION_MESSAGE
        );

        seleccionarFila(siguiente.getNroTicket());
    }

    private void iniciarAtencion() {
        if (colaAtencion == null || ticketServicio == null) {
            return;
        }
        Ticket siguiente = colaAtencion.verSiguiente();

        if (siguiente == null || siguiente.getNroTicket() == null || siguiente.getNroTicket().isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "La cola de atención está vacía.",
                    "Sin tickets",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String codigoObjetivo = siguiente.getNroTicket().trim();

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea iniciar la atención del ticket " + codigoObjetivo + "?",
                "Confirmar atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean estadoActualizado = ticketServicio.iniciarAtencion(codigoObjetivo);

        if (!estadoActualizado) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cambiar el estado del ticket " + codigoObjetivo + ".",
                    "Operación rechazada",
                    JOptionPane.WARNING_MESSAGE
            );
            sincronizarDesdeBD();
            actualizarTabla();
            return;
        }

        Ticket atendido = colaAtencion.desencolar();
        String codigoAtendido = (atendido != null && atendido.getNroTicket() != null && !atendido.getNroTicket().isBlank())
                ? atendido.getNroTicket().trim()
                : codigoObjetivo;

        if (pilaHistorial != null) {
            pilaHistorial.apilar("El ticket " + codigoAtendido + " inició su atención.");
        }

        JOptionPane.showMessageDialog(
                this,
                "El ticket " + codigoAtendido + " pasó a estado En proceso.",
                "Atención iniciada",
                JOptionPane.INFORMATION_MESSAGE
        );

        actualizarTabla();
    }

    private void buscarTicket() {
        if (colaAtencion == null) {
            return;
        }
        String nroTicket = txtCodigoBuscar.getText().trim();

        if (nroTicket.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el número del ticket.",
                    "Número obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );
            txtCodigoBuscar.requestFocus();
            return;
        }

        Ticket ticket = colaAtencion.buscarPorCodigo(nroTicket);

        if (ticket == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "El ticket no está dentro de la cola.",
                    "Ticket no encontrado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        seleccionarFila(ticket.getNroTicket());

        JOptionPane.showMessageDialog(
                this,
                "Ticket encontrado.\n"
                + "Número: " + mostrarTexto(ticket.getNroTicket())
                + "\nID Cliente: " + ticket.getIdCliente()
                + "\nID Vehículo: " + ticket.getIdVehiculo()
                + "\nID Trabajador: " + ticket.getIdTrabajador()
                + "\nEstado: " + mostrarTexto(ticket.getEstado()),
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void seleccionarFila(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return;
        }
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            String codigoTabla = String.valueOf(modeloTabla.getValueAt(fila, 1));
            if (codigoTabla.equalsIgnoreCase(codigo.trim())) {
                tblCola.setRowSelectionInterval(fila, fila);
                tblCola.scrollRectToVisible(tblCola.getCellRect(fila, 1, true));
                return;
            }
        }
    }

    private String mostrarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return "No especificado";
        }
        return valor;
    }

    private void volverAPrincipal() {
        for (java.awt.Window w : java.awt.Window.getWindows()) {
            if (w instanceof Principal principalExistente && w.isDisplayable()) {
                principalExistente.setExtendedState(MAXIMIZED_BOTH);
                principalExistente.setLocationRelativeTo(null);
                principalExistente.setVisible(true);
                principalExistente.toFront();
                return;
            }
        }
        Principal principal = new Principal();
        principal.setExtendedState(MAXIMIZED_BOTH);
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
    }

    private void cerrarVentana() {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar la cola de atención?",
                "Confirmar cierre",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            volverAPrincipal();
            dispose();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnIniciarAtencion;
    private javax.swing.JButton btnVerSiguiente;
    private javax.swing.JButton btn_volver;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCodigoBuscar;
    private javax.swing.JLabel lblSiguiente;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblCola;
    private javax.swing.JTextField txtCodigoBuscar;
    // End of variables declaration//GEN-END:variables
}
