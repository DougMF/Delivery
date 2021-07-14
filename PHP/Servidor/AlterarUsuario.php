<?php
    require_once '/storage/ssd1/304/8861304/public_html/PHPDelivery/incluir/Funcoes.php';
    
    $db = new Funcoes();
    
    if(isset($_POST["usuario"])){
        $db->alterarUsuario($_POST["usuario"], $_POST["senhaAtual"]);
    }else if(isset($_POST["senha"])){
        $db->alterarSenha($_POST["senha"], $_POST["senhaAtual"]);
    }
?>