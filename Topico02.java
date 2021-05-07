/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topico02;

/**
 *
 * @author heito
 */
public class Topico02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ProgExemplo lex = new ProgExemplo(args[0]);
        Token t = null;
        while((t = lex.proximoToken()).nome != TipoToken.FIM) {
            System.out.print(t);
        }
    }
    
}
