<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html data-ng-app="inclusaoDisciplina" data-ng-controller="inclusaoController">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SCA - Requerimento de inclusão de disciplina</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	<script src="${pageContext.request.contextPath}/js/vendor/jquery-1.11.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular.min.js"></script>
    <script>
    	var url = "${pageContext.request.contextPath}/rest/turma";
    	var numeroSolicitacoes = ${requestScope.numeroSolicitacoes};
    	var solicitacoesRestantes = 3 - numeroSolicitacoes;
    </script>
    
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>

<body class="inclusao-disciplina">
    <div class="container">
        <div class="row">
            <h1 class="text-center">Requerimento de inclusão de disciplina</h1>
            <h3><b>Atenção:</b></h3>
            <h4><b>Após a autorização do professor/chefe de departamento, 
            	a matrícula somente será efetivada caso não haja choque de 
            	horário nem quebra de pré-requisito (Exceto opção 3)</b></h4>
        </div>
        <div class="text-inline">
            <h4 class="aluno">Aluno: ${requestScope.aluno.nome}</h4>
            <h4>Matrícula: ${requestScope.aluno.matricula}</h4>
        </div>
        <hr/>
        <c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
        <div class="row">
            <form action="${pageContext.request.contextPath}/inclusaoDisciplina/incluiSolicitacao" 
            	  method="post" enctype="multipart/form-data">
                <div class="departamento-select">
					<p><b>Departamento:</b></p>
	                <select class="form-control" name="departamento" required>
	                	<option value="" label="Escolha o departamento desejado" selected disabled> </option>
	                	<c:forEach items="${requestScope.departamentos}" var="departamento">
	                		<option value="${departamento.codigoDepartamento}">
	                			${departamento.nomeDepartamento}
	                		</option>
	                	</c:forEach>
	                </select>
                </div>
                
                <hr/>
                <div class="row" id="turma-disciplina" >
                	<ul>
                        <li id="turmas" data-ng-repeat="input in inputs">
                            <div id="inputs" class="col-md-4">
	                            <p><b>Código da Turma:</b></p>
	                            <input class="form-control input-lg" data-ng-model="turma" data-ng-change="changeTurma(this, $index)" 
	                            name="codigoTurma{{$index}}" type="text"  placeholder="Digite o código da turma..." required/>    
	                        </div>
	                        <div id="inputs" class="col-md-7">
	                            <p><b>Disciplina:</b></p>
	                            <input type="text" class="form-control input-lg" value="{{disciplina[$index]}}" readonly required>
	                        </div>
	                        <div class="col-md-1">
	                           <a class="remove" data-ng-click="removeInput($index)"><i class="fa fa-minus-circle fa-3x"></i></a> 
	                        </div> 
                        </li>
                    </ul>
                </div>
              	<div class="row">
              		<a data-ng-click="addInput()" class="add"><i class="fa fa-plus-circle fa-3x"></i> <h4>Adicionar Turma</h4> </a>
              	</div>
                <div class="row radio">
                    <hr/>
                    <p><b>Opção:</b></p>
                    <label class="radio-input">
                        <input type="radio" name="opcao" id="optionsRadios1" value="1" required> 
                        1 - <b>Inclusão de disciplina em turma do próprio curso.</b> 
                        Opção restrita a alunos que na matrícula pela internet, 
                        solicitaram a inscrição na disciplina e turma ora requerida, mas sua solicitação 
                        foi rejeitada por falta de vagas.<b> Anexar solicitação de matrícula do período atual</b>
                    </label>
                    <label class="radio-input">
                        <input type="radio" name="opcao" id="optionsRadios2" value="2" required> 
                        2 - Inclusão de disciplina na qual houve impossibilidade de inscrição na matrícula pela internet em 
                        <b>decorrência de erro</b> no lançamento de nota do período anterior.
                    </label>
                    <label class="radio-input">
                        <input type="radio" name="opcao" id="optionsRadios3" value="3" required> 
                        3 - Inclusão de <b>disciplina com quebra de pré-requisito</b>, 
                        envolvendo prováveis formandos que poderão alcançar a integralização 
                        do repectivo curso (manual do aluno pág. 19).
                    </label>
                </div>
                
                <hr/>
                <div class="form-group">
					<label for="observacao">Observações adicionais:</label>
					<textarea name="observacao" class="form-control" rows="6" id="observacao"
							  maxlength="500" 
							  placeholder="(opcional, max 500 caracteres)"></textarea>
					<h5>Faltam <span id="max"></span> caracteres.</h5>
				</div>
                
                <hr/>
                
                <label for="inputFile">Anexar comprovante de solicitação de matrícula do período atual 
                (Formatos aceitos: PDF, JPEG ou PNG. Tamanho Máximo: 10mb)</label>
                <input type="file" name="file" id="inputFile" required>
                
                <hr/>
                <div class="row checkbox">
                    <label>
                        <input type="checkbox" value="" required> Estou ciente de que o presente requerimento 
                        <b>não garante a matrícula na disciplina solicitada</b> 
                        e de que posso verificar se a inscrição foi efetivada, ou não, 
                        através de consulta ao histórico escolar, disponível no portal do aluno
                    </label>
                </div>
                
                <hr/>
				<div class=text-center>
					<input type="hidden" name="numeroSolicitacoes" value="${requestScope.numeroSolicitacoes}">
					<button type="submit" class="btn btn-primary btn-lg text-center">Confirmar</button>
				</div>
                
            </form>
        </div>
        <a class="btn btn-default" href="${pageContext.request.contextPath}/inclusaoDisciplina/homeInclusao">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
    </div>
    
 
</body>

</html>
	