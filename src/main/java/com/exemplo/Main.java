package com.exemplo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.exemplo.Model.Menu;
import com.exemplo.Model.Produto;
import com.exemplo.Service.GerenciamentoProdutos;
import com.exemplo.Query.ArmazenamentoProdutos;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args){
		//CONECTANDO AO BANCO
		String username = "Diego";
		String password = "@Galo2013";

		try (Connection conn = DriverManager
			.getConnection("jdbc:mysql://localhost/Cadastro_Produto?serverTimezone=UTC",
				username, password)) {
			conn.setAutoCommit(false);
			System.out.println("Conexão estabelecida com sucesso!");
			try{Thread.sleep(2000);} catch(Exception erro){}

			//RESGATANDO PRODUTOS DO BANCO
			ArmazenamentoProdutos querys = new ArmazenamentoProdutos();
			GerenciamentoProdutos gerencia = new GerenciamentoProdutos();
			querys.carregaEstoque(gerencia);
			querys.limparTerminal();
			System.out.println("Banco de dados preparado");
                        try{Thread.sleep(2000);} catch(Exception erro){}

			//MENU
			Menu menu = new Menu(gerencia, querys);
			menu.exibir();

			conn.close();
		} catch (SQLException ex) {
                        System.err.println("Erro na conexão com o banco: " + ex.getMessage());
                } 
	}
}
