package main.java.com.secretKeeper.model;

import java.util.Objects;

public class Participante {
    private String nome;
    private Participante amigoSecreto;

    public Participante(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public Participante getAmigoSecreto() {
        return amigoSecreto;
    }

    public void setAmigoSecreto(Participante amigoSecreto) {
        this.amigoSecreto = amigoSecreto;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return false;
        }
        if(!(o instanceof Participante)){
            return false;
        }
        Participante that = (Participante) o;

        return nome.equalsIgnoreCase(that.nome);
    }

    @Override
    public int hashCode(){
        return Objects.hash(nome.toLowerCase());
    }
}