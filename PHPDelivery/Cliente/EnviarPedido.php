<?php
    require_once '/storage/ssd1/304/8861304/public_html/PHPDelivery/incluir/Funcoes.php';
    $db = new Funcoes();
    $db->enviarPedido($_POST['itens'], $_POST['total'], $_POST['usuario']);
?>