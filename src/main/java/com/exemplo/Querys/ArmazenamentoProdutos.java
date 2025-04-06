
package com.exemplo.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.nio.file.Paths;

import com.exemplo.Model.Produto;
import com.exemplo.Service.GerenciamentoProdutos;

import java.util.ArrayList;

public class ArmazenamentoProdutos{
	//MÉTODOS
	public Connection conectarAoBanco() throws SQLException{
		//método para conectar ao banco de dados do projeto
		Connection conn = null;

		//lendo JSON com as informações do banco
		JSONObject jsonObject;
		JSONParser parser = new JSONParser();
		String url = null;
		String user = null;
		String password = null;

		try {
			String path = Paths.get("dados_banco.json").toAbsolutePath().toString();
			jsonObject = (JSONObject) parser.parse(new FileReader(path));
			url = (String) jsonObject.get("url");
			user = (String) jsonObject.get("user");
			password = (String) jsonObject.get("password");

 		} catch (FileNotFoundException e) {
     		   	System.err.println("Arquivo JSON não encontrado!");
        		e.printStackTrace();
    		} catch (IOException | ParseException e) {
                        System.err.println("Erro ao ler o JSON: " + e.getMessage());
		}

		if (url == null || user == null || password == null) {
        		throw new SQLException("Dados de conexão inválidos. URL, usuário ou senha estão nulos.");
    		}

		return DriverManager.getConnection(url, user, password);
	}

	public void salvarProdutos(GerenciamentoProdutos gerencia) throws SQLException {
		//o método resgata a conexão feita com o banco e recebe uma lista de produtos para salvar cada um na tabela produto
		Connection conn = null;
		conn = this.conectarAoBanco();
		conn.setAutoCommit(false);

		PreparedStatement verificationStmt = conn.prepareStatement("SELECT COUNT(*) FROM produto WHERE nome = ?");
		int insertedRows = 0;
		for(Produto i : gerencia.getProdutos()){ //resgata o nome do produto na lista e verifica se ele já está no banco
   			verificationStmt.setString(1, i.getNome()); //passa no SELECT o nome resgatado para ver se suas aparições são igual a 0
			ResultSet rs = verificationStmt.executeQuery();

			if(rs.next() && rs.getInt(1) == 0) { //verifica se o resultado, rs, armazenado pela query de SELECT retornou 0 na busca pelo produto
				try{
					PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO produto (nome, preco, quantidadeEstoque) VALUES (?,?,?)");

            				insertStmt.setString(1, i.getNome());
            				insertStmt.setDouble(2, i.getPreco());
            				insertStmt.setInt(3, i.getQuantidade());
					insertStmt.executeUpdate(); //finaliza a operação de Insert de um novo produto na tabela
				} catch (SQLException ex) {
					System.out.println("Erro ao adicionar produto no banco");
				}

				insertedRows++;
				System.out.println("Produto foi adicionado!");
	                } else {
     		   		///System.out.println("Produto '" + i.getNome() + "' já existe no banco!");
    			}
		}
		conn.commit();
		System.out.println("Foram adicionados " + insertedRows + " produtos");
	}

	public void carregaEstoque(GerenciamentoProdutos gerencia) throws SQLException {
		///Esse método entra em contato com o banco e adiciona todos os produtos na lista de gerenciamento
		Connection conn = null;
                conn = this.conectarAoBanco();
                conn.setAutoCommit(false);

		PreparedStatement selectionAll = conn.prepareStatement("SELECT * FROM produto");
		ResultSet rs = selectionAll.executeQuery();

		while (rs.next()) { //para cada item da tabela, é criado uma instância de Produto para adicionar à lista
			//dados do item
			int id = rs.getInt("id");
			String Nome = rs.getString("nome");
			double Preco = rs.getDouble("preco");
			int Estoque = rs.getInt("quantidadeEstoque");

			Produto produto = new Produto(id, Nome, Preco, Estoque);
			gerencia.cadastrarProduto(produto);
		}
	}

	public void buscarProduto(int id) throws SQLException{
		//O méotodo recebe um id e retorna as informações do produto
		Connection conn = null;
                conn = this.conectarAoBanco();
                conn.setAutoCommit(false);
                PreparedStatement resetID = null;

                try {
			PreparedStatement buscarProduto = conn.prepareStatement("SELECT * FROM produto WHERE id = ?");
                        buscarProduto.setInt(1, id);
                        ResultSet rs = buscarProduto.executeQuery(); //verifica se existe um produto com o id no banco
                        if (rs.next()) {
                                System.out.println("Nome: " + rs.getString("nome") + "\n" +
						    "Preço: " + rs.getString("preco") + "\n" +
						    "Quantidade disponível: " + rs.getString("quantidadeEstoque")
				);
                        }else{
                        	System.out.println("Produto com ID " + id + " não encontrado! Consulte a lista de produtos.");
                        }


		}catch (SQLException e) {
                        if (conn != null) {
                                conn.rollback();
                        }
                }
	}

	public void excluirProduto(int id) throws SQLException{
		///Méotodo do CRUD para excluir o produto do banco
		Connection conn = null;
                conn = this.conectarAoBanco();
                conn.setAutoCommit(false);
		PreparedStatement buscarProduto = null;
		ResultSet rs = null;

		try {
			buscarProduto = conn.prepareStatement("SELECT * FROM produto WHERE id = ?");
			buscarProduto.setInt(1, id);
			rs = buscarProduto.executeQuery(); //verifica se o id existe no banco

			//ações para caso exista ou não o registro do id passado
			if (!rs.next()) { //não existe
                        	System.out.println("Produto com ID " + id + " não encontrado");
                                return; //sai do método
                        } else { //existe
        			System.out.println("Deletando o produto " + rs.getString("nome") + "...");
    			}

			PreparedStatement excluirProduto = conn.prepareStatement("DELETE FROM produto WHERE id = ?");
			excluirProduto.setInt(1, id);
			excluirProduto.executeUpdate();
			excluirProduto.close();

		} catch (SQLException e) {
    			if (conn != null) {
        			try {
            				conn.rollback();
        			} catch (SQLException ex) {
            				System.err.println("Erro ao fazer rollback: " + ex.getMessage());
        			}
    			}
    			throw e;
		}finally{
			if (rs != null) {
        			try {
            				rs.close();
        			} catch (SQLException e) {
            				System.err.println("Erro ao fechar ResultSet: " + e.getMessage());
        			}
			}
    			if (buscarProduto != null) {
        			try {
            				buscarProduto.close();
        			} catch (SQLException e) {
			        	System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
        			}
    			}
    			if (conn != null) {
        			try {
            				if (!conn.getAutoCommit()) {
                				conn.setAutoCommit(true);
            				}
            				conn.close();
        			} catch (SQLException e) {
            				System.err.println("Erro ao fechar Connection: " + e.getMessage());
        			}
			}
    		}
	}

	public void editarProduto(int id) throws SQLException{
                //O método recebe um id e permite o usuário informar novos valores para seus campos na tabela
		Connection conn = null;
	    	Scanner sc = new Scanner(System.in);

                try {
	                conn = this.conectarAoBanco();
                	conn.setAutoCommit(false);

			//try usado para verificar se o id está registrado
			try (PreparedStatement verificaExistencia = conn.prepareStatement("SELECT * FROM produto WHERE id = ?")) {
            			verificaExistencia.setInt(1, id);
            			try (ResultSet rs = verificaExistencia.executeQuery()) {
                			if (!rs.next()) {
                    				System.out.println("Produto com ID " + id + " não encontrado");
                				return;
					}
            			}
        		}

			System.out.println("Novo nome: ");
			String novoNome = sc.nextLine();
                	System.out.println("Novo preço: ");
			Double novoPreco = Double.parseDouble(sc.nextLine());
                	System.out.println("Nova quantidade: ");
			int novaQuantidade = Integer.parseInt(sc.nextLine());

			//Editando as informações do produto com os novos valores recebidos
			PreparedStatement editarProduto = conn.prepareStatement("UPDATE produto SET nome=?, preco=?, quantidadeEstoque=? WHERE id=?");
			editarProduto.setString(1, novoNome);
			editarProduto.setDouble(2, novoPreco);
			editarProduto.setInt(3, novaQuantidade);
			editarProduto.setInt(4, id);
			editarProduto.executeUpdate();
			conn.commit();

			System.out.println("Produto editado!");

		}catch (SQLException e) {
                	if (conn != null) {
           	 		conn.rollback();
        		}
                }finally{
			if (conn != null) {
                                conn.close();
                        }
                }

	}
	public void limparTerminal() {
		//méotodo acionado quando é necessário limpar o terminal
		try {
        		if (System.getProperty("os.name").contains("Windows")) {
            			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        		} else {
            			System.out.print("\033[H\033[2J");  // Código ANSI para limpar
            			System.out.flush();
        		}
    		} catch (Exception e) {
        		System.out.println("\n".repeat(50));  // Fallback básico
		}
	}
}
