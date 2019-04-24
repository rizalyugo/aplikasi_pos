/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.main;

/**
 *
 * @author AMJ
 */
public class TesLisensi {
   // String [] hurufdanangka;
    
    public static void main(String[] args) {
        String txt="rizal";
        char[] tes = txt.toCharArray();
        char[] hurufdanangka = {'1','2','3','4','5','6','a','b','c','d','e','f','g',
        'h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
   
        String key="";
        for (int i=0; i<hurufdanangka.length; i++){
            for(int a=0; a<tes.length; a++){
                if(tes[a] == hurufdanangka[i]){
                    key =key+ hurufdanangka[i+2];
                }
                //key=key+tes[a];
            }
            
        }
        System.out.print(key);
    }
    
    
}
