package Vista;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luisv
 */
public class Registro_Tickets extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Registro_Tickets.class.getName());

    private static final double IGV_RATE = 0.18;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final List<ComboItem> listaTrabajadores = new ArrayList<>();
    private final List<ServicioItem> listaServicios = new ArrayList<>();
    private Integer idClienteActual = null;
    private Integer idVehiculoActual = null;

    /**
     * Creates new form Registro_Tickets
     */
    public Registro_Tickets() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        configurarFormulario();
        actualizarFechaCabecera();
    }

    private void configurarFormulario() {
        configurarTabla();
        cargarTrabajadoresDesdeBD();
        cargarServiciosDesdeBD();
        cargarEstadosDesdeBD();
        inicializarFechasYCorrelativo();
        configurarEventosCombos();
        actualizarResumenFinanciero();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tbl_detalle.getModel();
        modelo.setRowCount(0);
    }

    private void configurarEventosCombos() {
        cbb_servicio.addActionListener(e -> {
            ServicioItem seleccionado = getServicioSeleccionado();
            if (seleccionado != null) {
                txt_precioAplicado.setText(String.format(java.util.Locale.US, "%.2f", seleccionado.precioBase));
                if (txt_recargoServicios.getText().trim().isEmpty()) {
                    txt_recargoServicios.setText("0.00");
                }
            } else {
                txt_precioAplicado.setText("");
            }
        });
    }

    private void inicializarFechasYCorrelativo() {
        LocalDateTime ahora = LocalDateTime.now();
        txt_fechaIngreso.setText(ahora.format(FORMATO_FECHA));
        txt_fechaTermino.setText(ahora.plusMinutes(45).format(FORMATO_FECHA));
        obtenerSiguienteCodigoTicket();
    }

    private void obtenerSiguienteCodigoTicket() {
        String sql = "SELECT COALESCE(MAX(ID_ORDEN), 0) + 1 AS siguiente FROM orden_servicio";
        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int siguiente = rs.getInt("siguiente");
                lbl_ticket.setText(String.format("Ticket: TCK - %03d", siguiente));
            }
        } catch (SQLException ex) {
            logger.warning("No se pudo obtener correlativo de tickets: " + ex.getMessage());
            lbl_ticket.setText("Ticket: TCK - 001");
        }
    }

    private int resolverIdVehiculo(Connection cn) throws SQLException {
        if (idVehiculoActual != null && idVehiculoActual > 0) {
            return idVehiculoActual;
        }

        String placa = txt_placa.getText().trim().toUpperCase();
        String sqlBuscar = "SELECT ID_VEHICULO FROM vehiculo WHERE PLACA = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sqlBuscar)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("ID_VEHICULO");
                    idVehiculoActual = id;
                    return id;
                }
            }
        }

        int idCliente = (idClienteActual != null && idClienteActual > 0)
                ? idClienteActual
                : upsertClienteBasico(cn);

        String tipo = txt_tipoVehiculo.getText().trim().isEmpty()
                ? "AUTO"
                : txt_tipoVehiculo.getText().trim().toUpperCase();
        String marca = txt_marca.getText().trim();
        String modelo = txt_modelo.getText().trim();
        String color = txt_color.getText().trim();

        String sqlInsert = "INSERT INTO vehiculo (ID_CLIENTE, PLACA, TIPO_VEHICULO, MARCA, MODELO, COLOR) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCliente);
            ps.setString(2, placa);
            ps.setString(3, tipo);
            ps.setString(4, marca);
            ps.setString(5, modelo);
            ps.setString(6, color);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    idVehiculoActual = id;
                    return id;
                }
            }
        }
        throw new SQLException("No se pudo registrar el vehículo.");
    }

    private void cargarTrabajadoresDesdeBD() {
        listaTrabajadores.clear();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Seleccione trabajador --");

        String sql = "SELECT ID_TRABAJADOR, CONCAT(PRIMER_NOMBRE, ' ', PRIMER_APELLIDO) AS nombre_completo "
                + "FROM trabajador WHERE ACTIVO = 1 ORDER BY nombre_completo ASC";
        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ComboItem item = new ComboItem(rs.getInt("ID_TRABAJADOR"), rs.getString("nombre_completo"));
                listaTrabajadores.add(item);
                model.addElement(item.texto);
            }
        } catch (SQLException ex) {
            logger.severe("Error cargando trabajadores desde BD: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los trabajadores desde la base de datos.\nDetalle: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        cbb_trabajador.setModel(model);
    }

    private void cargarServiciosDesdeBD() {
        listaServicios.clear();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Seleccione servicio --");

        String sql = "SELECT ID_SERVICIO, NOMBRE FROM servicio WHERE ACTIVO = 1 ORDER BY NOMBRE ASC";
        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ServicioItem item = new ServicioItem(
                        rs.getInt("ID_SERVICIO"),
                        rs.getString("NOMBRE"),
                        0.0
                );
                listaServicios.add(item);
                model.addElement(item.nombre);
            }
        } catch (SQLException ex) {
            logger.severe("Error cargando servicios desde BD: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los servicios desde la base de datos.\nDetalle: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        cbb_servicio.setModel(model);
    }

    private void cargarEstadosDesdeBD() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Seleccione estado --");

        String sql = "SELECT nombre_estado FROM estados_ticket ORDER BY id_estado ASC";
        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addElement(rs.getString("nombre_estado"));
            }
        } catch (SQLException ex) {
            logger.warning("Tabla estados_ticket no encontrada, usando estados estándar del sistema: " + ex.getMessage());
            model.addElement("REGISTRADO");
            model.addElement("EN_PROCESO");
            model.addElement("TERMINADO");
            model.addElement("COBRADO");
            model.addElement("CANCELADO");
        }
        cbb_estado.setModel(model);
        cbb_estado.setSelectedItem("REGISTRADO");
    }

    private void buscarVehiculoPorPlaca() {
        String placa = txt_placa.getText().trim().toUpperCase();
        txt_placa.setText(placa);

        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese una placa para buscar el vehículo.",
                    "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            txt_placa.requestFocus();
            return;
        }

        String sql = "SELECT ID_VEHICULO, ID_CLIENTE, TIPO_VEHICULO, MARCA, MODELO, COLOR "
                + "FROM vehiculo WHERE PLACA = ? LIMIT 1";
        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idVehiculoActual = rs.getInt("ID_VEHICULO");
                    txt_tipoVehiculo.setText(rs.getString("TIPO_VEHICULO"));
                    txt_marca.setText(rs.getString("MARCA"));
                    txt_modelo.setText(rs.getString("MODELO"));
                    txt_color.setText(rs.getString("COLOR"));

                    int idCliVehiculo = rs.getInt("ID_CLIENTE");
                    if (idClienteActual == null) {
                        idClienteActual = idCliVehiculo;
                        lbl_IDcliente.setText("ID CLIENTE: " + idClienteActual);
                        cargarClientePorId(idClienteActual);
                    }
                } else {
                    idVehiculoActual = null;
                    txt_tipoVehiculo.setText("");
                    txt_marca.setText("");
                    txt_modelo.setText("");
                    txt_color.setText("");
                    JOptionPane.showMessageDialog(this,
                            "Vehículo no encontrado con placa: " + placa,
                            "Vehículo no encontrado", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            logger.severe("Error buscando vehículo: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al consultar vehículo en la BD: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarClientePorId(int idCli) {
        String sql = "SELECT DNI, RUC, PRIMER_NOMBRE, PRIMER_APELLIDO, RAZON_SOCIAL, CELULAR "
                + "FROM cliente WHERE ID_CLIENTE = ? LIMIT 1";
        try (Connection cn = obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCli);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dni = rs.getString("DNI");
                    String ruc = rs.getString("RUC");
                    txt_buscarCliente.setText((dni != null && !dni.isBlank()) ? dni : (ruc != null ? ruc : ""));

                    String razonSocial = rs.getString("RAZON_SOCIAL");
                    String nombreCompleto = ((rs.getString("PRIMER_NOMBRE") != null ? rs.getString("PRIMER_NOMBRE").trim() : "") + " "
                            + (rs.getString("PRIMER_APELLIDO") != null ? rs.getString("PRIMER_APELLIDO").trim() : "")).trim();

                    if (dni != null && !dni.isBlank() && !nombreCompleto.isBlank()) {
                        txt_nombre.setText(nombreCompleto);
                    } else if (razonSocial != null && !razonSocial.isBlank()) {
                        txt_nombre.setText(razonSocial);
                    } else {
                        txt_nombre.setText(nombreCompleto);
                    }
                    txt_celular.setText(rs.getString("CELULAR") != null ? rs.getString("CELULAR") : "");
                }
            }
        } catch (SQLException ex) {
            logger.warning("No se pudo autocompletar datos del cliente desde vehículo: " + ex.getMessage());
        }
    }

    private void agregarServicioATabla() {
        ServicioItem servicio = getServicioSeleccionado();
        if (servicio == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un servicio válido de la lista.",
                    "Servicio Requerido", JOptionPane.WARNING_MESSAGE);
            cbb_servicio.requestFocus();
            return;
        }

        String precioStr = txt_precioAplicado.getText().trim().replace(",", ".");
        String recargoStr = txt_recargoServicios.getText().trim().replace(",", ".");
        String motivo = txt_motivo.getText().trim();

        if (precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El campo 'Precio aplicado' no puede estar vacío.",
                    "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            txt_precioAplicado.requestFocus();
            return;
        }

        if (recargoStr.isEmpty()) {
            recargoStr = "0.00";
        }

        double precioAplicado;
        double recargo;
        try {
            precioAplicado = Double.parseDouble(precioStr);
            recargo = Double.parseDouble(recargoStr);
            if (precioAplicado < 0 || recargo < 0) {
                throw new NumberFormatException("Valores negativos no permitidos");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese montos numéricos válidos (mayores o iguales a 0) en Precio aplicado y Recargo.",
                    "Monto Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (recargo > 0 && motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe indicar el 'Motivo de recargo' cuando el recargo es mayor a 0.",
                    "Motivo Requerido", JOptionPane.WARNING_MESSAGE);
            txt_motivo.requestFocus();
            return;
        }

        if (motivo.isEmpty()) {
            motivo = "-";
        }

        double totalFila = precioAplicado + recargo;
        DefaultTableModel modelo = (DefaultTableModel) tbl_detalle.getModel();
        modelo.addRow(new Object[]{
            servicio.id,
            servicio.nombre,
            redondear(precioAplicado),
            redondear(recargo),
            motivo,
            redondear(totalFila)
        });

        txt_recargoServicios.setText("0.00");
        txt_motivo.setText("");
        actualizarResumenFinanciero();
    }

    private void actualizarResumenFinanciero() {
        DefaultTableModel modelo = (DefaultTableModel) tbl_detalle.getModel();
        double subtotal = 0.0;
        double recargoTotal = 0.0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            double precio = parseDoubleSeguro(modelo.getValueAt(i, 2));
            double recargo = parseDoubleSeguro(modelo.getValueAt(i, 3));
            subtotal += precio;
            recargoTotal += recargo;
        }

        double baseImponible = subtotal + recargoTotal;
        double descuento = 0.0;
        double igv = baseImponible * IGV_RATE;
        double totalPagar = baseImponible - descuento + igv;

        lbl_subtotal.setText(String.format(java.util.Locale.US, "S/. %.2f", subtotal));
        lbl_descuento.setText(String.format(java.util.Locale.US, "S/. %.2f", descuento));
        lbl_recargo.setText(String.format(java.util.Locale.US, "S/. %.2f", recargoTotal));
        lbl_igv.setText(String.format(java.util.Locale.US, "S/. %.2f", igv));
        lbl_total.setText(String.format(java.util.Locale.US, "S/. %.2f", totalPagar));
    }

    private boolean validarCamposObligatorios() {
        if (txt_buscarCliente.getText().trim().isEmpty()) {
            return errorCampo(txt_buscarCliente, "Debe ingresar el DNI o RUC del cliente.");
        }
        if (txt_nombre.getText().trim().isEmpty()) {
            return errorCampo(txt_nombre, "El campo Nombre / Razón social no puede estar vacío.");
        }
        if (txt_celular.getText().trim().isEmpty()) {
            return errorCampo(txt_celular, "El campo Celular no puede estar vacío.");
        }
        if (txt_placa.getText().trim().isEmpty()) {
            return errorCampo(txt_placa, "Debe ingresar la placa del vehículo.");
        }
        if (txt_tipoVehiculo.getText().trim().isEmpty()) {
            return errorCampo(txt_tipoVehiculo, "El campo Tipo de vehículo no puede estar vacío.");
        }
        if (txt_marca.getText().trim().isEmpty()) {
            return errorCampo(txt_marca, "El campo Marca no puede estar vacío.");
        }
        if (txt_modelo.getText().trim().isEmpty()) {
            return errorCampo(txt_modelo, "El campo Modelo no puede estar vacío.");
        }
        if (txt_color.getText().trim().isEmpty()) {
            return errorCampo(txt_color, "El campo Color no puede estar vacío.");
        }
        if (getTrabajadorSeleccionado() == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un trabajador asignado.",
                    "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            cbb_trabajador.requestFocus();
            return false;
        }
        if (txt_fechaIngreso.getText().trim().isEmpty()) {
            return errorCampo(txt_fechaIngreso, "La fecha/hora de ingreso es obligatoria.");
        }
        if (txt_fechaTermino.getText().trim().isEmpty()) {
            return errorCampo(txt_fechaTermino, "La fecha estimada de término es obligatoria.");
        }
        if (cbb_estado.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un estado para el ticket.",
                    "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            cbb_estado.requestFocus();
            return false;
        }
        if (tbl_detalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe agregar al menos un servicio al detalle del ticket.",
                    "Detalle Vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean errorCampo(javax.swing.JTextField campo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        campo.requestFocus();
        return false;
    }

    private void registrarTicketEnBD() {
        DefaultTableModel modeloTabla = (DefaultTableModel) tbl_detalle.getModel();
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe agregar al menos un servicio al detalle del ticket.",
                    "Detalle Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection cn = null;
        try {
            cn = obtenerConexion();
            cn.setAutoCommit(false);

            int idCliente = (idClienteActual != null && idClienteActual > 0)
                    ? idClienteActual
                    : upsertClienteBasico(cn);
            int idVehiculo = resolverIdVehiculo(cn);

            int idxTrabajador = cbb_trabajador.getSelectedIndex();
            int idTrabajador = (idxTrabajador > 0 && idxTrabajador <= listaTrabajadores.size())
                    ? listaTrabajadores.get(idxTrabajador - 1).id
                    : 1;

            String estado = cbb_estado.getSelectedItem() != null
                    ? cbb_estado.getSelectedItem().toString().trim().toUpperCase().replace(" ", "_")
                    : "REGISTRADO";

            String nroTicket = lbl_ticket.getText().replace("Ticket:", "").trim();
            LocalDateTime ldtIngreso = LocalDateTime.parse(txt_fechaIngreso.getText().trim(), FORMATO_FECHA);
            LocalDateTime ldtTermino = LocalDateTime.parse(txt_fechaTermino.getText().trim(), FORMATO_FECHA);

            String sqlOrden = "INSERT INTO orden_servicio (NRO_TICKET, ID_CLIENTE, ID_VEHICULO, ID_TRABAJADOR, FECHA_INGRESO, FECHA_ESTIMADA, ESTADO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int idOrdenGenerada = 0;

            try (PreparedStatement psOrden = cn.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
                psOrden.setString(1, nroTicket);
                psOrden.setInt(2, idCliente);
                psOrden.setInt(3, idVehiculo);
                psOrden.setInt(4, idTrabajador);
                psOrden.setTimestamp(5, Timestamp.valueOf(ldtIngreso));
                psOrden.setTimestamp(6, Timestamp.valueOf(ldtTermino));
                psOrden.setString(7, estado);
                psOrden.executeUpdate();

                try (ResultSet rsKeys = psOrden.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        idOrdenGenerada = rsKeys.getInt(1);
                    }
                }
            }

            String sqlDetalle = "INSERT INTO detalle_orden (ID_ORDEN, ID_SERVICIO, PRECIO_APLICADO, RECARGO, MOTIVO_RECARGO) "
                    + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psDet = cn.prepareStatement(sqlDetalle)) {
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    int idServicio = Integer.parseInt(modeloTabla.getValueAt(i, 0).toString());
                    double precioAplicado = Double.parseDouble(modeloTabla.getValueAt(i, 2).toString());
                    double recargo = Double.parseDouble(modeloTabla.getValueAt(i, 3).toString());
                    String motivo = modeloTabla.getValueAt(i, 4) != null ? modeloTabla.getValueAt(i, 4).toString() : "";

                    psDet.setInt(1, idOrdenGenerada);
                    psDet.setInt(2, idServicio);
                    psDet.setDouble(3, precioAplicado);
                    psDet.setDouble(4, recargo);
                    psDet.setString(5, motivo);
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            cn.commit();
            JOptionPane.showMessageDialog(this,
                    "Ticket registrado correctamente (N° " + idOrdenGenerada + ").",
                    "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarTodo();
        } catch (Exception ex) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException rollbackEx) {
                    logger.severe("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            logger.severe("Error al registrar ticket: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el ticket en la base de datos.\nDetalle: " + ex.getMessage(),
                    "Error de Transacción", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (cn != null) {
                try {
                    cn.setAutoCommit(true);
                    cn.close();
                } catch (SQLException closeEx) {
                    logger.warning("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }

    private int upsertClienteBasico(Connection cn) throws SQLException {
        String doc = txt_buscarCliente.getText().trim();
        boolean esDni = (doc.length() == 8);
        String dni = esDni ? doc : null;
        String ruc = (doc.length() == 11) ? doc : null;

        String textoNombre = txt_nombre.getText().trim();
        String primerNombre = "";
        String primerApellido = "";
        String razonSocial = null;

        if (esDni) {
            String[] partes = textoNombre.split("\\s+", 2);
            primerNombre = partes.length > 0 ? partes[0] : "";
            primerApellido = (partes.length > 1 && !partes[1].isBlank()) ? partes[1] : "-";
        } else {
            razonSocial = textoNombre;
            primerNombre = textoNombre;
            primerApellido = "-";
        }

        String sql = "INSERT INTO cliente (PRIMER_NOMBRE, PRIMER_APELLIDO, CODIGO_PAIS, CELULAR, DNI, RUC, RAZON_SOCIAL) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, primerNombre);
            ps.setString(2, primerApellido);
            ps.setString(3, "+51");
            ps.setString(4, txt_celular.getText().trim());
            ps.setString(5, dni);
            ps.setString(6, ruc);
            ps.setString(7, razonSocial);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    idClienteActual = id;
                    lbl_IDcliente.setText("ID CLIENTE: " + id);
                    return id;
                }
            }
        }
        throw new SQLException("No se pudo registrar el cliente.");
    }

    private void limpiarDatosCliente() {
        idClienteActual = null;
        lbl_IDcliente.setText("ID CLIENTE:");
        txt_nombre.setText("");
        txt_celular.setText("");
    }

    private void limpiarTodo() {
        txt_buscarCliente.setText("");
        limpiarDatosCliente();

        idVehiculoActual = null;
        txt_placa.setText("");
        txt_tipoVehiculo.setText("");
        txt_marca.setText("");
        txt_modelo.setText("");
        txt_color.setText("");

        txt_precioAplicado.setText("");
        txt_recargoServicios.setText("");
        txt_motivo.setText("");

        if (cbb_trabajador.getItemCount() > 0) {
            cbb_trabajador.setSelectedIndex(0);
        }
        if (cbb_servicio.getItemCount() > 0) {
            cbb_servicio.setSelectedIndex(0);
        }
        if (cbb_estado.getItemCount() > 0) {
            cbb_estado.setSelectedIndex(0);
        }

        ((DefaultTableModel) tbl_detalle.getModel()).setRowCount(0);
        inicializarFechasYCorrelativo();
        actualizarResumenFinanciero();
    }

    private void actualizarFechaCabecera() {
        String fechaHoy = java.time.LocalDate.now().toString();
        lbl_fecha.setText("Fecha: " + fechaHoy);
    }

    private void abrirVentanaServicio() {
        Servicio ventanaServicio = new Servicio();
        ventanaServicio.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ventanaServicio.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                cargarServiciosDesdeBD();
            }
        });
        ventanaServicio.setLocationRelativeTo(this);
        ventanaServicio.setVisible(true);
    }

    private Connection obtenerConexion() throws SQLException {
        Connection cn = Util.ConexionBD.conn();
        if (cn == null) {
            throw new SQLException("No se pudo establecer conexión con la base de datos.");
        }
        return cn;
    }

    private double parseDoubleSeguro(Object valor) {
        if (valor == null) {
            return 0.0;
        }
        try {
            return Double.parseDouble(valor.toString().replace("S/.", "").replace(",", ".").trim());
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private ComboItem getTrabajadorSeleccionado() {
        int idx = cbb_trabajador.getSelectedIndex();
        if (idx > 0 && idx <= listaTrabajadores.size()) {
            return listaTrabajadores.get(idx - 1);
        }
        return null;
    }

    private ServicioItem getServicioSeleccionado() {
        int idx = cbb_servicio.getSelectedIndex();
        if (idx > 0 && idx <= listaServicios.size()) {
            return listaServicios.get(idx - 1);
        }
        return null;
    }
    
    private void buscarClientePorDocumento() {
        // Implementa aquí tu búsqueda por DNI/RUC si aún no la tenías en este tramo
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_ticket = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_buscarCliente = new javax.swing.JTextField();
        txt_nombre = new javax.swing.JTextField();
        txt_celular = new javax.swing.JTextField();
        btn_nuevoCliente = new javax.swing.JButton();
        btn_buscarCliente = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_placa = new javax.swing.JTextField();
        txt_tipoVehiculo = new javax.swing.JTextField();
        txt_marca = new javax.swing.JTextField();
        txt_modelo = new javax.swing.JTextField();
        txt_color = new javax.swing.JTextField();
        btn_buscarVehiculo = new javax.swing.JButton();
        lbl_IDcliente = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_fechaIngreso = new javax.swing.JTextField();
        txt_fechaTermino = new javax.swing.JTextField();
        cbb_trabajador = new javax.swing.JComboBox<>();
        cbb_estado = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_detalle = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_motivo = new javax.swing.JTextField();
        txt_precioAplicado = new javax.swing.JTextField();
        txt_recargoServicios = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        btn_servicio = new javax.swing.JButton();
        cbb_servicio = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lbl_descuento = new javax.swing.JLabel();
        lbl_subtotal = new javax.swing.JLabel();
        lbl_recargo = new javax.swing.JLabel();
        lbl_igv = new javax.swing.JLabel();
        lbl_total = new javax.swing.JLabel();
        btn_volver = new javax.swing.JButton();
        btn_limpiar = new javax.swing.JButton();
        btn_registrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setMinimumSize(new java.awt.Dimension(1390, 966));
        jPanel1.setPreferredSize(new java.awt.Dimension(1390, 966));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setMaximumSize(new java.awt.Dimension(1200, 800));
        jPanel2.setMinimumSize(new java.awt.Dimension(1200, 800));

        jPanel3.setBackground(new java.awt.Color(18, 17, 17));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 102, 0));
        jLabel1.setText("REGISTRO DE TICKETS");

        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Módulo de registro y emisión de tickets de atención");

        lbl_ticket.setBackground(new java.awt.Color(153, 102, 0));
        lbl_ticket.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lbl_ticket.setForeground(new java.awt.Color(204, 204, 204));
        lbl_ticket.setText("  TICKET:");
        lbl_ticket.setOpaque(true);

        lbl_fecha.setForeground(new java.awt.Color(204, 204, 204));
        lbl_fecha.setText("  Fecha: ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 765, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_ticket, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_fecha))
                .addGap(27, 27, 27))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_ticket, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_fecha)))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(18, 17, 17));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 3, true), " DATOS DEL CLIENTE ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 102, 0))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Buscar cliente (DNI / RUC):");

        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("Celular:");

        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("Nombre / Razón social:");

        txt_buscarCliente.setBackground(new java.awt.Color(51, 51, 51));
        txt_buscarCliente.setForeground(new java.awt.Color(204, 204, 204));
        txt_buscarCliente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_nombre.setBackground(new java.awt.Color(51, 51, 51));
        txt_nombre.setForeground(new java.awt.Color(204, 204, 204));
        txt_nombre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_celular.setBackground(new java.awt.Color(51, 51, 51));
        txt_celular.setForeground(new java.awt.Color(204, 204, 204));
        txt_celular.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        btn_nuevoCliente.setBackground(new java.awt.Color(51, 51, 51));
        btn_nuevoCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_nuevoCliente.setForeground(new java.awt.Color(153, 102, 0));
        btn_nuevoCliente.setText("Nuevo cliente");
        btn_nuevoCliente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_nuevoCliente.setContentAreaFilled(false);
        btn_nuevoCliente.addActionListener(this::btn_nuevoClienteActionPerformed);

        btn_buscarCliente.setBackground(new java.awt.Color(51, 51, 51));
        btn_buscarCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_buscarCliente.setForeground(new java.awt.Color(153, 102, 0));
        btn_buscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/lupaMini.png"))); // NOI18N
        btn_buscarCliente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_buscarCliente.setContentAreaFilled(false);
        btn_buscarCliente.addActionListener(this::btn_buscarClienteActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txt_buscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_buscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(36, 36, 36)
                        .addComponent(txt_nombre))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_nuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_buscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_buscarCliente))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_celular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nuevoCliente))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(18, 17, 17));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 3, true), " DATOS DEL VEHÍCULO ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 102, 0))); // NOI18N
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("Marca:");

        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        jLabel7.setText("Modelo:");

        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("Color:");

        jLabel9.setForeground(new java.awt.Color(204, 204, 204));
        jLabel9.setText("Tipo de vehículo:");

        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("Placa:");

        txt_placa.setBackground(new java.awt.Color(51, 51, 51));
        txt_placa.setForeground(new java.awt.Color(204, 204, 204));
        txt_placa.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_tipoVehiculo.setBackground(new java.awt.Color(51, 51, 51));
        txt_tipoVehiculo.setForeground(new java.awt.Color(204, 204, 204));
        txt_tipoVehiculo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_marca.setBackground(new java.awt.Color(51, 51, 51));
        txt_marca.setForeground(new java.awt.Color(204, 204, 204));
        txt_marca.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_modelo.setBackground(new java.awt.Color(51, 51, 51));
        txt_modelo.setForeground(new java.awt.Color(204, 204, 204));
        txt_modelo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_color.setBackground(new java.awt.Color(51, 51, 51));
        txt_color.setForeground(new java.awt.Color(204, 204, 204));
        txt_color.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        btn_buscarVehiculo.setBackground(new java.awt.Color(51, 51, 51));
        btn_buscarVehiculo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_buscarVehiculo.setForeground(new java.awt.Color(153, 102, 0));
        btn_buscarVehiculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/lupaMini.png"))); // NOI18N
        btn_buscarVehiculo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_buscarVehiculo.setContentAreaFilled(false);
        btn_buscarVehiculo.addActionListener(this::btn_buscarVehiculoActionPerformed);

        lbl_IDcliente.setBackground(new java.awt.Color(153, 102, 0));
        lbl_IDcliente.setForeground(new java.awt.Color(204, 204, 204));
        lbl_IDcliente.setText("  ID CLIENTE:");
        lbl_IDcliente.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9))
                .addGap(36, 36, 36)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txt_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_buscarVehiculo, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                    .addComponent(txt_tipoVehiculo)
                    .addComponent(txt_marca)
                    .addComponent(txt_modelo)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(txt_color)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_IDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txt_placa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_buscarVehiculo))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(txt_tipoVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(txt_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(txt_color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lbl_IDcliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(18, 17, 17));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 3, true), " DATOS DEL TRABAJADOR Y FECHAS ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 102, 0))); // NOI18N
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setForeground(new java.awt.Color(204, 204, 204));
        jLabel11.setText("Fecha estimada del termino:");

        jLabel12.setForeground(new java.awt.Color(204, 204, 204));
        jLabel12.setText("Estado:");

        jLabel13.setForeground(new java.awt.Color(204, 204, 204));
        jLabel13.setText("Fecha / Hora de ingreso:");

        jLabel14.setForeground(new java.awt.Color(204, 204, 204));
        jLabel14.setText("Trabajador asignado:");

        txt_fechaIngreso.setBackground(new java.awt.Color(51, 51, 51));
        txt_fechaIngreso.setForeground(new java.awt.Color(204, 204, 204));
        txt_fechaIngreso.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_fechaTermino.setBackground(new java.awt.Color(51, 51, 51));
        txt_fechaTermino.setForeground(new java.awt.Color(204, 204, 204));
        txt_fechaTermino.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        cbb_trabajador.setBackground(new java.awt.Color(51, 51, 51));
        cbb_trabajador.setForeground(new java.awt.Color(204, 204, 204));
        cbb_trabajador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbb_estado.setBackground(new java.awt.Color(51, 51, 51));
        cbb_estado.setForeground(new java.awt.Color(204, 204, 204));
        cbb_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REGISTRADO", "EN_PROCESO", "TERMINADO", "COBRADO", "CANCELADO" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_fechaTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel12))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_fechaIngreso)
                                    .addComponent(cbb_trabajador, 0, 395, Short.MAX_VALUE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(cbb_estado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(29, 29, 29))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cbb_trabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_fechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txt_fechaTermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cbb_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPanel5.setBackground(new java.awt.Color(18, 17, 17));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 3, true), "DETALE DE SERVICIO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 102, 0))); // NOI18N
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));

        tbl_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "SERVICIO", "PRECIO APLICADO", "RECARGO", "MOTIVO RECARGO", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl_detalle);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(18, 17, 17));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 3, true), "SELECCIÓN DE SERVICIOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 102, 0))); // NOI18N
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));

        jLabel15.setForeground(new java.awt.Color(204, 204, 204));
        jLabel15.setText("Precio aplicado S/ :");

        jLabel16.setForeground(new java.awt.Color(204, 204, 204));
        jLabel16.setText("Motivo de recargo:");

        jLabel17.setForeground(new java.awt.Color(204, 204, 204));
        jLabel17.setText("Servicio:");

        txt_motivo.setBackground(new java.awt.Color(51, 51, 51));
        txt_motivo.setForeground(new java.awt.Color(204, 204, 204));
        txt_motivo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_precioAplicado.setBackground(new java.awt.Color(51, 51, 51));
        txt_precioAplicado.setForeground(new java.awt.Color(204, 204, 204));
        txt_precioAplicado.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        txt_recargoServicios.setBackground(new java.awt.Color(51, 51, 51));
        txt_recargoServicios.setForeground(new java.awt.Color(204, 204, 204));
        txt_recargoServicios.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel21.setForeground(new java.awt.Color(204, 204, 204));
        jLabel21.setText("Recargo S/ :");

        btn_servicio.setBackground(new java.awt.Color(51, 51, 51));
        btn_servicio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_servicio.setForeground(new java.awt.Color(153, 102, 0));
        btn_servicio.setText("Agregar servicio");
        btn_servicio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_servicio.setContentAreaFilled(false);
        btn_servicio.addActionListener(this::btn_servicioActionPerformed);

        cbb_servicio.setBackground(new java.awt.Color(51, 51, 51));
        cbb_servicio.setForeground(new java.awt.Color(204, 204, 204));
        cbb_servicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txt_precioAplicado, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_recargoServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btn_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbb_servicio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cbb_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txt_precioAplicado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_recargoServicios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txt_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_servicio))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(18, 17, 17));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 3, true), "RESUMEN FINANCIERO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 102, 0))); // NOI18N
        jPanel9.setForeground(new java.awt.Color(255, 255, 255));

        jPanel10.setBackground(new java.awt.Color(18, 17, 17));

        jLabel22.setForeground(new java.awt.Color(204, 204, 204));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel22.setText("Subtotal:");

        jLabel23.setForeground(new java.awt.Color(204, 204, 204));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel23.setText("Descuento (0%):");

        jLabel24.setForeground(new java.awt.Color(204, 204, 204));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel24.setText("Recargo tarjeta:");

        jLabel25.setForeground(new java.awt.Color(204, 204, 204));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel25.setText("I.G.V. (18%):");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(153, 102, 0));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel26.setText("TOTAL A PAGAR:");

        lbl_descuento.setForeground(new java.awt.Color(204, 204, 204));
        lbl_descuento.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_descuento.setText("S/.0");

        lbl_subtotal.setForeground(new java.awt.Color(204, 204, 204));
        lbl_subtotal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_subtotal.setText("S/.0");

        lbl_recargo.setForeground(new java.awt.Color(204, 204, 204));
        lbl_recargo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_recargo.setText("S/.0");

        lbl_igv.setForeground(new java.awt.Color(204, 204, 204));
        lbl_igv.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_igv.setText("S/.0");

        lbl_total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_total.setForeground(new java.awt.Color(153, 102, 0));
        lbl_total.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_total.setText("S/.0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_recargo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_igv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_descuento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_subtotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_total, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(42, 42, 42))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(lbl_subtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(lbl_descuento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(lbl_recargo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(lbl_igv))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(lbl_total))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btn_volver.setBackground(new java.awt.Color(18, 17, 17));
        btn_volver.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_volver.setForeground(new java.awt.Color(153, 102, 0));
        btn_volver.setText("VOLVER");
        btn_volver.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_volver.setBorderPainted(false);
        btn_volver.addActionListener(this::btn_volverActionPerformed);

        btn_limpiar.setBackground(new java.awt.Color(18, 17, 17));
        btn_limpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_limpiar.setForeground(new java.awt.Color(153, 102, 0));
        btn_limpiar.setText("LIMPIAR");
        btn_limpiar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_limpiar.setBorderPainted(false);
        btn_limpiar.addActionListener(this::btn_limpiarActionPerformed);

        btn_registrar.setBackground(new java.awt.Color(153, 102, 0));
        btn_registrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_registrar.setText("REGISTRAR TICKET");
        btn_registrar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 2, true));
        btn_registrar.setBorderPainted(false);
        btn_registrar.addActionListener(this::btn_registrarActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(242, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
        registrarTicketEnBD();
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiarActionPerformed
        limpiarTodo();
    }//GEN-LAST:event_btn_limpiarActionPerformed

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        Principal principal = new Principal();
        principal.setExtendedState(MAXIMIZED_BOTH);
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

    private void btn_buscarVehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarVehiculoActionPerformed
        buscarVehiculoPorPlaca();
    }//GEN-LAST:event_btn_buscarVehiculoActionPerformed

    private void btn_nuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoClienteActionPerformed
        JOptionPane.showMessageDialog(this,
                "Complete los datos del cliente y vehículo; si no existen en la BD se registrarán al emitir el ticket.",
                "Nuevo Cliente", JOptionPane.INFORMATION_MESSAGE);
        txt_nombre.requestFocus();
    }//GEN-LAST:event_btn_nuevoClienteActionPerformed

    private void btn_buscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarClienteActionPerformed
        buscarClientePorDocumento();
    }//GEN-LAST:event_btn_buscarClienteActionPerformed

    private void btn_servicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_servicioActionPerformed
        agregarServicioATabla();
    }//GEN-LAST:event_btn_servicioActionPerformed

    /**
     * @param args the command line arguments
     */
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

        java.awt.EventQueue.invokeLater(() -> new Registro_Tickets().setVisible(true));
    }

    private static final class ComboItem {
        final int id;
        final String texto;

        ComboItem(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }
    }

    private static final class ServicioItem {
        final int id;
        final String nombre;
        final double precioBase;

        ServicioItem(int id, String nombre, double precioBase) {
            this.id = id;
            this.nombre = nombre;
            this.precioBase = precioBase;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscarCliente;
    private javax.swing.JButton btn_buscarVehiculo;
    private javax.swing.JButton btn_limpiar;
    private javax.swing.JButton btn_nuevoCliente;
    private javax.swing.JButton btn_registrar;
    private javax.swing.JButton btn_servicio;
    private javax.swing.JButton btn_volver;
    private javax.swing.JComboBox<String> cbb_estado;
    private javax.swing.JComboBox<String> cbb_servicio;
    private javax.swing.JComboBox<String> cbb_trabajador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_IDcliente;
    private javax.swing.JLabel lbl_descuento;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_igv;
    private javax.swing.JLabel lbl_recargo;
    private javax.swing.JLabel lbl_subtotal;
    private javax.swing.JLabel lbl_ticket;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JTable tbl_detalle;
    private javax.swing.JTextField txt_buscarCliente;
    private javax.swing.JTextField txt_celular;
    private javax.swing.JTextField txt_color;
    private javax.swing.JTextField txt_fechaIngreso;
    private javax.swing.JTextField txt_fechaTermino;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_modelo;
    private javax.swing.JTextField txt_motivo;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_placa;
    private javax.swing.JTextField txt_precioAplicado;
    private javax.swing.JTextField txt_recargoServicios;
    private javax.swing.JTextField txt_tipoVehiculo;
    // End of variables declaration//GEN-END:variables
}
