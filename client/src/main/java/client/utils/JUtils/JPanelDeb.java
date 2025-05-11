package client.utils.JUtils;

import server.managers.ConfigManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JPanelDeb extends JPanel {
    public JPanelDeb(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public JPanelDeb(LayoutManager layout) {
        super(layout);
    }

    public JPanelDeb(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public JPanelDeb() {
        super();
    }

    @Override
    public void setBorder(Border border) {
        if (ConfigManager.getClientDebugMode()) {
            super.setBorder(border);
        }
    }

    public void setAlwaysVisibleBorder(Border border) {
        super.setBorder(border);
    }
}
