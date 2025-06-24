/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ec.edu.viajecito.view;

import ec.edu.viajecito.controller.BoletosController;
import ec.edu.viajecito.controller.FacturasController;
import ec.edu.viajecito.model.Boleto;
import ec.edu.viajecito.model.Factura;
import ec.edu.viajecito.model.Usuario;
import ec.edu.viajecito.model.Vuelo;
import ec.edu.viajecito.model.Amortizacion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Drouet
 */
public class FacturaDetalleFrm extends javax.swing.JFrame {
    
    private Usuario usuario;
    List<Amortizacion> amortizacions = new ArrayList<>();

    /**
     * Creates new form LoginFrm
     */
    public FacturaDetalleFrm(Usuario usuario, int idFactura) {
        initComponents();
        tableStyle();
        this.usuario = usuario;
        lblNombre.setText(usuario.getNombre());
        lblCedula.setText(usuario.getCedula());
        lblCorreo.setText(usuario.getCorreo());
        lbltelf.setText(usuario.getTelefono());
        cargarTablaFacturaDetalle(idFactura);
        cargarAmortizacion(idFactura);
        
    }
    
    private void cargarAmortizacion(int idFactura) {
        FacturasController facturasController = new FacturasController();
        
        amortizacions = facturasController.obtenerAmortizacionPorFactura(idFactura);   
        
        if (!amortizacions.isEmpty()) {
            btnAmortizacion.setEnabled(true);
        }
        
    }
    
    private void cargarTablaFacturaDetalle(int idFactura) {      
        FacturasController facturasController = new FacturasController();
                
        Factura factura = facturasController.obtenerFacturaPorId(idFactura);
                
        llenarTabla(factura);    
        
        // Formatear fecha
        String fechaSalida = new SimpleDateFormat("yyyy-MM-dd").format(factura.getFechaFactura());
        
        double subtotal = factura.getPrecioSinIVA();
        double total = factura.getPrecioConIVA();
        double iva = subtotal * 0.15;

        lblFecha.setText(fechaSalida);
        lblSubtotal.setText(String.format("%.2f $", subtotal));
        lblIVA.setText(String.format("%.2f $", iva));
        lblTotal.setText(String.format("%.2f $", total));
    }

    private void llenarTabla(Factura factura) {
        // Definir modelo de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("N.");
        model.addColumn("Boleto");
        model.addColumn("Detalle");
        model.addColumn("Cantidad");
        model.addColumn("PrecioUnitario");

        int index = 1;

        for (Boleto boleto : factura.getBoletos()) {
            String numeroBoleto = boleto.getNumeroBoleto();

            Vuelo vuelo = boleto.getVuelo();
            String ciudadOrigen = vuelo.getCiudadOrigen().getNombreCiudad();
            String ciudadDestino = vuelo.getCiudadDestino().getNombreCiudad();

            // Formatear fecha
            String fechaSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(vuelo.getHoraSalida());

            String detalle = ciudadOrigen + " - " + ciudadDestino + " - " + fechaSalida;

            String precioUnitario = String.format("%.2f $", boleto.getPrecioCompra().doubleValue());

            model.addRow(new Object[]{
                index++,
                numeroBoleto,
                detalle,
                1,
                precioUnitario
            });
        }

        tblFacturaDetalle.setModel(model);
    }



    private void tableStyle() {
        // Cambiar fuente
        tblFacturaDetalle.setFont(new Font("Maiandra GD", Font.PLAIN, 14));

        // Cambiar alto de las filas
        tblFacturaDetalle.setRowHeight(25);

        // Cambiar color de fondo y texto de las filas
        tblFacturaDetalle.setBackground(Color.WHITE);
        tblFacturaDetalle.setForeground(Color.BLACK);
        
        tblFacturaDetalle.setFillsViewportHeight(true);

        // Cambiar colores de encabezado
        JTableHeader header = tblFacturaDetalle.getTableHeader();        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setFont(new Font("Maiandra GD", Font.BOLD, 14));
                label.setBackground(new Color(60,59,46));
                label.setForeground(Color.WHITE);  
                label.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });      

        // Bordes
        tblFacturaDetalle.setShowHorizontalLines(true);
        tblFacturaDetalle.setShowVerticalLines(true);
        tblFacturaDetalle.setGridColor(new Color(230, 230, 230)); // Líneas suaves
        
        tblFacturaDetalle.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(new Color(144, 202, 249)); // azul claro para selección
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(255, 241, 191)); // filas alternas
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFacturaDetalle = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblCedula = new javax.swing.JLabel();
        lbltelf = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        jlabel = new javax.swing.JLabel();
        lblIVA = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Descuento = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblNumFactura = new javax.swing.JLabel();
        lblCedula3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        btnAmortizacion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        btnSalir.setBackground(new java.awt.Color(35, 103, 138));
        btnSalir.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("Salir");
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(27, 27, 30));
        jLabel5.setFont(new java.awt.Font("Maiandra GD", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(60, 59, 46));
        jLabel5.setText("VIAJECITO S.A.");

        tblFacturaDetalle.setBackground(new java.awt.Color(255, 255, 255));
        tblFacturaDetalle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblFacturaDetalle.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        tblFacturaDetalle.setForeground(new java.awt.Color(0, 0, 0));
        tblFacturaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N. Boleto", "Detalle", "Cantidad", "Precio Unitario"
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
        tblFacturaDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        tblFacturaDetalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturaDetalleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFacturaDetalle);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(60, 59, 46));
        jLabel1.setText("RUC:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(60, 59, 46));
        jLabel2.setText("Datos del Cliente");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(60, 59, 46));
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(60, 59, 46));
        jLabel4.setText("Cédula:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(60, 59, 46));
        jLabel6.setText("Teléfono:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(60, 59, 46));
        jLabel7.setText("Correo:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(60, 59, 46));
        jLabel8.setText("Detalle de la factura");

        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(60, 59, 46));
        lblNombre.setText("David Alejandro Calle De La Cueva");

        lblCedula.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCedula.setForeground(new java.awt.Color(60, 59, 46));
        lblCedula.setText("1710982846");

        lbltelf.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbltelf.setForeground(new java.awt.Color(60, 59, 46));
        lbltelf.setText("0964987961");

        lblCorreo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCorreo.setForeground(new java.awt.Color(60, 59, 46));
        lblCorreo.setText("david_calle_24@outlook.com");

        jlabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jlabel.setForeground(new java.awt.Color(60, 59, 46));
        jlabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlabel.setText("0 $");

        lblIVA.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblIVA.setForeground(new java.awt.Color(60, 59, 46));
        lblIVA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIVA.setText("15 $");

        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(60, 59, 46));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("115 $");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(60, 59, 46));
        jLabel9.setText("Subtotal:");

        Descuento.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Descuento.setForeground(new java.awt.Color(60, 59, 46));
        Descuento.setText("Descuento:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(60, 59, 46));
        jLabel11.setText("IVA 15%:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(60, 59, 46));
        jLabel12.setText("Total:");

        lblSubtotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(60, 59, 46));
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("100 $");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(60, 59, 46));
        jLabel10.setText("FACTURA:");

        lblNumFactura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNumFactura.setForeground(new java.awt.Color(60, 59, 46));
        lblNumFactura.setText("1710-982846");

        lblCedula3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCedula3.setForeground(new java.awt.Color(60, 59, 46));
        lblCedula3.setText("1710708973001");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(60, 59, 46));
        jLabel13.setText("Fecha emisión:");

        lblFecha.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(60, 59, 46));
        lblFecha.setText("2025-06-14");

        btnAmortizacion.setBackground(new java.awt.Color(35, 103, 138));
        btnAmortizacion.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        btnAmortizacion.setForeground(new java.awt.Color(255, 255, 255));
        btnAmortizacion.setText("Amortización");
        btnAmortizacion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAmortizacion.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnAmortizacion.setEnabled(false);
        btnAmortizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAmortizacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(80, 80, 80)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel13))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCedula3)
                                    .addComponent(lblNumFactura)
                                    .addComponent(lblFecha)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(489, 489, 489)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(Descuento)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblIVA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblSubtotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                    .addComponent(jlabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel2)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel4))
                                            .addGap(25, 25, 25)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblCedula)
                                                .addComponent(lblNombre))))
                                    .addGap(127, 127, 127)
                                    .addComponent(btnAmortizacion))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(15, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbltelf)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblCorreo)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(lblNumFactura))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblCedula3)))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lblFecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblCedula))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lbltelf))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblCorreo))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel8))
                    .addComponent(btnAmortizacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblSubtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Descuento)
                    .addComponent(jlabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblIVA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
        );

        btnSalir.getAccessibleContext().setAccessibleDescription("init");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void tblFacturaDetalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturaDetalleMouseClicked
    }//GEN-LAST:event_tblFacturaDetalleMouseClicked

    private void btnAmortizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAmortizacionActionPerformed
        AmortizacionFrm amortizacionFrm = new AmortizacionFrm(amortizacions);
        amortizacionFrm.setVisible(true);
    }//GEN-LAST:event_btnAmortizacionActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FacturaDetalleFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FacturaDetalleFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FacturaDetalleFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FacturaDetalleFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FacturaDetalleFrm(new Usuario(1,"david", "username", "sad", "1234567890", "0987654321", "david@a"), 1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Descuento;
    private javax.swing.JButton btnAmortizacion;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlabel;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblCedula3;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblIVA;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNumFactura;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbltelf;
    private javax.swing.JTable tblFacturaDetalle;
    // End of variables declaration//GEN-END:variables
}
