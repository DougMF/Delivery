<?php
	class ConexaoDB {
		private $conexao;
	 
		// Conexão com o banco
		public function conectar() {
			require_once 'Config.php';
			 
			$this->conexao = new mysqli(hostDB, usuarioDB, senhaDB, bancoDados);
			 
			return $this->conexao;
		}
	}
 
?>