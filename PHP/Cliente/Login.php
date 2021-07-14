<?php
    require_once '/storage/ssd1/304/8861304/public_html/PHPDelivery/incluir/Funcoes.php';
    $db = new Funcoes();
     
    //Parâmetros POST
    $email = "jujua@hotmail.com";
    $senha = "12345";
 
    //Procurar usuário por e-mail e senha
    $usuario = $db->loginCliente($email, $senha);
    
    if ($usuario != "erro") {
        //Usuário encontrado
        echo $usuario;
    } else {
        //Usuário não encontrado
        echo "erro";
    }
?>