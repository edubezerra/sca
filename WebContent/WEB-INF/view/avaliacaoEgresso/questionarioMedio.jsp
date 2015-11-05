<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/egresso.css" />
    <title>Form egresso</title>
</head>
<body>
    <form method="POST">
        <div class="row">
            <div class="col-md-8 form">
              <img src="${pageContext.request.contextPath}/images/logo-cefet.jpg">
                <h3 style="margin-left:100px; font-weight:bold;">
                    PROGRAMA DE ACOMPANHAMENTO DE EGRESSOS
                </h3>
                <hr />
                <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">1. Atualmente o(a) Sr.(a) está:</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-1">
                                    Trabalhando (vá para 2)
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-1">
                                    Trabalhando e estudando (vá para 2)
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-1">
                                    Apenas estudando (vá para 13)
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-1">
                                    Não está trabalhando e nem estudando (vá para 13)
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-1">
                                    Outros
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />
                <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">2. O(a) Sr.(a) trabalha na área em que se formou? </p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-2">
                                    Sim
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-2">
                                    Não
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />

                <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">3. Qual é o seu VÍNCULO EMPREGATÍCIO?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Empregado com carteira assinada
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Empregado sem carteira assinada
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Servidor público concursado
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Autônomo/Prestador de serviços
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Em contrato temporário
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Estagiário
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Proprietário de empresa/negócio
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-3">
                                    Outros
                                </label>
                            </div>
                        </div>
                    </div>
                </div><br/><br/>

               <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">4. Porte da instituição onde exerce a atividade profissional:</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-4">
                                    empresa individual (Autônomo ou Profissional Liberal);
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-4">
                                    microempresa;
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-4">
                                    média empresa;
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-4">
                                    grande empresa;
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                    <br/><br/>
                 <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">5. O(a) Sr.(a) já trabalhava antes de iniciar o seu curso? </p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-5">
                                    Sim
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-5">
                                    Não
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />
                <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">6. Qual o principal TIPO DE ATIVIDADE que o(a) Sr.(a) exerce no seu trabalho atual? </p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-6">
                                    Atividade Técnica
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-6">
                                    Atividade Administrativa
                                </label>
                            </div>
                           <div class="radio">
                                <label>
                                    <input type="radio" name="question-6">
                                    Atividade Gerencial
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-6">
                                    Atividade Comercial
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-6">
                                    Outra
                                </label>
                            </div>
                        </div>
                      </div>
                    </div>
                    <br /><br />
                    <div class="question-box">
	                    <div class="question-wrapper">
	                        <p class="question">7. Qual é a sua especialidade ou área de atuação na sua profissão? </p>
	                        <div style="margin-left: 5px;margin-bottom:10px;">
	                            <textarea cols=60 id="opiniao" rows="3" maxlength="500" wrap="hard" placeholder="coragem, você consegue ! "></textarea>
	                        </div>
                    </div>
                </div>
                <br /><br />
                 <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">8. O conhecimento técnico adquirido no CEFET-RJ durante sua formação foi suficiente e está adequado ao seu trabalho ?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Sim
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Não
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />
                <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">9. É possível destacar a área onde você mais necessitou de conhecimentos teóricos adicionais ?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Área tecnológica 
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Área administrativa
                                </label>
                            </div>
                                                        <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Área de planejamento
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Não necessitou
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />
                <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">10. Onde você considera que o curso deve ser aprimorado para que houvesse uma melhor performance em sua atividade profissional ?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Práticas em laboratório
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Aulas teóricas
                                </label>
                            </div>
                                                        <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Articulação teoria x práticas em laboratório
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Outro.  Qual ? <input type="text" class="form-control" style="width:240px; margin-left:-20px;">
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />
                <div class="question-box">
                    <div class="question-wrapper">
                        <p class="question">11. Onde está LOCALIZADO o seu trabalho atual?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    No próprio município onde realizou o curso.
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Com distância de até 50 Km de onde realizou o curso.
                                </label>
                            </div>
                                                        <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Em município com distância entre 50 e 100 Km de onde realizou o curso.
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Em município com distância entre 100 e 400 Km
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Em município com distância superior a 400 Km
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />
				<div class="question-box">
                    <div class="question-wrapper">
                        <p class="question"> 12. Considerando o salário mínimo federal de R$ 678,00, qual a sua renda mensal em salários mínimos?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Entre 1 e 5 salários mínimos (até R$ 3.390,00)
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Entre 5 e 10 salários mínimos (até R$ 6.780,00)
                                </label>
                            </div>
                                                        <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Maior que 10 salários mínimos (> R$ 6.780,00)
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <br /><br />
				<div class="question-box">
                    <div class="question-wrapper">
                        <p class="question"> 13. Qual o seu grau de satisfação com a ÁREA PROFISSIONAL em que o(a) Sr.(a) fez o seu curso?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Muito satisfeito 
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Satisfeito 
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Indiferente
                                </label>
                            </div>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Insatisfeito 
                                </label>
                            </div> 
                           <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Muito insatisfeito 
                                </label>
                            </div>
                        	<div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Não sabe / Não opinou
                                </label>
                            </div>  
                        </div>
                    </div>
                    <br /><br />
				<div class="question-box">
                    <div class="question-wrapper">
                        <p class="question"> 14. Você tem interesse em dar continuidade aos seus estudos ?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Sim (vá para 15)
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Não 
                                </label>
                            </div>
                        </div>
                    </div>
			    </div>
			    
			     <br /><br />
				<div class="question-box">
                    <div class="question-wrapper">
                        <p class="question"> 15. Na mesma área ?</p>
                        <div style="margin-left: 30px;">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Sim 
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="question-8">
                                    Não. Em qual? <input type="text" class="form-control" style="width:240px; margin-left:-20px;">
                                </label>
                            </div>
                        </div>
                    </div>
			    </div>
			    
			    
			    <br /><br />
			    
			    <div class="checkbox" style="margin-left:200px">
				    <label>
				      <input type="checkbox"> Autorizo a divulgação dos meus contatos pessoais junto às empresas.
				    </label>
				 </div>
			
            <br/><br/>
            <div style="margin-left:250px;">
             <div style="float:left; margin-right:30px;">
                <button type="button" class="btn btn-primary">
                    <span class="glyphicon glyphicon-ok"></span>  Confirmar envio    
                </button>
             </div>
                <div style="float:left;">
                  <button type="button" class="btn btn-default">
                    <span class="glyphicon glyphicon-remove"></span>  Limpar respostas    
                  </button>
                </div>
            </div>

            <br/><br/> <br/><br/>
            
            </div>
        </div>
    </form>
</body>
</html>