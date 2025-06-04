/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ec.edu.viajecito.view;

import ec.edu.viajecito.controller.BoletosController;
import ec.edu.viajecito.controller.CiudadesController;
import ec.edu.viajecito.controller.VuelosController;
import ec.edu.viajecito.model.Ciudad;
import ec.edu.viajecito.model.CompraBoletoRequest;
import ec.edu.viajecito.model.Usuario;
import ec.edu.viajecito.model.Vuelo;
import ec.edu.viajecito.model.VueloCompra;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Drouet
 */
public class ComprarFrm extends javax.swing.JFrame {

    private Usuario usuario;
    private List<Ciudad> ciudades;
    private List<Vuelo> vuelos;
    private List<VueloCompra> vuelosComprar;
    private List<Vuelo> vuelosAgregados;

    
    /**
     * Creates new form LoginFrm
     */
    public ComprarFrm(Usuario usuario1) {
        initComponents();
        SpinnerDateModel model = new SpinnerDateModel();
        spinDate.setModel(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinDate, "yyyy-MM-dd");
        spinDate.setEditor(editor);
        SpinnerNumberModel model1 = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        spnNumBoletos.setModel(model1);
        cargarCiudadesEnCombo();
        
        this.usuario = usuario1;
        vuelos = new ArrayList<>();
        this.vuelosComprar = new ArrayList<>();
        this.vuelosAgregados = new ArrayList<>();
        tableStyle();
        configTable();

    }
    
    private void configTable() {
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Vuelo", "Detalle", "Precio Unit", "Cantidad"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 3; // Solo cantidad editable
                }
            };
        
        tblVuelos.setModel(modeloTabla);

        // Listener para detectar cambios en la cantidad
        modeloTabla.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                int row = e.getFirstRow();
                int nuevaCantidad = Integer.parseInt(modeloTabla.getValueAt(row, 3).toString());
                vuelosComprar.get(row).setCantidad(nuevaCantidad);
            }
        });
    }
    
    private void tableStyle() {
        // Cambiar fuente
        tblVuelos.setFont(new Font("Maiandra GD", Font.PLAIN, 14));

        // Cambiar alto de las filas
        tblVuelos.setRowHeight(25);

        // Cambiar color de fondo y texto de las filas
        tblVuelos.setBackground(Color.WHITE);
        tblVuelos.setForeground(Color.BLACK);
        
        tblVuelos.setFillsViewportHeight(true);

        // Cambiar colores de encabezado
        JTableHeader header = tblVuelos.getTableHeader();        
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
        tblVuelos.setShowHorizontalLines(true);
        tblVuelos.setShowVerticalLines(true);
        tblVuelos.setGridColor(new Color(230, 230, 230)); // Líneas suaves
        tblVuelos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
    
    private void cargarCiudadesEnCombo() {
        ciudades = new CiudadesController().obtenerTodasCiudades();
        cmbOrigen.removeAllItems();
        cmbDestino.removeAllItems();

        for (Ciudad c : ciudades) {
            cmbOrigen.addItem(c.getCodigoCiudad() + " - " + c.getNombreCiudad());
            cmbDestino.addItem(c.getCodigoCiudad() + " - " + c.getNombreCiudad());
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogConfirm = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnComprar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lblViajesMsg = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbOrigen = new javax.swing.JComboBox<>();
        cmbDestino = new javax.swing.JComboBox<>();
        spinDate = new javax.swing.JSpinner();
        cmbVuelos = new javax.swing.JComboBox<>();
        spnNumBoletos = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        btnSalir1 = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVuelos = new javax.swing.JTable();

        dialogConfirm.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N

        javax.swing.GroupLayout dialogConfirmLayout = new javax.swing.GroupLayout(dialogConfirm.getContentPane());
        dialogConfirm.getContentPane().setLayout(dialogConfirmLayout);
        dialogConfirmLayout.setHorizontalGroup(
            dialogConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        dialogConfirmLayout.setVerticalGroup(
            dialogConfirmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setBackground(new java.awt.Color(60, 59, 46));
        jLabel2.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(60, 59, 46));
        jLabel2.setText("Vuelos Disponibles:");

        jLabel3.setBackground(new java.awt.Color(27, 27, 30));
        jLabel3.setFont(new java.awt.Font("Maiandra GD", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(60, 59, 46));
        jLabel3.setText("Comprar Boletos");

        jLabel4.setBackground(new java.awt.Color(60, 59, 46));
        jLabel4.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(27, 55, 58));
        jLabel4.setText("Fecha de Viaje:");

        btnComprar.setBackground(new java.awt.Color(35, 103, 138));
        btnComprar.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        btnComprar.setForeground(new java.awt.Color(255, 255, 255));
        btnComprar.setText("Comprar");
        btnComprar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnComprar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnComprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprarActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(27, 27, 30));
        jLabel5.setFont(new java.awt.Font("Maiandra GD", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(60, 59, 46));
        jLabel5.setText("VIAJECITO S.A.");

        lblViajesMsg.setBackground(new java.awt.Color(60, 59, 46));
        lblViajesMsg.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        lblViajesMsg.setForeground(new java.awt.Color(60, 59, 46));

        jLabel8.setBackground(new java.awt.Color(60, 59, 46));
        jLabel8.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(27, 55, 58));
        jLabel8.setText("Ciudad de Origen:");

        jLabel9.setBackground(new java.awt.Color(60, 59, 46));
        jLabel9.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(27, 55, 58));
        jLabel9.setText("Ciudad de Destino:");

        cmbOrigen.setBackground(new java.awt.Color(255, 255, 255));
        cmbOrigen.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        cmbOrigen.setForeground(new java.awt.Color(60, 59, 46));

        cmbDestino.setBackground(new java.awt.Color(255, 255, 255));
        cmbDestino.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        cmbDestino.setForeground(new java.awt.Color(60, 59, 46));

        spinDate.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        spinDate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinDateStateChanged(evt);
            }
        });

        cmbVuelos.setBackground(new java.awt.Color(255, 255, 255));
        cmbVuelos.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        cmbVuelos.setForeground(new java.awt.Color(60, 59, 46));

        spnNumBoletos.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        spnNumBoletos.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnNumBoletosStateChanged(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(60, 59, 46));
        jLabel6.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(27, 55, 58));
        jLabel6.setText("Número de boletos:");

        btnSalir1.setBackground(new java.awt.Color(255, 51, 51));
        btnSalir1.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        btnSalir1.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir1.setText("Salir");
        btnSalir1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnSalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalir1ActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(35, 103, 138));
        btnBuscar.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(35, 103, 138));
        btnAdd.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Añadir");
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(35, 103, 138));
        btnDelete.setFont(new java.awt.Font("Maiandra GD", 1, 24)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Eliminar Seleccioando");
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tblVuelos.setBackground(new java.awt.Color(255, 255, 255));
        tblVuelos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblVuelos.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        tblVuelos.setForeground(new java.awt.Color(0, 0, 0));
        tblVuelos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vuelo", "ID", "Detalle", "Precio Unit", "Cantidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVuelos.setGridColor(new java.awt.Color(0, 0, 0));
        tblVuelos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVuelosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblVuelos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(lblViajesMsg)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(117, 117, 117)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(btnSalir1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnComprar, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(cmbVuelos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(66, 66, 66)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4)
                                    .addComponent(spinDate)
                                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(spnNumBoletos, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete))
                            .addComponent(jScrollPane1))))
                .addGap(58, 58, 58))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbVuelos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnNumBoletos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblViajesMsg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(141, 141, 141)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnComprar, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(btnSalir1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );

        btnComprar.getAccessibleContext().setAccessibleDescription("init");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void spinDateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinDateStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_spinDateStateChanged

    private void spnNumBoletosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnNumBoletosStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_spnNumBoletosStateChanged

    private void btnComprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprarActionPerformed
        if (vuelosComprar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay vuelos seleccionados para comprar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder resumen = new StringBuilder();
        double total = 0;

        for (int i = 0; i < vuelosComprar.size(); i++) {
            VueloCompra vc = vuelosComprar.get(i);
            Vuelo vuelo = vuelosAgregados.get(i); // Ya no necesitas buscar
            
            
            double precioParcial = vuelo.getValor().doubleValue() * vc.getCantidad();
            total += precioParcial;

            resumen.append("- ").append(vuelo.getCodigoVuelo())
                   .append(" | De ").append(vuelo.getCiudadOrigen().getNombreCiudad())
                   .append(" a ").append(vuelo.getCiudadDestino().getNombreCiudad())
                   .append(" | Cantidad: ").append(vc.getCantidad())
                   .append(" | Total: ").append(precioParcial).append(" $\n");
        }

        int opcion = JOptionPane.showOptionDialog(
            this,
            "¿Está seguro que desea realizar la compra?\n" +
            "Vuelos a comprar:\n" + resumen.toString() +
            "TOTAL: " + total + "$",
            "Confirmar compra",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{"Confirmar", "Cancelar"},
            "Confirmar"
        );

        if (opcion == JOptionPane.YES_OPTION) {
            CompraBoletoRequest req = new CompraBoletoRequest();
            req.setIdUsuario(usuario.getIdUsuario());
            req.setVuelos(vuelosComprar);

            boolean ok = new BoletosController().comprarBoletos(req);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Compra realizada con éxito.");
                vuelosComprar.clear();
                vuelosAgregados.clear(); // <<<< Agregado
                ((DefaultTableModel) tblVuelos.getModel()).setRowCount(0);
            } else {
                JOptionPane.showMessageDialog(this, "Error en la compra, intente más tarde.");
            }
        }
    }//GEN-LAST:event_btnComprarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String origen = ciudades.get(cmbOrigen.getSelectedIndex()).getCodigoCiudad();
        String destino = ciudades.get(cmbDestino.getSelectedIndex()).getCodigoCiudad();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String horaSalida = sdf.format((Date) spinDate.getValue());
        
        vuelos = new VuelosController().obtenerVuelosPorCiudad(origen, destino, horaSalida);
        cmbVuelos.removeAllItems();

        if (vuelos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vuelo no Disponible.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Vuelo v : vuelos) {
                cmbVuelos.addItem(v.getCodigoVuelo() + " - Precio: " + v.getValor() + "$ - Disponibles: " + v.getDisponibles());
            }
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnSalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalir1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalir1ActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int fila = tblVuelos.getSelectedRow();
        if (fila >= 0) {
            vuelosComprar.remove(fila);
            vuelosAgregados.remove(fila);
            ((DefaultTableModel) tblVuelos.getModel()).removeRow(fila);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un vuelo para eliminar.");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        int index = cmbVuelos.getSelectedIndex();
        if (index < 0 || index >= vuelos.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione un vuelo válido.");
            return;
        }

        Vuelo vuelo = vuelos.get(index);
        int cantidad = (int) spnNumBoletos.getValue();

        if (cantidad < 1 || cantidad > vuelo.getDisponibles()) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida o superior a la disponibilidad.");
            return;
        }

        // Verificar si ya fue añadido
        for (VueloCompra vc : vuelosComprar) {
            if (vc.getIdVuelo() == vuelo.getIdVuelo()) {
                JOptionPane.showMessageDialog(this, "Este vuelo ya fue añadido.");
                return;
            }
        }

        // Agregar a lista y tabla
        vuelosComprar.add(new VueloCompra(vuelo.getIdVuelo(), cantidad));
        vuelosAgregados.add(vuelo);

        DefaultTableModel modelo = (DefaultTableModel) tblVuelos.getModel();
        modelo.addRow(new Object[]{
            vuelo.getCodigoVuelo(),
            vuelo.getCiudadOrigen().getNombreCiudad() + " a " + vuelo.getCiudadDestino().getNombreCiudad(),
            vuelo.getValor(),
            cantidad
        });
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblVuelosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVuelosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblVuelosMouseClicked

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
            java.util.logging.Logger.getLogger(ComprarFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ComprarFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ComprarFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ComprarFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ComprarFrm(new Usuario(1)).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnComprar;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSalir1;
    private javax.swing.JComboBox<String> cmbDestino;
    private javax.swing.JComboBox<String> cmbOrigen;
    private javax.swing.JComboBox<String> cmbVuelos;
    private javax.swing.JDialog dialogConfirm;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblViajesMsg;
    private javax.swing.JSpinner spinDate;
    private javax.swing.JSpinner spnNumBoletos;
    private javax.swing.JTable tblVuelos;
    // End of variables declaration//GEN-END:variables
}
