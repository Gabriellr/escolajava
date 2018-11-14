package br.com.projeto.escolajava.repositorys;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.projeto.escolajava.codecs.AlunoCodec;
import br.com.projeto.escolajava.models.Aluno;

@Repository
public class AlunoRepositorys {

	public void salvar(Aluno aluno){

		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);


		AlunoCodec alunoCodec = new AlunoCodec(codec);

		CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),

		CodecRegistries.fromCodecs(alunoCodec));
		
		MongoClientOptions	opcoes = MongoClientOptions.builder().codecRegistry(registro).build();

		MongoClient cliente = new MongoClient("localhost:27017",opcoes);
		MongoDatabase bancoDeDados = cliente.getDatabase("test");
		MongoCollection<Aluno> alunos = bancoDeDados.getCollection("alunos", Aluno.class);
		alunos.insertOne(aluno);

		cliente.close();
	}
 public List

}
