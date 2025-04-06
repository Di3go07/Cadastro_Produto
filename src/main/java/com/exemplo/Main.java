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
		//INSTÂNCIAS
		ArmazenamentoProdutos querys = new ArmazenamentoProdutos();
		GerenciamentoProdutos gerencia = new GerenciamentoProdutos();

		Connection conn = null;
		try{
			//CONECTANDO AO BANCO
                	conn = querys.conectarAoBanco();
                	conn.setAutoCommit(false);

			System.out.println("Conexão estabelecida com sucesso!");
			try{Thread.sleep(2000);} catch(Exception erro){}

			//RESGATANDO PRODUTOS DO BANCO
			querys.carregaEstoque(gerencia);
			querys.limparTerminal();
			System.out.println("Banco de dados preparado");
                	try{Thread.sleep(2000);} catch(Exception erro){}

			//MENU
			Menu menu = new Menu(gerencia, querys);
			menu.exibir();

		} catch (SQLException ex) {
                        System.err.println("Erro na conexão com o banco: " + ex.getMessage());
                } finally {
			if (conn != null) {
            			try {
					conn.setAutoCommit(true);
					conn.close();

                			DriverManager.drivers().forEach(driver -> {
                				try {
                    					DriverManager.deregisterDriver(driver);
                				} catch (SQLException e) {
                    					System.err.println("Erro ao deregistrar driver: " + e.getMessage());
                				}
            				});

					System.out.println("Conexão fechada com sucesso.");
				} catch (SQLException e) {
             	   			System.err.println("Erro ao fechar conexão: " + e.getMessage());
            			}
			}
		}
	}
}
