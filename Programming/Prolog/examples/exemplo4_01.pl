%dynamic faz espécie de "declaração" dos predicados
:- dynamic p/0, a/0, b/0, c/0.

%fatos
c.
%a.
%b.

%regras
p:-a,!,b.
p:-c.

%a:-writef("tentando a"),nl.
%b:-writef("tentando b"),nl.
%c:-writef("tentando c"),nl.

% Ver http://www.cse.unsw.edu.au/~billw/cs9414/notes/prolog/intro.html#cut
% e http://en.wikibooks.org/wiki/Prolog/Cuts_and_Negation
%cut impede a busca de uma segunda solução.
% teste: remover o cut e ver o que acontece nos dois casos

%se o que está à esquerda do cut falhar, não continua
% teste: comentar a regra que inicia com b
%        depois remover o cut e ver o que acontece nos dois casos

