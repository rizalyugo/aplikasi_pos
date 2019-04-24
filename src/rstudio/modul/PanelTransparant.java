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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Dendi pradigta <awak.dendi@gmail.com>
 */
public class PanelTransparant extends JPanel {

    private Color col;

    public PanelTransparant() {
        setOpaque(false);
        col = new Color(getBackground().getRed(), getBackground().getGreen(),
                getBackground().getBlue(), 120);
    } 

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        col = new Color(getBackground().getRed(), getBackground().getGreen(),
                getBackground().getBlue(), 120);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g.create();
        gr.setColor(col);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.dispose();
    }

}
