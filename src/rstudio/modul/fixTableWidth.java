/*
 * Copyright (C) 2015 Dendi Pradigta <awak.dendi@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package rstudio.modul;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author Dendi Pradigta <awak.dendi@gmail.com>
 */
public class fixTableWidth {

    int columnIndex;
    int width;
    JTable table;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public void fixWidth() {
        TableColumn column = this.getTable().getColumnModel().getColumn(this.getColumnIndex());
        column.setMinWidth(this.getWidth());
        column.setMaxWidth(this.getWidth());
        column.setPreferredWidth(this.getWidth());
    }

}
