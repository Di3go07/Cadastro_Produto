package com.exemplo.Model;

import com.exemplo.Model.Produto;
import com.exemplo.Service.GerenciamentoProdutos;
import com.exemplo.Query.ArmazenamentoProdutos;

import java.util.Scanner;
import java.sql.SQLException;

public class Menu{
	private final GerenciamentoProdutos gerencia;
    	private final ArmazenamentoProdutos querys;

    	public Menu(GerenciamentoProdutos gerencia, ArmazenamentoProdutos querys) {
        	this.gerencia = gerencia;
        	this.querys = querys;
    	}
	public void exibir() {
		querys.limparTerminal();

		System.out.println("╔════════════════════════╗\n" +
    				   "║      MENU PRINCIPAL    ║\n" +
    				   "╠════════════════════════╣\n" +
    				   "║ 1 – Cadastrar produto  ║\n" +
    				   "║ 2 – Listar produtos    ║\n" +
    				   "║ 3 – Buscar produto     ║\n" +
    				   "║ 4 – Editar produto     ║\n" +
    				   "║ 5 – Excluir produto    ║\n" +
    				   "║ 0 – Sair               ║\n" +
    				   "╚════════════════════════╝");

		Scanner sc = new Scanner(System.in);
		System.out.println("O que deseja fazer? ");
                int escolha = Integer.parseInt(sc.nextLine());

		switch (escolha){
			case 0:
				System.out.println("Fechando...");
				break;
			case 1:
				System.out.println("Nome: ");
                        	String nome = sc.nextLine();
                        	System.out.println("Preço: ");
                	        Double preco = Double.parseDouble(sc.nextLine());
        	                System.out.println("Quantidade: ");
	                        int quantidade = Integer.parseInt(sc.nextLine());

				Produto produto = new Produto(nome,preco,quantidade);

				gerencia.cadastrarProduto(produto);

				 try {
           	 			querys.salvarProdutos(gerencia);
                                } catch (SQLException e) {
				   	 System.out.println("Erro ao salvar produto: " + e.getMessage());
					break;
				}

				System.out.println("Produto cadastrado com sucesso!");
				break;
			case 2:
				gerencia.listarProdutos();
				break;
			case 3:
				break;
			case 4:
				try {
					System.out.println("Digite o id do produto: ");
                                	int id = Integer.parseInt(sc.nextLine());
					querys.editarProduto(id);
					break;
				} catch (NumberFormatException e) {
    					System.out.println("ID inválido! Digite apenas números.");
					break;
				} catch (SQLException e) {
    					System.out.println("Erro no banco de dados: " + e.getMessage());
    					break;
				}
			case 5:
				try {
                                        System.out.println("Digite o id do produto: ");
                                        int id = Integer.parseInt(sc.nextLine());
                                        querys.excluirProduto(id);
                                        break;
                                } catch (NumberFormatException e) {
                                        System.out.println("ID inválido! Digite apenas números.");
                                        break;
                                } catch (SQLException e) {
    					System.out.println("Erro no banco de dados: " + e.getMessage());
    					break;
				}

			default:
				System.out.println("Operação inválida");
				break;
		}
		System.out.println("Deseja retornar ao menu[S/n]: ");
                String retorno = sc.nextLine().trim().toUpperCase();
                switch (retorno){
			case "S":
				this.exibir();
				break;
			case "N":
				System.out.println("Dados salvos com sucesso!");
				break;
			default:
                                System.out.println("Operação inválida");
				this.exibir();
				break;
		}
	}
}
