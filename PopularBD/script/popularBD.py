import json
import mysql.connector
from mysql.connector import Error
import random

def criar_conexao():
    with open('dados_banco.json', 'r', encoding='utf-8') as arquivo:  
        dados = json.load(arquivo)

    try:
        conexao = mysql.connector.connect(
            host='localhost',
            user=dados['user'],
            password=dados['password'],
            database='Cadastro_Produto'
        )
        return conexao
    except Error as e:
        print(f"Erro ao conectar ao MySQL: {e}")
        return None

def popular_tabela_produtos():
    conexao = criar_conexao()
    if conexao is None:
        return

    try:
        cursor = conexao.cursor()

        # Lista de produtos fictícios para inserção
        produtos = [
            ("Notebook Dell Inspiron", 4299.90, 15),
            ("Smartphone Samsung Galaxy S23", 3899.00, 30),
            ("Tablet Amazon Fire HD", 899.90, 42),
            ("Mouse sem fio Logitech", 129.90, 75),
            ("Teclado mecânico Redragon", 349.90, 28),
            ("SSD Kingston 1TB NVMe", 499.90, 37),
        ]

        # Filtro para ver se existem produtos iguais
        novosProdutos = []
        for produto in produtos:
            nome = produto[0]
            cursor.execute("SELECT COUNT(*) FROM produto WHERE nome = %s", (nome,))
            
            if cursor.fetchone()[0] == 0:
                novosProdutos.append(produto)

        # Comando SQL para inserção
        sql = """INSERT INTO produto (nome, preco, quantidadeEstoque)
                 VALUES (%s, %s, %s)"""
  
        if (len(novosProdutos) <= 0):
            print("Nenhum produto novo")
            return 

        # Inserir cada produto
        cursor.executemany(sql, novosProdutos)
        conexao.commit()

        print(f"{cursor.rowcount} registros inseridos com sucesso!")

    except Error as e:
        print(f"Erro ao popular tabela: {e}")
    finally:
        if conexao.is_connected():
            cursor.close()
            conexao.close()

# Executar a função principal
if __name__ == "__main__":
    popular_tabela_produtos()
