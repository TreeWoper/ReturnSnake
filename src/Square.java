/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sehaf
 */
public class Square {
    private int type;
    private int size;
    
    public Square(){
        type = 0;
        size = 60;
    }
    
    public Square(int t){
        type = t;
        size = 60;
    }
    
    public Square(int t, int s){
        type = t;
        size = s;
    }
    
    public void setType(int t){
        type = t;
    }
    
    public int getType(){
        return type;
    }
    
    public void setSize(int s){
        size = s;
    }
    
    public int getSize(){
        return size;
    }
}
