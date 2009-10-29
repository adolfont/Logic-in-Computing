% teste do operador cut

%fatos
a(c1).
a(c2).
a(c3).

b(c1).

c(c1).
c(c2).
c(c3).

d(X):-
	a(X),writef( '%w%w' , [ 'Tentando ' , X] ), nl,
	b(X),c(X).
e(X):-
	a(X),writef( '%w%w' , [ 'Tentando ' , X] ), nl, 
	b(X),!,c(X).


