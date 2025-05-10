package client.screens;

import client.client.UDPClient;
import client.utils.*;
import server.object.MovieGenre;
import server.object.MpaaRating;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainScreen {
    private final UDPClient client;

    private JFrame authorizationFrame = null;

    private JFrame frame;
    private JComboBox<String> languageComboBox;
    private JToggleButton tableModeButton;
    private JToggleButton scheduleModeButton;

    private JPanelDeb workspacePanel;
    private JPanelDeb tablePanel;
    private JPanelDeb schedulePanel;

    private final Color buttonPanelsColor = new Color(200, 213, 236);

    public MainScreen(UDPClient client) {
        this.client = client;
    }

    public MainScreen(UDPClient client, JFrame frame) {
        this.authorizationFrame = frame;
        this.client = client;
    }

    public void run() {
        initializeFrame();
    }

    private void initializeFrame() {
        frame = new JFrame(Languages.get("mainScreen"));
        frame.setSize(new Dimension(1000, 550));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(authorizationFrame);
        if (authorizationFrame != null) authorizationFrame.dispose();

        frame.add(buildMainPanel());
        frame.setVisible(true);
    }

    private JPanelDeb buildMainPanel() {
        JPanelDeb mainPanel = new JPanelDeb(new GridBagLayout());
        mainPanel.setBorder(new LineBorder(Color.RED, 2));

        mainPanel.add(buildLeftPanel(), buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 0.2, 1));
        mainPanel.add(buildRightPanel(), buildGBC(1, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 0.8, 1));
        return mainPanel;
    }

    private JPanelDeb buildLeftPanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.GREEN, 2));
        panel.setBackground(buttonPanelsColor);

        panel.add(buildSettingsPanel(), buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 1, 0));
        panel.add(buildCommandsJScrollPanel(), buildGBC(0, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        return panel;
    }

    private JPanelDeb buildSettingsPanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.RED, 2));
        panel.setMinimumSize(new Dimension(0, 60));
        panel.setBackground(buttonPanelsColor);

        panel.add(buildSettingButton("☺"), buildGBC(0, 0, GridBagConstraints.NONE, 10, 18, 10, 5, 0, 0));
        panel.add(buildSettingButton("☼"), buildGBC(1, 0, GridBagConstraints.NONE, 10, 0, 10, 5, 0, 0));

        languageComboBox = buildLanguageComboBox();
        panel.add(languageComboBox, buildGBC(2, 0, GridBagConstraints.HORIZONTAL, 10, 0, 10, 10, 1, 0));
        return panel;
    }

    private JButton buildSettingButton(String text) {
        JButton button = new JButton("<html><body style='white-space:nowrap;'>" + text + "</body></html>");

        button.setPreferredSize(new Dimension(40, 40));
        button.setMinimumSize(new Dimension(40, 40));
        button.setMaximumSize(new Dimension(40, 40));

        return button;
    }

    private GridBagConstraints buildGBC(int gridx, int gridy, int fill, int top, int left, int bottom, int right, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.fill = fill;
        gbc.insets = new Insets(top, left, bottom, right);
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        return gbc;
    }

    private ActionListener buildToggleButtonActionListener(JToggleButton clicked, JToggleButton turnedOf, JPanel clickedPanel, JPanel turnedOfPanel) {
        return e -> {
            if (clicked.isSelected()) {
                turnedOf.setSelected(false);
                if (workspacePanel != null && turnedOfPanel != null) {
                    workspacePanel.remove(turnedOfPanel);
                    if (clickedPanel != null) {
                        workspacePanel.add(clickedPanel, buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
                    }
                    workspacePanel.revalidate();
                    workspacePanel.repaint();
                }
            }
            if (!clicked.isSelected()) {
                SwingUtilities.invokeLater(() -> clicked.setSelected(true));
            }
        };
    }

    private void buildToggleButtons() {
        tableModeButton = buildSelectModeButton("table");
        tableModeButton.setSelected(true);
        scheduleModeButton = buildSelectModeButton("schedule");
        tableModeButton.addActionListener(buildToggleButtonActionListener(tableModeButton, scheduleModeButton, tablePanel, schedulePanel));
        scheduleModeButton.addActionListener(buildToggleButtonActionListener(scheduleModeButton, tableModeButton, schedulePanel, tablePanel));
    }

    private JToggleButton buildSelectModeButton(String key) {
        JToggleButton button = new JToggleButton(Languages.get(key));
        button.setPreferredSize(new Dimension(0, 40));
        freezeButtonSize(button); // из-за перевода размер текста увеличивался, из-за чего пропорции кнопок менялись
        return button;
    }

    private void freezeButtonSize(JToggleButton button) {
        Dimension size = button.getPreferredSize();
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
    }

    private JComboBox<String> buildLanguageComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(Languages.getLangs());
        comboBox.setSelectedItem(Languages.getLanguage());
        comboBox.setPreferredSize(new Dimension(0, 40));
        comboBox.setMinimumSize(new Dimension(0, 40));
        comboBox.addActionListener(e -> {
            int selectedLanguage = comboBox.getSelectedIndex();
            Languages.setLanguage(Languages.getLocalNames()[selectedLanguage]);
            updateLocal();
        });
        return comboBox;
    }

    private void updateLocal() {
        frame.setTitle(Languages.get("mainScreen"));
        tableModeButton.setText(Languages.get("table"));
        scheduleModeButton.setText(Languages.get("schedule"));
    }

    private JPanelDeb buildRightPanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.YELLOW, 2));

        panel.add(buildWorkspacePanel(), buildGBC(0, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        panel.add(buildSelectModeButtonPanel(), buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 1, 0));
        return panel;
    }

    private JPanelDeb buildSelectModeButtonPanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.GRAY, 2));
        panel.setPreferredSize(new Dimension(0, 60));
        panel.setBackground(buttonPanelsColor);

        buildToggleButtons();
        panel.add(tableModeButton, buildGBC(0, 0, GridBagConstraints.BOTH, 10, 0, 10, 5, 0.45, 1));
        panel.add(scheduleModeButton, buildGBC(1, 0, GridBagConstraints.BOTH, 10, 5, 10, 10, 0.45, 1));

        return panel;
    }

    private JPanelDeb buildCommandsJScrollPanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.GRAY, 2));

        panel.add(buildCommandsJScrollPane(), buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        return panel;
    }

    private JScrollPane buildCommandsJScrollPane() {
        JScrollPane pane = new JScrollPane(buildCommandsPanel(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setBorder(BorderFactory.createEmptyBorder());
        moveScrollBarToLeft(pane);
        return pane;
    }

    private void moveScrollBarToLeft(JScrollPane pane) {
        pane.setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                super.layoutContainer(parent);

                if (vsb != null && viewport != null) {
                    Rectangle vpBounds = viewport.getBounds();
                    Dimension vsbSize = vsb.getPreferredSize();

                    vsb.setBounds(
                            vpBounds.x,
                            vpBounds.y,
                            vsbSize.width,
                            vpBounds.height
                    );
                    viewport.setBounds(
                            vpBounds.x + vsbSize.width,
                            vpBounds.y,
                            vpBounds.width,
                            vpBounds.height
                    );
                }
            }
        });
    }

    private JPanelDeb buildCommandsPanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.PINK, 2));
        panel.setBackground(buttonPanelsColor);
        panel.add(buildCommandButton("help"), buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 3, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 4, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 5, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 6, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 7, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 8, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 9, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 10, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 11, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 12, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 13, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(buildCommandButton("help1"), buildGBC(0, 14, GridBagConstraints.HORIZONTAL, 1, 1, 0, 10, 1, 0));
        panel.add(Box.createGlue(), buildGBC(0, 15, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        return panel;
    }


    private JButton buildCommandButton(String key) {
        JButton button = new JButton(Languages.get(key));
        button.setPreferredSize(new Dimension(0, 40));
        button.addActionListener(e -> {
            System.out.println(frame.getSize().toString());
        });
        return button;
    }

    private JPanelDeb buildWorkspacePanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setAlwaysVisibleBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        workspacePanel = panel;

        tablePanel = buildTablePanel();
        schedulePanel = buildSchedulePanel();

        workspacePanel.add(tablePanel, buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        return panel;
    }

    private JPanelDeb buildTablePanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.CYAN, 4));

        panel.add(buildTableJScrollPane(), buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        return panel;
    }

    private JScrollPane buildTableJScrollPane() {
        JScrollPane scrollPane = new JScrollPane(buildTable());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private JTable buildTable() {
        JMovieTable table = new JMovieTable(new JMovieTableModel());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);
        table.setAutoCreateColumnsFromModel(false);
        table.getTableHeader().addMouseListener(new JMovieTableModel.JMovieTableHeaderMouseReader(table));
        JComboBox<MovieGenre> genreComboBox = new JComboBox<>(MovieGenre.values());
        JComboBox<MpaaRating> ratingComboBox = new JComboBox<>(MpaaRating.values());
        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(genreComboBox));
        table.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(ratingComboBox));
        table.getColumnModel().getColumn(0).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(1).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(2).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(3).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(5).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(9).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(10).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(11).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getColumnModel().getColumn(12).setCellEditor(new ValidatingCellEditor(new JTextField(), table));
        table.getModel().addTableModelListener(e -> {
            table.autoResizeColumnWidth();
        });

        return table;
    }

    private JPanelDeb buildSchedulePanel() {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.MAGENTA, 4));

        return panel;
    }
}
