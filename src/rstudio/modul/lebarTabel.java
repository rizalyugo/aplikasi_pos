/*
 * Copyright (C) 2016 Dendi Pradigta <awak.dendi@gmail.com>
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
public class lebarTabel {

    public void lebarKolom(JTable jt, int kolom0, int kolom1, int kolom2, int kolom3, int kolom4, int kolom5, int value0, int value1, int value2, int value3, int value4, int value5) {
        TableColumn column;
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column = jt.getColumnModel().getColumn(kolom0);
        column.setPreferredWidth(value0);
        column = jt.getColumnModel().getColumn(kolom1);
        column.setPreferredWidth(value1);
        column = jt.getColumnModel().getColumn(kolom2);
        column.setPreferredWidth(value2);
        column = jt.getColumnModel().getColumn(kolom3);
        column.setPreferredWidth(value3);
        column = jt.getColumnModel().getColumn(kolom4);
        column.setPreferredWidth(value4);
        column = jt.getColumnModel().getColumn(kolom5);
        column.setPreferredWidth(value5);
    }
}
