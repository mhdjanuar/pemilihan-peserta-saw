/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package application.views;

import application.dao.KaryawanDao;
import application.daoimpl.KaryawanDaoImpl;
import application.models.KaryawanModel;
import application.models.UserModel;
import application.utils.DatabaseUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

    /**
     *
     * @author mhdja
     */
   public class Karyawan extends javax.swing.JPanel {
        public final KaryawanDao karyawanDao;
        public String selectedId;
        public String batch;
        public String kursus;

        public void getAllData() {
             // Ambil data karyawan dari database
             List<KaryawanModel> karyawanList = karyawanDao.findAll();

             // Set Model untuk JTable
             DefaultTableModel model = new DefaultTableModel();
             model.setColumnIdentifiers(new Object[]{
                 "ID", "Nama", "Usia", "Alamat", "Batch", "Kursus"
             });

             // Masukkan data karyawan ke dalam model JTable
             for (KaryawanModel karyawan : karyawanList) {
                 model.addRow(new Object[]{
                     karyawan.getId(), // disimpan tapi tidak ditampilkan
                     karyawan.getName(),
                     karyawan.getUsia(),
                     karyawan.getAlamat(),
                     karyawan.getBatch(),
                     karyawan.getKursus()
                 });
             }

             // Set model ke JTable
             jTable1.setModel(model);

             // Sembunyikan kolom ID
             jTable1.getColumnModel().getColumn(0).setMinWidth(0);
             jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
             jTable1.getColumnModel().getColumn(0).setWidth(0);
         }
        
        public void getBatchAndKursus(String batch, String kursus) {
            System.out.println("getBatchAndKursus() called with batch: " + batch + ", kursus: " + kursus);

            // Ambil data karyawan dari database
            List<KaryawanModel> karyawanList = karyawanDao.findBatchAndKursus(batch, kursus);
            System.out.println("Jumlah data karyawan ditemukan: " + karyawanList.size());

            // Set Model untuk JTable
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{
                "ID", "Nama", "Usia", "Alamat", "Batch", "Kursus"
            });

            // Masukkan data karyawan ke dalam model JTable
            for (KaryawanModel karyawan : karyawanList) {
                System.out.println("Memasukkan data karyawan: ID=" + karyawan.getId()
                    + ", Nama=" + karyawan.getName()
                    + ", Usia=" + karyawan.getUsia()
                    + ", Alamat=" + karyawan.getAlamat()
                    + ", Batch=" + karyawan.getBatch()
                    + ", Kursus=" + karyawan.getKursus());

                model.addRow(new Object[]{
                    karyawan.getId(),
                    karyawan.getName(),
                    karyawan.getUsia(),
                    karyawan.getAlamat(),
                    karyawan.getBatch(),
                    karyawan.getKursus()
                });
            }

            // Set model ke JTable
            jTable1.setModel(model);
            System.out.println("Model berhasil di-set ke jTable1.");

            // Sembunyikan kolom ID
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(0).setWidth(0);
            System.out.println("Kolom ID disembunyikan.");
        }

        
        
        private void showKaryawanDetailDialog(String nama, String usia, String alamat, String batch, String kursus) {
            // Buat panel
            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Tambahkan komponen ke panel
            panel.add(new JLabel("Nama     : " + nama));
            panel.add(new JLabel("Usia     : " + usia));
            panel.add(new JLabel("Alamat   : " + alamat));
            panel.add(new JLabel("Batch    : " + batch));
            panel.add(new JLabel("Kursus   : " + kursus));

            // Tampilkan dialog
            JOptionPane.showMessageDialog(
                null,
                panel,
                "Detail Karyawan",
                JOptionPane.PLAIN_MESSAGE
            );
        }
    /**
     * Creates new form ListDataView
     */
    public Karyawan() {
        this.karyawanDao = new KaryawanDaoImpl();
        
        initComponents();
        
        getAllData();
        
        // Tambahkan event listener pada JTable
       jTable1.getSelectionModel().addListSelectionListener(e -> {
            // Cegah event dua kali saat update
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                int selectedRow = jTable1.getSelectedRow();

                // Ambil data dari baris yang diklik
                String nama = jTable1.getValueAt(selectedRow, 1).toString();
                String usia = jTable1.getValueAt(selectedRow, 2).toString();
                String alamat = jTable1.getValueAt(selectedRow, 3).toString();
                String batch = jTable1.getValueAt(selectedRow, 4).toString();  // Tambahan batch
                String kursus = jTable1.getValueAt(selectedRow, 5).toString(); // Tambahan kursus

                this.selectedId = jTable1.getValueAt(selectedRow, 0).toString();

                // Tampilkan ke form
                txtNama.setText(nama);
                txtUsia.setText(usia);
                txtAddress.setText(alamat);
                txtBatch.setText(batch);  // Set batch ke field txtBatch
                cbKursus.setSelectedItem(kursus);  // Set kursus ke combo box
                
                showKaryawanDetailDialog(nama, usia, alamat, batch, kursus);
            }
        });
    }
    
    public void clearForm() {
        // Clear all the text fields
        txtNama.setText("");  // Menghapus teks di text field Nama
        txtUsia.setText("");  // Menghapus teks di text field Usia
        txtAddress.setText("");  // Menghapus teks di text field Alamat

        // Clear gender selection (radio button group)
        gender.clearSelection();  // Menghapus pilihan pada button group Gender

        // Log untuk memastikan form di-clear
        System.out.println("Form berhasil dibersihkan.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gender = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        txtUsia = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtBatch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbKursus = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtBatch1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbKursus1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        setPreferredSize(new java.awt.Dimension(700, 700));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Masukan Nama");

        jLabel2.setText("Masukan Usia");

        jLabel6.setText("Alamat");

        jButton1.setText("SIMPAN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("UBAH");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("HAPUS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setText("Masukan Batch");

        jLabel4.setText("Masukan Kursus");

        cbKursus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hemat", "Dasar", "Terampil", "Mahir" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(txtUsia, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                    .addComponent(txtNama)
                    .addComponent(txtAddress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3)
                        .addComponent(txtBatch)
                        .addComponent(jLabel4)
                        .addComponent(cbKursus, 0, 297, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton3)
                            .addComponent(jButton4))
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(28, 28, 28)))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setText("Batch :");

        jLabel7.setText("Kursus  :");

        cbKursus1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hemat", "Dasar", "Terampil", "Mahir" }));
        cbKursus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKursus1ActionPerformed(evt);
            }
        });

        jButton2.setText("CARI");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("RESET");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBatch1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbKursus1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtBatch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cbKursus1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       // Ambil nilai input dari form
        String nama = txtNama.getText().trim();
        String usiaText = txtUsia.getText().trim();
        String alamat = txtAddress.getText().trim();
        String batch = txtBatch.getText().trim();  // Tambahan untuk batch
        String kursus = (String) cbKursus.getSelectedItem();  // Tambahan untuk kursus

        // Log input yang diterima
        System.out.println("Nama: " + nama);
        System.out.println("Usia: " + usiaText);
        System.out.println("Alamat: " + alamat);
        System.out.println("Batch: " + batch);
        System.out.println("Kursus: " + kursus);

        // Validasi usia angka
        int usia = 0;
        try {
            usia = Integer.parseInt(usiaText);
            System.out.println("Usia valid: " + usia);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Usia harus berupa angka.");
            return;
        }

        // Set ke model
        KaryawanModel karyawan = new KaryawanModel();
        karyawan.setName(nama);
        karyawan.setUsia(usia);
        karyawan.setAlamat(alamat);
        karyawan.setBatch(batch);
        karyawan.setKursus(kursus);

        // Log data yang akan disimpan
        System.out.println("Data Karyawan akan disimpan:");
        System.out.println("Nama: " + karyawan.getName());
        System.out.println("Usia: " + karyawan.getUsia());
        System.out.println("Kontak: " + karyawan.getKontak());
        System.out.println("Email: " + karyawan.getEmail());
        System.out.println("Alamat: " + karyawan.getAlamat());
        System.out.println("Gender: " + karyawan.getGender());
        System.out.println("Batch: " + karyawan.getBatch());
        System.out.println("Kursus: " + karyawan.getKursus());

        // Simpan ke DB
        int result = karyawanDao.create(karyawan);
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Data karyawan berhasil disimpan.");
            getAllData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data karyawan.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
         // Ambil nilai input dari form
        String nama = txtNama.getText().trim();
        String usiaText = txtUsia.getText().trim();
        String alamat = txtAddress.getText().trim();
        String batch = txtBatch.getText().trim();  // Tambahan untuk batch
        String kursus = (String) cbKursus.getSelectedItem();  // Tambahan untuk kursus

        // Validasi usia angka
        int usia = 0;
        try {
            usia = Integer.parseInt(usiaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Usia harus berupa angka.");
            return;
        }

        // Set ke model
        KaryawanModel karyawan = new KaryawanModel();
        karyawan.setId(parseInt(this.selectedId)); // ID Karyawan yang akan diupdate
        karyawan.setName(nama);
        karyawan.setUsia(usia);
        karyawan.setAlamat(alamat);
        karyawan.setBatch(batch);
        karyawan.setKursus(kursus);

        // Panggil fungsi update di DAO
        int result = karyawanDao.update(karyawan);
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Data karyawan berhasil diperbarui.");
            getAllData();  // Refresh data yang ada di tabel
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data karyawan.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
      
            int success = karyawanDao.deleteKaryawan(parseInt(this.selectedId));
            if (success > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus.");
                getAllData(); // reload data tabel
                clearForm();
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus data.");
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cbKursus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKursus1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbKursus1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String batch = txtBatch1.getText().trim();  // Tambahan untuk batch
        String kursus = (String) cbKursus1.getSelectedItem();  // Tambahan untuk kursus

        this.batch = batch;
        this.kursus = kursus;

        getBatchAndKursus(batch, kursus);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        txtBatch.setText("");
        this.batch = "";
        this.kursus = "";
        getAllData();
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbKursus;
    private javax.swing.JComboBox<String> cbKursus1;
    private javax.swing.ButtonGroup gender;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBatch;
    private javax.swing.JTextField txtBatch1;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtUsia;
    // End of variables declaration//GEN-END:variables
}
