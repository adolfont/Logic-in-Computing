% obs.: Todos os discos começam no pino esquerdo
hanoi(N):-
  move(N, esquerdo, central, direito).

move(0 , _, _, _) :- ! .

move(N, A, B, C) :-
  M is N-1,
  move(M, A, C, B),
  inform(A, B) ,
  move(M, C, B, A) .

inform(X, Y) :-
  writef( '%w%w%w%w' , [ 'Mova o disco do pino ' , X, ' para o pino ' , Y] ) ,
  nl.
