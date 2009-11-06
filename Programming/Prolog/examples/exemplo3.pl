% Autor: Adolfo Neto
% Data: 26-10-2009

% fatos sobre uma pessoa específica
febre.
tosse.
cansaço.
estudante.

% regras
gripe_suína:-febre, tosse, cansaço.
ficar_isolado:-gripe_suína.
faltar_aula:-ficar_isolado, estudante.
ser_reprovado:-faltar_aula,estudar_pouco.

% testes:
% 
% gripe_suína.
% >> deve retornar true.
% ficar_isolado.
% >> deve retornar true.
% faltar_aula.
% >> deve retornar true.
% ser_reprovado.
% >> NÃO deve retornar true.
% Testar assert(estudar_pouco).
% e novamente ser_reprovado.
% >> deve retornar true.

