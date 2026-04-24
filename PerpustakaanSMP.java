import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class PerpustakaanSMP extends JFrame {
    private JTextField txtNis, txtNama, txtAlamat;
    private JTable table;
    private DefaultTableModel model;
    private final String FILE_NAME = "siswa.csv";

    public PerpustakaanSMP() {
        setTitle("Sistem Data Perpustakaan SMP");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblJudul = new JLabel("DATA SISWA PERPUSTAKAAN", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblJudul, BorderLayout.NORTH);

        JPanel pnlEntri = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlEntri.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pnlEntri.add(new JLabel("NIS:"));
        txtNis = new JTextField();
        pnlEntri.add(txtNis);
        
        pnlEntri.add(new JLabel("Nama Siswa:"));
        txtNama = new JTextField();
        pnlEntri.add(txtNama);
        
        pnlEntri.add(new JLabel("Alamat:"));
        txtAlamat = new JTextField();
        pnlEntri.add(txtAlamat);

        JPanel pnlTombol = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        
        pnlTombol.add(btnTambah);
        pnlTombol.add(btnEdit);
        pnlTombol.add(btnHapus);

        JPanel pnlAtas = new JPanel(new BorderLayout());
        pnlAtas.add(pnlEntri, BorderLayout.CENTER);
        pnlAtas.add(pnlTombol, BorderLayout.SOUTH);
        add(pnlAtas, BorderLayout.CENTER);

        String[] kolom = {"NIS", "Nama Siswa", "Alamat"};
        model = new DefaultTableModel(kolom, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(580, 250));
        add(scrollPane, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        table.getSelectionModel().addListSelectionListener(e -> isiFieldDariTabel());

        loadData();
    }

    private void loadData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String baris;
            while ((baris = br.readLine()) != null) {
                String[] data = baris.split(",");
                if (data.length == 3) {
                    model.addRow(data);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Peringatan: File " + FILE_NAME + " tidak ditemukan. File baru akan otomatis dibuat saat data disimpan.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan membaca file: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < model.getRowCount(); i++) {
                bw.write(model.getValueAt(i, 0) + "," + model.getValueAt(i, 1) + "," + model.getValueAt(i, 2));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan menyimpan file: " + e.getMessage());
        }
    }

    private void tambahData() {
        String nis = txtNis.getText().trim();
        String nama = txtNama.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String[] dataBaru = {nis, nama, alamat};
        model.addRow(dataBaru);
        saveData();
        kosongkanField();
    }

    private void editData() {
        int baris = table.getSelectedRow();
        if (baris >= 0) {
            String nisBaru = txtNis.getText().trim();            
            model.setValueAt(nisBaru, baris, 0);
            model.setValueAt(txtNama.getText().trim(), baris, 1);
            model.setValueAt(txtAlamat.getText().trim(), baris, 2);
                
            saveData(); 
            kosongkanField();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel yang ingin diedit.");
        }
    }

    private void hapusData() {
        int baris = table.getSelectedRow();
        if (baris >= 0) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                model.removeRow(baris);
                saveData(); 
                kosongkanField();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel yang ingin dihapus.");
        }
    }

    private void isiFieldDariTabel() {
        int baris = table.getSelectedRow();
        if (baris >= 0) {
            txtNis.setText(model.getValueAt(baris, 0).toString());
            txtNama.setText(model.getValueAt(baris, 1).toString());
            txtAlamat.setText(model.getValueAt(baris, 2).toString());
        }
    }

    private void kosongkanField() {
        txtNis.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PerpustakaanSMP().setVisible(true));
    }
}