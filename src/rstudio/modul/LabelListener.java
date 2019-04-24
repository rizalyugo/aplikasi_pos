package rstudio.modul;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

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
/**
 *
 * @author Dendi Pradigta <awak.dendi@gmail.com>
 */
public class LabelListener implements ActionListener {

    JLabel lbl;
    //Konstruktor

    public LabelListener(JLabel lblParam) {
        lbl = lblParam;
    } //Akhir Konstruktor

    public void actionPerformed(ActionEvent act) {
        String text = lbl.getText();
        String text2 = text.substring(1) + text.substring(0, 1);
        lbl.setText(text2);
    }
}
