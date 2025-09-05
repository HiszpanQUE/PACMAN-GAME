package game.core;

import javax.swing.*;

public class ResizableTable extends JTable {
    public ResizableTable(GameMapTableModel model) {
        super(model);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    @Override
    public void doLayout() {
        int totalWidth = getWidth();
        int totalHeight = getHeight();

        int rows = getRowCount();
        int cols = getColumnCount();

        int cellWidth = totalWidth / cols;
        int cellHeight = totalHeight / rows;
        int cellSize = Math.min(cellWidth, cellHeight);

        for (int row = 0; row < rows; row++) {
            setRowHeight(row, cellSize);
        }

        for (int col = 0; col < cols; col++) {
            getColumnModel().getColumn(col).setPreferredWidth(cellSize);
        }

        super.doLayout();
    }
}
