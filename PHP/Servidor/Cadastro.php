<?php
    require_once '/storage/ssd1/304/8861304/public_html/PHPDelivery/incluir/Funcoes.php';
    $db = new Funcoes();
     
    //Parâmetros POST
    $admin = "admin";
    $senha = "123";
 
    //Procurar usuário por admin e senha
    $db->cadastrarAdmin($admin, $senha);
?>