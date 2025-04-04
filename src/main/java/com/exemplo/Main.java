package com.exemplo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

			//RESGATANDO PRODUTOS DO BANCO
			ArmazenamentoProdutos querys = new ArmazenamentoProdutos(); //instancia a classe com as querys
			GerenciamentoProdutos gerencia = new GerenciamentoProdutos();
			querys.carregaEstoque(gerencia);

			//CRIANDO UM PRODUTO
			///Produto produto = new Produto("SSD Kingston", 38.0, 16);

			//produto.diminuirQuantidade(20);
			//produto.diminuirQuantidade(2);

                	Produto produto2 = new Produto("Samsung", 1229.99, 18);

			//GERENCIAR PRODUTOS
			///gerencia.listarProdutos();
			///gerencia.cadastrarProduto(produto);
                	///gerencia.cadastrarProduto(produto2);

			//MANIPULAR O BANCO
			querys.salvarProdutos(gerencia); //passa a lista de produtos criada e chama a função para salva-los

			///querys.excluirProduto(3);

		} catch (SQLException ex) {
                        System.err.println("Erro na conexão com o banco: " + ex.getMessage());
                }

	}
}
