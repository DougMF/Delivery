<?php
    class Funcoes {
 
        private $conexao;
     
        //Construtor
        function __construct() {
            require_once 'ConexaoDB.php';
            //Conexão com o banco
            $db = new ConexaoDB();
            $this->conexao = $db->conectar();
        }
     
        //Destrutor
        function __destruct() {
             
        }
     
        //Cadastrar usuário
        public function cadastrarUsuario($nome, $email, $senha, $telefone, $logradouro, 
                                $bairro, $numero, $cep, $complemento) {
            $hash = $this->hashSSHA($senha);
            $senhaCriptografada = $hash["encrypted"];
            $salt = $hash["salt"];
     
            //Inicia a transação para inserir nas tabelas "usuarios" e "endereco"
            $this->conexao->begin_transaction();
     
            $stmt = $this->conexao->prepare("INSERT INTO usuarios(nome, email, senha, salt, telefone) VALUES(?, ?, ?, ?, ?)");
            $stmt->bind_param("sssss", $nome, $email, $senhaCriptografada, $salt, $telefone);
            $resultado = $stmt->execute();
            $stmt->close();
     
            //Verifica se os dados foram inseridos com sucesso
            if ($resultado) {
                $stmt = $this->conexao->prepare("SELECT id from usuarios WHERE email = '" .$email. "'");
            
                $stmt->execute();
                $usuario = $stmt->get_result()->fetch_assoc();
                $stmt->close();
                
                if ($usuario["id"]) {
                    $stmt = $this->conexao->prepare("INSERT INTO endereco(logradouro, 
                                bairro, numero, cep, complemento, usuario) VALUES(?, ?, ?, ?, ?, ?)");
                    $stmt->bind_param("ssssss", $logradouro, $bairro, $numero, $cep, $complemento, $usuario["id"]);
                    $resultado = $stmt->execute();
                    $stmt->close();
                    
                    //Verifica se os dados foram inseridos na tabela "endereco"
                    if($resultado){
                        $this->conexao->commit();
                        return true;
                    } else {
                        $this->conexao->rollback();
                        return false;
                    }
                }else{
                    $this->conexao->rollback();
                    return false;
                }
            } else {
                $this->conexao->rollback();
                return false;
            }
        }
     
        //Verifica se o email e senha estão corretos
        public function loginCliente($email, $senha) {
     
            $stmt = $this->conexao->prepare("SELECT id, salt, senha FROM usuarios WHERE email = '" .$email. "'");
     
            if ($stmt->execute()) {
                $usuario = $stmt->get_result()->fetch_assoc();
                $stmt->close();
     
                //Verifica a senha
                $salt = $usuario['salt'];
                $senhaCriptografada = $usuario['senha'];
                $hash = $this->checkhashSSHA($salt, $senha);
                
                //Verifica se a senha digitada está correta
                if ($senhaCriptografada == $hash) {
                    return $usuario['id'];
                }
            } else {
                return NULL;
            }
        }
     
        //Verifica se e-mail informado pelo usuário já não está em uso
        public function verificarDuplicidade($email) {
            $stmt = $this->conexao->prepare("SELECT email from usuarios WHERE email = '" .$email. "'");
            
            $stmt->execute();
            $stmt->store_result();
     
            if ($stmt->num_rows > 0) {
                //E-mail em uso
                $stmt->close();
                return false;
            } else {
                //E-mail disponível
                $stmt->close();
                return true;
            }
        }
     
        //Codifica a senha informada pelo usuário no cadastro
        public function hashSSHA($senha) {
     
            $salt = sha1(rand());
            $salt = substr($salt, 0, 10);
            $criptografada = base64_encode(sha1($senha . $salt, true) . $salt);
            $hash = array("salt" => $salt, "encrypted" => $criptografada);
            return $hash;
        }
     
        //Codifica a senha informada pelo usuário no login
        public function checkhashSSHA($salt, $senha) {
     
            $hash = base64_encode(sha1($senha . $salt, true) . $salt);
     
            return $hash;
        }
        
        public function enviarPedido($itens, $total, $usuario){
            $stmt = $this->conexao->prepare("insert into pedidos (itens, total, usuario) values (?,?,?)");
            
            $stmt->bind_param("sss", $itens, $total, $usuario);
            
            if($stmt->execute()){
                $stmt->close();
                echo "ok";
            }else echo "erro";
        }
        
        public function getCardapio(){
            $stmt = $this->conexao->prepare("select item, descricao, preco from cardapio");
            
            if($stmt->execute()){
                $itens = array();
                $stmt = $stmt->get_result();
                
                while(($item = $stmt->fetch_assoc())){
                    array_push($itens, $item);
                }
                
                echo json_encode($itens);
            }else echo "erro";
            
            $stmt->close();
        }
        
        public function verificarStatus($usuario){
            $stmt = $this->conexao->prepare("select status from pedidos where usuario = '" .$usuario. "'");
            
            if($stmt->execute()){
                $status = $stmt->get_result()->fetch_assoc();
                
                if($status) echo $status["status"];
                else echo "fim";
                
                $stmt->close();
            }else echo "erro";
        }
        
        /**********************
         * Funções do servidor*
         * *******************/
         
        //Verifica se o email e senha estão corretos
        public function loginAdmin($admin, $senha) {
     
            $stmt = $this->conexao->prepare("SELECT salt, senha FROM admin WHERE admin = '" .$admin. "'");
     
            if ($stmt->execute()) {
                $admin = $stmt->get_result()->fetch_assoc();
                $stmt->close();
     
                //Verifica a senha
                $salt = $admin['salt'];
                $senhaCriptografada = $admin['senha'];
                $hash = $this->checkhashSSHA($salt, $senha);
                
                //Verifica se a senha digitada está correta
                if ($senhaCriptografada == $hash) {
                    echo "ok";
                }
            } else {
                return NULL;
            }
        }
        
        public function getPedidos(){
            $stmt = $this->conexao->prepare("select p.itens, p.total, p.usuario, u.nome, u.telefone, e.logradouro, e.cep, e.numero, e.bairro, e.complemento from pedidos p join endereco e on p.usuario = e.usuario join usuarios u on u.id = p.usuario where p.status ='A'");
            
            if($stmt->execute()){
                $pedidos = array();
                $stmt = $stmt->get_result();
                
                while(($pedido = $stmt->fetch_assoc())){
                    array_push($pedidos, $pedido);
                }
                
                echo json_encode($pedidos);
                
            }else echo "erro";
            
            $stmt->close();
        }
        
        public function getClientes(){
            $stmt = $this->conexao->prepare("select u.id, u.nome, u.telefone, u.email,
                                            e.logradouro, e.cep, e.numero, e.bairro, e.complemento from usuarios u
                                            join endereco e on u.id = e.usuario");
            
            if($stmt->execute()){
                $clientes = array();
                $stmt = $stmt->get_result();
                
                while(($cliente = $stmt->fetch_assoc())){
                    array_push($clientes, $cliente);
                }
                
                echo json_encode($clientes);
                
            }else echo "erro";
            
            $stmt->close();
        }
        
        public function alterarStatus($usuario, $status){
            $stmt = $this->conexao->prepare("update pedidos set status = '" .$status. "' where usuario = '" .$usuario. "'");
            
            if($stmt->execute()) echo "ok";
            else echo "erro";
            
            $stmt->close();
        }
        
        public function removerPedido($usuario){
            $stmt = $this->conexao->prepare("delete from pedidos where usuario = '" .$usuario. "'");
            
            if($stmt->execute()) echo "ok";
            else echo "erro";
            
            $stmt->close();
        }
    }
?>