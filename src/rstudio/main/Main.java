/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.main;

import com.alee.laf.WebLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import rstudio.main.Login;

/**
 *
 * @author DAFI
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
//            WebLookAndFeel.install();
//            
//            Login l = new Login();
//            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//            l.setLocation(dim.width/2-l.getSize().width/2, dim.height/2-l.getSize().height/2);
//            l.setVisible(true);
//        } catch (Exception e) {
//        }
//    }

try {
            // TODO code application logic here
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.invokeLater(new Runnable() {


            @Override
            public void run() {
                
             Login l = new Login();
                
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                l.setLocation(dim.width/2-l.getSize().width/2, dim.height/2-l.getSize().height/2);
                l.setVisible(true);
             
            }
        });
    }
}
