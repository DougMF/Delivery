<?php

    require_once '/storage/ssd1/304/8861304/public_html/PHPDelivery/incluir/Funcoes.php';
    $db = new Funcoes();
     
    //Recebimento dos parâmetros POST
    $nome = $_POST['nome'];
    $email = $_POST['email'];
    $senha = $_POST['senha'];
    $telefone = $_POST['telefone'];
    $logradouro = $_POST['logradouro'];
    $bairro = $_POST['bairro'];
    $numero = $_POST['numero'];
    $cep = $_POST['cep'];
    $complemento = $_POST['complemento'];
 
    //Verifica se um usuário com o mesmo e-mail já está cadastrado
    if (!$db->verificarDuplicidade($email)) {
        //Usuário já existe
        echo "erro1";
    } else {
        //Usuário não existe. Criação de um novo
        $usuario = $db->cadastrarUsuario($nome, $email, $senha, $telefone, $logradouro, 
                                $bairro, $numero, $cep, $complemento);
        
        if ($usuario) {
            //Sucesso na operação
            echo "ok";
        } else {
            //Falha na operação
            echo "erro2";
        }
    }
?>