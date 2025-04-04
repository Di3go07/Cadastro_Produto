package com.exemplo.Service;

import com.exemplo.Model.Produto;
import java.util.ArrayList;

public class GerenciamentoProdutos{
	//VARIÁVEL
	private ArrayList<Produto> produtos = new ArrayList<>();;

	//CONSTRUTOR
	public GerenciamentoProdutos(){
		this.produtos = produtos;
	}

	//GETTERS E SETTERS
	public void cadastrarProduto(Produto produto){
		this.produtos.add(produto);
	}
	public void listarProdutos(){
		System.out.println("PRODUTOS DISPONÍVEIS");
		int contador = 1;
		for(Produto produto : this.produtos){
			System.out.println("Item " + contador);
			System.out.println(produto);
			contador++;
		}
	}
	public ArrayList<Produto> getProdutos(){
		return this.produtos;
	}
}
