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
public class ProgExemplo {
    LeitorDeArquivosTexto ldat;
    public ProgExemplo(String arquivo) {
        ldat = new LeitorDeArquivosTexto(arquivo);
    }
    
    
    private Token operadorAritmetico() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '*') {
            c = (char)ldat.lerProximoCaractere();
            if(c == '*'){
                return new Token(TipoToken.OPPOT, ldat.getLexema());
            }
            else{
                ldat.retroceder();
                return new Token(TipoToken.OPMULT, ldat.getLexema());        
            }  
        } else if (c == '/') {
            c = (char)ldat.lerProximoCaractere();
            if(c == '/'){
                return new Token(TipoToken.OPSQRR, ldat.getLexema());
            }
            else{
              ldat.retroceder();
              return new Token(TipoToken.OPDIV, ldat.getLexema());  
            }  
        } else if (c == '+') {
            return new Token(TipoToken.OPSUM, ldat.getLexema());
        } else if (c == '-') {
            return new Token(TipoToken.OPMINUS, ldat.getLexema());
        }
        
        else {
            return null;
        }
    }
    
    private Token delimitador() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '-') {
            c = (char)ldat.lerProximoCaractere();
            if(c == '>'){
                return new Token(TipoToken.DELIM, ldat.getLexema());
            }
            else{
                ldat.retroceder();
                return new Token(TipoToken.OPMINUS, ldat.getLexema());
            }
        } else {
            return null;
        }
        
    }
    
    private Token parenteses() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '(') {
            return new Token(TipoToken.OPPAR, ldat.getLexema());
        } else if (c == ')') {
            return new Token(TipoToken.CLPAR, ldat.getLexema());
        } else {
            return null;
        }
    }
    
    private Token operadorRelacional() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '<') {
            c = (char) ldat.lerProximoCaractere();
            if (c == '>') {
                return new Token(TipoToken.OPDIF, ldat.getLexema());
            } else if (c == '=') {
                return new Token(TipoToken.OPLESSEQ, ldat.getLexema());
            } else {
                ldat.retroceder();
                return new Token(TipoToken.OPLESS, ldat.getLexema());
            }
        } else if (c == '=') {
            c = (char)ldat.lerProximoCaractere();
            if(c == '='){
                return new Token(TipoToken.OPEQUAL, ldat.getLexema());
            }
            else{
                ldat.retroceder();
                return new Token(TipoToken.OPEQUAL, ldat.getLexema());
            }
            
        } else if (c == '>') {
            c = (char) ldat.lerProximoCaractere();
            if (c == '=') {
                return new Token(TipoToken.OPGREATEQ, ldat.getLexema());
            } else if (c == '>') {
                return new Token(TipoToken.OPCON, ldat.getLexema());
            }
            else {
                ldat.retroceder();
                return new Token(TipoToken.OPGREAT, ldat.getLexema());
            }
        }
        return null;
    }
        
    private Token numeros() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isDigit(c)) {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (c == '.') {
                    c = (char) ldat.lerProximoCaractere();
                    if (Character.isDigit(c)) {
                        estado = 3;
                    } else {
                        return null;
                    }
                } else if (!Character.isDigit(c)) {
                    ldat.retroceder();
                    return new Token(TipoToken.NUMINT, ldat.getLexema());
                }
            } else if (estado == 3) {
                if (!Character.isDigit(c)) {
                    ldat.retroceder();
                    return new Token(TipoToken.NUMREAL, ldat.getLexema());
                }
            }
        }
    }
    
    private Token variavel() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isLetter(c)) {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (!Character.isLetterOrDigit(c)) {
                    ldat.retroceder();
                    return new Token(TipoToken.VAR, ldat.getLexema());
                }
            }
        }
    }
    
     private Token cadeia() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (c == '\'') {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (c == '\n') {
                    return null;
                }
                if (c == '\'') {
                    return new Token(TipoToken.CADEIA, ldat.getLexema());
                } else if (c == '\\') {
                    estado = 3;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return null;
                } else {
                    estado = 2;
                }
            }
        }
    }
     
    private void espacosEComentarios() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isWhitespace(c) || c == ' ') {
                    estado = 2;
                } else if (c == '~') {
                    estado = 3;
                } else {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 2) {
                if (c == '~') {
                    estado = 3;
                } else if (!(Character.isWhitespace(c) || c == ' ')) {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return;
                }
            }
        }
    }
    
    private Token palavrasChave() {
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (!Character.isLetter(c)) {
                ldat.retroceder();
                String lexema = ldat.getLexema();
                if (lexema.equals("fn")) {
                    return new Token(TipoToken.FN, ldat.getLexema());
                } else if (lexema.equals("if")) {
                    return new Token(TipoToken.IF, ldat.getLexema());
                } else if (lexema.equals("ini")) {
                    return new Token(TipoToken.INI, ldat.getLexema());
                } else if (lexema.equals("float")) {
                    return new Token(TipoToken.FLOAT, ldat.getLexema());
                } else if (lexema.equals("int")) {
                    return new Token(TipoToken.INT, ldat.getLexema());
                } else if (lexema.equals("char")) {
                    return new Token(TipoToken.CHAR, ldat.getLexema());
                } else if (lexema.equals("string")) {
                    return new Token(TipoToken.STRING, ldat.getLexema());
                } else if (lexema.equals("boolean")) {
                    return new Token(TipoToken.BOOLEAN, ldat.getLexema());
                } else if (lexema.equals("while")) {
                    return new Token(TipoToken.WHILE, ldat.getLexema());
                }
                } else {
                    return null;
                }
            }
        }
    

     private Token fim() {
        int caractereLido = ldat.lerProximoCaractere();
        if (caractereLido == -1) {
            return new Token(TipoToken.FIM, "Fim");
            
        }
        return null;
    }
     
     public Token proximoToken() {
        Token proximo = null;
        espacosEComentarios();
        ldat.confirmar();
        proximo = fim();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = palavrasChave();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = variavel();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = numeros();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = operadorAritmetico();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = operadorRelacional();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = delimitador();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = parenteses();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = cadeia();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        System.err.println("Erro léxico!");
        System.err.println(ldat.toString());
        return null;
    }
}
