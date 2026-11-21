import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 1. INTERFACE: Kontrak perilaku transaksi
interface Pembayaran {
    double hitungTotal();
    String cetakStruk();
}

// 2. ABSTRACT CLASS: Kerangka dasar objek Produk
abstract class Produk implements Pembayaran {
    protected String namaProduk;
    protected double hargaDasar;
    protected int jumlah;

    public Produk(String namaProduk, double hargaDasar, int jumlah) {
        this.namaProduk = namaProduk;
        this.hargaDasar = hargaDasar;
        this.jumlah = jumlah;
    }

    // Abstract method yang wajib di-override oleh subclass
    public abstract double getBiayaTambahan();

    @Override
    public double hitungTotal() {
        return (hargaDasar * jumlah) + getBiayaTambahan();
    }
}

// Subclass 1: Produk Digital (Tanpa ongkir, pajak 11%)
class ProdukDigital extends Produk {
    public ProdukDigital(String namaProduk, double hargaDasar, int jumlah) {
        super(namaProduk, hargaDasar, jumlah);
    }

    @Override
    public double getBiayaTambahan() {
        return (hargaDasar * jumlah) * 0.11; // PPN 11%
    }

    @Override
    public String cetakStruk() {
        return "=== STRUK PRODUK DIGITAL ===\n"
             + "Item          : " + namaProduk + "\n"
             + "Harga Satuan  : Rp " + hargaDasar + "\n"
             + "Jumlah        : " + jumlah + "\n"
             + "PPN (11%)     : Rp " + getBiayaTambahan() + "\n"
             + "Total Bayar   : Rp " + hitungTotal();
    }
}

// Subclass 2: Produk Fisik (Ada biaya kirim Rp 15.000)
class ProdukFisik extends Produk {
    public ProdukFisik(String namaProduk, double hargaDasar, int jumlah) {
        super(namaProduk, hargaDasar, jumlah);
    }

    @Override
    public double getBiayaTambahan() {
        return 15000; // Ongkos kirim flat
    }

    @Override
    public String cetakStruk() {
        return "=== STRUK PRODUK FISIK ===\n"
             + "Item          : " + namaProduk + "\n"
             + "Harga Satuan  : Rp " + hargaDasar + "\n"
             + "Jumlah        : " + jumlah + "\n"
             + "Ongkos Kirim  : Rp " + getBiayaTambahan() + "\n"
             + "Total Bayar   : Rp " + hitungTotal();
    }
}

// 3. CLASS UTAMA GUI (5 Komponen: JLabel, JTextField, JComboBox, JRadioButton, JButton)
public class Main extends JFrame {

    private JLabel lblJudul, lblBarang, lblHarga, lblJumlah, lblJenis;
    private JComboBox<String> cmbBarang;
    private JTextField txtHarga, txtJumlah;
    private JRadioButton rbDigital, rbFisik;
    private ButtonGroup bgJenis;
    private JButton btnProses, btnReset;

    public Main() {
        setTitle("Sistem Kasir OOP - Gunanta");
        setSize(420, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Komponen 1: JLabel
        lblJudul = new JLabel("KASIR TOKO (ABSTRACT & INTERFACE)", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblJudul, gbc);

        gbc.gridwidth = 1;

        // Komponen 2: JComboBox
        lblBarang = new JLabel("Pilih Produk:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblBarang, gbc);

        String[] daftarBarang = {"Lisensi Software", "E-Book Pemrograman", "Keyboard Mechanical", "Mouse Wireless"};
        cmbBarang = new JComboBox<>(daftarBarang);
        gbc.gridx = 1; gbc.gridy = 1;
        add(cmbBarang, gbc);

        // Komponen 3: JTextField
        lblHarga = new JLabel("Harga Satuan (Rp):");
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblHarga, gbc);

        txtHarga = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        add(txtHarga, gbc);

        lblJumlah = new JLabel("Jumlah Beli:");
        gbc.gridx = 0; gbc.gridy = 3;
        add(lblJumlah, gbc);

        txtJumlah = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 3;
        add(txtJumlah, gbc);

        // Komponen 4: JRadioButton
        lblJenis = new JLabel("Kategori Produk:");
        gbc.gridx = 0; gbc.gridy = 4;
        add(lblJenis, gbc);

        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rbDigital = new JRadioButton("Digital (PPN 11%)");
        rbFisik = new JRadioButton("Fisik (Ongkir 15rb)");
        rbDigital.setSelected(true);

        bgJenis = new ButtonGroup();
        bgJenis.add(rbDigital);
        bgJenis.add(rbFisik);

        panelRadio.add(rbDigital);
        panelRadio.add(rbFisik);
        gbc.gridx = 1; gbc.gridy = 4;
        add(panelRadio, gbc);

        // Komponen 5: JButton
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnProses = new JButton("Hitung & Cetak");
        btnReset = new JButton("Reset");
        panelTombol.add(btnProses);
        panelTombol.add(btnReset);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(panelTombol, gbc);

        // Aksi Tombol Proses
        btnProses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nama = (String) cmbBarang.getSelectedItem();
                    double harga = Double.parseDouble(txtHarga.getText());
                    int jumlah = Integer.parseInt(txtJumlah.getText());

                    Produk transaksi;
                    if (rbDigital.isSelected()) {
                        transaksi = new ProdukDigital(nama, harga, jumlah);
                    } else {
                        transaksi = new ProdukFisik(nama, harga, jumlah);
                    }

                    JOptionPane.showMessageDialog(null, transaksi.cetakStruk(), "Struk Pembayaran", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Masukkan angka yang valid pada Harga dan Jumlah!", "Error Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Aksi Tombol Reset
        btnReset.addActionListener(e -> {
            cmbBarang.setSelectedIndex(0);
            txtHarga.setText("");
            txtJumlah.setText("");
            rbDigital.setSelected(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}