package com.exemplo.Model;

public class Produto {
	//VARIÁVEIS
	private int id;
	private String nome;
	private double preco;
	private int quantidadeEmEstoque;

	//CONSTRUTOR
	public Produto(int id, String nome, double preco, int quantidadeEmEstoque){
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.quantidadeEmEstoque = quantidadeEmEstoque;
	}

	//GETTER E SEETERS
	public String getNome(){
		return this.nome;
	}
	public void setNome(String nome){
		this.nome = nome;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int Id){
		this.id = id;
	}
	public double getPreco(){
		return this.preco;
	}
	public void setPreco(double preco){
		this.preco = preco;
	}

	public int getQuantidade(){
		return this.quantidadeEmEstoque;
	}
	public void aumentarQuantidade(int aumento){
		this.quantidadeEmEstoque += aumento;
	}
	public void diminuirQuantidade(int diminuicao){
		if (diminuicao <= 0){
			System.out.println("Deve ser um número positivo");
		}
		if (this.quantidadeEmEstoque - diminuicao >= 0){
			this.quantidadeEmEstoque -= diminuicao;
		} else {
			System.out.println("Não pode ter quantidade negativa em estoque!");
		}
	}

	//MÉTODOS DA CLASSE
	public String formartarPreco(){
                return "R$"+ preco;
        }

	@Override
	public String toString(){
		return "ID: " + id + "\n" +
		"Produto: " + nome + "\n" +
		"Preço: " + formartarPreco() + "\n" +
		"Quantidade disponível: " + quantidadeEmEstoque;
	}
}
