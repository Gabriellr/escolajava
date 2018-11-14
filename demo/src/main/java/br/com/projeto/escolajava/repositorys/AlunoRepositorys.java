package br.com.projeto.escolajava.repositorys;

import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.projeto.escolajava.models.Aluno;

@Repository
public class AlunoRepositorys {
	
	public void salvar(Aluno aluno){
		MongoClient cliente = new MongoClient();
		MongoDatabase bancoDeDados = cliente.getDatabase("test");
		MongoCollection<Aluno> alunos = bancoDeDados.getCollection("alunos", Aluno.class);
		alunos.insertOne(aluno);
		
		cliente.close();
	}
	

}
