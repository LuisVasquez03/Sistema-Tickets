package Estructuras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Busqueda_Ticket extends JFrame {
    private JTextField txtCodigo;
    private JTextArea txtResultado;
    private Arbol_AVL arbolAVL;

    public Busqueda_Ticket(Arbol_AVL arbolAVL) {
        this.arbolAVL = arbolAVL;

        setTitle("Sistema de Gestión - Carwash");
        setSize(520, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(new Color(248, 249, 250));
        setContentPane(panelPrincipal);

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(30, 41, 59));
        panelHeader.setPreferredSize(new Dimension(520, 60));
        panelHeader.setLayout(new GridBagLayout());
        
        JLabel lblTituloHeader = new JLabel("CONSULTA RÁPIDA DE TICKETS");
        lblTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloHeader.setForeground(Color.WHITE);
        panelHeader.add(lblTituloHeader);
        
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelCentro.setOpaque(false);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panelBusqueda.setOpaque(false);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCodigo.setForeground(new Color(51, 65, 85));

        txtCodigo = new JTextField(12);
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCodigo.setPreferredSize(new Dimension(140, 30));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setBackground(new Color(37, 99, 235));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setPreferredSize(new Dimension(90, 32));

        panelBusqueda.add(lblCodigo);
        panelBusqueda.add(txtCodigo);
        panelBusqueda.add(btnBuscar);

        panelCentro.add(panelBusqueda, BorderLayout.NORTH);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtResultado.setBackground(Color.WHITE);
        txtResultado.setForeground(new Color(15, 23, 42));
        txtResultado.setMargin(new Insets(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = txtCodigo.getText().trim();
                if (codigo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un código válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Ticket encontrado = arbolAVL.buscar(codigo);

                if (encontrado != null) {
                    txtResultado.setText("  ========================================\n" +
                            "            DETALLE DEL TICKET ENCONTRADO\n" +
                            "  ========================================\n\n" +
                            "   • Código del Ticket : " + encontrado.getCodigo() + "\n" +
                            "   • Fecha y Hora      : " + encontrado.getFechaHora() + "\n" +
                            "   • Teléfono Cliente  : " + encontrado.getTelefonoCliente() + "\n" +
                            "   • Información Auto  : " + encontrado.getVehiculoInfo() + "\n" +
                            "   • Servicio Asignado : " + encontrado.getServicioNombre() + "\n" +
                            "   • Monto a Pagar     : S/ " + String.format("%.2f", encontrado.getPrecio()) + "\n" +
                            "   • Estado Actual     : " + encontrado.getEstado() + "\n\n" +
                            "  ========================================");
                } else {
                    txtResultado.setText("\n\n           [X] No se encontró ningún registro\n           con el código ingresado: " + codigo);
                }
            }
        });
    }

    public static void main(String[] args) {
        Arbol_AVL arbolPrueba = new Arbol_AVL();
        arbolPrueba.insertar(new Ticket("T001", "2026-10-19 17:00", "987654321", "Hyundai Elantra - Blanco", "Lavado Completo + Cera", 45.0, "En Proceso"));

        SwingUtilities.invokeLater(() -> {
            new Busqueda_Ticket(arbolPrueba).setVisible(true);
        });
    }
}