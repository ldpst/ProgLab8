package client.utils;

import java.awt.*;

public class GBCUtils {
    public static GridBagConstraints buildGBC(int gridx, int gridy, int fill, int top, int left, int bottom, int right, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.fill = fill;
        gbc.insets = new Insets(top, left, bottom, right);
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        return gbc;
    }
}
