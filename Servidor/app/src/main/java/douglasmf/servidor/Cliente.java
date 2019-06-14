package douglasmf.servidor;

public class Cliente {
    public String nome,
                  telefone,
                  email,
                  endereco;

    public int id;

    public Cliente(String nome, String telefone, String email, String endereco, int id){
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.id = id;
    }
}
