package br.com.projeto.escolajava.codecs;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import br.com.projeto.escolajava.models.Aluno;
import br.com.projeto.escolajava.models.Curso;
import br.com.projeto.escolajava.models.Habilidade;
import br.com.projeto.escolajava.models.Nota;

public class AlunoCodec  implements CollectibleCodec<Aluno>{


	private Codec<Document> codec;

	public AlunoCodec(Codec<Document> codec){
		this.codec = codec;

	}


	@Override
	public void encode(BsonWriter writer, Aluno aluno, EncoderContext encoder) {

		ObjectId id =  aluno.getId();
		String nome = aluno.getNome();
		Date dataNascimento = aluno.getDataNascimento();
		Curso curso = aluno.getCurso();
		List<Habilidade> habilidades = aluno.getHabilidades();
		List<Nota> notas = aluno.getNotas();


		Document document = new Document();
		document.put("_id", id);
		document.put("nome", nome);
		document.put("data_nascimento", dataNascimento);
		document.put("curso", new Document("nome", curso.getNome()));
		if(habilidades != null){
			List<Document> habilidadesDocument = new ArrayList<>();

			for( Habilidade habilidade : habilidades){
				habilidadesDocument.add(new Document("nome", habilidade.getNome())
						.append("nivel", habilidade.getNivel()));

			}
			document.put("habilidades", habilidadesDocument);

		}

		if(notas != null){
			List<Double> notasParaSalvar = new ArrayList<>();
			for (Nota nota : notas) {
				notasParaSalvar.add(nota.getValor());
			}
			document.put("notas", notasParaSalvar);

		}

		codec.encode(writer, document, encoder);

	}

	@Override
	public Class<Aluno> getEncoderClass() {

		return Aluno.class;
	}

	@Override
	public Aluno decode(BsonReader reader, DecoderContext decoder) {
		Document document = codec.decode(reader, decoder);

		Aluno aluno = new Aluno();
		aluno.setId(document.getObjectId("_id"));
		aluno.setNome(document.getString("nome"));
		aluno.setDataNascimento(document.getDate("data_nascimento"));
		Document curso =(Document) document.get("curso");
		if(curso != null){
			String nomeCurso = curso.getString("nome");
			aluno.setCurso(new Curso(nomeCurso));
		}



		List<Double> notas = new ArrayList<Double>();

		if (notas != null) {
			List<Nota> notasDoAluno = new ArrayList<>();
			for (Double nota : notas) {
				notasDoAluno.add(new Nota(nota));
			}

			aluno.setNotas(notasDoAluno);
		}

		List<Document> habilidades = (List<Document>) document.get("habilidades");


		if(habilidades != null){
			List<Habilidade> habilidadesDoAluno = new ArrayList<>();
			for(Document documentHabilidade: habilidades){
				habilidadesDoAluno.add(new Habilidade(documentHabilidade.getString("nome"),
						documentHabilidade.getString("nivel")));


			}

			aluno.setHabilidades(habilidadesDoAluno);
		}

		return aluno;
	}

	@Override
	public boolean documentHasId(Aluno aluno) {

		return aluno.getId()== null;
	}

	@Override
	public Aluno generateIdIfAbsentFromDocument(Aluno aluno) {

		return documentHasId(aluno) ? aluno.criarId(): aluno;
	}

	@Override
	public BsonValue getDocumentId(Aluno aluno) {
		if(!documentHasId(aluno)){
			throw new IllegalStateException("Esse Documento nao tem id");

		}
		return new BsonString(aluno.getId().toHexString());
	}

}
