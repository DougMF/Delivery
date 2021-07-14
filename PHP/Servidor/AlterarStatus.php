<?php
    require_once '/storage/ssd1/304/8861304/public_html/PHPDelivery/incluir/Funcoes.php';
    $db = new Funcoes();
    $db->alterarStatus($_POST["usuario"], $_POST["status"]);
?>