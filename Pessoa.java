
@Entity
public class Pessoa {

	String nome;
	
	@OneToOne
	Email email;

	@Embeded
	Endereco endereco;
}
