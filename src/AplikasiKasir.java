import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AplikasiKasir extends JFrame {
    
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    
    private JTable tabelProduk;
    private DefaultTableModel modelTabel;
    private JTextField fieldKode, fieldNama, fieldHarga, fieldJumlah;
    private JTextField fieldTotal, fieldBayar, fieldKembalian;
    private JButton tombolTambah, tombolHapus, tombolBayar, tombolCetak, tombolBaru;
    private JTextArea areaCetak;
    
    
    private ArrayList<Produk> daftarProduk;
    private double totalHarga = 0;
    
    public AplikasiKasir() {
        
        super("Aplikasi Kasir Aaisyah Nur Haniifah");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        daftarProduk = new ArrayList<>();
        
        
        setupUI();
        
        setVisible(true);
    }
    
    private void setupUI() {
        
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        
        JPanel panelInput = new JPanel(new GridLayout(5, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Produk"));
        
        
        panelInput.add(new JLabel("Kode Produk:"));
        fieldKode = new JTextField();
        panelInput.add(fieldKode);
        
        panelInput.add(new JLabel("Nama Produk:"));
        fieldNama = new JTextField();
        panelInput.add(fieldNama);
        
        panelInput.add(new JLabel("Harga (Rp):"));
        fieldHarga = new JTextField();
        panelInput.add(fieldHarga);
        
        panelInput.add(new JLabel("Jumlah:"));
        fieldJumlah = new JTextField();
        panelInput.add(fieldJumlah);
        
        
        tombolTambah = new JButton("Tambah Produk");
        tombolHapus = new JButton("Hapus Produk");
        
        JPanel panelTombolInput = new JPanel(new GridLayout(1, 2, 5, 0));
        panelTombolInput.add(tombolTambah);
        panelTombolInput.add(tombolHapus);
        panelInput.add(new JLabel(""));
        panelInput.add(panelTombolInput);
        
        
        modelTabel = new DefaultTableModel(
                new Object[]{"No", "Kode", "Nama Produk", "Harga", "Jumlah", "Subtotal"}, 0);
        tabelProduk = new JTable(modelTabel);
        JScrollPane scrollTabel = new JScrollPane(tabelProduk);
        
        
        JPanel panelPembayaran = new JPanel(new GridLayout(3, 2, 5, 5));
        panelPembayaran.setBorder(BorderFactory.createTitledBorder("Pembayaran"));
        
        panelPembayaran.add(new JLabel("Total Harga:"));
        fieldTotal = new JTextField();
        fieldTotal.setEditable(false);
        panelPembayaran.add(fieldTotal);
        
        panelPembayaran.add(new JLabel("Jumlah Bayar:"));
        fieldBayar = new JTextField();
        panelPembayaran.add(fieldBayar);
        
        panelPembayaran.add(new JLabel("Kembalian:"));
        fieldKembalian = new JTextField();
        fieldKembalian.setEditable(false);
        panelPembayaran.add(fieldKembalian);
        
        
        JPanel panelTombolBayar = new JPanel(new GridLayout(1, 3, 5, 0));
        tombolBayar = new JButton("Proses Bayar");
        tombolCetak = new JButton("Cetak Struk");
        tombolBaru = new JButton("Transaksi Baru");
        
        panelTombolBayar.add(tombolBayar);
        panelTombolBayar.add(tombolCetak);
        panelTombolBayar.add(tombolBaru);
        
        
        areaCetak = new JTextArea();
        areaCetak.setEditable(false);
        areaCetak.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollStruk = new JScrollPane(areaCetak);
        scrollStruk.setBorder(BorderFactory.createTitledBorder("Struk Belanja"));
        
        
        JPanel panelKiri = new JPanel(new BorderLayout(0, 10));
        panelKiri.add(panelInput, BorderLayout.NORTH);
        panelKiri.add(scrollTabel, BorderLayout.CENTER);
        panelKiri.add(panelPembayaran, BorderLayout.SOUTH);
        
        
        JPanel panelKanan = new JPanel(new BorderLayout(0, 10));
        panelKanan.add(scrollStruk, BorderLayout.CENTER);
        panelKanan.add(panelTombolBayar, BorderLayout.SOUTH);
        
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelKiri, panelKanan);
        splitPane.setResizeWeight(0.6);
        panelUtama.add(splitPane, BorderLayout.CENTER);
        
        
        setContentPane(panelUtama);
        
        
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        
        tombolTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahProduk();
            }
        });
        
        
        tombolHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusProduk();
            }
        });
        
        
        tombolBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesPembayaran();
            }
        });
        
        
        tombolCetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cetakStruk();
            }
        });
        
        
        tombolBaru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transaksiBaru();
            }
        });
    }
    
    private void tambahProduk() {
        try {
            
            if (fieldKode.getText().isEmpty() || fieldNama.getText().isEmpty() || 
                fieldHarga.getText().isEmpty() || fieldJumlah.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", 
                                           "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String kode = fieldKode.getText();
            String nama = fieldNama.getText();
            double harga = Double.parseDouble(fieldHarga.getText());
            int jumlah = Integer.parseInt(fieldJumlah.getText());
            
            if (harga <= 0 || jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Harga dan jumlah harus lebih dari 0!", 
                                           "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
            double subtotal = harga * jumlah;
            
            
            Produk produk = new Produk(kode, nama, harga, jumlah, subtotal);
            daftarProduk.add(produk);
            
            
            modelTabel.addRow(new Object[]{
                modelTabel.getRowCount() + 1,
                kode,
                nama,
                CURRENCY_FORMAT.format(harga),
                jumlah,
                CURRENCY_FORMAT.format(subtotal)
            });
            
            
            totalHarga += subtotal;
            fieldTotal.setText(CURRENCY_FORMAT.format(totalHarga));
            
            
            fieldKode.setText("");
            fieldNama.setText("");
            fieldHarga.setText("");
            fieldJumlah.setText("");
            fieldKode.requestFocus();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid untuk harga dan jumlah!", 
                                       "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusProduk() {
        int selectedRow = tabelProduk.getSelectedRow();
        if (selectedRow != -1) {
            
            totalHarga -= daftarProduk.get(selectedRow).getSubtotal();
            fieldTotal.setText(CURRENCY_FORMAT.format(totalHarga));
            
            
            daftarProduk.remove(selectedRow);
            modelTabel.removeRow(selectedRow);
            
            
            for (int i = 0; i < modelTabel.getRowCount(); i++) {
                modelTabel.setValueAt(i + 1, i, 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk yang akan dihapus terlebih dahulu!", 
                                       "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void prosesPembayaran() {
        if (daftarProduk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada produk yang ditambahkan!", 
                                       "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double bayar = Double.parseDouble(fieldBayar.getText());
            if (bayar < totalHarga) {
                JOptionPane.showMessageDialog(this, "Jumlah pembayaran kurang!", 
                                           "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double kembalian = bayar - totalHarga;
            fieldKembalian.setText(CURRENCY_FORMAT.format(kembalian));
            
            
            tombolCetak.setEnabled(true);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah yang valid untuk pembayaran!", 
                                       "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cetakStruk() {
        if (fieldKembalian.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Proses pembayaran terlebih dahulu!", 
                                       "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String tglTransaksi = dateFormat.format(new Date());
        
        StringBuilder struk = new StringBuilder();
        struk.append("===================================================\n");
        struk.append("                STRUK PEMBAYARAN                   \n");
        struk.append("===================================================\n");
        struk.append("Tanggal: ").append(tglTransaksi).append("\n");
        struk.append("---------------------------------------------------\n");
        struk.append(String.format("%-5s %-15s %-15s %5s %12s\n", 
                    "No", "Nama Produk", "Harga", "Qty", "Subtotal"));
        struk.append("---------------------------------------------------\n");
        
        for (int i = 0; i < daftarProduk.size(); i++) {
            Produk p = daftarProduk.get(i);
            struk.append(String.format("%-5d %-15s %-15s %5d %12s\n", 
                        (i + 1), 
                        p.getNama(), 
                        CURRENCY_FORMAT.format(p.getHarga()),
                        p.getJumlah(),
                        CURRENCY_FORMAT.format(p.getSubtotal())));
        }
        
        struk.append("---------------------------------------------------\n");
        struk.append(String.format("%-36s %12s\n", "TOTAL:", CURRENCY_FORMAT.format(totalHarga)));
        
        double bayar = 0;
        try {
            bayar = Double.parseDouble(fieldBayar.getText());
        } catch (NumberFormatException ex) {
            bayar = 0;
        }
        
        double kembalian = bayar - totalHarga;
        
        struk.append(String.format("%-36s %12s\n", "TUNAI:", CURRENCY_FORMAT.format(bayar)));
        struk.append(String.format("%-36s %12s\n", "KEMBALIAN:", CURRENCY_FORMAT.format(kembalian)));
        struk.append("===================================================\n");
        struk.append("               Terima Kasih                        \n");
        struk.append("           Selamat Datang Kembali                  \n");
        struk.append("===================================================\n");
        
        areaCetak.setText(struk.toString());
    }
    
    private void transaksiBaru() {
        
        daftarProduk.clear();
        modelTabel.setRowCount(0);
        totalHarga = 0;
        
        
        fieldKode.setText("");
        fieldNama.setText("");
        fieldHarga.setText("");
        fieldJumlah.setText("");
        fieldTotal.setText("");
        fieldBayar.setText("");
        fieldKembalian.setText("");
        areaCetak.setText("");
        
        
        tombolCetak.setEnabled(false);
        
        
        fieldKode.requestFocus();
    }
    
    
    private class Produk {
        private String kode;
        private String nama;
        private double harga;
        private int jumlah;
        private double subtotal;
        
        public Produk(String kode, String nama, double harga, int jumlah, double subtotal) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
            this.jumlah = jumlah;
            this.subtotal = subtotal;
        }
        
        public String getKode() {
            return kode;
        }
        
        public String getNama() {
            return nama;
        }
        
        public double getHarga() {
            return harga;
        }
        
        public int getJumlah() {
            return jumlah;
        }
        
        public double getSubtotal() {
            return subtotal;
        }
    }
    
    public static void main(String[] args) {
       
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AplikasiKasir();
            }
        });
    }
}