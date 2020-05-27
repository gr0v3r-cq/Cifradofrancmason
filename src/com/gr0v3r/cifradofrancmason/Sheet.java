/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gr0v3r.cifradofrancmason;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author gr0v3r
 */
public class Sheet extends JPanel {
    private final String[] APLHA_ARRAY = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private List<Letter> letterList = new ArrayList<Letter>();
    private final int MAX_NUM_COLUMN = 32;
    private final int SIZE_BOX = 24;
    private final Color BG_COLOR = new Color(255,255,255);
    private Dimension dimencion;
    private BufferedImage bufferedImage = null;
    private int heightSheet = 0;
    private int widthSheet = 0;
    
    public Sheet(){
        super();
        dimencion = new Dimension(100,100);
        Sheet.this.setBackground(BG_COLOR);
        Sheet.this.setSize(dimencion);
        Sheet.this.setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 =(Graphics2D) g;        
        bufferedImage = new BufferedImage( widthSheet, heightSheet, BufferedImage.TYPE_INT_RGB );
        Graphics2D graphics2D = bufferedImage.createGraphics();        
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setColor( BG_COLOR );
        graphics2D.fill(new Rectangle2D.Double(0,0,widthSheet,heightSheet)); 
        //pinta letras/caracter
        for(Letter l:letterList)
        {            
            l.drawLetter(graphics2D);            
        }
        g2.drawImage(bufferedImage, 0, 0, this);
    }
    
    /**
     * Metodo para escribir en disco
     * @param file
     */
    public void saveBufferedImage(File file){
        try {           
            if( bufferedImage!=null)
                ImageIO.write(bufferedImage, "png", file);            
	} catch (IOException e) {            
            System.err.println("IOException:" + e.getMessage());
	}
    }
    
    /**
     * Cifra una cadena de texto y añade letra cifrada en un arrayList
     * @param text texto a cifrar
     */
    public void cifrar(String text){
        String[] textArray = text.split("");        
        //elimina contenido
        letterList.clear();        
        int row=0;
        int col=0;
        //añade cada letra/caracter en un lista de objetos LETTER junto a su 
        //respectiva posicion en la matriz col x row
        for(String letter: textArray){            
            letterList.add( new Letter(letter, new Point(SIZE_BOX*col,SIZE_BOX*row)) );
            col++;
            if(col == MAX_NUM_COLUMN){
                col=0;
                row++;
            }            
        }        
        //redimensiona JPanel
        dimencion = new Dimension(MAX_NUM_COLUMN*SIZE_BOX,SIZE_BOX*row);
        setPreferredSize(dimencion);
        setSize(dimencion);        
        heightSheet = SIZE_BOX*(row+1);     
        widthSheet = MAX_NUM_COLUMN*SIZE_BOX;
        repaint();        
    }
    
    /**
     * Metodo para descifrar el contenido de una imagen cifrado por PigPen
     * @param file
     */
    public void descifrar(File file){
        //genera un abecedario de imagenes cifradas
        BufferedImage[] alpha = new BufferedImage[APLHA_ARRAY.length];
        for(int i=0; i<APLHA_ARRAY.length;i++){
            Letter tmp = new Letter(APLHA_ARRAY[i]);
            alpha[i] = tmp.getImage(true);
        }
        //genera un abecedario de imagenes no cifradas
        BufferedImage[] abc = new BufferedImage[APLHA_ARRAY.length];
        for(int i=0; i<APLHA_ARRAY.length;i++){
            Letter tmp = new Letter(APLHA_ARRAY[i]);
            abc[i] = tmp.getImage(false);
        }        
        //lee imagen de archivo
        try {        
            BufferedImage bImage = ImageIO.read(file);             
            //validacion
            if(bImage.getWidth()<SIZE_BOX*MAX_NUM_COLUMN || bImage.getHeight()<SIZE_BOX ){
                JOptionPane.showMessageDialog(null, "Error: La imagen es incorrecta");
                return;
            }
            //crea un array de imagenes extrayendo imagenes 24x24 de imagen principal
            int rows = bImage.getHeight()/SIZE_BOX;
            int cols = MAX_NUM_COLUMN;
            BufferedImage[] imageArray = new BufferedImage[rows * cols];                        
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {
                    imageArray[(i * cols) + j] = bImage.getSubimage(j * SIZE_BOX, i * SIZE_BOX, SIZE_BOX,SIZE_BOX);
                }
            }
            //Array de imagenes destino, aqui se colocal las letras descifradas asi como otros caracteres
            BufferedImage[] imageArrayTarget = new BufferedImage[rows * cols];
            
            //compara el array de imagenes con array de imagenes A..Z cifradas y va colocando en array de imagenes destino
            for (int k = 0; k < imageArray.length; k++){
                int val=-1;
                for(int p=0; p<alpha.length;p++){//abcdario                    
                    double res = compare(imageArray[k],alpha[p]);                        
                    if( res < 1){                    
                        val = p;                    
                        break;
                    }         
                }
                if(val!=-1){
                    imageArrayTarget[k] = abc[val];
                }else{
                   imageArrayTarget[k] = imageArray[k]; 
                }
            }            
            
            //Crea una imagen en memoria 
            bufferedImage = new BufferedImage(SIZE_BOX*cols, SIZE_BOX*rows, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setColor( BG_COLOR );
            graphics2D.fill(new Rectangle2D.Double(0,0,SIZE_BOX*cols,SIZE_BOX*rows)); 
            int col=0;
            int row=0;
            for(BufferedImage o:imageArrayTarget){
                graphics2D.drawImage(o, null, SIZE_BOX*col, row);    
                col++;
                if(col==MAX_NUM_COLUMN)
                {
                    col=0;
                    row +=SIZE_BOX;
                }
            }        
            //guarda imagen en disco
            try {    
                String absolutePath = file.getAbsolutePath();        
                String newPath = absolutePath .substring(0, absolutePath .length()-4) + "_" + System.currentTimeMillis() + ".png";                
                ImageIO.write(bufferedImage, "png", new File(newPath));            
                JOptionPane.showMessageDialog(null, "Save: " + newPath );                
                graphics2D.dispose();
            } catch (IOException e) {            
                System.err.println("IOException:" + e.getMessage());
            }             
        } catch (IOException ex) {
            System.err.println("IOException" + ex.getMessage());
        }
        
    }
    
    /**
     * Metodo para comparar imagenes
     * @param BufferedImage imagen 1
     * @param BufferedImage imagen 2
     * @return double mientras más alto es el valor de retorno las imagenes son menos iguales
     *    0 : imagene son iguales
     *  100 : las imagenes no son iguales
     * Fuente codigo: http://rosettacode.org/wiki/Percentage_difference_between_images
     */
    private double compare(BufferedImage img1, BufferedImage img2){
        int width1 = img1.getWidth(null);        
        int height1 = img1.getHeight(null);        
            long diff = 0;
            for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
              int rgb1 = img1.getRGB(x, y);
              int rgb2 = img2.getRGB(x, y);              
              diff += Math.abs(((rgb1 >> 16) & 0xff) - ((rgb2 >> 16) & 0xff));              
              diff += Math.abs(((rgb1 >>  8) & 0xff) - ((rgb2 >>  8) & 0xff));              
              diff += Math.abs(((rgb1      ) & 0xff) - ((rgb2      ) & 0xff));
            }
          }
          double n = width1 * height1 * 3;
          double p = diff / n / 255.0;          
          return p * 100.0;
    }
}
