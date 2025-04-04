
package com.exemplo.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.exemplo.Model.Produto;
import com.exemplo.Service.GerenciamentoProdutos;

import java.util.ArrayList;

public class ArmazenamentoProdutos{
	//MÉTODOS
	public Connection conectarAoBanco() throws SQLException {
		//método para conectar ao banco de dados do projeto
		String url = "jdbc:mysql://localhost/Cadastro_Produto?serverTimezone=UTC";
		String user = "Diego";
                String password = "@Galo2013";

		return DriverManager.getConnection(url, user, password);
	}

	public void salvarProdutos(GerenciamentoProdutos gerencia) throws SQLException {
		//o método resgata a conexão feita com o banco e recebe uma lista de produtos para salvar cada um na tabela produto
		Connection conn = null;
		conn = this.conectarAoBanco();
		conn.setAutoCommit(false);

		PreparedStatement verificationStmt = conn.prepareStatement("SELECT COUNT(*) FROM produto WHERE nome = ?");
		int insertedRows = 0;
		for(Produto i : gerencia.getProdutos()){
   			verificationStmt.setString(1, i.getNome());
			ResultSet rs = verificationStmt.executeQuery();

			if(rs.next() && rs.getInt(1) == 0) { //verifica se o resultado, rs, armazenado pela query de SELECT retornou 0 na busca pelo produto
				try{
					PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO produto (nome, preco, quantidadeEstoque) VALUES (?,?,?)");

            				insertStmt.setString(1, i.getNome());
            				insertStmt.setDouble(2, i.getPreco());
            				insertStmt.setInt(3, i.getQuantidade());
					insertStmt.executeUpdate();
				} catch (SQLException ex) {
					System.out.println("Erro ao adicionar produto no banco");
				}

				insertedRows++;
				System.out.println("Produto foi adicionado!");
	                } else {
     		   		System.out.println("Produto '" + i.getNome() + "' já existe no banco!");
    			}
		}
		conn.commit();
		System.out.println("Foram adicionados " + insertedRows + " produtos");
	}

	public void carregaEstoque(GerenciamentoProdutos gerencia) throws SQLException {
		Connection conn = null;
                conn = this.conectarAoBanco();
                conn.setAutoCommit(false);

		PreparedStatement selectionAll = conn.prepareStatement("SELECT * FROM produto");
		ResultSet rs = selectionAll.executeQuery();

		while (rs.next()) {
			String Nome = rs.getString("nome");
			double Preco = rs.getDouble("preco");
			int Estoque = rs.getInt("quantidadeEstoque");

			Produto produto = new Produto(Nome, Preco, Estoque);
			gerencia.cadastrarProduto(produto);
		}
	}

	public void excluirProduto(int id) throws SQLException{
		Connection conn = null;
                conn = this.conectarAoBanco();
                conn.setAutoCommit(false);
    		PreparedStatement resetID = null;

		try {
			PreparedStatement buscarProduto = conn.prepareStatement("SELECT * FROM produto WHERE id = ?");
			buscarProduto.setInt(1, id);
			ResultSet rs = buscarProduto.executeQuery();
			if (rs.next()) {
        			System.out.println("Deletando o produto " + rs.getString("nome") + "...");
    			}
			rs.close();
			buscarProduto.close();

			PreparedStatement excluirProduto = conn.prepareStatement("DELETE FROM produto WHERE id = ?");
			excluirProduto.setInt(1, id);
			excluirProduto.executeUpdate();

			//redefinir ids
			System.out.println("Redefinindo IDs...");
        		resetID = conn.prepareStatement("SET @count = 0;");
        		resetID.executeUpdate();

        		resetID = conn.prepareStatement("UPDATE produto SET id = @count := @count + 1;");
        		resetID.executeUpdate();

        		resetID = conn.prepareStatement("ALTER TABLE produto AUTO_INCREMENT = 1;");
        		resetID.executeUpdate();

        		conn.commit();
        		System.out.println("Produto removido e IDs redefinidos!");
		} catch (SQLException e) {
            		conn.rollback();
		}finally {
			System.out.println("saindo...");
        		if (resetID != null) resetID.close();
			if (conn != null) conn.close();
		}
	}
}
