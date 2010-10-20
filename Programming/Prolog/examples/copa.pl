vencedor(uruguai,1930).
vencedor(italia,1934).
vencedor(italia,1938).
vencedor(uruguai,1950).
vencedor(alemanha,1954).
vencedor(brasil,1958).
vencedor(brasil,1962).
vencedor(inglaterra,1966).
vencedor(brasil,1970).
vencedor(alemanha,1974).
vencedor(argentina,1978).
vencedor(italia,1982).
vencedor(argentina,1986).
vencedor(alemanha,1990).
vencedor(brasil,1994).
vencedor(franca,1998).
vencedor(brasil,2002).
vencedor(italia,2006).
vencedor(espanha,2010).

campeao(X):-vencedor(X,_).

titulos(X):-
  vencedor(X,Y),
  writef( '%w%w%w', [X, ' foi campeão em ', Y]), nl,
  fail.

conta_titulos(Pais,Titulos):-
  findall(Y,vencedor(Pais,Y),Z),
  compr(Z,Titulos).
  
compr([],0).
compr([_|Xs],C):-compr(Xs,C1), C is C1+1.

campeoes(Z):-
  setof(X,campeao(X),Z).

total_campeoes(J):-
  campeoes(Z),
  compr(Z,J).


campeao_do_ano(X,Y):-
  vencedor(Y,X),!.


