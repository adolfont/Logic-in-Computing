pai(joao,jose).
mae(maria,jose).
irmao(pedro,joao).
%irmao(joao,pedro).
pai(pedro,larissa).
irmao(X,Y):-irmao(Y,X),!.  %comentar
tio(X,Y):-irmao(X,Z),pai(Z,Y).
%tio(X,Y):-irmao(Z,X),pai(Z,Y).
primo(X,Y):-pai(Z,X),pai(T,Y),irmao(Z,T),!.

% Teste: primo(Y,X). -->> out of local stack =?= loop infinito?
