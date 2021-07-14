<?php
    require_once '/storage/ssd1/304/8861304/public_html/PHPDelivery/incluir/Funcoes.php';
    $db = new Funcoes();
    
    //Parâmetros POST
    if(isset($_POST["usuario"]) && isset($_POST["senha"])){
        //Procurar usuário por admin e senha
        $db->loginAdmin($_POST["usuario"], $_POST["senha"]);
    }else echo "erro";
?>