/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gr0v3r.cifradofrancmason;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
/**
 *
 * @author gr0v3r
 */
public class OutDialog extends java.awt.Dialog {
    
    private Frame parent;
    /**
     * Creates new form OutDialog
     */
    public OutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        this.setTitle("Cifrado Pig Pen - gr0v3r");
        
        /**
         * Cuando se realiza un clic sobre la imagen, esta se guarda en disco
         */
        sheet1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){                                
                JFileChooser fileChooser = new JFileChooser();
                //fileChooser.setCurrentDirectory(new java.io.File("E:\\imagenes\\"));
                if ( fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ){                    
                    sheet1.saveBufferedImage(fileChooser.getSelectedFile());
                    JOptionPane.showMessageDialog(null, "Save: " + fileChooser.getSelectedFile() );                
                }
            }
         });
    }
    
    public void setText(String text){
        if(text.length()>0){            
            sheet1.cifrar(text);                      
            this.setSize( sheet1.getSize().width+24, sheet1.getSize().height+60  );
            setLocationRelativeTo(parent);      
            sheet1.repaint();            
        }else{
            setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sheet1 = new com.gr0v3r.cifradofrancmason.Sheet();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(sheet1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OutDialog dialog = new OutDialog(new java.awt.Frame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.gr0v3r.cifradofrancmason.Sheet sheet1;
    // End of variables declaration//GEN-END:variables
}
