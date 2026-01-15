/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.SongController;
import Model.Song;
import java.awt.CardLayout;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author pappu
 */
public class MainFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());
    private SongController songController;
    private DefaultTableModel songTableModel;
    private DefaultTableModel resultTableModel;
    private DefaultTableModel baseTableModel;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {

        initComponents();
        setLocationRelativeTo(null);

        songController = new SongController();

        setupTables();
        loadData();
        setupListeners();
    }

    private void setupTables() {
        String[] columns = {"ID", "Title", "Artist", "Album", "Genre", "Year", "Duration"};

        songTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSongs.setModel(songTableModel);

        resultTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblResult.setModel(resultTableModel);

        baseTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBase.setModel(baseTableModel);
    }

    private void loadData() {
        // Load songs into table
        songController.populateTable(songTableModel, songController.getAllSongs());
        songController.populateTable(baseTableModel, songController.getAllSongs());

        // Update summary
        updateSummary();

        // Update recent songs
        updateRecentSongs();
    }

    private void updateSummary() {
        totalSong.setText("Total Songs: " + songController.getTotalSongs());
        txtSummary.setText(songController.getSummaryStatistics());
    }

    private void updateRecentSongs() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Get recent songs from controller
        java.util.List<Song> recentSongs = songController.getRecentSongs();

        // If there are no recent songs
        if (recentSongs == null || recentSongs.isEmpty()) {
            listModel.addElement("No recent songs yet.");
            listModel.addElement("Add songs to see them here.");
        } // If songs exist
        else {
            for (int i = 0; i < recentSongs.size(); i++) {
                Song song = recentSongs.get(i);
                listModel.addElement(song.toString());
            }
        }

        // Set model to JList
        jList1.setModel(listModel);
    }

    private void setupListeners() {

        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleAdd();
            }
        });

        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleUpdate();
            }
        });

        tblSongs.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {

            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

                // Avoid double execution
                if (evt.getValueIsAdjusting()) {
                    return;
                }

                // Check if a row is selected
                int selectedRow = tblSongs.getSelectedRow();
                if (selectedRow != -1) {
                    loadSelectedSong();
                }
            }
        }
        );
    }

    private void loadSelectedSong() {
        int row = tblSongs.getSelectedRow();
        if (row >= 0) {
            txtSongId.setText(songTableModel.getValueAt(row, 0).toString());
            txtTitle.setText(songTableModel.getValueAt(row, 1).toString());
            txtArtist.setText(songTableModel.getValueAt(row, 2).toString());
            txtAlbum.setText(songTableModel.getValueAt(row, 3).toString());
            txtGenre.setText(songTableModel.getValueAt(row, 4).toString());
            txtYear.setText(songTableModel.getValueAt(row, 5).toString());
            txtDuration.setText(songTableModel.getValueAt(row, 6).toString());
        }
    }

    private void handleAdd() {
        try {
            String id = txtSongId.getText().trim();
            String title = txtTitle.getText().trim();
            String artist = txtArtist.getText().trim();
            String album = txtAlbum.getText().trim();
            String genre = txtGenre.getText().trim();
            String yearStr = txtYear.getText().trim();
            String durationStr = txtDuration.getText().trim();

            // Validation
            if (id.isEmpty() || title.isEmpty() || artist.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "ID, Title, and Artist are required!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int year = Integer.parseInt(yearStr);
            int duration = Integer.parseInt(durationStr);

            Song newSong = new Song(id, title, artist, album, genre, year, duration);

            if (songController.addSong(newSong)) {
                JOptionPane.showMessageDialog(this,
                        "Song added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Song ID already exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Year and Duration must be valid numbers!",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        try {
            String id = txtSongId.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a song to update!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String title = txtTitle.getText().trim();
            String artist = txtArtist.getText().trim();
            String album = txtAlbum.getText().trim();
            String genre = txtGenre.getText().trim();
            int year = Integer.parseInt(txtYear.getText().trim());
            int duration = Integer.parseInt(txtDuration.getText().trim());

            Song updatedSong = new Song(id, title, artist, album, genre, year, duration);

            if (songController.updateSong(updatedSong)) {
                JOptionPane.showMessageDialog(this,
                        "Song updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Song not found!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = tblSongs.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a song to delete!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String songId = songTableModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this song?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (songController.deleteSong(songId)) {
                JOptionPane.showMessageDialog(this,
                        "Song deleted! Use 'Undo Delete' to restore.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearFields();
            }
        }
    }

    private void handleUndo() {
        Song song = songController.undoDelete();
        if (song != null) {
            JOptionPane.showMessageDialog(this,
                    "Song restored: " + song.getTitle(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No deleted songs to restore!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleLinearSearch() {
        String keyword = txtSearch.getText().trim();
        String searchBy = cmbSearchBy.getSelectedItem().toString();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a search keyword!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        var results = songController.linearSearch(keyword, searchBy);
        songController.populateTable(resultTableModel, results);

        JOptionPane.showMessageDialog(this,
                "Found " + results.size() + " matching song(s)",
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        txtSongId.setText(songController.generateNextId());
        txtTitle.setText("");
        txtArtist.setText("");
        txtAlbum.setText("");
        txtGenre.setText("");
        txtYear.setText("");
        txtDuration.setText("");
    }

    private void handleSort() {
        String sortBy = jComboBox1.getSelectedItem().toString();
        boolean ascending = ascRadio.isSelected();

        var sortedSongs = songController.sortSongs(sortBy, ascending);
        songController.populateTable(baseTableModel, sortedSongs);

        String order = ascending ? "ascending" : "descending";
        JOptionPane.showMessageDialog(this,
                "Songs sorted by " + sortBy + " in " + order + " order",
                "Sort Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        MainPanel = new javax.swing.JPanel();
        homePanel = new javax.swing.JPanel();
        panelSummary = new javax.swing.JPanel();
        totalSong = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSummary = new javax.swing.JTextArea();
        panelRecent = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        btnPanel = new javax.swing.JPanel();
        btnManageCrud = new javax.swing.JButton();
        btnSearchSort = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        lblHomebg = new javax.swing.JLabel();
        crudPanel = new javax.swing.JPanel();
        pnlCrudTop = new javax.swing.JPanel();
        lblSongs = new javax.swing.JLabel();
        scrollCrud = new javax.swing.JScrollPane();
        tblSongs = new javax.swing.JTable();
        pnlCrudBottom = new javax.swing.JPanel();
        pnlFields = new javax.swing.JPanel();
        lblSongId = new javax.swing.JLabel();
        txtSongId = new javax.swing.JTextField();
        lblTitle = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        lblArtist = new javax.swing.JLabel();
        txtArtist = new javax.swing.JTextField();
        lblAlbum = new javax.swing.JLabel();
        txtAlbum = new javax.swing.JTextField();
        lblGenre = new javax.swing.JLabel();
        lblYear = new javax.swing.JLabel();
        txtGenre = new javax.swing.JTextField();
        lblDuration = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        txtDuration = new javax.swing.JTextField();
        pnlButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUndo = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        lblCrudBg = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        pnlSearchHeader = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        pnlSearchControl = new javax.swing.JPanel();
        lblSearch = new javax.swing.JLabel();
        lblKeyword = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        lblSearchBy = new javax.swing.JLabel();
        cmbSearchBy = new javax.swing.JComboBox<>();
        btnLinearSearch = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        pnlSortControl = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblSortBy = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        ascRadio = new javax.swing.JRadioButton();
        descRadio = new javax.swing.JRadioButton();
        btnBackHomeSearch = new javax.swing.JButton();
        sortBtn = new javax.swing.JButton();
        pnlResults = new javax.swing.JPanel();
        pnlResult = new javax.swing.JPanel();
        spResult = new javax.swing.JScrollPane();
        tblResult = new javax.swing.JTable();
        pnlBaseSong = new javax.swing.JPanel();
        spBase = new javax.swing.JScrollPane();
        tblBase = new javax.swing.JTable();
        lblSearchBg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Music Playlist Organizer");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setType(java.awt.Window.Type.POPUP);

        MainPanel.setLayout(new java.awt.CardLayout());

        homePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSummary.setBackground(new java.awt.Color(10, 25, 50));
        panelSummary.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 204, 255), 2, true), "Summary Statistics", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        panelSummary.setForeground(new java.awt.Color(255, 255, 255));
        panelSummary.setOpaque(false);

        totalSong.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        totalSong.setForeground(new java.awt.Color(255, 255, 255));
        totalSong.setText("Total Songs");

        jScrollPane1.setBackground(new java.awt.Color(15, 23, 42));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(64, 180, 255), 2));

        txtSummary.setEditable(false);
        txtSummary.setBackground(new java.awt.Color(255, 255, 255));
        txtSummary.setColumns(20);
        txtSummary.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        txtSummary.setForeground(new java.awt.Color(220, 240, 255));
        txtSummary.setLineWrap(true);
        txtSummary.setRows(5);
        txtSummary.setWrapStyleWord(true);
        txtSummary.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(90, 180, 255)));
        txtSummary.setCaretColor(new java.awt.Color(180, 230, 255));
        txtSummary.setOpaque(false);
        jScrollPane1.setViewportView(txtSummary);

        javax.swing.GroupLayout panelSummaryLayout = new javax.swing.GroupLayout(panelSummary);
        panelSummary.setLayout(panelSummaryLayout);
        panelSummaryLayout.setHorizontalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalSong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSummaryLayout.setVerticalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalSong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addContainerGap())
        );

        homePanel.add(panelSummary, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 410, 440));

        panelRecent.setBackground(new java.awt.Color(8, 20, 40));
        panelRecent.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 204, 255), 2), "Recently Added", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 24), new java.awt.Color(255, 255, 255))); // NOI18N
        panelRecent.setForeground(new java.awt.Color(255, 255, 255));
        panelRecent.setOpaque(false);
        panelRecent.setPreferredSize(new java.awt.Dimension(418, 444));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));

        jList1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(190, 220, 255), 2));
        jList1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { ".No Recent Songs Yet.", "Add Songs to see them here." };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionBackground(new java.awt.Color(60, 120, 200));
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout panelRecentLayout = new javax.swing.GroupLayout(panelRecent);
        panelRecent.setLayout(panelRecentLayout);
        panelRecentLayout.setHorizontalGroup(
            panelRecentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRecentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelRecentLayout.setVerticalGroup(
            panelRecentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRecentLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                .addContainerGap())
        );

        homePanel.add(panelRecent, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 440, 440));

        btnPanel.setBackground(new java.awt.Color(255, 255, 255));
        btnPanel.setForeground(new java.awt.Color(220, 235, 255));
        btnPanel.setOpaque(false);

        btnManageCrud.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        btnManageCrud.setForeground(new java.awt.Color(255, 255, 255));
        btnManageCrud.setText("Manage Songs");
        btnManageCrud.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(90, 180, 255), 2));
        btnManageCrud.setContentAreaFilled(false);
        btnManageCrud.setFocusPainted(false);
        btnManageCrud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageCrudActionPerformed(evt);
            }
        });

        btnSearchSort.setBackground(new java.awt.Color(15, 25, 45));
        btnSearchSort.setFont(new java.awt.Font("Serif", 1, 22)); // NOI18N
        btnSearchSort.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchSort.setText("Search and Sort");
        btnSearchSort.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(64, 180, 255), 2));
        btnSearchSort.setContentAreaFilled(false);
        btnSearchSort.setFocusPainted(false);
        btnSearchSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchSortActionPerformed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        btnExit.setForeground(new java.awt.Color(0, 204, 204));
        btnExit.setText("Exit");
        btnExit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(64, 180, 255), 2));
        btnExit.setContentAreaFilled(false);
        btnExit.setFocusPainted(false);

        javax.swing.GroupLayout btnPanelLayout = new javax.swing.GroupLayout(btnPanel);
        btnPanel.setLayout(btnPanelLayout);
        btnPanelLayout.setHorizontalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPanelLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(btnManageCrud, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(btnSearchSort, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        btnPanelLayout.setVerticalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPanelLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnManageCrud, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchSort, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        homePanel.add(btnPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 600, 900, 170));

        lblHomebg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/ChatGPT Image Jan 5, 2026, 08_08_07 PM.png"))); // NOI18N
        homePanel.add(lblHomebg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 770));

        MainPanel.add(homePanel, "HOME");

        crudPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlCrudTop.setOpaque(false);

        lblSongs.setBackground(new java.awt.Color(220, 235, 255));
        lblSongs.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        lblSongs.setForeground(new java.awt.Color(255, 255, 255));
        lblSongs.setText("Songs");

        scrollCrud.setBackground(new java.awt.Color(15, 25, 45));
        scrollCrud.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 235), 2));
        scrollCrud.setForeground(new java.awt.Color(200, 230, 235));
        scrollCrud.setOpaque(false);

        tblSongs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Title ", "Artist", "Album", "Genre", "Year", "Duration"
            }
        ));
        scrollCrud.setViewportView(tblSongs);

        javax.swing.GroupLayout pnlCrudTopLayout = new javax.swing.GroupLayout(pnlCrudTop);
        pnlCrudTop.setLayout(pnlCrudTopLayout);
        pnlCrudTopLayout.setHorizontalGroup(
            pnlCrudTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCrudTopLayout.createSequentialGroup()
                .addGap(281, 281, 281)
                .addComponent(lblSongs, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(463, Short.MAX_VALUE))
            .addGroup(pnlCrudTopLayout.createSequentialGroup()
                .addComponent(scrollCrud)
                .addContainerGap())
        );
        pnlCrudTopLayout.setVerticalGroup(
            pnlCrudTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCrudTopLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblSongs)
                .addGap(18, 18, 18)
                .addComponent(scrollCrud, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        crudPanel.add(pnlCrudTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 470));

        pnlCrudBottom.setOpaque(false);

        pnlFields.setOpaque(false);

        lblSongId.setBackground(new java.awt.Color(220, 235, 255));
        lblSongId.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblSongId.setForeground(new java.awt.Color(255, 255, 255));
        lblSongId.setText("Song ID");

        txtSongId.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtSongId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        txtSongId.setCaretColor(new java.awt.Color(30, 30, 30));

        lblTitle.setBackground(new java.awt.Color(220, 235, 255));
        lblTitle.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Title");

        txtTitle.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtTitle.setForeground(new java.awt.Color(30, 30, 30));
        txtTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        txtTitle.setCaretColor(new java.awt.Color(30, 30, 30));
        txtTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTitleActionPerformed(evt);
            }
        });

        lblArtist.setBackground(new java.awt.Color(220, 235, 255));
        lblArtist.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblArtist.setForeground(new java.awt.Color(255, 255, 255));
        lblArtist.setText("Artist");

        txtArtist.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtArtist.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        txtArtist.setCaretColor(new java.awt.Color(30, 30, 30));

        lblAlbum.setBackground(new java.awt.Color(220, 235, 255));
        lblAlbum.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblAlbum.setForeground(new java.awt.Color(255, 255, 255));
        lblAlbum.setText("Album");

        txtAlbum.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtAlbum.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        txtAlbum.setCaretColor(new java.awt.Color(30, 30, 30));
        txtAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlbumActionPerformed(evt);
            }
        });

        lblGenre.setBackground(new java.awt.Color(220, 235, 255));
        lblGenre.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblGenre.setForeground(new java.awt.Color(255, 255, 255));
        lblGenre.setText("Genre");

        lblYear.setBackground(new java.awt.Color(220, 235, 255));
        lblYear.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblYear.setForeground(new java.awt.Color(255, 255, 255));
        lblYear.setText("Year");

        txtGenre.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtGenre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        txtGenre.setCaretColor(new java.awt.Color(30, 30, 30));

        lblDuration.setBackground(new java.awt.Color(220, 235, 255));
        lblDuration.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblDuration.setForeground(new java.awt.Color(255, 255, 255));
        lblDuration.setText("Duration(sec)");

        txtYear.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        txtYear.setCaretColor(new java.awt.Color(30, 30, 30));

        txtDuration.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtDuration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        txtDuration.setCaretColor(new java.awt.Color(30, 30, 30));

        javax.swing.GroupLayout pnlFieldsLayout = new javax.swing.GroupLayout(pnlFields);
        pnlFields.setLayout(pnlFieldsLayout);
        pnlFieldsLayout.setHorizontalGroup(
            pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFieldsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSongId, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                    .addComponent(lblSongId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFieldsLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFieldsLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFieldsLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(lblArtist, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFieldsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtArtist, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)))
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFieldsLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(lblGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFieldsLayout.createSequentialGroup()
                        .addComponent(txtAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFieldsLayout.createSequentialGroup()
                        .addComponent(lblYear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFieldsLayout.createSequentialGroup()
                        .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDuration)
                    .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );
        pnlFieldsLayout.setVerticalGroup(
            pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFieldsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSongId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSongId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtArtist, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pnlButtons.setOpaque(false);

        btnAdd.setBackground(new java.awt.Color(15, 25, 45));
        btnAdd.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(200, 230, 235));
        btnAdd.setText("Add");
        btnAdd.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 190, 255), 2, true));
        btnAdd.setContentAreaFilled(false);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(15, 25, 45));
        btnUpdate.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(200, 230, 235));
        btnUpdate.setText("Update");
        btnUpdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        btnUpdate.setContentAreaFilled(false);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setFocusPainted(false);

        btnDelete.setBackground(new java.awt.Color(15, 25, 45));
        btnDelete.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        btnDelete.setContentAreaFilled(false);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUndo.setBackground(new java.awt.Color(15, 25, 45));
        btnUndo.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnUndo.setForeground(new java.awt.Color(200, 230, 235));
        btnUndo.setText("Undo Delete (Stack)");
        btnUndo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        btnUndo.setContentAreaFilled(false);
        btnUndo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUndo.setFocusPainted(false);
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(15, 25, 45));
        btnClear.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnClear.setForeground(new java.awt.Color(200, 230, 235));
        btnClear.setText("Clear");
        btnClear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        btnClear.setContentAreaFilled(false);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnHome.setBackground(new java.awt.Color(15, 25, 45));
        btnHome.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnHome.setForeground(new java.awt.Color(200, 230, 235));
        btnHome.setText("Back Home");
        btnHome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        btnHome.setContentAreaFilled(false);
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.setFocusPainted(false);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCrudBottomLayout = new javax.swing.GroupLayout(pnlCrudBottom);
        pnlCrudBottom.setLayout(pnlCrudBottomLayout);
        pnlCrudBottomLayout.setHorizontalGroup(
            pnlCrudBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlButtons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCrudBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlCrudBottomLayout.setVerticalGroup(
            pnlCrudBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCrudBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        crudPanel.add(pnlCrudBottom, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 510, 940, 240));

        lblCrudBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/ChatGPT Image Jan 5, 2026, 08_08_07 PM.png"))); // NOI18N
        crudPanel.add(lblCrudBg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 750));

        MainPanel.add(crudPanel, "CRUD");

        searchPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlSearchHeader.setOpaque(false);

        lblHeader.setFont(new java.awt.Font("SansSerif", 1, 30)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(220, 235, 255));
        lblHeader.setText("Search and Sort");

        pnlSearchControl.setOpaque(false);

        lblSearch.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        lblSearch.setForeground(new java.awt.Color(220, 235, 255));
        lblSearch.setText("Search");

        lblKeyword.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblKeyword.setForeground(new java.awt.Color(220, 235, 255));
        lblKeyword.setText("Keyword");

        txtSearch.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(30, 30, 30));
        txtSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));

        lblSearchBy.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        lblSearchBy.setForeground(new java.awt.Color(220, 235, 255));
        lblSearchBy.setText("Search By");

        cmbSearchBy.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        cmbSearchBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Title", "Artist", "Album", "Genre", "Year" }));
        cmbSearchBy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));

        btnLinearSearch.setFont(new java.awt.Font("SansSerif", 3, 18)); // NOI18N
        btnLinearSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnLinearSearch.setText("Linear Search");
        btnLinearSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        btnLinearSearch.setContentAreaFilled(false);
        btnLinearSearch.setFocusPainted(false);
        btnLinearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinearSearchActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Silom", 3, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(200, 230, 255));
        jButton2.setText("Binary Search");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);

        javax.swing.GroupLayout pnlSearchControlLayout = new javax.swing.GroupLayout(pnlSearchControl);
        pnlSearchControl.setLayout(pnlSearchControlLayout);
        pnlSearchControlLayout.setHorizontalGroup(
            pnlSearchControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSearchControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSearchControlLayout.createSequentialGroup()
                        .addComponent(lblKeyword)
                        .addGap(20, 20, 20)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSearchControlLayout.createSequentialGroup()
                        .addGroup(pnlSearchControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblSearchBy, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(lblSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(23, 23, 23)
                        .addComponent(cmbSearchBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlSearchControlLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnLinearSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        pnlSearchControlLayout.setVerticalGroup(
            pnlSearchControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSearchControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(pnlSearchControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSearchBy, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSearchBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlSearchControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLinearSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pnlSortControl.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(220, 235, 255));
        jLabel1.setText("Sort");

        lblSortBy.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        lblSortBy.setForeground(new java.awt.Color(220, 235, 255));
        lblSortBy.setText("Sort  By");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Title", "Artist", "Album", "Genre", "Year", "Duration" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));

        buttonGroup1.add(ascRadio);
        ascRadio.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        ascRadio.setForeground(new java.awt.Color(220, 235, 255));
        ascRadio.setText("Ascending");

        buttonGroup1.add(descRadio);
        descRadio.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        descRadio.setForeground(new java.awt.Color(220, 235, 255));
        descRadio.setText("Descending");

        btnBackHomeSearch.setFont(new java.awt.Font("SansSerif", 3, 17)); // NOI18N
        btnBackHomeSearch.setForeground(new java.awt.Color(200, 230, 255));
        btnBackHomeSearch.setText("Back Home");
        btnBackHomeSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        btnBackHomeSearch.setContentAreaFilled(false);
        btnBackHomeSearch.setFocusPainted(false);
        btnBackHomeSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeSearchActionPerformed(evt);
            }
        });

        sortBtn.setText("Sort");
        sortBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSortControlLayout = new javax.swing.GroupLayout(pnlSortControl);
        pnlSortControl.setLayout(pnlSortControlLayout);
        pnlSortControlLayout.setHorizontalGroup(
            pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSortControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSortControlLayout.createSequentialGroup()
                        .addGroup(pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlSortControlLayout.createSequentialGroup()
                                .addComponent(lblSortBy)
                                .addGap(32, 32, 32)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlSortControlLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(sortBtn)
                        .addGap(64, 64, 64)
                        .addGroup(pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSortControlLayout.createSequentialGroup()
                                .addComponent(btnBackHomeSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(54, 54, 54))
                            .addGroup(pnlSortControlLayout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ascRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(descRadio, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                                .addContainerGap(85, Short.MAX_VALUE))))))
        );
        pnlSortControlLayout.setVerticalGroup(
            pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSortControlLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSortBy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ascRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSortControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSortControlLayout.createSequentialGroup()
                        .addComponent(descRadio)
                        .addGap(24, 24, 24)
                        .addComponent(btnBackHomeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sortBtn))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlSearchHeaderLayout = new javax.swing.GroupLayout(pnlSearchHeader);
        pnlSearchHeader.setLayout(pnlSearchHeaderLayout);
        pnlSearchHeaderLayout.setHorizontalGroup(
            pnlSearchHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchHeaderLayout.createSequentialGroup()
                .addContainerGap(179, Short.MAX_VALUE)
                .addComponent(pnlSearchControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSortControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlSearchHeaderLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(lblHeader)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSearchHeaderLayout.setVerticalGroup(
            pnlSearchHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSearchHeaderLayout.createSequentialGroup()
                .addGroup(pnlSearchHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSearchHeaderLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pnlSortControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSearchHeaderLayout.createSequentialGroup()
                        .addComponent(lblHeader)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(pnlSearchControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );

        searchPanel.add(pnlSearchHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 300));

        pnlResults.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255), 2));
        pnlResults.setOpaque(false);
        pnlResults.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlResult.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255)), "Results", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Helvetica Neue", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        pnlResult.setOpaque(false);

        spResult.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255)));

        tblResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Title", "Artist", "Album", "Genre", "Year", "Duration"
            }
        ));
        spResult.setViewportView(tblResult);

        javax.swing.GroupLayout pnlResultLayout = new javax.swing.GroupLayout(pnlResult);
        pnlResult.setLayout(pnlResultLayout);
        pnlResultLayout.setHorizontalGroup(
            pnlResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spResult, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        pnlResultLayout.setVerticalGroup(
            pnlResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spResult, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        pnlResults.add(pnlResult, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 520, 360));

        pnlBaseSong.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 190, 255)), "Base Song List", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Helvetica Neue", 1, 17), new java.awt.Color(255, 255, 255))); // NOI18N
        pnlBaseSong.setOpaque(false);

        tblBase.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Title ", "Artist", "Album", "Genre", "Year", "Duration"
            }
        ));
        spBase.setViewportView(tblBase);

        javax.swing.GroupLayout pnlBaseSongLayout = new javax.swing.GroupLayout(pnlBaseSong);
        pnlBaseSong.setLayout(pnlBaseSongLayout);
        pnlBaseSongLayout.setHorizontalGroup(
            pnlBaseSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spBase, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        );
        pnlBaseSongLayout.setVerticalGroup(
            pnlBaseSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBaseSongLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(spBase, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlResults.add(pnlBaseSong, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 530, 360));

        searchPanel.add(pnlResults, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 1080, 380));

        lblSearchBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/ChatGPT Image Jan 5, 2026, 08_08_07 PM.png"))); // NOI18N
        searchPanel.add(lblSearchBg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 770));

        MainPanel.add(searchPanel, "SEARCH");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 899, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnManageCrudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageCrudActionPerformed
        CardLayout cl = (CardLayout) MainPanel.getLayout();
        cl.show(MainPanel, "CRUD");// TODO add your handling code here:
    }//GEN-LAST:event_btnManageCrudActionPerformed

    private void txtTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTitleActionPerformed

    private void txtAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlbumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlbumActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        // TODO add your handling code here:
        handleUndo();
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnBackHomeSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackHomeSearchActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) MainPanel.getLayout();
        cl.show(MainPanel, "HOME");
    }//GEN-LAST:event_btnBackHomeSearchActionPerformed

    private void btnLinearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinearSearchActionPerformed
        // TODO add your handling code here:
        handleLinearSearch();
    }//GEN-LAST:event_btnLinearSearchActionPerformed

    private void btnSearchSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchSortActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) MainPanel.getLayout();
        cl.show(MainPanel, "SEARCH");
    }//GEN-LAST:event_btnSearchSortActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) MainPanel.getLayout();
        cl.show(MainPanel, "HOME");
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnAddActionPerformed

    private void sortBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortBtnActionPerformed
        // TODO add your handling code here:
        handleSort();
    }//GEN-LAST:event_sortBtnActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        handleDelete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public void main(String args[]) {
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPanel;
    private javax.swing.JRadioButton ascRadio;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBackHomeSearch;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnLinearSearch;
    private javax.swing.JButton btnManageCrud;
    private javax.swing.JPanel btnPanel;
    private javax.swing.JButton btnSearchSort;
    private javax.swing.JButton btnUndo;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbSearchBy;
    private javax.swing.JPanel crudPanel;
    private javax.swing.JRadioButton descRadio;
    private javax.swing.JPanel homePanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAlbum;
    private javax.swing.JLabel lblArtist;
    private javax.swing.JLabel lblCrudBg;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblHomebg;
    private javax.swing.JLabel lblKeyword;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSearchBg;
    private javax.swing.JLabel lblSearchBy;
    private javax.swing.JLabel lblSongId;
    private javax.swing.JLabel lblSongs;
    private javax.swing.JLabel lblSortBy;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblYear;
    private javax.swing.JPanel panelRecent;
    private javax.swing.JPanel panelSummary;
    private javax.swing.JPanel pnlBaseSong;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlCrudBottom;
    private javax.swing.JPanel pnlCrudTop;
    private javax.swing.JPanel pnlFields;
    private javax.swing.JPanel pnlResult;
    private javax.swing.JPanel pnlResults;
    private javax.swing.JPanel pnlSearchControl;
    private javax.swing.JPanel pnlSearchHeader;
    private javax.swing.JPanel pnlSortControl;
    private javax.swing.JScrollPane scrollCrud;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JButton sortBtn;
    private javax.swing.JScrollPane spBase;
    private javax.swing.JScrollPane spResult;
    private javax.swing.JTable tblBase;
    private javax.swing.JTable tblResult;
    private javax.swing.JTable tblSongs;
    private javax.swing.JLabel totalSong;
    private javax.swing.JTextField txtAlbum;
    private javax.swing.JTextField txtArtist;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextField txtGenre;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSongId;
    private javax.swing.JTextArea txtSummary;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
}
