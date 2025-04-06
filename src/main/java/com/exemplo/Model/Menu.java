package com.exemplo.Model;

import com.exemplo.Model.Produto;
import com.exemplo.Service.GerenciamentoProdutos;
import com.exemplo.Query.ArmazenamentoProdutos;

import java.util.Scanner;
import java.sql.SQLException;

public class Menu{
	//Classe para acessar o menu com as funcionalidades da aplicação
	private final GerenciamentoProdutos gerencia;
    	private final ArmazenamentoProdutos querys;

    	public Menu(GerenciamentoProdutos gerencia, ArmazenamentoProdutos querys) {
        	this.gerencia = gerencia;
        	this.querys = querys;
    	}

	public void exibir() throws SQLException{
		Scanner sc = new Scanner(System.in);
		int escolha;

		do {
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

			System.out.println("O que deseja fazer? ");
	                escolha = Integer.parseInt(sc.nextLine());

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

					//criar id do novo produto
                			int maiorId = 0;
                			for (Produto p : this.gerencia.getProdutos()) {
                        			if (p.getId() > maiorId) {
                                			maiorId = p.getId();
                        			}
                			}
			                int novoId = maiorId + 1;

					Produto produto = new Produto(novoId, nome,preco,quantidade);

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
					try {
                                	System.out.println("Digite o id do produto: ");
                                	int id = Integer.parseInt(sc.nextLine());
					querys.buscarProduto(id);
					break;
                                	} catch (NumberFormatException e) {
                                        	System.out.println("ID inválido! Digite apenas números.");
                                        	break;
                                	} catch (SQLException e) {
                                        	System.out.println("Erro no banco de dados: " + e.getMessage());
                                        	break;
                                	}

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
			if (escolha != 0) {
				System.out.println("Deseja retornar ao menu[S/n]: ");
                		String retorno = sc.nextLine().trim().toUpperCase();
			        if (retorno.equals("N")) {
					escolha = 0; //interrompe o looping
				}
			}
		} while (escolha !=0); //o menu aparece ate a escolha ser a opção 0
		sc.close();
	}
}

