package game.core;

import javax.swing.table.AbstractTableModel;

public class GameMapTableModel extends AbstractTableModel {
    private int[][] gameMap;

    public GameMapTableModel(int[][] gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public int getRowCount() {
        return gameMap.length;
    }

    @Override
    public int getColumnCount() {
        return gameMap[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return gameMap[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Integer.class;
    }
}
