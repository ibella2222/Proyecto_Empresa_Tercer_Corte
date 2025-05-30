/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package co.edu.unicauca.proyectocurso.presentation;

import co.edu.unicauca.proyectocurso.access.ProjectRepositoryImpl;
import co.edu.unicauca.proyectocurso.domain.entities.Project;
import co.edu.unicauca.proyectocurso.domain.services.ProjectService;

import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ibell
 */
public class GUIEliminarProyectos extends javax.swing.JFrame {
    private DefaultTableModel modeloTabla;
    private ProjectService projectService;

    /**
     * Creates new form GUIEliminarProyectos
     */
    public GUIEliminarProyectos() {
        initComponents();
        configurarTabla();
         projectService = new ProjectService(new ProjectRepositoryImpl());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNIT = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        tableProyectos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Eliminar Proyecto");

        jLabel2.setText("Nit Empresa:");

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Eliminar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tableProyectos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollPane.setViewportView(tableProyectos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNIT, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jButton1)))
                        .addGap(66, 66, 66))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(26, 26, 26)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Evita edición manual de la tabla
        }
    };
    tableProyectos.setModel(modeloTabla);
    tableProyectos.setRowSelectionAllowed(true);
    }
    private void loadProjects(String companyNIT) {
    if (companyNIT == null || companyNIT.isBlank()) {
        JOptionPane.showMessageDialog(this, "Debe ingresar un NIT válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

    try {
        // Pasar el repositorio al servicio
        ProjectService projectService = new ProjectService(new ProjectRepositoryImpl());
        List<Project> projects = projectService.findProjectsByCompanyNIT(companyNIT);

        if (projects == null || projects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron proyectos para el NIT ingresado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Llenar la tabla con los proyectos encontrados
        for (Project p : projects) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getName(), p.getDescription()});
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Ocurrió un error al cargar los proyectos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Para depuración en consola
    }
}

    private void deleteSelectedProject() {
        int selectedRow = tableProyectos.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Obtener el ID del proyecto desde la tabla
            Object value = modeloTabla.getValueAt(selectedRow, 0);
            UUID projectId;

            if (value instanceof UUID) {
                projectId = (UUID) value;
            } else if (value instanceof String) {
                projectId = UUID.fromString((String) value);
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener el ID del proyecto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Confirmación antes de eliminar
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este proyecto?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Intentar eliminar el proyecto desde la base de datos
            boolean deleted = projectService.delete(projectId);

            if (deleted) {
                modeloTabla.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Proyecto eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el proyecto en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el proyecto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Mostrar detalles en la consola
        }

}
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        deleteSelectedProject();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String nit = txtNIT.getText().trim();
        loadProjects(nit);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(GUIEliminarProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIEliminarProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIEliminarProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIEliminarProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIEliminarProyectos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tableProyectos;
    private javax.swing.JTextField txtNIT;
    // End of variables declaration//GEN-END:variables
}
