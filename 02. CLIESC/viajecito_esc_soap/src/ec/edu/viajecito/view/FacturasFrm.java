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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
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
public class FacturasFrm extends javax.swing.JFrame {
    
    private Usuario usuario;
    private List<Factura> facturas;

    /**
     * Creates new form LoginFrm
     */
    public FacturasFrm(Usuario usuario) {
        initComponents();
        tableStyle();
        this.usuario = usuario;
        cargarTablaFactura();
    }
    
    private void cargarTablaFactura() { 
        FacturasController facturasController = new FacturasController();
        facturas = facturasController.obtenerFacturasPorUsuario(usuario.getIdUsuario());

        // Definir columnas
        String[] columnas = {"N. Factura", "Fecha", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Formateador de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Agregar filas al modelo
        for (Factura factura : facturas) {
            String numero = factura.getNumeroFactura();
            String fecha = factura.getFechaFactura() != null ? sdf.format(factura.getFechaFactura()) : "Sin fecha";
            String precio = String.format("$ %.2f", factura.getPrecioConIVA());

            Object[] fila = {numero, fecha, precio};
            modelo.addRow(fila);
        }

        // Asignar modelo a la tabla
        tblFactura.setModel(modelo);

        // Opcional: ajustar tamaño de columnas si lo deseas
        tblFactura.getColumnModel().getColumn(0).setPreferredWidth(150); // N. Factura
        tblFactura.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha
        tblFactura.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
    }



    private void tableStyle() {
        // Cambiar fuente
        tblFactura.setFont(new Font("Maiandra GD", Font.PLAIN, 14));

        // Cambiar alto de las filas
        tblFactura.setRowHeight(25);

        // Cambiar color de fondo y texto de las filas
        tblFactura.setBackground(Color.WHITE);
        tblFactura.setForeground(Color.BLACK);
        
        tblFactura.setFillsViewportHeight(true);

        // Cambiar colores de encabezado
        JTableHeader header = tblFactura.getTableHeader();        
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
        tblFactura.setShowHorizontalLines(true);
        tblFactura.setShowVerticalLines(true);
        tblFactura.setGridColor(new Color(230, 230, 230)); // Líneas suaves
        
        tblFactura.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        jLabel3 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFactura = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        jLabel3.setBackground(new java.awt.Color(27, 27, 30));
        jLabel3.setFont(new java.awt.Font("Maiandra GD", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(60, 59, 46));
        jLabel3.setText("Mis Boletos");

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

        tblFactura.setBackground(new java.awt.Color(255, 255, 255));
        tblFactura.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblFactura.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        tblFactura.setForeground(new java.awt.Color(0, 0, 0));
        tblFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N. Factura", "Fecha", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFactura.setGridColor(new java.awt.Color(0, 0, 0));
        tblFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFactura);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(60, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(60, 60, 60))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir)
                .addContainerGap(34, Short.MAX_VALUE))
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

    private void tblFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturaMouseClicked
        
        try {
            int fila = tblFactura.rowAtPoint(evt.getPoint());
            Factura factura = facturas.get(fila);
            System.out.println(factura.getNumeroFactura());
            new FacturaDetalleFrm(usuario, factura.getIdFactura()).setVisible(true);
        } catch (Exception e) {
        }
        
    }//GEN-LAST:event_tblFacturaMouseClicked

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
            java.util.logging.Logger.getLogger(FacturasFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FacturasFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FacturasFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FacturasFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new FacturasFrm(new Usuario(1)).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFactura;
    // End of variables declaration//GEN-END:variables
}
